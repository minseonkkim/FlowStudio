package com.ssafy.flowstudio.api.service.rag;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.ssafy.flowstudio.api.service.rag.request.KnowledgeCreateServiceRequest;
import com.ssafy.flowstudio.api.service.rag.request.KnowledgeSearchServiceRequest;
import com.ssafy.flowstudio.api.service.rag.response.ChunkListResponse;
import com.ssafy.flowstudio.api.service.rag.response.ChunkResponse;
import com.ssafy.flowstudio.api.service.rag.response.KnowledgeCreateServiceResponse;
import com.ssafy.flowstudio.api.service.rag.response.KnowledgeResponse;
import com.ssafy.flowstudio.common.exception.BaseException;
import com.ssafy.flowstudio.common.exception.ErrorCode;
import com.ssafy.flowstudio.common.util.MilvusUtils;
import com.ssafy.flowstudio.domain.knowledge.entity.Knowledge;
import com.ssafy.flowstudio.domain.knowledge.entity.KnowledgeRepository;
import com.ssafy.flowstudio.domain.user.entity.User;
import io.milvus.v2.client.MilvusClientV2;
import io.milvus.v2.common.ConsistencyLevel;
import io.milvus.v2.service.collection.request.CreateCollectionReq;
import io.milvus.v2.service.collection.request.DescribeCollectionReq;
import io.milvus.v2.service.collection.request.GetLoadStateReq;
import io.milvus.v2.service.collection.request.HasCollectionReq;
import io.milvus.v2.service.collection.response.DescribeCollectionResp;
import io.milvus.v2.service.partition.request.CreatePartitionReq;
import io.milvus.v2.service.partition.request.HasPartitionReq;
import io.milvus.v2.service.partition.request.LoadPartitionsReq;
import io.milvus.v2.service.partition.request.ReleasePartitionsReq;
import io.milvus.v2.service.vector.request.*;
import io.milvus.v2.service.vector.request.data.BaseVector;
import io.milvus.v2.service.vector.request.data.FloatVec;
import io.milvus.v2.service.vector.response.*;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.document.Document;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class VectorStoreService {
    private static final Logger log = LoggerFactory.getLogger(VectorStoreService.class);
    private final MilvusClientV2 milvusClient;
    private final LangchainService langchainService;
    private final MilvusUtils milvusUtils;
    private final KnowledgeRepository knowledgeRepository;

    public void createCollection(String collectionName) {
        if (!hasCollection(collectionName)) {
            CreateCollectionReq createCollectionReq = CreateCollectionReq.builder()
                    .collectionName(collectionName)
                    .consistencyLevel(ConsistencyLevel.STRONG)
                    .dimension(milvusUtils.generateEmbeddingModel().dimensions())
                    .build();

            milvusClient.createCollection(createCollectionReq);
        }
    }

    public void createPartition(String collectionName, String partitionName) {
        if (!hasPartition(collectionName, partitionName)) {
            CreatePartitionReq createPartitionReq = CreatePartitionReq.builder()
                    .collectionName(collectionName)
                    .partitionName(partitionName)
                    .build();
            milvusClient.createPartition(createPartitionReq);
        }
    }

    public ChunkListResponse getDocumentChunks(User user, KnowledgeResponse knowledge, Long chunkId) {
        List<ChunkResponse> chunkList = getDocumentChunk(user, knowledge, chunkId);

        return ChunkListResponse.builder()
                .chunkCount(chunkList.size())
                .chunkList(chunkList)
                .build();

    }

    public List<ChunkResponse> getDocumentChunk(User user, KnowledgeResponse knowledge, Long chunkId) {
        String collectionName = milvusUtils.generateName(user.getId());
        List<String> partitionNames = milvusUtils.generateName(List.of(knowledge.getKnowledgeId()));
        loadPartition(collectionName, partitionNames);

        QueryResp queryResp = milvusClient.query(QueryReq.builder()
                .collectionName(collectionName)
                .partitionNames(partitionNames)
                .outputFields(List.of("id", "content")) // 조회하려는 필드명
                .filter(chunkId == -1 ? "id <= " + 400 : "id == " + chunkId)
                .build());

        if (queryResp.getQueryResults().isEmpty()) {
            return new ArrayList<ChunkResponse>();
        }

        return queryResp.getQueryResults().stream()
                .map(QueryResp.QueryResult::getEntity)
                .map(result -> ChunkResponse.builder()
                        .chunkId(Long.parseLong(result.get("id").toString()))
                        .content(result.get("content").toString())
                        .build())
                .toList();
    }

    @Transactional
    public KnowledgeResponse copyDocument(User user, Long originKnowledgeId) {
        Knowledge originKnowledge = knowledgeRepository.findByIdAndPublic(originKnowledgeId, true)
                .orElseThrow(() -> new BaseException(ErrorCode.KNOWLEDGE_NOT_FOUND));

        List<ChunkResponse> chunkList = getDocumentChunk(originKnowledge.getUser(), KnowledgeResponse.from(originKnowledge), -1L);
        List<Object> ids = new ArrayList<>();
        chunkList.forEach(chunkResponse -> ids.add(chunkResponse.getChunkId()));

        Knowledge copyKnowledge = Knowledge.builder()
                .user(user)
                .title(originKnowledge.getTitle())
                .isPublic(originKnowledge.isPublic())
                .totalToken(originKnowledge.getTotalToken())
                .build();

        knowledgeRepository.save(copyKnowledge);

        String collectionName = milvusUtils.generateName(user.getId());
        String partitionName = milvusUtils.generateName(copyKnowledge.getId());
        createCollection(collectionName);
        createPartition(collectionName, partitionName);

        if (ids.isEmpty()) {
            throw new BaseException(ErrorCode.KNOWLEDGE_NOT_FOUND);
        }

        GetReq getReq = GetReq.builder()
                .collectionName(milvusUtils.generateName(originKnowledge.getUser().getId()))
                .partitionName(milvusUtils.generateName(originKnowledge.getId()))
                .ids(ids)
                .build();

        GetResp getResp = milvusClient.get(getReq);


        List<JsonObject> data = new ArrayList<>();
        getResp.getGetResults()
                .forEach(result -> {
                    data.add(new Gson().toJsonTree(result.getEntity()).getAsJsonObject());
                });

        UpsertReq upsertReq = UpsertReq.builder()
                .collectionName(collectionName)
                .partitionName(partitionName)
                .data(data)
                .build();
        milvusClient.upsert(upsertReq);

        UpsertResp upsertResp = milvusClient.upsert(upsertReq);
        if (upsertResp.getUpsertCnt() != chunkList.size()) throw new BaseException(ErrorCode.FAIL_COPY_VECTOR_STORE);

        return KnowledgeResponse.from(copyKnowledge);
    }

    public Boolean upsertChunk(User user, KnowledgeResponse knowledge, Long chunkId, String content) {
        String collectionName = milvusUtils.generateName(user.getId());
        String partitionName = milvusUtils.generateName(knowledge.getKnowledgeId());

        List<JsonObject> data = new ArrayList<>();
        data.add(milvusUtils.documentToJson(chunkId, content));

        UpsertReq upsertReq = UpsertReq.builder()
                .collectionName(collectionName)
                .partitionName(partitionName)
                .data(data)
                .build();
        UpsertResp upsertResp = milvusClient.upsert(upsertReq);

        return upsertResp.getUpsertCnt() > 0;
    }

    public KnowledgeCreateServiceResponse upsertDocument(String collectionName, String partitionName, KnowledgeCreateServiceRequest request) {
        String textContent = milvusUtils.getTextContent(request.getFile());
        List<String> splitterContent = langchainService.getSplitText(request.getChunkSize(), request.getChunkOverlap(), textContent);
        List<Document> splitterDocuments = milvusUtils.textsToDocuments(splitterContent);

        long id = 0;
        List<JsonObject> data = new ArrayList<>();
        for (Document document : splitterDocuments) {
            data.add(milvusUtils.documentToJson(id++, document.getContent()));
        }

        UpsertReq upsertReq = UpsertReq.builder()
                .collectionName(collectionName)
                .partitionName(partitionName)
                .data(data)
                .build();
        milvusClient.upsert(upsertReq);

        UpsertResp upsertResp = milvusClient.upsert(upsertReq);

        return KnowledgeCreateServiceResponse.builder()
                .isComplete(upsertResp.getUpsertCnt() > 0)
                .totalToken(milvusUtils.getTokenCount(splitterContent))
                .build();
    }

    public Boolean deleteChunk(User user, KnowledgeResponse knowledge, Long chunkId) {
        DeleteResp deleteResp = milvusClient.delete(DeleteReq.builder()
                .collectionName(milvusUtils.generateName(user.getId()))
                .partitionName(milvusUtils.generateName(knowledge.getKnowledgeId()))
                .ids(List.of(chunkId))
                .build());

        return deleteResp.getDeleteCnt() > 0;
    }

    public Boolean hasCollection(String collectionName) {
        HasCollectionReq hasCollectionReq = HasCollectionReq.builder()
                .collectionName(collectionName)
                .build();

        return milvusClient.hasCollection(hasCollectionReq);
    }

    public Boolean hasPartition(String collectionName, String partitionName) {
        HasPartitionReq hasPartitionReq = HasPartitionReq.builder()
                .collectionName(collectionName)
                .partitionName(partitionName)
                .build();

        return milvusClient.hasPartition(hasPartitionReq);
    }

    public ChunkListResponse previewChunks(KnowledgeCreateServiceRequest request) {
        List<String> splitText = langchainService.getSplitText(request.getChunkSize(), request.getChunkOverlap(), milvusUtils.getTextContent(request.getFile()));

        final long[] i = {0};
        return ChunkListResponse.builder()
                .chunkList(splitText.stream()
                        .limit(5)
                        .map(text -> ChunkResponse.builder()
                                .chunkId(i[0]++)
                                .content(text)
                                .build())
                        .toList())
                .chunkCount(splitText.size())
                .build();
    }

    public List<String> searchVector(KnowledgeSearchServiceRequest request) {
        if (request.getScoreThreshold() < 0.0f || request.getScoreThreshold() > 1.0f)
            throw new BaseException(ErrorCode.SEARCH_INVALID_INPUT);
        if (request.getTopK() < 0 || request.getTopK() > 10) throw new BaseException(ErrorCode.SEARCH_INVALID_INPUT);
        if (request.getInterval() < 0 || request.getInterval() > 5)
            throw new BaseException(ErrorCode.SEARCH_INVALID_INPUT);

        String collectionName = milvusUtils.generateName(request.getKnowledge().getUserId());
        String partitionName = milvusUtils.generateName(request.getKnowledge().getKnowledgeId());

        if (loadPartition(collectionName, List.of(partitionName))) {
            try {
                int count = 0;
                while (!getLoadState(collectionName, partitionName) && count < request.getInterval() * 2) {
                    Thread.sleep(500);
                    count++;
                }
                if (!getLoadState(collectionName, partitionName)) {
                    throw new BaseException(ErrorCode.PARTITION_NOT_AVAILABLE);
                }
            } catch (InterruptedException e) {
                log.error("Vector Search Interrupted", e);
            }
        }

        List<BaseVector> vectors = new ArrayList<>();
        EmbeddingModel embeddingModel = milvusUtils.generateEmbeddingModel();

        BaseVector baseVector = new FloatVec(embeddingModel.embed(request.getQuery()));
        vectors.add(baseVector);
        SearchReq searchReq = SearchReq.builder()
                .collectionName(collectionName)
                .partitionNames(List.of(partitionName))
                .data(vectors)
                .topK(request.getTopK())
                .searchParams(Map.of("radius", request.getScoreThreshold()))
                .build();
        SearchResp searchResp = milvusClient.search(searchReq);


        List<Object> ids = new ArrayList<>();
        for (List<SearchResp.SearchResult> results : searchResp.getSearchResults()) {
            for (SearchResp.SearchResult result : results) {
                ids.add(result.getId());
            }
        }

        if (ids.isEmpty()) {
            return new ArrayList<>();
        }

        GetReq getReq = GetReq.builder()
                .collectionName(collectionName)
                .partitionName(partitionName)
                .ids(ids)
                .build();

        GetResp getResp = milvusClient.get(getReq);

        if (!releasePartition(collectionName, List.of(partitionName))) {
            releasePartition(collectionName, List.of(partitionName));
        }

        return getResp.getGetResults().stream()
                .map(result -> result.getEntity().getOrDefault("content", "").toString())
                .toList();
    }

    public Boolean loadPartition(String collectionName, List<String> partitionNames) {
        if (partitionNames.isEmpty()) throw new BaseException(ErrorCode.PARTITION_NOT_FOUND);

        if (!hasCollection(collectionName)) {
            throw new BaseException(ErrorCode.COLLECTION_NOT_FOUND);
        }

        for (String partitionName : partitionNames) {
            if (!hasPartition(collectionName, partitionName)) {
                throw new BaseException(ErrorCode.PARTITION_NOT_FOUND);
            }
        }

        LoadPartitionsReq loadPartitionsReq = LoadPartitionsReq.builder()
                .collectionName(collectionName)
                .partitionNames(partitionNames)
                .build();

        milvusClient.loadPartitions(loadPartitionsReq);

        return true;
    }

    public Boolean releasePartition(String collectionName, List<String> partitionNames) {
        if (partitionNames.isEmpty()) throw new BaseException(ErrorCode.PARTITION_NOT_FOUND);

        if (!hasCollection(collectionName)) {
            throw new BaseException(ErrorCode.COLLECTION_NOT_FOUND);
        }

        for (String partitionName : partitionNames) {
            if (!hasPartition(collectionName, partitionName)) {
                throw new BaseException(ErrorCode.PARTITION_NOT_FOUND);
            }
        }

        ReleasePartitionsReq releasePartitionsReq = ReleasePartitionsReq.builder()
                .collectionName(collectionName)
                .partitionNames(partitionNames)
                .build();

        milvusClient.releasePartitions(releasePartitionsReq);

        return true;
    }

    /**
     * load = true, unload = false
     *
     * @return Boolean
     */
    public Boolean getLoadState(String collectionName, String partitionName) {
        GetLoadStateReq loadStateReq = GetLoadStateReq.builder()
                .collectionName(collectionName)
                .partitionName(partitionName)
                .build();

        return milvusClient.getLoadState(loadStateReq);
    }

    @Deprecated
    public Boolean addDescription(String collectionName, String partitionName, String description) {
        DescribeCollectionReq describeCollectionReq = DescribeCollectionReq.builder()
                .collectionName(collectionName)
                .build();

        DescribeCollectionResp describeCollectionResp = milvusClient.describeCollection(describeCollectionReq);

        return true;
    }
}

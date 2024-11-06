package com.ssafy.flowstudio.api.service.rag;

import com.google.gson.JsonObject;
import com.ssafy.flowstudio.api.service.rag.request.KnowledgeCreateServiceRequest;
import com.ssafy.flowstudio.api.service.rag.response.ChunkListResponse;
import com.ssafy.flowstudio.api.service.rag.response.ChunkResponse;
import com.ssafy.flowstudio.api.service.rag.response.KnowledgeCreateServiceResponse;
import com.ssafy.flowstudio.api.service.rag.response.KnowledgeResponse;
import com.ssafy.flowstudio.common.exception.BaseException;
import com.ssafy.flowstudio.common.exception.ErrorCode;
import com.ssafy.flowstudio.common.util.MilvusUtils;
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

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class VectorStoreService {
    private static final Logger log = LoggerFactory.getLogger(VectorStoreService.class);
    private final MilvusClientV2 milvusClient;
    private final LangchainService langchainService;
    private final MilvusUtils milvusUtils;

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
                        .chunkId(Integer.parseInt(result.get("id").toString()))
                        .content(result.get("content").toString())
                        .build())
                .toList();
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

    public List<String> previewChunks(KnowledgeCreateServiceRequest request) {
        return langchainService.getSplitText(request.getChunkSize(), request.getChunkOverlap(), milvusUtils.getTextContent(request.getFile())).stream()
                .limit(5)
                .toList();
    }

    public List<String> searchVector(User user, KnowledgeResponse knowledge, String search) {
        String collectionName = milvusUtils.generateName(user.getId());
        String partitionName = milvusUtils.generateName(knowledge.getKnowledgeId());
        List<BaseVector> vectors = new ArrayList<>();
        EmbeddingModel embeddingModel = milvusUtils.generateEmbeddingModel();

        BaseVector baseVector = new FloatVec(embeddingModel.embed(search));
        vectors.add(baseVector);
        SearchReq searchReq = SearchReq.builder()
                .collectionName(collectionName)
                .partitionNames(List.of(partitionName))
                .data(vectors)
                .topK(1)
                .build();
        SearchResp searchResp = milvusClient.search(searchReq);

        List<Object> ids = new ArrayList<>();
        for (List<SearchResp.SearchResult> results : searchResp.getSearchResults()) {
            for (SearchResp.SearchResult result : results) {
                ids.add(result.getId());
            }
        }

        GetReq getReq = GetReq.builder()
                .collectionName(collectionName)
                .partitionName(partitionName)
                .ids(ids)
                .build();

        GetResp getResp = milvusClient.get(getReq);
        for (QueryResp.QueryResult result : getResp.getGetResults()) {
            log.info(result.toString());
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
     * @param collectionName
     * @param partitionName
     * @return
     */
    public Boolean getLoadState(String collectionName, String partitionName) {
        GetLoadStateReq loadStateReq = GetLoadStateReq.builder()
                    .collectionName(collectionName)
                    .partitionName(partitionName)
                    .build();

        return milvusClient.getLoadState(loadStateReq);
    }

    public Boolean addDescription(String collectionName, String partitionName, String description) {
        DescribeCollectionReq describeCollectionReq = DescribeCollectionReq.builder()
                .collectionName(collectionName)
                .build();

        DescribeCollectionResp describeCollectionResp = milvusClient.describeCollection(describeCollectionReq);

        return true;
    }
}

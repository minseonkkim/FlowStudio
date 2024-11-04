package com.ssafy.flowstudio.api.service.rag;

import com.google.gson.JsonObject;
import com.ssafy.flowstudio.api.service.rag.request.KnowledgeCreateServiceRequest;
import com.ssafy.flowstudio.api.service.rag.response.ChunkListResponse;
import com.ssafy.flowstudio.api.service.rag.response.ChunkResponse;
import com.ssafy.flowstudio.api.service.rag.response.KnowledgeResponse;
import com.ssafy.flowstudio.common.util.MilvusUtils;
import com.ssafy.flowstudio.domain.user.entity.User;
import io.milvus.param.collection.FlushParam;
import io.milvus.v2.client.MilvusClientV2;
import io.milvus.v2.common.ConsistencyLevel;
import io.milvus.v2.service.collection.request.CreateCollectionReq;
import io.milvus.v2.service.collection.request.DescribeCollectionReq;
import io.milvus.v2.service.collection.request.HasCollectionReq;
import io.milvus.v2.service.collection.response.DescribeCollectionResp;
import io.milvus.v2.service.partition.request.CreatePartitionReq;
import io.milvus.v2.service.partition.request.LoadPartitionsReq;
import io.milvus.v2.service.vector.request.*;
import io.milvus.v2.service.vector.request.data.BaseVector;
import io.milvus.v2.service.vector.request.data.FloatVec;
import io.milvus.v2.service.vector.response.GetResp;
import io.milvus.v2.service.vector.response.QueryResp;
import io.milvus.v2.service.vector.response.SearchResp;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.document.Document;
import org.springframework.ai.document.MetadataMode;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.writer.FileDocumentWriter;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class VectorStoreService {
    private final MilvusClientV2 milvusClient;
    private final LangchainService langchainService;
    private final MilvusUtils milvusUtils;

    public void createCollection(String collectionName) {
        HasCollectionReq hasCollectionReq = HasCollectionReq.builder()
                .collectionName(collectionName)
                .build();

        if (!milvusClient.hasCollection(hasCollectionReq)) {
            CreateCollectionReq createCollectionReq = CreateCollectionReq.builder()
                    .collectionName(collectionName)
                    .consistencyLevel(ConsistencyLevel.STRONG)
                    .dimension(milvusUtils.generateEmbeddingModel().dimensions())
                    .build();

            milvusClient.createCollection(createCollectionReq);
        }

        DescribeCollectionReq describeCollectionReq = DescribeCollectionReq.builder()
                .collectionName(collectionName)
                .build();

        DescribeCollectionResp describeCollectionResp = milvusClient.describeCollection(describeCollectionReq);
    }

    public void createPartition(String collectionName, String partitionName) {
        CreatePartitionReq createPartitionReq = CreatePartitionReq.builder()
                .collectionName(collectionName)
                .partitionName(partitionName)
                .build();
        milvusClient.createPartition(createPartitionReq);
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
        String partitionName = milvusUtils.generateName(knowledge.getKnowledgeId());
        loadPartition(collectionName, partitionName);

        QueryResp queryResp = milvusClient.query(QueryReq.builder()
                .collectionName(collectionName)
                .partitionNames(List.of(partitionName))
                .outputFields(List.of("id", "content")) // 조회하려는 필드명
                .filter(chunkId == -1 ? "id <= " + 400 : "id == " + chunkId)
                .build());

        return queryResp.getQueryResults().stream()
                .map(QueryResp.QueryResult::getEntity)
                .map(result -> ChunkResponse.builder()
                        .chunkId(result.get("id").toString())
                        .content(result.get("content").toString())
                        .build())
                .toList();
    }

    public void upsertChunk(User user, KnowledgeResponse knowledge, Long chunkId, String content) {
        String collectionName = milvusUtils.generateName(user.getId());
        String partitionName = milvusUtils.generateName(knowledge.getKnowledgeId());

        List<JsonObject> data = new ArrayList<>();
        data.add(milvusUtils.documentToJson(chunkId, content));

        UpsertReq upsertReq = UpsertReq.builder()
                .collectionName(collectionName)
                .partitionName(partitionName)
                .data(data)
                .build();
        milvusClient.upsert(upsertReq);
    }

    public void upsertDocument(String collectionName, String partitionName, KnowledgeCreateServiceRequest request) {
        String textContent = milvusUtils.getTextContent(request.getFile());
        List<Document> splitterDocuments = milvusUtils.textsToDocuments(langchainService.getSplitText(request.getChunkSize(), request.getChunkOverlap(), textContent));

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
    }

    public void deleteChunk(User user, KnowledgeResponse knowledge, Long chunkId) {
        milvusClient.delete(DeleteReq.builder()
                .collectionName(milvusUtils.generateName(user.getId()))
                .partitionName(milvusUtils.generateName(knowledge.getKnowledgeId()))
                .ids(List.of(chunkId))
                .build());
    }

    public List<String> previewChunks(KnowledgeCreateServiceRequest request) {
        return langchainService.getSplitText(request.getChunkSize(), request.getChunkOverlap(), milvusUtils.getTextContent(request.getFile())).stream()
                .limit(5)
                .toList();
    }


    public void search(User user, KnowledgeResponse knowledge, String search) {
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
        System.out.println(getResp.toString());
    }

    private void loadPartition(String collectionName, String partitionName) {
        LoadPartitionsReq loadPartitionsReq = LoadPartitionsReq.builder()
                .collectionName(collectionName)
                .partitionNames(List.of(partitionName))
                .build();

        milvusClient.loadPartitions(loadPartitionsReq);
    }
}

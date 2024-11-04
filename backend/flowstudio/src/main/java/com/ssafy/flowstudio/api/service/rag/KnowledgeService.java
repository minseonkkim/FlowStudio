package com.ssafy.flowstudio.api.service.rag;

import com.ssafy.flowstudio.api.service.rag.request.KnowledgeCreateServiceRequest;
import com.ssafy.flowstudio.api.service.rag.response.KnowledgeListResponse;
import com.ssafy.flowstudio.api.service.rag.response.KnowledgeResponse;
import com.ssafy.flowstudio.common.exception.BaseException;
import com.ssafy.flowstudio.common.exception.ErrorCode;
import com.ssafy.flowstudio.common.util.MilvusUtils;
import com.ssafy.flowstudio.domain.knowledge.entity.Knowledge;
import com.ssafy.flowstudio.domain.knowledge.entity.KnowledgeRepository;
import com.ssafy.flowstudio.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class KnowledgeService {
    private final KnowledgeRepository knowledgeRepository;
    private final VectorStoreService vectorStoreService;
    private final MilvusUtils milvusUtils;

    public List<KnowledgeListResponse> getKnowledges(User user) {
        List<Knowledge> knowledgeList = knowledgeRepository.findByUserId(user.getId());

        return knowledgeList.stream()
                .map(KnowledgeListResponse::from)
                .toList();
    }

    public KnowledgeResponse getKnowledge(User user, Long knowledgeId) {
        return KnowledgeResponse.from(knowledgeRepository.findById(knowledgeId)
                .orElseThrow(() -> new BaseException(ErrorCode.KNOWLEDGE_NOT_FOUND))
        );
    }

    @Transactional
    public KnowledgeResponse createKnowledge(User user, KnowledgeCreateServiceRequest request) {
        Knowledge knowledge = Knowledge.create(user, request.getFile().getOriginalFilename(), false);
        knowledgeRepository.save(knowledge);

        String collectionName = milvusUtils.generateName(user.getId());
        String partitionName = milvusUtils.generateName(knowledge.getId());

        vectorStoreService.createCollection(collectionName);
        vectorStoreService.createPartition(collectionName, partitionName);
        vectorStoreService.upsertDocument(collectionName, partitionName, request);

        return KnowledgeResponse.from(knowledge);
    }
}

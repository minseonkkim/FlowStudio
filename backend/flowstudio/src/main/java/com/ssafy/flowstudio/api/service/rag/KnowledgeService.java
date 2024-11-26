package com.ssafy.flowstudio.api.service.rag;

import com.ssafy.flowstudio.api.service.rag.request.KnowledgeCreateServiceRequest;
import com.ssafy.flowstudio.api.service.rag.request.KnowledgeServiceRequest;
import com.ssafy.flowstudio.api.service.rag.response.KnowledgeCreateServiceResponse;
import com.ssafy.flowstudio.api.service.rag.response.KnowledgeListResponse;
import com.ssafy.flowstudio.api.service.rag.response.KnowledgeResponse;
import com.ssafy.flowstudio.api.service.rag.response.KnowledgeSearchResponse;
import com.ssafy.flowstudio.common.exception.BaseException;
import com.ssafy.flowstudio.common.exception.ErrorCode;
import com.ssafy.flowstudio.common.util.MilvusUtils;
import com.ssafy.flowstudio.domain.knowledge.entity.Knowledge;
import com.ssafy.flowstudio.domain.knowledge.entity.KnowledgeRepository;
import com.ssafy.flowstudio.domain.node.entity.Retriever;
import com.ssafy.flowstudio.domain.node.repository.RetrieverRepository;
import com.ssafy.flowstudio.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class KnowledgeService {
    private final KnowledgeRepository knowledgeRepository;
    private final RetrieverRepository retrieverRepository;
    private final VectorStoreService vectorStoreService;
    private final MilvusUtils milvusUtils;

    public List<KnowledgeListResponse> getKnowledges(User user, int page, int limit) {
        PageRequest pageable = PageRequest.of(page, limit, Sort.by("createdAt").descending());

        List<Knowledge> knowledgeList = knowledgeRepository.findByUserId(user.getId(), pageable);

        return knowledgeList.stream()
                .map(KnowledgeListResponse::from)
                .toList();
    }

    public KnowledgeResponse getKnowledge(User user, Long knowledgeId) {
        Knowledge knowledge = knowledgeRepository.findByUserIdAndId(user.getId(), knowledgeId)
                .orElseThrow(() -> new BaseException(ErrorCode.KNOWLEDGE_NOT_FOUND));
        return KnowledgeResponse.from(knowledge);
    }

    public KnowledgeSearchResponse getKnowledge(Long knowledgeId) {
        Knowledge knowledge = knowledgeRepository.findByIdAndPublic(knowledgeId, true)
                .orElseThrow(() -> new BaseException(ErrorCode.KNOWLEDGE_NOT_FOUND));
        return KnowledgeSearchResponse.from(knowledge);
    }

    @Transactional
    public KnowledgeResponse createKnowledge(User user, KnowledgeCreateServiceRequest request) {
        Knowledge knowledge = Knowledge.create(user, request.getFile().getOriginalFilename(), true, 0);
        knowledgeRepository.save(knowledge);

        String collectionName = milvusUtils.generateName(user.getId());
        String partitionName = milvusUtils.generateName(knowledge.getId());

        vectorStoreService.createCollection(collectionName);
        vectorStoreService.createPartition(collectionName, partitionName);
        KnowledgeCreateServiceResponse knowledgeCreateServiceResponse = vectorStoreService.upsertDocument(collectionName, partitionName, request);
        if (!knowledgeCreateServiceResponse.getIsComplete()) {
            throw new BaseException(ErrorCode.KNOWLEDGE_INSERT_UNAVAILABLE);
        }

        knowledge.updateToken(knowledgeCreateServiceResponse.getTotalToken());
        knowledgeRepository.save(knowledge);

        return KnowledgeResponse.from(knowledge);
    }

    @Transactional
    public KnowledgeResponse updateKnowledge(User user, Long knowledgeId, KnowledgeServiceRequest request) {
        Knowledge knowledge = knowledgeRepository.findByUserIdAndId(user.getId(), knowledgeId)
                .orElseThrow(() -> new BaseException(ErrorCode.KNOWLEDGE_NOT_FOUND));

        knowledge.update(request.getTitle(), request.getIsPublic());
        knowledgeRepository.save(knowledge);

        return KnowledgeResponse.from(knowledge);
    }

    @Transactional
    public boolean deleteKnowledge(User user, Long knowledgeId) {
        Knowledge knowledge = knowledgeRepository.findByUserIdAndId(user.getId(), knowledgeId)
                .orElseThrow(() -> new BaseException(ErrorCode.KNOWLEDGE_NOT_FOUND));

        List<Retriever> retrievers =  retrieverRepository.findByKnowledgeId(knowledgeId);
        retrievers.forEach(retriever -> retriever.updateKnowledge(null));

        knowledgeRepository.delete(knowledge);
        return true;
    }
}

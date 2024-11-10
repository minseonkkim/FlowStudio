package com.ssafy.flowstudio.api.service.node;

import com.ssafy.flowstudio.api.service.node.request.RetrieverUpdateServiceRequest;
import com.ssafy.flowstudio.api.service.node.response.RetrieverResponse;
import com.ssafy.flowstudio.common.exception.BaseException;
import com.ssafy.flowstudio.common.exception.ErrorCode;
import com.ssafy.flowstudio.domain.knowledge.entity.KnowledgeRepository;
import com.ssafy.flowstudio.domain.node.entity.Retriever;
import com.ssafy.flowstudio.domain.node.repository.RetrieverRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RetrieverService {
    private final RetrieverRepository retrieverRepository;
    private final KnowledgeRepository knowledgeRepository;

    @Transactional
    public RetrieverResponse updateRetriever(RetrieverUpdateServiceRequest request) {
        Retriever retriever = retrieverRepository.findById(request.getNodeId())
                .orElseThrow(() -> new BaseException(ErrorCode.NODE_NOT_FOUND));

        retriever.update(
                knowledgeRepository.findById(request.getKnowledgeId()).orElseThrow(() -> new BaseException(ErrorCode.KNOWLEDGE_NOT_FOUND)),
                request.getIntervalTime(),
                request.getScoreThreshold(),
                request.getTopK(),
                request.getQuery()
        );

        retrieverRepository.save(retriever);

        return RetrieverResponse.from(retriever);
    }
}

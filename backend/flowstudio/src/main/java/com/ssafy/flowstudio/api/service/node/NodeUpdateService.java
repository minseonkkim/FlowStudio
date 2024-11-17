package com.ssafy.flowstudio.api.service.node;

import com.ssafy.flowstudio.api.service.node.request.update.AnswerUpdateServiceRequest;
import com.ssafy.flowstudio.api.service.node.request.update.RetrieverUpdateServiceRequest;
import com.ssafy.flowstudio.api.service.node.request.update.LlmUpdateServiceRequest;
import com.ssafy.flowstudio.api.service.node.request.update.QuestionClassifierUpdateServiceRequest;
import com.ssafy.flowstudio.api.service.node.request.update.StartUpdateServiceRequest;
import com.ssafy.flowstudio.api.service.node.response.AnswerResponse;
import com.ssafy.flowstudio.api.service.node.response.LlmResponse;
import com.ssafy.flowstudio.api.service.node.response.QuestionClassifierResponse;
import com.ssafy.flowstudio.api.service.node.response.RetrieverResponse;
import com.ssafy.flowstudio.api.service.node.response.StartResponse;
import com.ssafy.flowstudio.api.service.node.response.detail.LlmDetailResponse;
import com.ssafy.flowstudio.common.exception.BaseException;
import com.ssafy.flowstudio.common.exception.ErrorCode;
import com.ssafy.flowstudio.domain.knowledge.entity.KnowledgeRepository;
import com.ssafy.flowstudio.domain.node.entity.Answer;
import com.ssafy.flowstudio.domain.node.entity.Coordinate;
import com.ssafy.flowstudio.domain.node.entity.LLM;
import com.ssafy.flowstudio.domain.node.entity.QuestionClassifier;
import com.ssafy.flowstudio.domain.node.entity.Retriever;
import com.ssafy.flowstudio.domain.node.entity.Start;
import com.ssafy.flowstudio.domain.node.repository.AnswerRepository;
import com.ssafy.flowstudio.domain.node.repository.LlmRepository;
import com.ssafy.flowstudio.domain.node.repository.NodeRepository;
import com.ssafy.flowstudio.domain.node.repository.QuestionClassifierRepository;
import com.ssafy.flowstudio.domain.node.repository.RetrieverRepository;
import com.ssafy.flowstudio.domain.node.repository.StartRepository;
import com.ssafy.flowstudio.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class NodeUpdateService {

    private final StartRepository startRepository;
    private final QuestionClassifierRepository questionClassifierRepository;
    private final RetrieverRepository retrieverRepository;
    private final LlmRepository llmRepository;
    private final KnowledgeRepository knowledgeRepository;
    private final AnswerRepository answerRepository;

    /**
     * 시작 노드 업데이트
     */
    public StartResponse updateStart(User user, Long nodeId, StartUpdateServiceRequest request) {
        Start start = startRepository.findById(nodeId)
                .orElseThrow(() -> new BaseException(ErrorCode.NODE_NOT_FOUND));

        if (!start.getChatFlow().getOwner().equals(user)) {
            throw new BaseException(ErrorCode.FORBIDDEN);
        }

        Coordinate coordinate = Coordinate.create(request.getCoordinate().getX(), request.getCoordinate().getY());
        start.update(request.getName(), coordinate, request.getMaxLength());

        return StartResponse.from(start);
    }

    /**
     * 질문분류기 노드 업데이트
     */
    public QuestionClassifierResponse updateQuestionClassifier(User user, Long nodeId, QuestionClassifierUpdateServiceRequest request) {
        QuestionClassifier questionClassifier = questionClassifierRepository.findById(nodeId)
                .orElseThrow(() -> new BaseException(ErrorCode.NODE_NOT_FOUND));

        if (!questionClassifier.getChatFlow().getOwner().equals(user)) {
            throw new BaseException(ErrorCode.FORBIDDEN);
        }

        Coordinate coordinate = Coordinate.create(request.getCoordinate().getX(), request.getCoordinate().getY());
        questionClassifier.update(request.getName(), coordinate);

        return QuestionClassifierResponse.from(questionClassifier);
    }


    /**
     * LLM 노드 업데이트
     */
    public LlmDetailResponse updateLlm(User user, Long nodeId, LlmUpdateServiceRequest request) {
        LLM llm = llmRepository.findById(nodeId)
                .orElseThrow(() -> new BaseException(ErrorCode.NODE_NOT_FOUND));

        if (!llm.getChatFlow().getOwner().equals(user)) {
            throw new BaseException(ErrorCode.FORBIDDEN);
        }

        Coordinate coordinate = Coordinate.create(request.getCoordinate().getX(), request.getCoordinate().getY());
        llm.update(
                request.getName(),
                coordinate,
                request.getPromptSystem(),
                request.getPromptUser(),
                request.getContext(),
                request.getTemperature(),
                request.getMaxTokens()
        );


        return LlmDetailResponse.from(llm);
    }

    /**
     * 지식검색기 노드 업데이트
     */
    public RetrieverResponse updateRetriever(User user, Long nodeId, RetrieverUpdateServiceRequest request) {
        Retriever retriever = retrieverRepository.findById(nodeId)
                .orElseThrow(() -> new BaseException(ErrorCode.NODE_NOT_FOUND));

        if (!retriever.getChatFlow().getOwner().equals(user)) {
            throw new BaseException(ErrorCode.FORBIDDEN);
        }

        Coordinate coordinate = Coordinate.create(request.getCoordinate().getX(), request.getCoordinate().getY());
        retriever.update(
                request.getName(),
                coordinate,
                request.getKnowledgeId() == null ? null : knowledgeRepository.findById(request.getKnowledgeId()).orElseThrow(() -> new BaseException(ErrorCode.KNOWLEDGE_NOT_FOUND)),
                request.getIntervalTime(),
                request.getScoreThreshold(),
                request.getTopK(),
                request.getQuery()
        );

        return RetrieverResponse.from(retriever);
    }

    /**
     * 답변 노드 업데이트
     */
    public AnswerResponse updateAnswer(User user, Long nodeId, AnswerUpdateServiceRequest request) {
        Answer answer = answerRepository.findById(nodeId)
                .orElseThrow(() -> new BaseException(ErrorCode.NODE_NOT_FOUND));

        if (!answer.getChatFlow().getOwner().equals(user)) {
            throw new BaseException(ErrorCode.FORBIDDEN);
        }

        Coordinate coordinate = Coordinate.create(request.getCoordinate().getX(), request.getCoordinate().getY());
        answer.update(request.getName(), coordinate, request.getOutputMessage());

        return AnswerResponse.from(answer);
    }

}

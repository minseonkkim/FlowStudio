package com.ssafy.flowstudio.api.service.node;

import com.ssafy.flowstudio.api.service.node.response.QuestionClassResponse;
import com.ssafy.flowstudio.api.service.node.request.QuestionClassCreateServiceRequest;
import com.ssafy.flowstudio.api.service.node.request.QuestionClassUpdateServiceRequest;
import com.ssafy.flowstudio.common.exception.BaseException;
import com.ssafy.flowstudio.common.exception.ErrorCode;
import com.ssafy.flowstudio.domain.edge.entity.Edge;
import com.ssafy.flowstudio.domain.edge.repository.EdgeRepository;
import com.ssafy.flowstudio.domain.node.entity.QuestionClass;
import com.ssafy.flowstudio.domain.node.entity.QuestionClassifier;
import com.ssafy.flowstudio.domain.node.repository.NodeRepository;
import com.ssafy.flowstudio.domain.node.repository.QuestionClassRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class QuestionClassService {

    private final NodeRepository nodeRepository;
    private final QuestionClassRepository questionClassRepository;

    public QuestionClassResponse createQuestionClass(Long nodeId) {
        QuestionClassifier questionClassifier = (QuestionClassifier) nodeRepository.findById(nodeId).orElseThrow(
                () -> new BaseException(ErrorCode.NODE_NOT_FOUND)
        );

        QuestionClass questionClass = QuestionClass.empty();
        questionClass.updateQuestionClassifier(questionClassifier);

        questionClassRepository.save(questionClass);

        return QuestionClassResponse.from(questionClass);
    }

    public QuestionClassResponse updateQuestionClass(Long questionClassId, QuestionClassUpdateServiceRequest request) {
        QuestionClass questionClass = questionClassRepository.findById(questionClassId).orElseThrow(
                () -> new BaseException(ErrorCode.QUESTION_CLASS_NOT_FOUND)
        );

        questionClass.update(request.getContent());

        return QuestionClassResponse.from(questionClass);
    }
}

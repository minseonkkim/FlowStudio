package com.ssafy.flowstudio.domain.node.repository;

import com.ssafy.flowstudio.domain.node.entity.QuestionClass;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface QuestionClassRepository extends JpaRepository<QuestionClass, Long> {

    @Query(
            "select c from QuestionClass c"
            + " join QuestionClassifier q"
            + " on c.questionClassifier.id = q.id"
            + " where q.chatFlow.id = :chatFlowId"
    )
    List<QuestionClass> findByChatFlowId(Long chatFlowId);
}

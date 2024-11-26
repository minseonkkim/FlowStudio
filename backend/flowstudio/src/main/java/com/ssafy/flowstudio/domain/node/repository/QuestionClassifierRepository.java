package com.ssafy.flowstudio.domain.node.repository;

import com.ssafy.flowstudio.domain.node.entity.QuestionClassifier;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface QuestionClassifierRepository extends JpaRepository<QuestionClassifier, Long> {

    @Query("SELECT q FROM QuestionClassifier q JOIN q.questionClasses qc WHERE qc.id = :questionClassId")
    Optional<QuestionClassifier> findByQuestionClassId(
            @Param("questionClassId") Long questionClassId
    );

}

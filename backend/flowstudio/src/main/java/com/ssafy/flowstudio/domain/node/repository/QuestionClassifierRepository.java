package com.ssafy.flowstudio.domain.node.repository;

import com.ssafy.flowstudio.domain.node.entity.QuestionClassifier;
import com.ssafy.flowstudio.domain.node.entity.Start;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuestionClassifierRepository extends JpaRepository<QuestionClassifier, Long> {
}

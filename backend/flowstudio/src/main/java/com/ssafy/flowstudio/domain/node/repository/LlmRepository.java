package com.ssafy.flowstudio.domain.node.repository;

import com.ssafy.flowstudio.domain.node.entity.LLM;
import com.ssafy.flowstudio.domain.node.entity.QuestionClass;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LlmRepository extends JpaRepository<LLM, Long> {
}

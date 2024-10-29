package com.ssafy.flowstudio.domain.chatflow.repository;

import com.ssafy.flowstudio.domain.chatflow.entity.ChatFlow;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ChatFlowRepository extends JpaRepository<ChatFlow, Long> {
    Optional<ChatFlow> findById(Long id);
}

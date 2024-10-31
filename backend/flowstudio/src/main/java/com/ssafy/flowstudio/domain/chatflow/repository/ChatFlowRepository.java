package com.ssafy.flowstudio.domain.chatflow.repository;

import com.ssafy.flowstudio.domain.chatflow.entity.ChatFlow;
import com.ssafy.flowstudio.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ChatFlowRepository extends JpaRepository<ChatFlow, Long>, CustomChatFlowRepository{
    Optional<ChatFlow> findById(Long id);
}

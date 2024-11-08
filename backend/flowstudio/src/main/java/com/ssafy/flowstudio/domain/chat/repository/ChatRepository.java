package com.ssafy.flowstudio.domain.chat.repository;

import com.ssafy.flowstudio.domain.chat.entity.Chat;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatRepository extends JpaRepository<Chat, Long> {
}

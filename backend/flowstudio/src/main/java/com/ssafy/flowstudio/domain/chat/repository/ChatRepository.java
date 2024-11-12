package com.ssafy.flowstudio.domain.chat.repository;

import com.ssafy.flowstudio.domain.chat.entity.Chat;
import com.ssafy.flowstudio.domain.chatflow.entity.ChatFlow;
import com.ssafy.flowstudio.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ChatRepository extends JpaRepository<Chat, Long> {

    @Query("SELECT c FROM Chat c WHERE c.chatFlow = :chatFlow AND c.user = :user AND c.isPreview = false")
    List<Chat> findByChatFlowAndUser(ChatFlow chatFlow, User user);

}

package com.ssafy.flowstudio.domain.chatflow.repository;

import com.ssafy.flowstudio.domain.chatflow.entity.ChatFlow;
import com.ssafy.flowstudio.domain.node.entity.Node;
import com.ssafy.flowstudio.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ChatFlowRepository extends JpaRepository<ChatFlow, Long>, CustomChatFlowRepository{

    @Query("SELECT c FROM ChatFlow c " +
            "LEFT JOIN FETCH c.nodes n " +
            "WHERE c.id = :id and c.owner.id = :userId ")
    Optional<ChatFlow> findByIdAndUserId(Long id, Long userId);

    List<ChatFlow> findByOwnerAndIsPublic(User owner, boolean isPublic);

    List<ChatFlow> findByIsPublicTrue();

    @Query("SELECT c FROM ChatFlow c " +
            "LEFT JOIN FETCH c.tests " +
            "WHERE c.id = :chatFlowId")
    Optional<ChatFlow> findByIdWithTests(Long chatFlowId);

    @Query("SELECT c FROM ChatFlow c " +
            "LEFT JOIN FETCH c.nodes n " +
            "WHERE c.id = :id")
    Optional<ChatFlow> findByIdWithNodes(Long id);

    @Query("SELECT c FROM ChatFlow c " +
            "JOIN FETCH c.tests t " +
            "WHERE c.owner.id = :userId ")
    List<ChatFlow> findByOwnerWithTest(Long userId);
}

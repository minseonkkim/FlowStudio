package com.ssafy.flowstudio.publish;

import com.ssafy.flowstudio.domain.chatflow.entity.ChatFlow;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface PublishChatFlowRepository extends JpaRepository<ChatFlow, Long> {
    Optional<ChatFlow> findById(Long id);

    @Query("select c " +
            "from ChatFlow c " +
            "left join fetch c.nodes " +
            "where c.publishUrl = :publishUrl " +
            "and c.isPublic = true"
    )
    Optional<ChatFlow> findByPublishUrl(String publishUrl);
}

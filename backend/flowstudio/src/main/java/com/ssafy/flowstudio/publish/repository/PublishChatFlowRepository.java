package com.ssafy.flowstudio.publish.repository;

import com.ssafy.flowstudio.domain.chatflow.entity.ChatFlow;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface PublishChatFlowRepository extends JpaRepository<ChatFlow, Long> {
    @Query("select c " +
            "from ChatFlow c " +
            "left join fetch c.nodes " +
            "where length(c.publishUrl) > 0 " +
            "and c.owner.id = :userId "
    )
    List<ChatFlow> findByUserId(Long userId);

    @Query("select c " +
            "from ChatFlow c " +
            "left join fetch c.nodes " +
            "where length(c.publishUrl) > 0 " +
            "and c.id = :publishChatFlowId"
    )
    Optional<ChatFlow> findById(Long publishChatFlowId);

    @Query("select c " +
            "from ChatFlow c " +
            "left join fetch c.nodes " +
            "where c.publishUrl = :publishUrl "
    )
    Optional<ChatFlow> findByPublishUrl(String publishUrl);

    @Query("SELECT c FROM ChatFlow c " +
            "LEFT JOIN FETCH c.nodes n " +
            "WHERE c.id = :id and c.owner.id = :userId ")
    Optional<ChatFlow> findByIdAndUserId(Long id, Long userId);
}

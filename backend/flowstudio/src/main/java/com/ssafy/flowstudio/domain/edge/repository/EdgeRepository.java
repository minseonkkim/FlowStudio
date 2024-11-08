package com.ssafy.flowstudio.domain.edge.repository;

import com.ssafy.flowstudio.domain.edge.entity.Edge;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface EdgeRepository extends JpaRepository<Edge, Long> {
    @Query(
            "select e from Edge e"
            + " join e.sourceNode n"
            + " where n.chatFlow.id = :chatFlowId"
    )
    List<Edge> findByChatFlowId(
            @Param("chatFlowId") Long chatFlowId
    );

    Optional<Edge> findBySourceConditionId(Long sourceConditionId);
}

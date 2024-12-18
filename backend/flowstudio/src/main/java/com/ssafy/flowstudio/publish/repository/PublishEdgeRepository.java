package com.ssafy.flowstudio.publish.repository;

import com.ssafy.flowstudio.domain.edge.entity.Edge;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PublishEdgeRepository extends JpaRepository<Edge, Long> {
    @Query(
            "select e from Edge e"
            + " join e.sourceNode n"
            + " where n.chatFlow.id = :chatFlowId"
    )
    List<Edge> findByChatFlowId(
            @Param("chatFlowId") Long chatFlowId
    );

    List<Edge> findAllBySourceConditionId(Long sourceConditionId);
}

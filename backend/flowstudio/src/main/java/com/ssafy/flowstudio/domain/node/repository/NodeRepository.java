package com.ssafy.flowstudio.domain.node.repository;

import com.ssafy.flowstudio.domain.node.entity.Node;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface NodeRepository extends JpaRepository<Node, Long> {
    @Query(
            "select n from Node n where n.chatFlow.id = :chatFlowId"
    )
    List<Node> findByChatFlowId(@Param("chatFlowId") Long chatFlowId);

    @Query("SELECT n FROM Node n " +
            "LEFT JOIN FETCH n.outputEdges " +
            "WHERE n.id IN :nodeIds")
    List<Node> findNodesWithOutputEdges(List<Long> nodeIds);
}

package com.ssafy.flowstudio.domain.node.repository;

import com.ssafy.flowstudio.domain.node.entity.Node;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface NodeRepository extends JpaRepository<Node, Long> {

    @Query(value = "WITH RECURSIVE node_tree AS (" +
            "  SELECT * FROM node AS n WHERE id = :nodeId " +
            "  UNION ALL " +
            "  SELECT * " +
            "  FROM node cn " +
            "  JOIN node_tree nt ON cn.id = nt.id" +
            ") " +
            "SELECT * FROM category_tree", nativeQuery = true)
    List<Node> findPrecedingNodesById(
            @Param("nodeId") Long nodeId
    );

    @Query(
            "select n from Node n where n.chatFlow.id = :chatFlowId"
    )
    List<Node> findByChatFlowId(
            @Param("chatFlowId") Long chatFlowId
    );
}

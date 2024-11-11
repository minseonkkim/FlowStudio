package com.ssafy.flowstudio.domain.node.repository;

import com.ssafy.flowstudio.domain.node.entity.Node;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface NodeRepository extends JpaRepository<Node, Long> {

    @Query(
            "select n from Node n where n.chatFlow.id = :chatFlowId"
    )
    List<Node> findByChatFlowId(
            @Param("chatFlowId") Long chatFlowId
    );
}

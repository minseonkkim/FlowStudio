package com.ssafy.flowstudio.domain.edge.entity;

import com.ssafy.flowstudio.domain.BaseEntity;
import com.ssafy.flowstudio.domain.node.entity.Node;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Edge extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "edge_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "source_node_id", nullable = false)
    private Node sourceNode;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "target_node_id", nullable = false)
    private Node targetNode;

    @Column
    private Long sourceConditionId;

    @Builder
    private Edge(Long id, Node sourceNode, Node targetNode, Long sourceConditionId) {
        this.id = id;
        this.sourceNode = sourceNode;
        this.targetNode = targetNode;
        this.sourceConditionId = sourceConditionId;
    }

    public static Edge create(Node sourceNode, Node targetNode) {
        return Edge.builder()
                .sourceNode(sourceNode)
                .targetNode(targetNode)
                .sourceConditionId(0L)
                .build();
    }

    public static Edge create(Node sourceNode, Node targetNode, Long sourceConditionId) {
        return Edge.builder()
                .sourceNode(sourceNode)
                .targetNode(targetNode)
                .sourceConditionId(sourceConditionId)
                .build();
    }

    public void update(Node sourceNode, Node targetNode, Long sourceConditionId) {
        this.sourceNode = sourceNode;
        this.targetNode = targetNode;
        this.sourceConditionId = sourceConditionId;
    }

}


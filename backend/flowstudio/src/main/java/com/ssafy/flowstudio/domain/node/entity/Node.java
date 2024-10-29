package com.ssafy.flowstudio.domain.node.entity;

import com.ssafy.flowstudio.domain.BaseEntity;
import com.ssafy.flowstudio.domain.edge.entity.Edge;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.OneToMany;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class Node extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "node_id")
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private NodeType type;

    @Column(nullable = false)
    @Embedded
    private Coordinate coordinate;

    @OneToMany(mappedBy = "sourceNode", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Edge> sourceEdges = new ArrayList<>();

    @OneToMany(mappedBy = "targetNode", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Edge> targetEdges = new ArrayList<>();


    protected Node(Long id, String name, NodeType type, Coordinate coordinate) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.coordinate = coordinate;
    }

}


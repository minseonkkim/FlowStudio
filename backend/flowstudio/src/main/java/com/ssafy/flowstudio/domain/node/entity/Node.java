package com.ssafy.flowstudio.domain.node.entity;

import com.ssafy.flowstudio.domain.BaseEntity;
import com.ssafy.flowstudio.domain.chat.entity.Chat;
import com.ssafy.flowstudio.domain.chatflow.entity.ChatFlow;
import com.ssafy.flowstudio.domain.edge.entity.Edge;
import jakarta.persistence.*;
import lombok.AccessLevel;
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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "chat_flow_id", nullable = false)
    private ChatFlow chatFlow;

    @Column(nullable = false)
    protected String name;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private NodeType type;

    @Column(nullable = false)
    @Embedded
    protected Coordinate coordinate;

    @OneToMany(mappedBy = "sourceNode", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Edge> outputEdges = new ArrayList<>();

    @OneToMany(mappedBy = "targetNode", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Edge> inputEdges = new ArrayList<>();

    protected Node(Long id, ChatFlow chatFlow, String name, NodeType type, Coordinate coordinate) {
        this.id = id;
        this.chatFlow = chatFlow;
        this.name = name;
        this.type = type;
        this.coordinate = coordinate;
    }

    public abstract void accept(NodeVisitor visitor, Chat chat);


}


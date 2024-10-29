package com.ssafy.flowstudio.domain.chatflow.entity;

import com.ssafy.flowstudio.domain.BaseEntity;
import com.ssafy.flowstudio.domain.node.entity.Node;
import com.ssafy.flowstudio.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class ChatFlow extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "chat_flow_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id", nullable = false)
    private User owner;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id", nullable = false)
    private User author;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;

    @Column(nullable = false)
    private String title;

    @Column
    private String description;

    @Column
    private boolean isPublic;

    @Column
    private int shareCount;

    @Column
    private String publishUrl;

    @OneToMany(mappedBy = "chatFlow", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Node> nodes = new ArrayList<>();

    @Builder
    private ChatFlow(Long id, User owner, User author, Category category, String title, String description, boolean isPublic, int shareCount, String publishUrl) {
        this.id = id;
        this.owner = owner;
        this.author = author;
        this.category = category;
        this.title = title;
        this.description = description;
        this.isPublic = isPublic;
        this.shareCount = shareCount;
        this.publishUrl = publishUrl;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChatFlow chatFlow = (ChatFlow) o;
        return Objects.equals(id, chatFlow.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    public static ChatFlow create(User owner, User author, Category category, String title, String description) {
        return ChatFlow.builder()
                .owner(owner)
                .author(author)
                .category(category)
                .title(title)
                .description(description)
                .isPublic(false)
                .shareCount(0)
                .publishUrl("")
                .build();
    }

}

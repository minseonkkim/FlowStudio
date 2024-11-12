package com.ssafy.flowstudio.domain.chatflow.entity;

import com.ssafy.flowstudio.domain.BaseEntity;
import com.ssafy.flowstudio.domain.chat.entity.Chat;
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

    @Column(nullable = false)
    private String title;

    @Column
    private String description;

    @Column
    private String thumbnail;

    @Column
    private boolean isPublic;

    @Column
    private int shareCount;

    @Column
    private String publishUrl;

    @OneToMany(mappedBy = "chatFlow", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ChatFlowCategory> categories = new ArrayList<>();

    @OneToMany(mappedBy = "chatFlow", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Node> nodes = new ArrayList<>();

    @OneToMany(mappedBy = "chatFlow", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Chat> chats = new ArrayList<>();

    @Builder
    private ChatFlow(Long id, User owner, User author, String title, String description, String thumbnail, boolean isPublic, int shareCount, String publishUrl) {
        this.id = id;
        this.owner = owner;
        this.author = author;
        this.title = title;
        this.description = description;
        this.thumbnail = thumbnail;
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

    public static ChatFlow create(User owner, User author, String title, String description, String thumbnail) {
        return ChatFlow.builder()
                .owner(owner)
                .author(author)
                .title(title)
                .description(description)
                .thumbnail(thumbnail)
                .isPublic(false)
                .publishUrl("")
                .build();
    }

    public void update(String title, String description, String thumbnail, List<Category> categories) {
        this.title = title;
        this.description = description;
        this.thumbnail = thumbnail;

        updateCategories(categories);
    }

    public void updateCategories(List<Category> categories) {
        this.categories.clear();
        for (Category category : categories) {
            this.categories.add(ChatFlowCategory.create(this, category));
        }
    }

    public void updatePublishUrl(String publishUrl) {
        this.publishUrl = publishUrl;
    }

    public void addNode(Node node) {
        this.nodes.add(node);
    }

}

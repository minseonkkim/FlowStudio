package com.ssafy.flowstudio.domain.chatflow.entity;

import com.ssafy.flowstudio.domain.BaseEntity;
import com.ssafy.flowstudio.domain.user.entity.User;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class ChatFlow extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "chat_flow_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id")
    private User owner;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id")
    private User author;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;

    @Column(nullable = false)
    private String name;

    @Column
    private String description;

    @Column
    private boolean isPublic;

    @Column
    private int shareCount;

    @Column
    private String publishUrl;

    @Builder
    private ChatFlow(Long id, User owner, User author, Category category, String name, String description, boolean isPublic, int shareCount, String publishUrl) {
        this.id = id;
        this.owner = owner;
        this.author = author;
        this.category = category;
        this.name = name;
        this.description = description;
        this.isPublic = isPublic;
        this.shareCount = shareCount;
        this.publishUrl = publishUrl;
    }

    public static ChatFlow create(User owner, User author, Category category, String name, String description, boolean isPublic, int shareCount, String publishUrl) {
        return ChatFlow.builder()
                .owner(owner)
                .author(author)
                .category(category)
                .name(name)
                .description(description)
                .isPublic(isPublic)
                .shareCount(shareCount)
                .publishUrl(publishUrl)
                .build();
    }

}

package com.ssafy.flowstudio.domain.chatflow.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.ssafy.flowstudio.domain.chatflow.entity.ChatFlow;
import com.ssafy.flowstudio.domain.user.entity.User;
import lombok.RequiredArgsConstructor;

import java.util.List;

import static com.ssafy.flowstudio.domain.chatflow.entity.QChatFlow.chatFlow;
import static com.ssafy.flowstudio.domain.chatflow.entity.QChatFlowCategory.chatFlowCategory;

@RequiredArgsConstructor
public class CustomChatFlowRepositoryImpl implements CustomChatFlowRepository {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<ChatFlow> findByUser(User user) {
        return jpaQueryFactory
                .selectFrom(chatFlow)
                .join(chatFlow.author).fetchJoin()
                .leftJoin(chatFlow.categories, chatFlowCategory).fetchJoin()
                .leftJoin(chatFlowCategory.category).fetchJoin()
                .where(chatFlow.author.eq(user))
                .fetch();
    }

}

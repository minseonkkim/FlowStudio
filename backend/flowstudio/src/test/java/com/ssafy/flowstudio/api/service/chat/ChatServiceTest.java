package com.ssafy.flowstudio.api.service.chat;

import com.ssafy.flowstudio.api.service.chat.response.ChatDetailResponse;
import com.ssafy.flowstudio.api.service.chat.response.ChatListResponse;
import com.ssafy.flowstudio.domain.chat.entity.Chat;
import com.ssafy.flowstudio.domain.chatflow.entity.ChatFlow;
import com.ssafy.flowstudio.domain.chatflow.repository.ChatFlowRepository;
import com.ssafy.flowstudio.domain.user.entity.User;
import com.ssafy.flowstudio.domain.user.repository.UserRepository;
import com.ssafy.flowstudio.support.IntegrationTestSupport;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;
import static org.junit.jupiter.api.Assertions.*;

@Transactional
@ActiveProfiles("test")
class ChatServiceTest extends IntegrationTestSupport {

    @Autowired
    private ChatService chatService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ChatFlowRepository chatFlowRepository;

    @DisplayName("채팅 목록 조회")
    @Test
    void getChats() {
        // given
        User user = User.builder()
                .username("username")
                .build();

        ChatFlow chatFlow = ChatFlow.builder()
                .author(user)
                .owner(user)
                .title("title")
                .thumbnail("thumbnail")
                .build();

        Chat chat1 = Chat.builder()
                .chatFlow(chatFlow)
                .user(user)
                .isPreview(false)
                .title("title1")
                .messageList("[]")
                .build();

        Chat chat2 = Chat.builder()
                .chatFlow(chatFlow)
                .user(user)
                .isPreview(false)
                .title("title2")
                .messageList("[]")
                .build();

        Chat chat3 = Chat.builder()
                .chatFlow(chatFlow)
                .user(user)
                .isPreview(true)
                .title("title3")
                .messageList("[]")
                .build();


        chatFlow.getChats().add(chat1);
        chatFlow.getChats().add(chat2);
        chatFlow.getChats().add(chat3);

        userRepository.save(user);
        chatFlowRepository.save(chatFlow);

        // when
        ChatListResponse response = chatService.getChats(user, chatFlow.getId(), 0, 20);

        // then
        assertThat(response).isNotNull()
                .extracting("id", "title", "thumbnail")
                .containsExactly(chatFlow.getId(), chatFlow.getTitle(), chatFlow.getThumbnail());
        assertThat(response.getChats()).hasSize(2)
                .extracting("id", "title")
                .containsExactlyInAnyOrder(
                        tuple(chat1.getId(), chat1.getTitle()),
                        tuple(chat2.getId(), chat2.getTitle())
                );
    }

    @DisplayName("채팅 상세 조회")
    @Test
    void getChat() {
        // given
        User user = User.builder()
                .username("username")
                .build();

        ChatFlow chatFlow = ChatFlow.builder()
                .author(user)
                .owner(user)
                .title("title")
                .thumbnail("thumbnail")
                .build();

        Chat chat1 = Chat.builder()
                .chatFlow(chatFlow)
                .user(user)
                .isPreview(false)
                .title("title1")
                .messageList("[]")
                .build();

        chatFlow.getChats().add(chat1);

        userRepository.save(user);
        chatFlowRepository.save(chatFlow);

        // when
        ChatDetailResponse response = chatService.getChat(user, chatFlow.getId(), chat1.getId());

        // then
        assertThat(response).isNotNull()
                .extracting("id", "title", "messageList")
                .containsExactly(chat1.getId(), chat1.getTitle(), chat1.getMessageList());
    }

}
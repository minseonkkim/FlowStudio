package com.ssafy.flowstudio.api.controller.node;

import com.ssafy.flowstudio.api.controller.node.request.CoordinateRequest;
import com.ssafy.flowstudio.api.controller.node.request.NodeCreateRequest;
import com.ssafy.flowstudio.api.service.chatflow.response.CoordinateResponse;
import com.ssafy.flowstudio.api.service.chatflow.response.EdgeResponse;
import com.ssafy.flowstudio.api.service.node.request.NodeCreateServiceRequest;
import com.ssafy.flowstudio.api.service.node.response.ModelListResponse;
import com.ssafy.flowstudio.api.service.node.response.NodeCreateResponse;
import com.ssafy.flowstudio.api.service.node.response.SimpleNodeResponse;
import com.ssafy.flowstudio.api.service.node.response.detail.AnswerDetailResponse;
import com.ssafy.flowstudio.domain.chatflow.entity.Category;
import com.ssafy.flowstudio.domain.chatflow.entity.ChatFlow;
import com.ssafy.flowstudio.domain.node.entity.Coordinate;
import com.ssafy.flowstudio.domain.node.entity.ModelName;
import com.ssafy.flowstudio.domain.node.entity.NodeType;
import com.ssafy.flowstudio.domain.node.entity.Start;
import com.ssafy.flowstudio.domain.user.entity.User;
import com.ssafy.flowstudio.support.ControllerTestSupport;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.ResultActions;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class NodeControllerTest extends ControllerTestSupport {

    @DisplayName("타입에 따라 노드를 생성한다")
    @WithMockUser
    @Test
    void createNode() throws Exception {
        // given
        CoordinateRequest coordinateRequest = CoordinateRequest.builder()
                .x(1)
                .y(1)
                .build();

        User user = User.builder()
                .id(1L)
                .username("test")
                .build();

        ChatFlow chatFlow = ChatFlow.create(user, user, null, "title", "description");

        NodeCreateRequest request = NodeCreateRequest.builder()
                .chatFlowId(1L)
                .coordinate(coordinateRequest)
                .type(NodeType.START)
                .build();

        AnswerDetailResponse answerNodeDetailResponse = AnswerDetailResponse.builder()
                .nodeId(1L)
                .name("Answer")
                .type(NodeType.ANSWER)
                .outputMessage("AI 답변 템플릿")
                .precedingNodes(new ArrayList<>())
                .coordinate(CoordinateResponse.from(Coordinate.builder().x(1.0F).y(1.0F).build()))
                .build();

        given(nodeService.createNode(any(User.class), any(NodeCreateServiceRequest.class)))
                .willReturn(answerNodeDetailResponse);

        // when
        ResultActions perform = mockMvc.perform(
                post("/api/v1/chat-flows/nodes")
                        .with(csrf())
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON));

        // then
        perform.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("200"))
                .andExpect(jsonPath("$.status").value("OK"))
                .andExpect(jsonPath("$.message").value("OK"))
                .andExpect(jsonPath("$.data").exists());
    }

    @DisplayName("노드 아이디로 노드를 삭제한다.")
    @WithMockUser
    @Test
    void deleteNode() throws Exception {
        // given

        // when
        ResultActions perform = mockMvc.perform(
                delete("/api/v1/chat-flows/nodes/{nodeId}", 1L)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON));

        // then
        perform.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("200"))
                .andExpect(jsonPath("$.status").value("OK"))
                .andExpect(jsonPath("$.message").value("OK"))
                .andExpect(jsonPath("$.data").isEmpty());
    }


    @DisplayName("모델 목록을 조회한다.")
    @WithMockUser
    @Test
    void getModels() throws Exception {
        // given
        ModelListResponse response1 = ModelListResponse.builder()
                .provider(ModelName.GPT_4_O.getProvider().name())
                .name(ModelName.GPT_4_O.name())
                .maxTokens(ModelName.GPT_4_O.getMaxTokens())
                .build();

        ModelListResponse response2 = ModelListResponse.builder()
                .provider(ModelName.CLAUDE_3_5_SONNET.getProvider().name())
                .name(ModelName.CLAUDE_3_5_SONNET.name())
                .maxTokens(ModelName.CLAUDE_3_5_SONNET.getMaxTokens())
                .build();

        given(nodeService.getModels()).willReturn(List.of(response1, response2));

        // when
        ResultActions perform = mockMvc.perform(
                get("/api/v1/chat-flows/nodes/models")
                        .contentType(MediaType.APPLICATION_JSON));

        // then
        perform.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("200"))
                .andExpect(jsonPath("$.status").value("OK"))
                .andExpect(jsonPath("$.message").value("OK"))
                .andExpect(jsonPath("$.data").exists());
    }
}
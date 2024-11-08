package com.ssafy.flowstudio.api.controller.user;

import com.ssafy.flowstudio.api.controller.user.request.ApiKeyRequest;
import com.ssafy.flowstudio.api.service.user.request.ApiKeyServiceRequest;
import com.ssafy.flowstudio.api.service.user.response.ApiKeyResponse;
import com.ssafy.flowstudio.domain.user.entity.ApiKey;
import com.ssafy.flowstudio.domain.user.entity.User;
import com.ssafy.flowstudio.support.ControllerTestSupport;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.ResultActions;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class ApiKeyControllerTest extends ControllerTestSupport {

    @DisplayName("사용자의 Api Key를 수정한다.")
    @WithMockUser
    @Test
    void updateApiKey() throws Exception {
        // given
        ApiKeyRequest apiKeyRequest = ApiKeyRequest.builder()
                .openAiKey("openai_or_null")
                .claudeKey("claude_or_null")
                .geminiKey("gemini_or_null")
                .clovaKey("clova_or_null")
                .build();

        User user = User.builder()
                .id(1L)
                .username("test")
                .build();

        ApiKey apiKey = ApiKey.builder()
                .openAiKey("openai_or_null")
                .claudeKey("claude_or_null")
                .geminiKey("gemini_or_null")
                .clovaKey("clova_or_null")
                .build();

        ApiKeyResponse response = ApiKeyResponse.from(apiKey);

        given(apiKeyService.updateApiKey(any(User.class), any(ApiKeyServiceRequest.class)))
                .willReturn(response);

        // when
        ResultActions perform = mockMvc.perform(
                put("/api/v1/users/keys")
                        .with(csrf())
                        .content(objectMapper.writeValueAsString(apiKeyRequest))
                        .contentType(MediaType.APPLICATION_JSON));

        // then
        perform.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("200"))
                .andExpect(jsonPath("$.status").value("OK"))
                .andExpect(jsonPath("$.message").value("OK"))
                .andExpect(jsonPath("$.data").exists());
    }

    @DisplayName("사용자의 Api Key를 조회한다.")
    @WithMockUser
    @Test
    void getApiKey() throws Exception {
        // given
        ApiKey apiKey = ApiKey.builder()
                .openAiKey("my_openai_key")
                .claudeKey("my_claude_key")
                .geminiKey("my_gemini_key")
                .clovaKey("my_clova_key")
                .build();

        User user = User.builder()
                .id(1L)
                .username("test")
                .apiKey(apiKey)
                .build();

        given(apiKeyService.getApiKey(any(User.class)))
                .willReturn(ApiKeyResponse.from(apiKey));

        // when
        ResultActions perform = mockMvc.perform(get("/api/v1/users/keys"));

        // then
        perform.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("200"))
                .andExpect(jsonPath("$.status").value("OK"))
                .andExpect(jsonPath("$.message").value("OK"))
                .andExpect(jsonPath("$.data").exists())
                .andExpect(jsonPath("$.data.openAiKey").value("my_openai_key"))
                .andExpect(jsonPath("$.data.claudeKey").value("my_claude_key"))
                .andExpect(jsonPath("$.data.geminiKey").value("my_gemini_key"))
                .andExpect(jsonPath("$.data.clovaKey").value("my_clova_key"));
    }

    @DisplayName("사용자의 빈 Api Key를 조회한다.")
    @WithMockUser
    @Test
    void getEmptyApiKey() throws Exception {
        // given
        ApiKey apiKey = ApiKey.empty();

        User user = User.builder()
                .id(1L)
                .username("test")
                .apiKey(apiKey)
                .build();

        given(apiKeyService.getApiKey(any(User.class)))
                .willReturn(ApiKeyResponse.from(apiKey));

        // when
        ResultActions perform = mockMvc.perform(get("/api/v1/users/keys"));

        // then
        perform.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("200"))
                .andExpect(jsonPath("$.status").value("OK"))
                .andExpect(jsonPath("$.message").value("OK"))
                .andExpect(jsonPath("$.data").exists())
                .andExpect(jsonPath("$.data.openAiKey").value(org.hamcrest.Matchers.nullValue()))
                .andExpect(jsonPath("$.data.claudeKey").value(org.hamcrest.Matchers.nullValue()))
                .andExpect(jsonPath("$.data.geminiKey").value(org.hamcrest.Matchers.nullValue()))
                .andExpect(jsonPath("$.data.clovaKey").value(org.hamcrest.Matchers.nullValue()));
    }
}
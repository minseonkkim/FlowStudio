package com.ssafy.flowstudio.docs.user;

import com.epages.restdocs.apispec.ResourceSnippetParameters;
import com.ssafy.flowstudio.api.controller.user.ApiKeyController;
import com.ssafy.flowstudio.api.controller.user.request.ApiKeyRequest;
import com.ssafy.flowstudio.api.service.user.ApiKeyService;
import com.ssafy.flowstudio.api.service.user.request.ApiKeyServiceRequest;
import com.ssafy.flowstudio.api.service.user.response.ApiKeyResponse;
import com.ssafy.flowstudio.api.service.user.response.UserResponse;
import com.ssafy.flowstudio.docs.RestDocsSupport;
import com.ssafy.flowstudio.domain.user.entity.ApiKey;
import com.ssafy.flowstudio.domain.user.entity.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.ResultActions;

import static com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper.document;
import static com.epages.restdocs.apispec.ResourceDocumentation.resource;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.put;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class ApiKeyControllerDocsTest extends RestDocsSupport {

    private final ApiKeyService apiKeyService = mock(ApiKeyService.class);

    @Override
    protected Object initController() {
        return new ApiKeyController(apiKeyService);
    }

    @DisplayName("사용자의 Api Key를 수정한다.")
    @Test
    void updateApiKey() throws Exception {
        // given
        User user = User.builder()
                .id(1L)
                .username("test")
                .build();

        ApiKeyRequest apiKeyRequest = ApiKeyRequest.builder()
                .openAiKey("openai_or_null")
                .claudeKey("claude_or_null")
                .geminiKey("gemini_or_null")
                .clovaKey("clova_or_null")
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
        perform
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("update-api-key",
                        preprocessResponse(prettyPrint()),
                        resource(ResourceSnippetParameters.builder()
                                .tag("ApiKey")
                                .summary("API Key 업데이트")
                                .requestFields(
                                        fieldWithPath("openAiKey").type(JsonFieldType.STRING)
                                                .description("OpenAI API 키"),
                                        fieldWithPath("claudeKey").type(JsonFieldType.STRING)
                                                .description("Claude API 키"),
                                        fieldWithPath("geminiKey").type(JsonFieldType.STRING)
                                                .description("Gemini API 키"),
                                        fieldWithPath("clovaKey").type(JsonFieldType.STRING)
                                                .description("CLOVA API 키")
                                )
                                .responseFields(
                                        fieldWithPath("openAiKey").type(JsonFieldType.STRING)
                                                .description("OpenAI API 키"),
                                        fieldWithPath("claudeKey").type(JsonFieldType.STRING)
                                                .description("Claude API 키"),
                                        fieldWithPath("geminiKey").type(JsonFieldType.STRING)
                                                .description("Gemini API 키"),
                                        fieldWithPath("clovaKey").type(JsonFieldType.STRING)
                                                .description("CLOVA API 키")
                                )
                                .build())));
    }
}

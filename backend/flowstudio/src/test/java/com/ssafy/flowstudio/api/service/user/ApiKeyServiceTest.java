package com.ssafy.flowstudio.api.service.user;

import com.ssafy.flowstudio.api.controller.user.request.ApiKeyRequest;
import com.ssafy.flowstudio.api.service.user.response.ApiKeyResponse;
import com.ssafy.flowstudio.domain.user.entity.ApiKey;
import com.ssafy.flowstudio.domain.user.entity.User;
import com.ssafy.flowstudio.domain.user.repository.UserRepository;
import com.ssafy.flowstudio.support.IntegrationTestSupport;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.assertThat;

class ApiKeyServiceTest extends IntegrationTestSupport {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ApiKeyService apiKeyService;

    @DisplayName("사용자의 Open AI Api Key를 등록한다.")
    @Test
    void updateApiKey() {
        // given
        ApiKeyRequest apiKeyRequest = ApiKeyRequest.builder()
                .openAiKey("openai_key")
                .build();

        User user = User.builder()
                .id(1L)
                .username("test")
                .apiKey(ApiKey.empty())
                .build();

        userRepository.save(user);

        // when
        ApiKeyResponse apiKeyResponse = apiKeyService.updateApiKey(user, apiKeyRequest.toServiceRequest());

        // then
        assertThat(apiKeyResponse).isNotNull()
                .extracting("openAiKey")
                .isEqualTo("openai_key");
    }

    @DisplayName("사용자의 Open AI Api Key와 Geminy Key를 동시에 등록한다.")
    @Test
    void updateTwoApiKeys() {
        // given
        ApiKeyRequest apiKeyRequest = ApiKeyRequest.builder()
                .openAiKey("openai_key")
                .geminiKey("gemini_key")
                .build();

        User user = User.builder()
                .id(1L)
                .username("test")
                .apiKey(ApiKey.empty())
                .build();

        userRepository.save(user);

        // when
        ApiKeyResponse apiKeyResponse = apiKeyService.updateApiKey(user, apiKeyRequest.toServiceRequest());

        // then
        assertThat(apiKeyResponse).isNotNull()
                .extracting("openAiKey", "geminiKey")
                .containsExactly("openai_key", "gemini_key");
    }
}
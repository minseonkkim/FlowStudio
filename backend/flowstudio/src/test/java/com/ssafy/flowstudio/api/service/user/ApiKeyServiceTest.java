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

    @DisplayName("사용자의 OpenAI Api Key를 등록한다.")
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

    @DisplayName("사용자의 OpenAI Api Key와 Gemini Key를 동시에 등록한다.")
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

    @DisplayName("사용자의 OpenAI API 키를 지운다.")
    @Test
    void deleteOpenAIApiKey() {
        // given
        ApiKeyRequest apiKeyRequest = ApiKeyRequest.builder()
                .openAiKey(null)
                .geminiKey("gemini_key")
                .build();

        User user = User.builder()
                .id(1L)
                .username("test")
                .apiKey(ApiKey.builder()
                        .openAiKey("openai_key")
                        .geminiKey("gemini_key")
                        .build())
                .build();

        userRepository.save(user);

        // when
        ApiKeyResponse apiKeyResponse = apiKeyService.updateApiKey(user, apiKeyRequest.toServiceRequest());

        // then
        assertThat(apiKeyResponse).isNotNull()
                .extracting("openAiKey", "geminiKey")
                .containsExactly(null, "gemini_key");
    }

    @DisplayName("사용자의 Api Key를 조회한다.")
    @Test
    void getApiKey() {
        // given
        ApiKey apiKey = ApiKey.builder()
                .openAiKey("openai_key")
                .geminiKey("gemini_key")
                .build();

        User user = User.builder()
                .id(1L)
                .username("test")
                .apiKey(apiKey)
                .build();

        userRepository.save(user);

        // when
        ApiKeyResponse apiKeyResponse = apiKeyService.getApiKey(user);

        // then
        assertThat(apiKeyResponse).isNotNull()
                .extracting("openAiKey", "geminiKey")
                .containsExactly("openai_key", "gemini_key");
    }

    @DisplayName("사용자의 빈 Api Key를 조회한다.")
    @Test
    void getEmptyApiKey() {
        // given
        ApiKey apiKey = ApiKey.empty();

        User user = User.builder()
                .id(1L)
                .username("test")
                .apiKey(apiKey)
                .build();

        userRepository.save(user);

        // when
        ApiKeyResponse apiKeyResponse = apiKeyService.getApiKey(user);

        // then
        assertThat(apiKeyResponse).isNotNull()
                .extracting("openAiKey", "claudeKey", "geminiKey", "clovaKey")
                .containsExactly(null, null, null, null);
    }
}
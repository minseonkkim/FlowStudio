package com.ssafy.flowstudio.api.service.user;

import com.ssafy.flowstudio.api.controller.user.request.ApiKeyRequest;
import com.ssafy.flowstudio.api.service.user.request.ApiKeyServiceRequest;
import com.ssafy.flowstudio.api.service.user.response.ApiKeyResponse;
import com.ssafy.flowstudio.domain.user.entity.ApiKey;
import com.ssafy.flowstudio.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class ApiKeyService {
    public ApiKeyResponse updateApiKey(User user, ApiKeyServiceRequest request) {
        ApiKey apiKey = user.getApiKey();

        apiKey.update(request.getOpenAiKey(), request.getClaudeKey(), request.getGeminiKey(), request.getClovaKey());

        return ApiKeyResponse.from(apiKey);
    }
}

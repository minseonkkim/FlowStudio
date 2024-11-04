package com.ssafy.flowstudio.api.controller.user;


import com.ssafy.flowstudio.api.controller.user.request.ApiKeyRequest;
import com.ssafy.flowstudio.api.service.user.ApiKeyService;
import com.ssafy.flowstudio.api.service.user.response.ApiKeyResponse;
import com.ssafy.flowstudio.common.annotation.CurrentUser;
import com.ssafy.flowstudio.common.payload.ApiResponse;
import com.ssafy.flowstudio.domain.user.entity.User;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class ApiKeyController {

    private final ApiKeyService apiKeyService;

    /**
     * Api Key 수정
     * @param user
     * @param request
     * @return ApiKeyResponse
     */
    @PutMapping("api/v1/users/keys")
    public ApiResponse<ApiKeyResponse> updateApiKey(
            @CurrentUser User user,
            @Valid @RequestBody ApiKeyRequest request
    ) {
        return ApiResponse.ok(apiKeyService.updateApiKey(user, request.toServiceRequest()));
    }

    /**
     * Api Key 수정
     * @param user
     * @return ApiKeyResponse
     */
    @GetMapping("api/v1/users/keys")
    public ApiResponse<ApiKeyResponse> getApiKey(
            @CurrentUser User user
    ) {
        return ApiResponse.ok(apiKeyService.getApiKey(user));
    }
}

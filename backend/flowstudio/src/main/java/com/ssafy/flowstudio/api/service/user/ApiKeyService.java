package com.ssafy.flowstudio.api.service.user;

import com.ssafy.flowstudio.api.service.user.request.ApiKeyServiceRequest;
import com.ssafy.flowstudio.api.service.user.response.ApiKeyResponse;
import com.ssafy.flowstudio.api.service.user.response.UserResponse;
import com.ssafy.flowstudio.common.exception.BaseException;
import com.ssafy.flowstudio.common.exception.ErrorCode;
import com.ssafy.flowstudio.domain.user.entity.ApiKey;
import com.ssafy.flowstudio.domain.user.entity.User;
import com.ssafy.flowstudio.domain.user.repository.UserRepository;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.encrypt.AesBytesEncryptor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class ApiKeyService {

    private final AesBytesEncryptor encryptor;
    private final EntityManager em;
    private final UserRepository userRepository;

    @Transactional
    public void updateApiKey(Long userId, ApiKeyServiceRequest request) {
        User user = userRepository.findById(userId).orElseThrow(
                () -> new BaseException(ErrorCode.NOT_FOUND_USER)
        );

        ApiKey apiKey = user.getApiKey();

        apiKey.update(
                encrypt(request.getOpenAiKey()),
                encrypt(request.getClaudeKey()),
                encrypt(request.getGeminiKey()),
                encrypt(request.getClovaKey())
        );
    }

    public ApiKeyResponse getApiKey(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(
                () -> new BaseException(ErrorCode.NOT_FOUND_USER)
        );

        ApiKey apiKey = user.getApiKey();

        return ApiKeyResponse.from(
                decrypt(apiKey.getOpenAiKey()),
                decrypt(apiKey.getClaudeKey()),
                decrypt(apiKey.getGeminiKey()),
                decrypt(apiKey.getClovaKey())
        );
    }

    public String encrypt(String rawApiKey) {
        if (rawApiKey == null) return null;
        return byteArrayToString(encryptor.encrypt(rawApiKey.getBytes(StandardCharsets.UTF_8)));
    }

    public String decrypt(String encryptedApiKey) {
        if (encryptedApiKey == null) return null;
        return new String(encryptor.decrypt(stringToByteArray(encryptedApiKey)), StandardCharsets.UTF_8);
    }

    public String byteArrayToString(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte abyte : bytes) {
            sb.append(abyte);
            sb.append(" ");
        }
        return sb.toString();
    }

    public byte[] stringToByteArray(String byteString) {
        String[] split = byteString.split("\\s");
        ByteBuffer buffer = ByteBuffer.allocate(split.length);
        for (String s : split) {
            buffer.put((byte) Integer.parseInt(s));
        }
        return buffer.array();
    }
}

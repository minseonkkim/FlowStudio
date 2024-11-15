package com.ssafy.flowstudio.api.service.user;

import com.ssafy.flowstudio.api.service.user.request.UserNicknameUpdateServiceRequest;
import com.ssafy.flowstudio.api.service.user.response.TokenUsageLogResponse;
import com.ssafy.flowstudio.api.service.user.response.UserResponse;
import com.ssafy.flowstudio.common.exception.BaseException;
import com.ssafy.flowstudio.common.exception.ErrorCode;
import com.ssafy.flowstudio.common.security.jwt.JWTService;
import com.ssafy.flowstudio.common.security.jwt.JwtToken;
import com.ssafy.flowstudio.common.service.S3ImageService;
import com.ssafy.flowstudio.domain.user.entity.TokenUsageLog;
import com.ssafy.flowstudio.domain.user.entity.TokenUsageLogRepository;
import com.ssafy.flowstudio.domain.user.entity.User;
import com.ssafy.flowstudio.domain.user.repository.UserRepository;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;
    private final S3ImageService s3ImageService;
    private final TokenUsageLogRepository tokenUsageLogRepository;
    private final JWTService jwtService;

    public UserResponse getUser(User user) {
        User findUser = userRepository.findByUsername(user.getUsername()).orElseThrow(
                () -> new BaseException(ErrorCode.NOT_FOUND_USER));

        return UserResponse.from(findUser);
    }

    public boolean checkNickname(String nickname) {
        if (userRepository.existsByNickname(nickname)) {
            throw new BaseException(ErrorCode.NICKNAME_ALREADY_EXISTS);
        }

        return true;
    }

    @Transactional
    public void updateNickname(User user, UserNicknameUpdateServiceRequest request) {
        if (userRepository.existsByNickname(request.getNickname())) {
            throw new BaseException(ErrorCode.NICKNAME_ALREADY_EXISTS);
        }

        user.updateNickname(request.getNickname());
        userRepository.save(user);
    }

    @Transactional
    public void updateProfileImage(User user, MultipartFile image) {
        String imageUrl = null;

        if (image != null && !image.isEmpty()) {
            imageUrl = s3ImageService.upload(image);
        }

        user.updateProfileImage(imageUrl);
        userRepository.save(user);
    }

    public List<TokenUsageLogResponse> getTokenUsageLogs(User user, LocalDate now) {
        LocalDateTime startDateTime = now.minusDays(90).atStartOfDay();
        List<TokenUsageLog> logs = tokenUsageLogRepository.findTokenUsageLogs(user.getId(), startDateTime);

        Map<LocalDate, Integer> dailyUsage = logs.stream()
                .collect(Collectors.groupingBy(
                        log -> log.getCreatedAt().toLocalDate(),
                        Collectors.summingInt(TokenUsageLog::getTokenUsage)
                ));


        return dailyUsage.entrySet().stream()
                .map(entry -> TokenUsageLogResponse.builder()
                        .date(entry.getKey())
                        .tokenUsage(entry.getValue())
                        .build())
                .collect(Collectors.toList());
    }

    @Transactional
    public UserResponse createAnonymousUser(HttpServletResponse servletResponse) {
        String username = "anonymous " + UUID.randomUUID().toString().substring(0, 10);
        User user = User.createAnonymous(username);
        userRepository.save(user);
        Collection<GrantedAuthority> collection = new ArrayList<>();
        collection.add((GrantedAuthority) () -> "ROLE_ANONYMOUS");

        JwtToken token = jwtService.generateToken(user.getUsername(), collection);
        servletResponse.setHeader("Authorization", token.getAccessToken());

        return UserResponse.from(user);
    }
}

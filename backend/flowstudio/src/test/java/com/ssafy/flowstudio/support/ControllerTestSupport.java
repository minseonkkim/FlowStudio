package com.ssafy.flowstudio.support;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ssafy.flowstudio.api.controller.chatflow.ChatFlowController;
import com.ssafy.flowstudio.api.controller.edge.EdgeController;
import com.ssafy.flowstudio.api.controller.node.NodeController;
import com.ssafy.flowstudio.api.controller.node.QuestionClassController;
import com.ssafy.flowstudio.api.controller.user.ApiKeyController;
import com.ssafy.flowstudio.api.controller.user.UserController;
import com.ssafy.flowstudio.api.service.auth.CustomUserService;
import com.ssafy.flowstudio.api.service.chatflow.ChatFlowService;
import com.ssafy.flowstudio.api.service.edge.EdgeService;
import com.ssafy.flowstudio.api.service.node.NodeService;
import com.ssafy.flowstudio.api.service.node.QuestionClassService;
import com.ssafy.flowstudio.api.service.user.ApiKeyService;
import com.ssafy.flowstudio.api.service.user.UserService;
import com.ssafy.flowstudio.common.config.SecurityConfig;
import com.ssafy.flowstudio.common.security.filter.JWTFilter;
import com.ssafy.flowstudio.domain.user.entity.ProviderType;
import com.ssafy.flowstudio.domain.user.entity.User;
import com.ssafy.flowstudio.domain.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@WebMvcTest(
        controllers = {
                UserController.class,
                NodeController.class,
                ChatFlowController.class,
                ApiKeyController.class,
                QuestionClassController.class,
                EdgeController.class,
        },
        excludeFilters = {
                @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = {SecurityConfig.class}),
                @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = {JWTFilter.class})
        }
)
@Import(CustomUserService.class)
public abstract class ControllerTestSupport {

    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    protected ObjectMapper objectMapper;

    @MockBean
    protected UserService userService;

    @MockBean
    protected NodeService nodeService;

    @MockBean
    protected ChatFlowService chatFlowService;

    @MockBean
    protected ApiKeyService apiKeyService;

    @MockBean
    protected EdgeService edgeService;

    @MockBean
    protected UserRepository userRepository;

    @MockBean
    protected QuestionClassService questionClassService;

    @BeforeEach
    public void setUp() {
        User user = User.create("testUser@gmail.com", "testUserNickname", "profileImage", ProviderType.GOOGLE);
        when(userRepository.findByUsername(any())).thenReturn(Optional.of(user));
    }

}

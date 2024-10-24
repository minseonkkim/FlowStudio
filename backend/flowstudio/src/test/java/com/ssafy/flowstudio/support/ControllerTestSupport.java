package com.ssafy.flowstudio.support;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ssafy.flowstudio.api.controller.auth.AuthController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(
        controllers = {
                AuthController.class,
        }
)
public abstract class ControllerTestSupport {

    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    protected ObjectMapper objectMapper;

}

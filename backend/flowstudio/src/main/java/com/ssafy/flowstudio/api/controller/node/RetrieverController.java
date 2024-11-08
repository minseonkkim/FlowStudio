package com.ssafy.flowstudio.api.controller.node;

import com.ssafy.flowstudio.api.controller.node.request.RetrieverUpdateRequest;
import com.ssafy.flowstudio.api.service.node.RetrieverService;
import com.ssafy.flowstudio.api.service.node.response.RetrieverResponse;
import com.ssafy.flowstudio.common.annotation.CurrentUser;
import com.ssafy.flowstudio.common.payload.ApiResponse;
import com.ssafy.flowstudio.domain.chat.repository.ChatRepository;
import com.ssafy.flowstudio.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/chat-flows/nodes/{nodeId}/retriever")
public class RetrieverController {
    private static final Logger log = LoggerFactory.getLogger(RetrieverController.class);
    private final RetrieverService retrieverService;
    private final ChatRepository chatRepository;

    @PutMapping
    public ApiResponse<RetrieverResponse> updateRetriever(
            @CurrentUser User user,
            @RequestBody RetrieverUpdateRequest request
    ) {
        return ApiResponse.ok(retrieverService.updateRetriever(request.toServiceRequest()));
    }
}

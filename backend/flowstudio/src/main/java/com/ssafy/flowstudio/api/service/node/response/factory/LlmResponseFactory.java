package com.ssafy.flowstudio.api.service.node.response.factory;

import com.ssafy.flowstudio.api.service.node.response.LlmResponse;
import com.ssafy.flowstudio.api.service.node.response.NodeResponse;
import com.ssafy.flowstudio.domain.node.entity.LLM;
import com.ssafy.flowstudio.domain.node.entity.Node;

public class LlmResponseFactory extends NodeResponseFactory {
    @Override
    public NodeResponse createNodeResponse(Node node) {
        return LlmResponse.from((LLM) node);
    }
}

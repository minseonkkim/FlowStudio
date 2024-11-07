package com.ssafy.flowstudio.api.service.node.response.factory;

import com.ssafy.flowstudio.api.service.node.response.NodeResponse;
import com.ssafy.flowstudio.api.service.node.response.StartResponse;
import com.ssafy.flowstudio.domain.node.entity.Node;
import com.ssafy.flowstudio.domain.node.entity.Start;

public class StartResponseFactory extends NodeResponseFactory {
    @Override
    public NodeResponse createNodeResponse(Node node) {
        return StartResponse.from((Start) node);
    }

    @Override
    public NodeResponse createNodeDetailResponse(Node node) {
        return StartResponse.from((Start) node);
    }
}

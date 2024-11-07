package com.ssafy.flowstudio.api.service.node.response.factory;

import com.ssafy.flowstudio.api.service.node.response.ConditionalResponse;
import com.ssafy.flowstudio.api.service.node.response.NodeResponse;
import com.ssafy.flowstudio.domain.node.entity.Conditional;
import com.ssafy.flowstudio.domain.node.entity.Node;

public class ConditionalResponseFactory extends NodeResponseFactory {
    @Override
    public NodeResponse createNodeResponse(Node node) {
        return ConditionalResponse.from((Conditional) node);
    }

    @Override
    public NodeResponse createNodeDetailResponse(Node node) {
        return ConditionalResponse.from((Conditional) node);
    }
}

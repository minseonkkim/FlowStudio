package com.ssafy.flowstudio.api.service.node.response.factory;

import com.ssafy.flowstudio.api.service.node.response.NodeResponse;
import com.ssafy.flowstudio.domain.node.entity.Node;

public abstract class NodeResponseFactory {
    public abstract NodeResponse createNodeResponse(Node node);
}

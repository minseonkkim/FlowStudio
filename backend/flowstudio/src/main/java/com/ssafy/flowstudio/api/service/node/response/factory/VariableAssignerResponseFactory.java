package com.ssafy.flowstudio.api.service.node.response.factory;

import com.ssafy.flowstudio.api.service.node.response.NodeResponse;
import com.ssafy.flowstudio.api.service.node.response.VariableAssignerResponse;
import com.ssafy.flowstudio.domain.node.entity.Node;
import com.ssafy.flowstudio.domain.node.entity.VariableAssigner;

public class VariableAssignerResponseFactory extends NodeResponseFactory {
    @Override
    public NodeResponse createNodeResponse(Node node) {
        return VariableAssignerResponse.from((VariableAssigner) node);
    }
}

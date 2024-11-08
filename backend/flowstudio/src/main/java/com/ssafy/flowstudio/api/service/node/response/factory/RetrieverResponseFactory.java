package com.ssafy.flowstudio.api.service.node.response.factory;

import com.ssafy.flowstudio.api.service.node.response.NodeResponse;
import com.ssafy.flowstudio.api.service.node.response.RetrieverResponse;
import com.ssafy.flowstudio.domain.node.entity.Node;
import com.ssafy.flowstudio.domain.node.entity.Retriever;

public class RetrieverResponseFactory extends NodeResponseFactory {
    @Override
    public NodeResponse createNodeResponse(Node node) {
        return RetrieverResponse.from((Retriever) node);
    }
}

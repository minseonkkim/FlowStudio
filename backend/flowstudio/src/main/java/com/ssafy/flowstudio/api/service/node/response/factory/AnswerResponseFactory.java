package com.ssafy.flowstudio.api.service.node.response.factory;

import com.ssafy.flowstudio.api.service.node.response.AnswerResponse;
import com.ssafy.flowstudio.api.service.node.response.NodeResponse;
import com.ssafy.flowstudio.domain.node.entity.Answer;
import com.ssafy.flowstudio.domain.node.entity.Node;

public class AnswerResponseFactory extends NodeResponseFactory {
    @Override
    public NodeResponse createNodeResponse(Node node) {
        return AnswerResponse.from((Answer) node);
    }

    @Override
    public NodeResponse createNodeDetailResponse(Node node) {
        return AnswerResponse.from((Answer) node);
    }
}

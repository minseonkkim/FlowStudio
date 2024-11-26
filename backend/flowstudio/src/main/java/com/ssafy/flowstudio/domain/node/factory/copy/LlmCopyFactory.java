package com.ssafy.flowstudio.domain.node.factory.copy;

import com.ssafy.flowstudio.domain.chatflow.entity.ChatFlow;
import com.ssafy.flowstudio.domain.node.entity.Coordinate;
import com.ssafy.flowstudio.domain.node.entity.LLM;
import com.ssafy.flowstudio.domain.node.entity.Node;
import com.ssafy.flowstudio.domain.node.entity.NodeType;

public class LlmCopyFactory extends NodeCopyFactory {
    @Override
    public Node copyNode(Node node, ChatFlow clonedChatFlow) {
        LLM originalLlm = (LLM) node;
        return LLM.builder()
                .chatFlow(clonedChatFlow)
                .name(originalLlm.getName())
                .type(NodeType.LLM)
                .coordinate(
                        Coordinate.builder().x(originalLlm.getCoordinate().getX())
                                .y(originalLlm.getCoordinate().getY())
                                .build()
                )
                .context(originalLlm.getContext())
                .temperature(originalLlm.getTemperature())
                .maxTokens(originalLlm.getMaxTokens())
                .modelName(originalLlm.getModelName())
                .promptSystem(originalLlm.getPromptSystem())
                .promptUser(originalLlm.getPromptUser())
                .build();
    }
}

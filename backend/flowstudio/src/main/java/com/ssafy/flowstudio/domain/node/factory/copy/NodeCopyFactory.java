package com.ssafy.flowstudio.domain.node.factory.copy;

import com.ssafy.flowstudio.domain.chatflow.entity.ChatFlow;
import com.ssafy.flowstudio.domain.knowledge.entity.Knowledge;
import com.ssafy.flowstudio.domain.node.entity.Node;

public abstract class NodeCopyFactory {
    public abstract Node copyNode(Node node, ChatFlow clonedChatFlow);

    public Node copyNode(Node node, ChatFlow clonedChatFlow, Knowledge knowledge) {
        return copyNode(node, clonedChatFlow);
    }

    public Node copyNode(Node node, ChatFlow clonedChatFlow, String clonedPromptSystem, String clonedPromptUser) {
        return copyNode(node, clonedChatFlow);
    }
}

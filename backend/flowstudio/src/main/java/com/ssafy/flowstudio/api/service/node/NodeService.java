package com.ssafy.flowstudio.api.service.node;

import com.ssafy.flowstudio.api.service.node.request.NodeCreateServiceRequest;
import com.ssafy.flowstudio.api.service.node.response.NodeCreateResponse;
import com.ssafy.flowstudio.api.service.node.response.NodeResponse;
import com.ssafy.flowstudio.api.service.node.response.factory.NodeResponseFactory;
import com.ssafy.flowstudio.api.service.node.response.factory.NodeResponseFactoryProvider;
import com.ssafy.flowstudio.common.exception.BaseException;
import com.ssafy.flowstudio.common.exception.ErrorCode;
import com.ssafy.flowstudio.domain.chatflow.entity.ChatFlow;
import com.ssafy.flowstudio.domain.chatflow.repository.ChatFlowRepository;
import com.ssafy.flowstudio.domain.node.entity.Coordinate;
import com.ssafy.flowstudio.domain.node.entity.Node;
import com.ssafy.flowstudio.domain.node.factory.NodeFactory;
import com.ssafy.flowstudio.domain.node.repository.NodeRepository;
import com.ssafy.flowstudio.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class NodeService {

    private final NodeRepository nodeRepository;
    private final ChatFlowRepository chatFlowRepository;
    private final NodeFactoryProvider nodeFactoryProvider;
    private final NodeResponseFactoryProvider nodeResponseFactoryProvider;

    @Transactional
    public NodeCreateResponse createNode(User user, NodeCreateServiceRequest request) {
        ChatFlow chatFlow = chatFlowRepository.findById(request.getChatFlowId())
                .orElseThrow(() -> new BaseException(ErrorCode.CHAT_FLOW_NOT_FOUND));

        if (!chatFlow.getOwner().equals(user)) {
            throw new BaseException(ErrorCode.FORBIDDEN);
        }

        NodeFactory factory = nodeFactoryProvider.getFactory(request.getType());
        Coordinate coordinate = Coordinate.create(request.getCoordinate().getY(), request.getCoordinate().getY());

        Node savedNode = nodeRepository.save(factory.createNode(chatFlow, coordinate));

        return NodeCreateResponse.from(savedNode);
    }

    @Transactional
    public boolean deleteNode(User user, Long nodeId) {
        Node node = nodeRepository.findById(nodeId)
                .orElseThrow(() -> new BaseException(ErrorCode.NODE_NOT_FOUND));

        if (!node.getChatFlow().getOwner().equals(user)) {
            throw new BaseException(ErrorCode.FORBIDDEN);
        }

        nodeRepository.delete(node);
        return true;
    }

    public NodeResponse getNode(Long nodeId) {
        Node node = nodeRepository.findById(nodeId)
                .orElseThrow(() -> new BaseException(ErrorCode.NODE_NOT_FOUND));

        NodeResponseFactory responseFactory = nodeResponseFactoryProvider.getFactory(node.getType());
        NodeResponse nodeDetailResponse = responseFactory.createNodeDetailResponse(node);

        // TODO : 선행노드 가져오기

        nodeDetailResponse.updatePrecedingNodes(null);
        return nodeDetailResponse;
    }
}

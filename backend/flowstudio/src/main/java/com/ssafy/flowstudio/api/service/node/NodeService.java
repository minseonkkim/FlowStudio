package com.ssafy.flowstudio.api.service.node;

import com.ssafy.flowstudio.api.service.node.request.NodeCreateServiceRequest;
import com.ssafy.flowstudio.api.service.node.response.NodeCreateResponse;
import com.ssafy.flowstudio.api.service.node.response.NodeResponse;
import com.ssafy.flowstudio.api.service.node.response.detail.NodeDetailResponse;
import com.ssafy.flowstudio.api.service.node.response.detail.NodeDetailResponseMapper;
import com.ssafy.flowstudio.common.exception.BaseException;
import com.ssafy.flowstudio.common.exception.ErrorCode;
import com.ssafy.flowstudio.domain.chatflow.entity.ChatFlow;
import com.ssafy.flowstudio.domain.chatflow.repository.ChatFlowRepository;
import com.ssafy.flowstudio.domain.edge.entity.Edge;
import com.ssafy.flowstudio.domain.edge.repository.EdgeRepository;
import com.ssafy.flowstudio.domain.node.entity.Coordinate;
import com.ssafy.flowstudio.domain.node.entity.Node;
import com.ssafy.flowstudio.domain.node.factory.NodeFactory;
import com.ssafy.flowstudio.domain.node.repository.NodeRepository;
import com.ssafy.flowstudio.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class NodeService {

    private final NodeRepository nodeRepository;
    private final ChatFlowRepository chatFlowRepository;
    private final NodeFactoryProvider nodeFactoryProvider;
    private final NodeDetailResponseMapper nodeDetailResponseMapper;
    private final EdgeRepository edgeRepository;

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

    public NodeDetailResponse getNode(User user, Long nodeId) {
        Node node = nodeRepository.findById(nodeId)
                .orElseThrow(() -> new BaseException(ErrorCode.NODE_NOT_FOUND));

        List<Node> precedingNodes = getPrecedingNodes(user, node);

        return nodeDetailResponseMapper.getCorrespondNodeDetailResponse(node, precedingNodes);
    }

    public List<Node> getPrecedingNodes(User user, Node node) {
        // 영속성 컨텍스트에 해당 ChatFlow의 모든 Edge들을 로드한다.
        List<Edge> edges = edgeRepository.findByChatFlowId(node.getChatFlow().getId());
        HashSet<Node> precedingNodes = new HashSet<>();
        traverse(node, precedingNodes);
        return precedingNodes.stream().toList();
    }

    public static void traverse(Node node, HashSet<Node> precedingNodes) {
        List<Edge> inputEdges = node.getInputEdges();
        for (Edge edge : inputEdges) {
            Node sourceNode = edge.getSourceNode();
            precedingNodes.add(sourceNode);
            traverse(sourceNode, precedingNodes);
        }
    }
}

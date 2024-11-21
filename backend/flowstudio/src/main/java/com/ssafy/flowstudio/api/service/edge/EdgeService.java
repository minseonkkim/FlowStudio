package com.ssafy.flowstudio.api.service.edge;

import com.ssafy.flowstudio.api.service.chatflow.response.EdgeResponse;
import com.ssafy.flowstudio.api.service.edge.request.EdgeServiceRequest;
import com.ssafy.flowstudio.common.exception.BaseException;
import com.ssafy.flowstudio.common.exception.ErrorCode;
import com.ssafy.flowstudio.domain.chatflow.entity.ChatFlow;
import com.ssafy.flowstudio.domain.chatflow.repository.ChatFlowRepository;
import com.ssafy.flowstudio.domain.edge.entity.Edge;
import com.ssafy.flowstudio.domain.edge.repository.EdgeRepository;
import com.ssafy.flowstudio.domain.node.entity.Node;
import com.ssafy.flowstudio.domain.node.entity.NodeType;
import com.ssafy.flowstudio.domain.node.entity.QuestionClassifier;
import com.ssafy.flowstudio.domain.node.repository.NodeRepository;
import com.ssafy.flowstudio.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class EdgeService {

    private final EdgeRepository edgeRepository;
    private final ChatFlowRepository chatFlowRepository;
    private final NodeRepository nodeRepository;

    @Transactional
    public EdgeResponse create(User user, Long chatFlowId, EdgeServiceRequest request) {
        ChatFlow chatFlow = chatFlowRepository.findById(chatFlowId)
                .orElseThrow(() -> new BaseException(ErrorCode.CHAT_FLOW_NOT_FOUND));

        if (!chatFlow.getOwner().equals(user)) {
            throw new BaseException(ErrorCode.FORBIDDEN);
        }

        Node sourceNode = nodeRepository.findById(request.getSourceNodeId())
                .orElseThrow(() -> new BaseException(ErrorCode.NODE_NOT_FOUND));
        Node targetNode = nodeRepository.findById(request.getTargetNodeId())
                .orElseThrow(() -> new BaseException(ErrorCode.NODE_NOT_FOUND));

        if (!canConnect(sourceNode, targetNode, request.getSourceConditionId())) {
            throw new BaseException(ErrorCode.MULTIPLE_EDGE_FORBIDDEN);
        }


        Edge savedEdge = Edge.create(sourceNode, targetNode, request.getSourceConditionId());
        edgeRepository.save(savedEdge);

        return EdgeResponse.from(savedEdge);
    }

    @Transactional
    public EdgeResponse update(User user, Long chatFlowId, Long edgeId, EdgeServiceRequest request) {
        ChatFlow chatFlow = chatFlowRepository.findById(chatFlowId)
                .orElseThrow(() -> new BaseException(ErrorCode.CHAT_FLOW_NOT_FOUND));

        if (!chatFlow.getOwner().equals(user)) {
            throw new BaseException(ErrorCode.FORBIDDEN);
        }

        Edge edge = edgeRepository.findById(edgeId)
                .orElseThrow(() -> new BaseException(ErrorCode.EDGE_NOT_FOUND));

        Node sourceNode = nodeRepository.findById(request.getSourceNodeId())
                .orElseThrow(() -> new BaseException(ErrorCode.NODE_NOT_FOUND));
        Node targetNode = nodeRepository.findById(request.getTargetNodeId())
                .orElseThrow(() -> new BaseException(ErrorCode.NODE_NOT_FOUND));

        if (!canConnect(sourceNode, targetNode, request.getSourceConditionId())) {
            throw new BaseException(ErrorCode.MULTIPLE_EDGE_FORBIDDEN);
        }

        edge.update(
                sourceNode,
                targetNode,
                request.getSourceConditionId()
        );

        return EdgeResponse.from(edge);
    }

    @Transactional
    public boolean delete(User user, Long chatFlowId, Long edgeId) {
        ChatFlow chatFlow = chatFlowRepository.findById(chatFlowId)
                .orElseThrow(() -> new BaseException(ErrorCode.CHAT_FLOW_NOT_FOUND));

        if (!chatFlow.getOwner().equals(user)) {
            throw new BaseException(ErrorCode.FORBIDDEN);
        }

        edgeRepository.findById(edgeId)
                .orElseThrow(() -> new BaseException(ErrorCode.EDGE_NOT_FOUND));

        edgeRepository.deleteById(edgeId);
        return true;
    }

    public List<Edge> getEdgeBySourceConditionId(Long sourceConditionId) {
        return edgeRepository.findAllBySourceConditionId(sourceConditionId);
    }

    public boolean canConnect(Node sourceNode, Node targetNode, Long sourceConditionId) {
        // Input Edge는 하나만 가질 수 있다.
        if (!targetNode.getInputEdges().isEmpty()) {
            return false;
        }

        // Source Condition을 가진 노드가 아니라면 OutputEdge는 하나만 가질 수 있다.
        if (sourceConditionId == 0 && !sourceNode.getOutputEdges().isEmpty()) {
            return false;
        }

        // 하나의 Source Condition당 하나의 Edge만 가질 수 있다.
        if (sourceConditionId != 0 && !getEdgeBySourceConditionId(sourceConditionId).isEmpty()) {
            return false;
        }

        return true;
    }
}

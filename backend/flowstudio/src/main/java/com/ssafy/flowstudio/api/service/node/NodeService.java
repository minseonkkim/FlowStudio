package com.ssafy.flowstudio.api.service.node;

import com.ssafy.flowstudio.api.service.node.request.NodeCreateServiceRequest;
import com.ssafy.flowstudio.api.service.node.response.NodeCreateResponse;
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
    private final NodeFactoryProvider factoryProvider;

    @Transactional
    public NodeCreateResponse createNode(User user, NodeCreateServiceRequest request) {
        System.out.println("request.getChatFlowId() = " + request.getChatFlowId());
        
        
        ChatFlow chatFlow = chatFlowRepository.findById(request.getChatFlowId())
                .orElseThrow(() -> new BaseException(ErrorCode.CHAT_FLOW_NOT_FOUND));

        if (!chatFlow.getOwner().equals(user)) {
            throw new BaseException(ErrorCode.FORBIDDEN);
        }

        NodeFactory factory = factoryProvider.getFactory(request.getType());
        Coordinate coordinate = Coordinate.create(request.getCoordinate().getY(), request.getCoordinate().getY());

        Node savedNode = nodeRepository.save(factory.createNode(coordinate));

        return NodeCreateResponse.from(savedNode);
    }

}

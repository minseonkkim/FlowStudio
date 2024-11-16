import { deleteNode, postNode } from "@/api/workflow";
import { NodeData, Knowledge } from "@/types/chatbot";
import { Dispatch, SetStateAction, useCallback } from "react";
import { Edge, Node } from "reactflow"

// 노드 생성 팩토리 함수
export const createNodeData = (
  params: Omit<NodeData, "onDelete" | "chatFlowId">, // onDelete와 chatFlowId를 제외
  chatFlowId: number, // chatFlowId를 외부에서 전달받음
  setNodes: Dispatch<SetStateAction<Node<any, string | undefined>[]>>, // 노드 상태 업데이트 함수
  setEdges: Dispatch<SetStateAction<Edge<any>[]>>, // 엣지 상태 업데이트 함수
  setSelectedNode: Dispatch<SetStateAction<Node<any, string | undefined> | null>>
): NodeData => {
  return {
    chatFlowId,
    nodeId: params.nodeId || 0,
    name: params.name || params.type + params.nodeId,
    type: params.type || "DEFAULT",
    coordinate: params.coordinate || { x: 1, y: 1 },
    outputEdges: params.outputEdges || [],
    inputEdges: params.inputEdges || [],
    onDelete: (nodeId: string) => {
      // 1. 연결된 엣지 삭제
      setEdges((eds) =>
        eds.filter((edge) => {
          const shouldDelete = edge.source === nodeId || edge.target === nodeId;
          return !shouldDelete; // 유지할 엣지만 반환
        })
      );

      // 2. 노드 삭제
      setNodes((nds) => nds.filter((node) => node.id !== nodeId)); // 상태에서 제거
      deleteNode(+nodeId); // API 호출
      setSelectedNode(null);
    },

    maxLength: params.maxLength || 10,

    outputMessage: params.outputMessage || "",

    // questionClasses: params.questionClasses || [],
    promptSystem: params.promptSystem || "",
    promptUser: params.promptUser || "",
    context: params.context || "",
    temperature: params.temperature || 0.7,
    maxTokens: params.maxTokens || 512,
    modelProvider: params.modelProvider || "",
    modelName: params.modelName || "",
    
    knowledge: params.knowledge || {} as Knowledge,
    knowledgeId: params.knowledgeId || params.knowledge?.knowledgeId,
    intervalTime: params.intervalTime || 1,
    topK: params.topK || 3,
  };
};

// 노드 추가
export const addNode = ((type: string, currentNode: Node, nodes: Node[], isDetail: Boolean) => {

  const isPositionOccupied = (x: number, y: number) => {
    return nodes.some(
      (node) =>
        Math.abs(node.position.x - x) < 200 &&
        Math.abs(node.position.y - y) < 160
    );
  };

  const newX = currentNode.position.x + (isDetail ? 200 : 0);
  let newY = currentNode.position.y;

  if (isDetail) {
    while (isPositionOccupied(newX, newY)) {
      newY += 160;
    }
  }

  const newNode = {
    chatFlowId: currentNode.data.chatFlowId,
    coordinate: {
      x: newX,
      y: newY,
    },
    nodeType: type,
  };

  console.log(currentNode);
  console.log(newNode);
  

  return postNode(newNode);
}
);


import { deleteNode, postNode } from "@/api/workflow";
import { NodeData, Knowledge, EdgeData } from "@/types/chatbot";
import { Dispatch, SetStateAction } from "react";
import { Edge, Node } from "reactflow"
import { deleteIconColors, nodeConfig } from "./nodeConfig";

// 노드 생성 팩토리 함수
export const createNodeData = (
  params: Omit<NodeData, "onDelete" | "chatFlowId">, // onDelete와 chatFlowId를 제외
  chatFlowId: number, // chatFlowId를 외부에서 전달받음
  setNodes: Dispatch<SetStateAction<Node<NodeData, string | undefined>[]>>, // 노드 상태 업데이트 함수
  setEdges: Dispatch<SetStateAction<Edge<EdgeData>[]>>, // 엣지 상태 업데이트 함수
  setSelectedNode: Dispatch<SetStateAction<Node<NodeData, string | undefined> | null>>
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
    scoreThreshold: params.scoreThreshold || 0.1,

    questionClasses: params.questionClasses || [],

    isComplete: false,
    isError: false,
  };
};

// 노드 추가
export const addNode = ((type: string, currentNode: Node, nodes: Node[], isDetail: boolean) => {

  // const isPositionOccupied = (x: number, y: number) => {
  //   return nodes.some(
  //     (node) =>
  //       Math.abs(node.position.x - x) < 200 &&
  //       Math.abs(node.position.y - y) < 160
  //   );
  // };

  const newX = currentNode.position.x + (isDetail ? 200 : 0);
  const newY = currentNode.position.y;

  // if (isDetail) {
  //   while (isPositionOccupied(newX, newY)) {
  //     newY += 160;
  //   }
  // }

  const newNode : NodeData = {
    ...currentNode.data,
    chatFlowId: currentNode.data.chatFlowId,
    coordinate:  {
      x: newX,
      y: newY,
    },
    type: type,
  };

  console.log(currentNode);
  console.log(newNode);
  

  return postNode(newNode);
}
);


/**
 * 디바운스 유틸리티 함수
 * @param func 실행할 함수
 * @param delay 지연 시간 (ms)
 */
export function debounce<T extends (...args: Parameters<T>) => void>(func: T, delay: number) {
  let timer: NodeJS.Timeout;

  return (...args: Parameters<T>) => {
    if (timer) {
      clearTimeout(timer); // 이전 타이머 취소
    }

    timer = setTimeout(() => {
      func(...args); // 지연 후 함수 실행
    }, delay);
  };
}


export const findAllParentNodes = (
  currentNodeId: string,
  nodes: Node<NodeData, string | undefined>[],
  edges: Edge<EdgeData>[]
): Node<NodeData, string | undefined>[] => {
  const visited = new Set<string>(); // 방문한 노드 ID를 추적
  const parentNodes: Node<NodeData, string | undefined>[] = [];

  const queue: string[] = [currentNodeId]; // BFS 탐색을 위한 큐

  while (queue.length > 0) {
    const nodeId = queue.shift(); // 큐에서 노드 ID를 하나 가져옴

    if (nodeId && !visited.has(nodeId)) {
      visited.add(nodeId); // 현재 노드 ID를 방문으로 처리

      // 현재 노드로 들어오는 간선 탐색
      const incomingEdges = edges.filter((edge) => edge.target === nodeId);

      // 부모 노드 추출
      incomingEdges.forEach((edge) => {
        if (!visited.has(edge.source)) {
          const parentNode = nodes.find((n) => n.id === edge.source);
          if (parentNode) {
            parentNodes.push(parentNode); // 부모 노드를 결과에 추가
            queue.push(parentNode.id); // 부모 노드도 큐에 추가
          }
        }
      });
    }
  }

  // 최종적으로 부모 노드 배열을 ID 기준으로 중복 제거
  const uniqueParentNodes = Array.from(
    new Map(parentNodes.map((node) => [node.id, node])).values()
  );

  // 본인도 제거
  const result = (uniqueParentNodes || [])
  .filter((value) => value.id != currentNodeId);
  return result;
};


/**
 * 모노스페이스 블럭을 만드는 팩토리 함수
 * @param content 
 * @returns 
 */
export const createMonospaceBlock = (node: Node<NodeData, string>): HTMLSpanElement => {
  const monoBlock = document.createElement("span");
  monoBlock.innerHTML = `${node.data.name}`;
  monoBlock.setAttribute("contenteditable", "false");
  if (node.type === 'START') {
    monoBlock.setAttribute("data-value", "{{INPUT_MESSAGE}}");
  } else {
    monoBlock.setAttribute("data-value", `{{${node.id}}}`);
  }
  const nodeData = nodeConfig[node.type] || { label: "Unknown", color: "D8D8D8", icon: null };
  const borderColor = deleteIconColors[node.type] || "#000000"; // 기본값 설정

  monoBlock.style.fontFamily = "monospace";
  monoBlock.style.backgroundColor = `#${nodeData.color}`;
  monoBlock.style.padding = "2px 4px";
  monoBlock.style.borderRadius = "3px";
  monoBlock.style.boxShadow = `0 0 0 0.25px ${borderColor}`; // 테두리를 얇게 표현
  monoBlock.style.display = "inline-block";
  monoBlock.style.marginRight = "2px";

  return monoBlock;
};


/**
 * 모노스페이스 복구 함수
 * @param text 
 * @returns 
 */
export const restoreMonospaceBlocks = (nodes: Node<NodeData, string>[], text: string): string => {
  return text.replace(/{{(.*?)}}/g, (_, nodeId) => {
    const node = nodes.find((n) => n.id === nodeId.trim() || n.type === "START");
    if (node) {
      const block = createMonospaceBlock(node); // 노드 데이터 전달
      return block.outerHTML;
    }
    return `{{${nodeId}}}`; // 노드가 없는 경우 원본 텍스트 유지
  });
};

/**
 * 모노스페이스 원본 텍스트 추출 함수
 * @param container 
 * @returns 
 */
export const extractActualValues = (container: HTMLDivElement): string => {
  const spans = container.querySelectorAll("span[data-value]");
  let result = container.innerText;

  spans.forEach((span) => {
    const actualValue = span.getAttribute("data-value");
    if (actualValue) {
      result = result.replace(span.textContent || "", actualValue);
    }
  });

  return result;
};

/**
 * 날짜 계산 함수
 * @param dateString 
 * @returns 
 */
export function timeDifferenceFromNow(dateString: string): string {
  if (dateString === "") return null;

  const inputDate = new Date(dateString.replace(" ", "T")); // 입력된 날짜 변환
  const now = new Date();
  const diffInMs = now.getTime() - inputDate.getTime();
  const diffInMinutes = Math.floor(diffInMs / (1000 * 60));
  const diffInHours = Math.floor(diffInMs / (1000 * 60 * 60));
  const diffInDays = Math.floor(diffInMs / (1000 * 60 * 60 * 24));

  if (diffInMinutes < 60) {
      return `${diffInMinutes}분 전`;
  } else if (diffInHours < 24) {
      return `${diffInHours}시간 전`;
  } else {
      return `${diffInDays}일 전`;
  }
}

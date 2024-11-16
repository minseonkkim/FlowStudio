"use client";

import { useCallback, useEffect, useRef, useState, MouseEvent, useMemo } from "react";
import ReactFlow, {
  Background,
  Controls,
  MiniMap,
  applyNodeChanges,
  applyEdgeChanges,
  OnNodesChange,
  NodeChange,
  EdgeChange,
  ReactFlowProvider,
  Edge,
  Node,
  NodeTypes,
  useNodesState,
  useOnSelectionChange,
  useEdgesState,
  addEdge,
  reconnectEdge,
  ReconnectEdgeOptions,
  Connection,
  Viewport,
  useViewport,
  useReactFlow,
  useOnViewportChange
} from "reactflow";
import "reactflow/dist/style.css";
import StartNode from "@/components/chatbot/chatflow/customnode/StartNode";
import StartNodeDetail from "@/components/chatbot/chatflow/nodedetail/StartNodeDetail";
import { BsArrowUpRight } from "@react-icons/all-files/bs/BsArrowUpRight";
import { MdKeyboardArrowDown } from "@react-icons/all-files/md/MdKeyboardArrowDown";
import { v4 as uunodeIdv4 } from "uuid";
import ConfirmationModal from "@/components/common/ConfirmationModal";
import { useMutation, useQuery, useQueryClient } from "@tanstack/react-query";
import { getChatFlow } from "@/api/chatbot";
import { ChatFlowDetail, Coordinate, NodeData } from "@/types/chatbot";
import { EdgeData } from "@/types/chatbot";
import { deleteEdge, deleteEdge as deleteEdgeApi, deleteNode, deleteNode as deleteNodeApi, postEdge, postNode, putNode } from "@/api/workflow";
import AnswerNode from "@/components/chatbot/chatflow/customnode/AnswerNode";
import AnswerNodeDetail from "@/components/chatbot/chatflow/nodedetail/AnswerNodeDetail";
import { createNodeData } from "@/utils/node";
import NodeAddMenu from "@/components/chatbot/chatflow/nodedetail/NodeAddMenu";
import RetrieverNode from "@/components/chatbot/chatflow/customnode/RetrieverNode";

interface ChatflowPageProps {
  params: {
    id: number;
  };
}

interface Model {
  nodeId: string;
  name: string;
}

const models: Model[] = [
  { nodeId: "gpt-3.5-turbo", name: "GPT-3.5 Turbo" },
  { nodeId: "gpt-4", name: "GPT-4" },
  { nodeId: "gpt-4-32k", name: "GPT-4 (32k)" },
];

interface ConnectedNode {
  nodeId: number;
  name: string;
}

// interface Variable {
//   name: string;
//   value: string;
//   type: string;
//   isEditing: boolean;
// }
const nodeTypes = {
  START: StartNode,
  ANSWER: AnswerNode,
  RETRIEVER: RetrieverNode,
}


export default function Page({ params }: ChatflowPageProps) {
  const [nodes, setNodes] = useNodesState([]);
  const [edges, setEdges, onEdgesChange] = useEdgesState([]);
  const [selectedNode, setSelectedNode] = useState<Node<any, string | undefined> | null>(null);
  const [menuPosition, setMenuPosition] = useState<{ x: number; y: number; } | null>(null);
  const [mousePosition, setMousePosition] = useState({ x: 0, y: 0 });

  /**
   * 처음 화면에 들어왔을 때 노드 초기화
   */
  useEffect(() => {
    getChatFlow(params.id).then((data) => {
      // 초기 노드 데이터 가져오기
      const initNodes: NodeData[] = data.nodes;

      // 팩토리 함수로 NodeData 생성
      const newNodes = initNodes.map((node) =>
        createNodeData(
          node,
          params.id, // chatFlowId 전달
          setNodes,
          setEdges,
          setSelectedNode,
        )
      );

      // ReactFlow에 필요한 노드 데이터로 변환
      const reactFlowNodes = newNodes.map((node) => ({
        id: node.nodeId.toString(),
        type: node.type,
        position: node.coordinate,
        data: node, // 팩토리 함수로 생성된 NodeData 객체 전달
      }));

      setNodes(reactFlowNodes); // 노드 상태 설정

      // 초기 엣지 데이터 가져오기
      const initEdges: EdgeData[] = data.edges;

      // ReactFlow에 필요한 엣지 데이터로 변환
      const reactFlowEdges = initEdges.map((edge) => ({
        id: edge.edgeId.toString(),
        source: edge.sourceNodeId.toString(),
        target: edge.targetNodeId.toString(),
        data: { ...edge },
      }));

      setEdges(reactFlowEdges); // 엣지 상태 설정
    });

  }, []);

  /**
   * 노드에 변화가 있을 때 처리
   * 선택, 드래그, 값수정
   */
  const onNodesChange: OnNodesChange = useCallback(
    (changes) =>
      setNodes((nds) => {
        console.log(nds);

        nds = nds.map((node) => ({
          ...node,
          data: {
            ...node.data,
            coordinate: node.position
          }
        }));


        // 첫 번째 선택된 노드 찾기
        const currentSelectNode = nds.find((node) => node.selected);
        console.log("CURRENT Node:", currentSelectNode);

        // 선택된 노드가 있을 때 처리, 선택을 취소하기만 하면 이전 선택 노드가 유지됨!!
        if (currentSelectNode) {
          console.log("PREV Node", selectedNode);

          // 이미 선택된 노드와 다른 노드면 저장
          if (selectedNode && selectedNode.id !== currentSelectNode.id) {
            putNode(selectedNode.data.nodeId, selectedNode.data);
          }
          setSelectedNode(currentSelectNode);
        }

        // ReactFlow 상태 업데이트
        return applyNodeChanges(changes, nds);
      }),
    [nodes, setNodes, selectedNode]
  );

  /**
   * 간선 연결
   */
  const onConnect = useCallback((connection: any) => {
    console.log(connection);
    const edgeData: EdgeData = {
      edgeId: 0,
      sourceNodeId: connection.source,
      targetNodeId: connection.target,
      sourceConditionId: 0,
    };
    postEdge(params.id, edgeData)
      .then((data) => {
        console.log(data);
        const newReactEdge: Edge = {
          id: data.edgeId.toString(),
          source: data.sourceNodeId.toString(),
          target: data.targetNodeId.toString(),
          data: { ...data }
        }
        setEdges((els) => {
          console.log(els);

          return addEdge(newReactEdge, els);
        })
        console.log(edges);
      });
  }, []);

  /**
   * 간선 삭제
   */
  const onEdgeDoubleClick = useCallback((event: MouseEvent, edge: Edge) => {
    deleteEdge(params.id, +edge.id);

    setEdges((eds) => eds.filter((e) => e.id !== edge.id));
  }, []);

  
  /**
   * NodeDetail 화면에서 닫기 했을때 처리
   */
  const handleNodeDetailClose = () => {
    if (selectedNode == null) return;

    putNode(selectedNode.data.nodeId, selectedNode.data);
    setNodes((prevNodes: Node[]) =>
      prevNodes.map((n) =>
        n.id === selectedNode?.id
          ? {
            ...n,
            selected: false
          }
          : n
      )
    );
    setSelectedNode(null);
  }
  

  /**
  * 마우스 우클릭 메뉴
  * @param event 
  */
  const handlePaneContextMenu = (event: React.MouseEvent) => {
    event.preventDefault(); // 기본 우클릭 메뉴 비활성화
    console.log("page", event.pageX, event.pageY);
    console.log("clientX", event.clientX, event.clientY);
    console.log("screen", event.screenX, event.screenY);
    
    setMenuPosition({ x: event.clientX + 180, y: event.clientY - 80}); // 클릭 위치 저장
  };

  /**
   * 메뉴 닫기
   */
  const closeMenu = () => {
    setMenuPosition(null); // 메뉴 위치 초기화
  };


  /**
   * 선택된 노드의 연결된 노드 찾기
   * @param sourceNode 
   * @returns 
   */
  const getConnectedNodes = (sourceNode: Node<any, string | undefined> | null): ConnectedNode[] => {
    return edges
      .filter((edge) => edge.source === sourceNode?.id) // 연결된 엣지 찾기
      .map((edge) => {
        const targetNode = nodes.find((node) => node.id === edge.target); // Map을 사용하여 노드 찾기
        return {
          nodeId: +(targetNode?.id || "0"), // 기본값 설정
          name: targetNode?.type || "Unknown", // 기본값 설정
        };
      }, [nodes, edges]);
  };
  const connectedNodes = useMemo(() => getConnectedNodes(selectedNode), [nodes, edges]);

  
  /**
   * @returns 노드 상세 화면
   */
  const renderNodeDetail = useMemo(() => {
    if (selectedNode == null) return null;
  
    console.log(selectedNode);
  
    if (selectedNode.data.type == "START") {
      return (
        <StartNodeDetail
          chatFlowId={params.id}
          node={selectedNode}
          nodes={nodes}
          edges={edges}
          setNodes={setNodes}
          setEdges={setEdges}
          setSelectedNode={setSelectedNode}
          onClose={handleNodeDetailClose}
          connectedNodes={connectedNodes}
        />
      );
    }
  
    if (selectedNode.data.type == "ANSWER") {
      return (
        <AnswerNodeDetail
          node={selectedNode}
          setNodes={setNodes}
          onClose={handleNodeDetailClose}
        />
      );
    }
  
    return null;
  }, [selectedNode, nodes, edges, connectedNodes]);

  /**
   * 드래그가 멈춘 노드는 위치정보를 업데이트
   * @param event 
   * @param node 드래그가 된 노드
   */
  function handleNodeDragStop(event: React.MouseEvent<Element>, node: Node<any, string | undefined>): void {
    putNode(node.data.nodeId, node.data);
  }

  return (
    <>
      <div className="absolute top-[80px] right-[30px] flex flex-row gap-3 z-[10]">
        <button
          className="px-3 py-2.5 bg-white hover:bg-[#F3F3F3] rounded-[10px] text-[#9A75BF] font-bold shadow-[0px_2px_8px_rgba(0,0,0,0.25)] cursor-pointer"
        // onClick={handleVariableButtonClick}
        >
          변수
        </button>
        <button
          className="flex flex-row gap-1 items-center px-3 py-2.5 bg-[#9A75BF] hover:bg-[#8A64B1] rounded-[10px] text-white font-bold shadow-[0px_2px_8px_rgba(0,0,0,0.25)] cursor-pointer"
        // onClick={handleChatbotCreationClick}
        >
          챗봇 생성 <MdKeyboardArrowDown className="size-4" />
        </button>
      </div>
      <div className="absolute top-[140px] right-[30px] z-[10] flex flex-row">
        {renderNodeDetail}
        {/* {renderVariableDetail()} */}
      </div>
      {/* {renderChatbotCreationModal()} */}
      <ReactFlowProvider>
        <div style={{ height: "calc(100vh - 60px)", backgroundColor: "#F0EFF1" }}>
          
          <ReactFlow
            nodes={nodes}
            edges={edges}
            onNodesChange={onNodesChange}
            onEdgesChange={onEdgesChange}
            // onNodeClick={onNodeClick}
            onEdgeDoubleClick={onEdgeDoubleClick}
            onConnect={onConnect}
            onPaneContextMenu={handlePaneContextMenu}
            onPaneClick={() => setMenuPosition(null)}
            onNodeDragStop={handleNodeDragStop}
            zoomOnScroll={true}
            zoomOnPinch={true}
            panOnScroll={true}
            minZoom={0.5}
            maxZoom={2}
            nodeTypes={nodeTypes}
            fitView
          >
            <MiniMap
              style={{
                bottom: 5,
                left: 40,
                width: 150,
                height: 100,
              }}
            />
            <Controls />
            <Background />
            {menuPosition && (
              <div
                style={{
                  position: "relative",
                  top: menuPosition.y,
                  left: menuPosition.x,
                  zIndex: 1000
                }}
                onClick={closeMenu} // 메뉴 클릭 시 닫기
              >
                <NodeAddMenu
                  node={{
                    position: { x: menuPosition.x, y: menuPosition.y },
                    data: { chatFlowId: params.id }
                  } as Node<any, string | undefined>
                  }
                  nodes={nodes}
                  setNodes={setNodes}
                  setEdges={setEdges}
                  setSelectedNode={setSelectedNode}
                  isDetail={false} // 세부 메뉴인지 여부
                />
              </div>
            )}
          </ReactFlow>
        </div>
      </ReactFlowProvider>
      {/* {showConfirmationModal && (
        <ConfirmationModal
          message={`${nodeTypeToDelete} 노드를 삭제하시겠습니까?`}
          onConfirm={handleConfirmDelete}
          onCancel={handleCancelDelete}
        />
      )} */}
    </>
  );
}

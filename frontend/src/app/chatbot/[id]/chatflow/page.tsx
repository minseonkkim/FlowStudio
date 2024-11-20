"use client";

import { useCallback, useEffect, useState, MouseEvent, useMemo, useRef } from "react";
import ReactFlow, {
  Background,
  Controls,
  MiniMap,
  applyNodeChanges,
  ReactFlowProvider,
  Edge,
  Node,
  useNodesState,
  useEdgesState,
  addEdge,
  Connection,
} from "reactflow";
import "reactflow/dist/style.css";
import StartNode from "@/components/chatbot/chatflow/customnode/StartNode";
import StartNodeDetail from "@/components/chatbot/chatflow/nodedetail/StartNodeDetail";
import { MdKeyboardArrowDown } from "@react-icons/all-files/md/MdKeyboardArrowDown";
import { getChatFlow } from "@/api/chatbot";
import { NodeData, EdgeData } from "@/types/chatbot";
import { deleteEdge, getNodeDetail, postEdge, putNode } from "@/api/workflow";
import AnswerNode from "@/components/chatbot/chatflow/customnode/AnswerNode";
import AnswerNodeDetail from "@/components/chatbot/chatflow/nodedetail/AnswerNodeDetail";
import { createNodeData, findAllParentNodes, restoreMonospaceBlocks } from "@/utils/node";
import NodeAddMenu from "@/components/chatbot/chatflow/menu/NodeAddMenu";
import RetrieverNode from "@/components/chatbot/chatflow/customnode/RetrieverNode";
import LlmNode from "@/components/chatbot/chatflow/customnode/LlmNode";
import QuestionClassifierNode from "@/components/chatbot/chatflow/customnode/QuestionClassifierNode";
import RetrieverNodeDetail from "@/components/chatbot/chatflow/nodedetail/RetrieverNodeDetail";
import LlmNodeDetail from "@/components/chatbot/chatflow/nodedetail/LlmNodeDetail";
import QuestionClassifierNodeDetail from "@/components/chatbot/chatflow/nodedetail/QuestionClassifierNodeDetail";
import PreviewChat from "@/components/chat/PreviewChat";
import VariableMenu from "@/components/chatbot/chatflow/menu/VariableMenu";
import ChatFlowPublishMenu from "@/components/chatbot/chatflow/menu/ChatFlowPublishMenu";
import { ConnectedNode, PublishChatFlowData } from "@/types/workflow";
import { RiPlayMiniLine } from "@react-icons/all-files/ri/RiPlayMiniLine";
import Loading from "@/components/common/Loading";
import { useSearchParams } from 'next/navigation';

interface ChatflowPageProps {
  params: {
    id: number;
  };
}

const nodeTypes = {
  START: StartNode,
  ANSWER: AnswerNode,
  RETRIEVER: RetrieverNode,
  LLM: LlmNode,
  QUESTION_CLASSIFIER: QuestionClassifierNode,
}


export default function Page({ params }: ChatflowPageProps) {
  const [nodes, setNodes] = useNodesState([]);
  const [edges, setEdges, onEdgesChange] = useEdgesState([]);
  const [selectedNode, setSelectedNode] = useState<Node<NodeData, string | undefined> | null>(null);
  const [menuPosition, setMenuPosition] = useState<{ x: number; y: number; } | null>(null);
  const [newNodePosition, setNewNodePosition] = useState<{ x: number; y: number; } | null>(null);
  const [publishedChatFlowData, setPublishedChatFlowData] = useState<PublishChatFlowData>(null);
  const [loading, setLoading] = useState<boolean>(true);
  // const [mousePosition, setMousePosition] = useState({ x: 0, y: 0 });
  const searchParams = useSearchParams();
  const isEditable = searchParams.get('isEditable') == 'false' ? false : true;

  // 노드와 엣지를 초기화하는 비동기 함수
  const initializeFlow = async () => {
    console.log("수정 가능하니? ", isEditable);
    
    setLoading(true);
    try {
      setLoading(true);
      const data = await getChatFlow(params.id);

      setPublishedChatFlowData({
        chatFlowId: params.id,
        publishUrl: data.publishUrl || "",
        publishedAt: data.publishedAt || "",
      })

      // 초기 노드 데이터 가져오기
      const initNodes: NodeData[] = data.nodes;

      // 비동기 작업 처리
      const newNodes: NodeData[] = await Promise.all(
        initNodes.map(async (node) => {
          const nodeDetail = await getNodeDetail(node.nodeId); // getNodeDetail 호출
          return createNodeData(
            nodeDetail,
            params.id, // chatFlowId 전달
            isEditable,
            setNodes,
            setEdges,
            setSelectedNode
          );
        })
      );

      // ReactFlow에 필요한 노드 데이터로 변환
      const reactFlowNodes = newNodes.map((node) => ({
        id: node.nodeId.toString(),
        type: node.type,
        position: node.coordinate,
        data: node, // 팩토리 함수로 생성된 NodeData 객체 전달
      }));

      // 초기 엣지 데이터 가져오기
      const initEdges: EdgeData[] = data.edges;

      // ReactFlow에 필요한 엣지 데이터로 변환
      const reactFlowEdges = initEdges.map((edge) => ({
        id: edge.edgeId.toString(),
        source: edge.sourceNodeId.toString(),
        sourceHandle: edge.sourceConditionId?.toString(), // Handle 특정하기
        target: edge.targetNodeId.toString(),
        data: { ...edge },
      }));

      const updatedNodes = await Promise.all(
        reactFlowNodes.map(async (newNode) => {
          if (newNode.type === "ANSWER") {
            // 부모 노드 찾기
            const parentNodes = findAllParentNodes(newNode.id, reactFlowNodes, reactFlowEdges);
  
            // 부모 노드에서 renderText에 사용할 값 생성
            const renderOutputMessage = restoreMonospaceBlocks(parentNodes, newNode.data.outputMessage);
  
            // 새로운 노드 데이터 업데이트
            return {
              ...newNode,
              data: {
                ...newNode.data,
                renderOutputMessage: {__html: renderOutputMessage},
              },
            };
          }
  
          if (newNode.type === "LLM") {
            // 부모 노드 찾기
            const parentNodes = findAllParentNodes(newNode.id, reactFlowNodes, reactFlowEdges);
  
            // 부모 노드에서 renderText에 사용할 값 생성
            const renderPromptSystem = restoreMonospaceBlocks(parentNodes, newNode.data.promptSystem);
            const renderPromptUser = restoreMonospaceBlocks(parentNodes, newNode.data.promptUser);
  
            // 새로운 노드 데이터 업데이트
            return {
              ...newNode,
              data: {
                ...newNode.data,
                renderPromptSystem: {__html: renderPromptSystem},
                renderPromptUser: {__html: renderPromptUser},
              },
            };
          }
  
          return newNode;
        })
      );

      setNodes(updatedNodes); // 노드 상태 설정
      setEdges(reactFlowEdges); // 엣지 상태 설정
    } catch (error) {
      console.error("Flow 초기화 중 오류 발생:", error);
    } finally {
      setLoading(false); 
    }
  };


  /**
  * 처음 화면에 들어왔을 때 노드 초기화
  */
  useEffect(() => {
    // 비동기 함수 호출
    initializeFlow();

  }, [params.id, setNodes, setEdges, setSelectedNode]);

  /**
   * 노드에 변화가 있을 때 처리
   * 선택, 드래그, 값수정
   */
  // const onNodesChange: OnNodesChange = useCallback(
  //   (changes) =>
  //     setNodes((nds) => {
  //       nds = nds.map((node) => ({
  //         ...node,
  //         data: {
  //           ...node.data,
  //           coordinate: node.position
  //         }
  //       }));


  //       // 첫 번째 선택된 노드 찾기
  //       const currentSelectNode = nds.find((node) => node.selected);
  //       console.log("CURRENT Node:", currentSelectNode);

  //       // 선택된 노드가 있을 때 처리, 선택을 취소하기만 하면 이전 선택 노드가 유지됨!!
  //       if (currentSelectNode) {
  //         console.log("PREV Node", selectedNode);

  //         // 이미 선택된 노드와 다른 노드면 저장
  //         if (selectedNode && selectedNode.id !== currentSelectNode.id) {
  //           putNode(selectedNode.data.nodeId, selectedNode.data);
  //         }
  //         setSelectedNode(currentSelectNode);
  //       }

  //       // ReactFlow 상태 업데이트
  //       return applyNodeChanges(changes, nds);
  //     }),
  //   [nodes, setNodes, selectedNode]
  // );

  /**
   * 간선 연결
   */
  const onConnect = useCallback((connection: Connection) => {
    const edgeData: EdgeData = {
      edgeId: 0,
      sourceNodeId: +connection.source,
      targetNodeId: +connection.target,
      sourceConditionId: +connection.sourceHandle,
    };
    postEdge(params.id, edgeData)
      .then((data) => {
        const newReactEdge: Edge = {
          id: data.edgeId.toString(),
          source: data.sourceNodeId.toString(),
          target: data.targetNodeId.toString(),
          sourceHandle: data.sourceConditionId?.toString(), // Handle 특정하기
          data: { ...data }
        }
        setEdges((els) => {
          return addEdge(newReactEdge, els);
        })
      });
  }, []);

  /**
   * 간선 삭제
   */
  // const onEdgeDoubleClick = useCallback((event: MouseEvent, edge: Edge) => {
  //   deleteEdge(params.id, +edge.id);

  //   setEdges((eds) => eds.filter((e) => e.id !== edge.id));
  // }, []);


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
  const reactFlowInstanceRef = useRef(null);

  const handlePaneContextMenu = useCallback(
  (event: MouseEvent) => {
    event.preventDefault();

    // 메뉴가 이미 열려있으면 메뉴를 닫고 종료
    if (menuPosition !== null) {
      closeMenu();
      return;
    }

    if (reactFlowInstanceRef.current) {
      const reactFlowInstance = reactFlowInstanceRef.current;

      // 화면 좌표를 Flow 좌표로 변환
      const flowCoordinates = reactFlowInstance.screenToFlowPosition({
        x: event.clientX,
        y: event.clientY,
      });

      setNewNodePosition({ x: flowCoordinates.x, y: flowCoordinates.y });

      // 메뉴 위치 설정 (화면 좌표 기준)
      setMenuPosition({ x: event.clientX + 180, y: event.clientY - 80 });
    }
  },
  [menuPosition] // menuPosition을 종속성으로 추가
);



  /**
   * ReactFlow 컴포넌트가 초기화될 때 reactFlowInstance 저장
   */
  const onInit = useCallback((instance) => {
    reactFlowInstanceRef.current = instance;
  }, []);

  /**
   * 메뉴 닫기
   */
   const closeMenu = useCallback(() => {
    setMenuPosition(null); // 메뉴 위치 초기화
  }, []);

  useEffect(() => {
    // 메뉴 상태가 업데이트될 때마다 리셋해주기
    if (!menuPosition) {
      closeMenu();
    }
  }, [menuPosition, closeMenu]);


  /**
   * 선택된 노드의 연결된 노드 찾기
   * @param sourceNode 
   * @returns 
   */
  const getConnectedNodes = (sourceNode: Node<NodeData, string | undefined> | null): ConnectedNode[] => {
    return edges
      .filter((edge) => edge.source === sourceNode?.id) // 연결된 엣지 찾기
      .map((edge) => {
        const targetNode = nodes.find((node) => node.id === edge.target); // Map을 사용하여 노드 찾기
        return {
          nodeId: +(targetNode?.id || "0"), // 기본값 설정
          type: targetNode?.type || "Unknown", // 기본값 설정
          name: targetNode?.data.name || "", // 기본값 설정
          sourceConditionId: Number(edge.sourceHandle || 0)
        };
      }, [nodes, edges]);
  };
  const connectedNodes = useMemo(() => getConnectedNodes(selectedNode), [nodes, edges]);


  /**
   * @returns 노드 상세 화면
   */
  const renderNodeDetail = useMemo(() => {
    if (selectedNode == null) return null;
    if (isEditable == false) return null;

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

    if (selectedNode.data.type == "QUESTION_CLASSIFIER") {
      return (
        <QuestionClassifierNodeDetail
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

    if (selectedNode.data.type == "RETRIEVER") {
      return (
        <RetrieverNodeDetail
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

    if (selectedNode.data.type == "LLM") {
      return (
        <LlmNodeDetail
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
          // chatFlowId={params.id}
          node={selectedNode}
          nodes={nodes}
          edges={edges}
          setNodes={setNodes}
          // setSelectedNode={setSelectedNode}
          onClose={handleNodeDetailClose}
        />
      );
    }

    return null;
  }, [selectedNode, nodes, edges, connectedNodes]);

  const onNodeClick = useCallback(
  (event, node) => {
    setSelectedNode(node);
  },
  []
);

  /**
   * 드래그가 멈춘 노드는 위치정보를 업데이트
   * @param event 
   * @param node 드래그가 된 노드
   */
  function handleNodeDragStop(event: React.MouseEvent<Element>, node: Node<NodeData, string | undefined>): void {
    putNode(node.data.nodeId, node.data);
  }


  /**
   * 챗봇 발행 메뉴
   */
  const publishMenuRef = useRef<{ toggleChatFlowPublishModal: () => void } | null>(null);
  const handleChatFlowPublishModal = () => {
    if (publishMenuRef.current) {
      publishMenuRef.current.toggleChatFlowPublishModal();
    }
  };

  /**
   * 변수 메뉴
   */
  const variableMenuRef = useRef<{ toggleVariableDetail: () => void } | null>(null);
  // const handleVariableMenuModal = () => {
  //   if (variableMenuRef.current) {
  //     variableMenuRef.current.toggleVariableDetail();
  //   }
  // };

  /**
   * 미리보기 메뉴
   */
  const [showPreviewChat, setShowPreviewChat] = useState<boolean>(false);
  const handlePreviewChatButtonClick = useCallback(() => {
    setNodes((prevNodes) =>
      prevNodes.map((n) => ({
        ...n,
        data: { ...n.data, isComplete: false, isError: false },
      }))
    );
    setShowPreviewChat((prev) => !prev);
  }, []);

  const handlePreviewClose = () => {
    setShowPreviewChat(false);
  }

  const [edgeContextMenu, setEdgeContextMenu] = useState<{
    x: number;
    y: number;
    edge: Edge | null;
  } | null>(null);

  // Function to handle right-click on an edge
  const onEdgeContextMenu = (event: MouseEvent, edge: Edge) => {
    event.preventDefault(); // Prevent the default context menu
    setEdgeContextMenu({
      x: event.clientX - 40,
      y: event.clientY - 40,
      edge,
    });
  };

  // Function to handle deleting an edge
  const handleDeleteEdge = () => {
    if (edgeContextMenu && edgeContextMenu.edge) {
      deleteEdge(params.id, +edgeContextMenu.edge.id);
      setEdges((eds) => eds.filter((e) => e.id !== edgeContextMenu.edge.id));
    }
    setEdgeContextMenu(null); // Hide the context menu
  };

  // Function to close the context menu
  const closeEdgeContextMenu = () => {
    setEdgeContextMenu(null);
  };

  if(loading) return <Loading/>;

  else return (
    <><ReactFlowProvider>
  <div className="absolute top-[80px] right-[30px] flex flex-row gap-3 z-[10]">
    {/* 미리보기 버튼 */}
    <button
      className="flex flex-row items-center gap-1 px-3 py-2.5 bg-white hover:bg-[#F3F3F3] rounded-[10px] text-[#9A75BF] font-bold shadow-[0px_2px_8px_rgba(0,0,0,0.25)] cursor-pointer"
      onClick={handlePreviewChatButtonClick}
    >
      <RiPlayMiniLine className="w-6 h-6" />
      미리보기
    </button>

    {/* 챗봇 생성 버튼 */}
    {isEditable && (
      <button
        className="flex flex-row gap-1 items-center px-3 py-2.5 bg-[#9A75BF] hover:bg-[#8A64B1] rounded-[10px] text-white font-bold shadow-[0px_2px_8px_rgba(0,0,0,0.25)] cursor-pointer"
        onClick={handleChatFlowPublishModal}
      >
        챗봇 생성 <MdKeyboardArrowDown className="size-4" />
      </button>
    )}
  </div>

  <div className="absolute top-[140px] right-[30px] z-[10] flex flex-row">
    {renderNodeDetail}
    <VariableMenu ref={variableMenuRef} />
    {showPreviewChat && (
      <PreviewChat
        onClose={handlePreviewClose}
        chatFlowId={String(params.id)}
        nodes={nodes}
        setNodes={setNodes}
      />
    )}
  </div>

  <ChatFlowPublishMenu
    publishedChatFlowData={publishedChatFlowData}
    setPublishedChatFlowData={setPublishedChatFlowData}
    ref={publishMenuRef}
  />

  <div style={{ height: "calc(100vh - 60px)", backgroundColor: "#F0EFF1" }}>
    <ReactFlow
      nodes={nodes}
      edges={edges}
      onNodesChange={isEditable ? (changes) => setNodes(applyNodeChanges(changes, nodes)) : undefined}
      onEdgesChange={isEditable ? onEdgesChange : undefined}
      onConnect={isEditable ? onConnect : undefined}
      onInit={onInit}
      onPaneContextMenu={handlePaneContextMenu}
      onPaneClick={() => {
        setMenuPosition(null);
        closeEdgeContextMenu();
      }}
      onNodeClick={onNodeClick}
      onNodeDragStop={isEditable ? handleNodeDragStop : undefined}
      onEdgeContextMenu={isEditable ? onEdgeContextMenu : undefined}
      zoomOnScroll
      zoomOnPinch
      panOnScroll
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
            position: "absolute",
            top: menuPosition.y,
            left: menuPosition.x,
            zIndex: 1000,
          }}
          onClick={closeMenu}
        >
          {isEditable && (
            <NodeAddMenu
              node={{
                position: { x: newNodePosition.x, y: newNodePosition.y },
                data: { chatFlowId: params.id },
              } as Node<NodeData, string | undefined>}
              nodes={nodes}
              setNodes={setNodes}
              setEdges={setEdges}
              setSelectedNode={setSelectedNode}
              isDetail={false}
              questionClass={0}
            />
          )}
        </div>
      )}
      {edgeContextMenu && (
        <div
          style={{
            position: "absolute",
            top: edgeContextMenu.y,
            left: edgeContextMenu.x,
            backgroundColor: "white",
            border: "1px solid #ccc",
            borderRadius: "4px",
            padding: "5px",
            zIndex: 1000,
          }}
          onMouseLeave={closeEdgeContextMenu}
        >
          <button onClick={handleDeleteEdge}>간선 삭제</button>
        </div>
      )}
    </ReactFlow>
  </div>
</ReactFlowProvider>

      {/* {showConfirmationModal && (
        <ConfirmationModal
          message={${nodeTypeToDelete} 노드를 삭제하시겠습니까?}
          onConfirm={handleConfirmDelete}
          onCancel={handleCancelDelete}
        />
      )} */}
    </>
  );
}
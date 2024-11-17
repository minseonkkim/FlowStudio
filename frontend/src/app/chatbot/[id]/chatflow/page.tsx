"use client";

import { useCallback, useEffect, useState, MouseEvent, useMemo } from "react";
import ReactFlow, {
  Background,
  Controls,
  MiniMap,
  applyNodeChanges,
  OnNodesChange,
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
import { getChatFlow, publishChatFlow } from "@/api/chatbot";
import { NodeData } from "@/types/chatbot";
import { EdgeData } from "@/types/chatbot";
import { deleteEdge, getNodeDetail, postEdge, putNode } from "@/api/workflow";
import AnswerNode from "@/components/chatbot/chatflow/customnode/AnswerNode";
import AnswerNodeDetail from "@/components/chatbot/chatflow/nodedetail/AnswerNodeDetail";
import { createNodeData } from "@/utils/node";
import NodeAddMenu from "@/components/chatbot/chatflow/nodedetail/NodeAddMenu";
import RetrieverNode from "@/components/chatbot/chatflow/customnode/RetrieverNode";
import LlmNode from "@/components/chatbot/chatflow/customnode/LlmNode";
import QuestionClassifierNode from "@/components/chatbot/chatflow/customnode/QuestionClassifierNode";
import RetrieverNodeDetail from "@/components/chatbot/chatflow/nodedetail/RetrieverNodeDetail";
import LlmNodeDetail from "@/components/chatbot/chatflow/nodedetail/LlmNodeDetail";
import QuestionClassifierNodeDetail from "@/components/chatbot/chatflow/nodedetail/QuestionClassifierNodeDetail";
import { BsArrowUpRight } from "@react-icons/all-files/bs/BsArrowUpRight";
import VariableDetail from "@/components/chatbot/workflow/VariableDetail";
import PreviewChat from "@/components/chat/PreviewChat";

interface ChatflowPageProps {
  params: {
    id: number;
  };
}

// interface Model {
//   nodeId: string;
//   name: string;
// }

// const models: Model[] = [
//   { nodeId: "gpt-3.5-turbo", name: "GPT-3.5 Turbo" },
//   { nodeId: "gpt-4", name: "GPT-4" },
//   { nodeId: "gpt-4-32k", name: "GPT-4 (32k)" },
// ];

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
  LLM: LlmNode,
  QUESTION_CLASSIFIER: QuestionClassifierNode,
}


export default function Page({ params }: ChatflowPageProps) {
  const [nodes, setNodes] = useNodesState([]);
  const [edges, setEdges, onEdgesChange] = useEdgesState([]);
  const [selectedNode, setSelectedNode] = useState<Node<NodeData, string | undefined> | null>(null);
  const [menuPosition, setMenuPosition] = useState<{ x: number; y: number; } | null>(null);
  // const [mousePosition, setMousePosition] = useState({ x: 0, y: 0 });

  // 노드와 엣지를 초기화하는 비동기 함수
  const initializeFlow = async () => {
    try {
      const data = await getChatFlow(params.id);

      // 초기 노드 데이터 가져오기
      const initNodes: NodeData[] = data.nodes;

      // Question Classifier 노드 필터링
      // const qcNodes: NodeData[] = initNodes.filter(
      //   (node: NodeData) => node.type === "QUESTION_CLASSIFIER"
      // );

      // 비동기 작업 처리
      const newNodes = await Promise.all(
        initNodes.map(async (node) => {
          const nodeDetail = await getNodeDetail(node.nodeId); // getNodeDetail 호출
          return createNodeData(
            nodeDetail,
            params.id, // chatFlowId 전달
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

      setNodes(reactFlowNodes); // 노드 상태 설정

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

      console.log(reactFlowEdges);

      setEdges(reactFlowEdges); // 엣지 상태 설정
    } catch (error) {
      console.error("Flow 초기화 중 오류 발생:", error);
    }
  };


  /**
  * 처음 화면에 들어왔을 때 노드 초기화
  */
  useEffect(() => {
    // 비동기 함수 호출
    initializeFlow();

  }, [params.id, setNodes, setEdges, setSelectedNode]);

  useEffect(() => {
    console.log("EDGES", edges);

  }, [edges]);

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
  const onConnect = useCallback((connection: Connection) => {
    console.log("간선 연결할때 정보", connection);
    const edgeData: EdgeData = {
      edgeId: 0,
      sourceNodeId: +connection.source,
      targetNodeId: +connection.target,
      sourceConditionId: +connection.sourceHandle,
    };
    postEdge(params.id, edgeData)
      .then((data) => {
        console.log(data);
        const newReactEdge: Edge = {
          id: data.edgeId.toString(),
          source: data.sourceNodeId.toString(),
          target: data.targetNodeId.toString(),
          sourceHandle: data.sourceConditionId?.toString(), // Handle 특정하기
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

    setMenuPosition({ x: event.clientX + 180, y: event.clientY - 80 }); // 클릭 위치 저장
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
  const getConnectedNodes = (sourceNode: Node<NodeData, string | undefined> | null): ConnectedNode[] => {
    return edges
      .filter((edge) => edge.source === sourceNode?.id) // 연결된 엣지 찾기
      .map((edge) => {
        const targetNode = nodes.find((node) => node.id === edge.target); // Map을 사용하여 노드 찾기
        return {
          nodeId: +(targetNode?.id || "0"), // 기본값 설정
          name: targetNode?.type || "Unknown", // 기본값 설정
          sourceConditionId: edge.sourceHandle || 0
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
  const [showChatbotCreationModal, setShowChatbotCreationModal] = useState(false);
  const handleChatbotCreationClick = useCallback(() => {
    setShowChatbotCreationModal((prev) => !prev);
  }, []);
  const handlePublishButtonClick = () => {
    publishChatFlow(params.id)
      .then((success) => {
        if (success) alert("발행 성공");
      })
  }
  const renderChatbotCreationModal = () => {
    if (!showChatbotCreationModal) return null;

    return (
      <div className="text-[14px] absolute top-[135px] right-[25px] p-4 bg-white shadow-lg rounded-[10px] flex flex-col justify-between gap-3 z-[100] w-[250px] h-[200px]">
        <button
          onClick={handlePublishButtonClick}
          className="px-3 py-2.5 bg-[#9A75BF] hover:bg-[#8D64B6] rounded-[8px] text-white font-bold cursor-pointer">
          업데이트
        </button>
        <div className="flex flex-col gap-3">
          <a
            href={`${process.env.NEXT_PUBLIC_FRONT_URL}/chat/${params.id}`}
            className="p-2 bg-[#F2F2F2] hover:bg-[#ECECEC] rounded-[8px] cursor-pointer text-start flex flex-row items-center gap-1">
            앱 실행<BsArrowUpRight />
          </a>
          <button className="p-2 bg-[#F2F2F2] hover:bg-[#ECECEC] rounded-[8px] cursor-pointer text-start flex flex-row items-center gap-1">
            사이트에 삽입<BsArrowUpRight />
          </button>
        </div>
      </div>
    );
  };


  /**
   * 변수 메뉴
   */
  const [showVariableDetail, setShowVariableDetail] = useState<boolean>(false);
  const handleVariableButtonClick = useCallback(() => {
    setShowVariableDetail((prev) => !prev);
  }, []);
  const [variables, setVariables] = useState<
    { name: string; value: string; type: string; isEditing: boolean }[]
  >([
    { name: "변수1", value: "", type: "string", isEditing: false },
    { name: "변수2", value: "", type: "string", isEditing: false },
  ]);
  const renderVariableDetail = () => {
    if (!showVariableDetail) return null;

    return (
      <VariableDetail
        variables={variables}
        handleVariableChange={handleVariableChange}
        handleAddVariable={handleAddVariable}
        handleRemoveVariable={handleRemoveVariable}
        handleEditToggle={handleEditToggle}
        onClose={() => setShowVariableDetail(false)}
      />
    );
  };
  const handleVariableChange = (
    index: number,
    key: "name" | "value" | "type",
    newValue: string
  ) => {
    setVariables((prev) =>
      prev.map((variable, i) =>
        i === index ? { ...variable, [key]: newValue } : variable
      )
    );
  };

  const handleAddVariable = () => {
    setVariables((prev) => [
      ...prev,
      { name: "", value: "", type: "string", isEditing: true },
    ]);
  };

  const handleRemoveVariable = (index: number) => {
    setVariables((prev) => prev.filter((_, i) => i !== index));
  };

  const handleEditToggle = (index: number) => {
    setVariables((prev) =>
      prev.map((variable, i) =>
        i === index ? { ...variable, isEditing: !variable.isEditing } : variable
      )
    );
  };

  /**
   * 미리보기 메뉴
   */
  const [showPreviewChat, setShowPreviewChat] = useState<boolean>(false);
  const handlePreviewChatButtonClick = useCallback(() => {
    setShowPreviewChat((prev) => !prev);
  }, []);


  return (
    <>
      <div className="absolute top-[80px] right-[30px] flex flex-row gap-3 z-[10]">
        <button
          className="px-3 py-2.5 bg-white hover:bg-[#F3F3F3] rounded-[10px] text-[#9A75BF] font-bold shadow-[0px_2px_8px_rgba(0,0,0,0.25)] cursor-pointer"
          onClick={handleVariableButtonClick}
        >
          변수
        </button>
        <button
          className="px-3 py-2.5 bg-white hover:bg-[#F3F3F3] rounded-[10px] text-[#9A75BF] font-bold shadow-[0px_2px_8px_rgba(0,0,0,0.25)] cursor-pointer"
          onClick={handlePreviewChatButtonClick}
        >
          미리보기
        </button>
        <button
          className="flex flex-row gap-1 items-center px-3 py-2.5 bg-[#9A75BF] hover:bg-[#8A64B1] rounded-[10px] text-white font-bold shadow-[0px_2px_8px_rgba(0,0,0,0.25)] cursor-pointer"
          onClick={handleChatbotCreationClick}
        >
          챗봇 생성 <MdKeyboardArrowDown className="size-4" />
        </button>
      </div>
      <div className="absolute top-[140px] right-[30px] z-[10] flex flex-row">
        {renderNodeDetail}
        {renderVariableDetail()}
        {showPreviewChat && <PreviewChat chatFlowId={String(params.id)} />}
      </div>
      {renderChatbotCreationModal()}
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
                  } as Node<NodeData, string | undefined>
                  }
                  nodes={nodes}
                  setNodes={setNodes}
                  setEdges={setEdges}
                  setSelectedNode={setSelectedNode}
                  isDetail={false} // 세부 메뉴인지 여부
                  questionClass={0}
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

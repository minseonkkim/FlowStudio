"use client"

import { useCallback, useEffect, useState } from "react";
import ReactFlow, {
  Background,
  Controls,
  MiniMap,
  applyNodeChanges,
  applyEdgeChanges,
  NodeChange,
  EdgeChange,
  ReactFlowProvider,
  Node,
} from "reactflow";
import "reactflow/dist/style.css";
import StartNode from "@/components/chatbot/workflow/customnode/StartNode";
import LlmNode from "@/components/chatbot/workflow/customnode/LlmNode";
import KnowledgeNode from "@/components/chatbot/workflow/customnode/KnowledgeNode";
import IfelseNode from "@/components/chatbot/workflow/customnode/IfelseNode";
import AnswerNode from "@/components/chatbot/workflow/customnode/AnswerNode";
import QuestionClassifierNode from "@/components/chatbot/workflow/customnode/QuestionClassifierNode";
import VariableAllocatorNode from "@/components/chatbot/workflow/customnode/VariableAllocatorNode";
import StartNodeDetail from "@/components/chatbot/workflow/nodedetail/StartNodeDetail";
import LlmNodeDetail from "@/components/chatbot/workflow/nodedetail/LlmNodeDetail";
import KnowledgeNodeDetail from "@/components/chatbot/workflow/nodedetail/KnowledgeNodeDetail";
import IfelseNodeDetail from "@/components/chatbot/workflow/nodedetail/IfelseNodeDetail";
import AnswerNodeDetail from "@/components/chatbot/workflow/nodedetail/AnswerNodeDetail";
import QuestionClassifierNodeDetail from "@/components/chatbot/workflow/nodedetail/QuestionClassifierNodeDetail";
import VariableAllocatorNodeDetail from "@/components/chatbot/workflow/nodedetail/VariableAllocatorNodeDetail";
import VariableDetail from "@/components/chatbot/workflow/VariableDetail";
import { BsArrowUpRight } from "@react-icons/all-files/bs/BsArrowUpRight";
import { MdKeyboardArrowDown } from "@react-icons/all-files/md/MdKeyboardArrowDown";
import { v4 as uuidv4 } from "uuid";

interface Model {
  id: string;
  name: string;
}

const models: Model[] = [
  { id: "gpt-3.5-turbo", name: "GPT-3.5 Turbo" },
  { id: "gpt-4", name: "GPT-4" },
  { id: "gpt-4-32k", name: "GPT-4 (32k)" },
];

const initialNodes: Node[] = [
  {
    id: "1",
    type: "startNode",
    data: { maxChars: undefined },
    position: { x: 250, y: 100 },
  },
  {
    id: "2",
    type: "llmNode",
    data: { prompts: [{type: "system", text: ""}], model: models[0].id },
    position: { x: 460, y: 100 },
  },
  {
    id: "3",
    type: "knowledgeNode",
    data: {  },
    position: { x: 700, y: 100 },
  },
  {
    id: "4",
    type: "ifelseNode",
    data: {  },
    position: { x: 900, y: 100 },
  },
  {
    id: "5",
    type: "answerNode",
    data: { answer: "" },
    position: { x: 1100, y: 100 },
  },
  {
    id: "6",
    type: "questionclassifierNode",
    data: { classes: [{text: ""}, {text: ""}] },
    position: { x: 1300, y: 100 },
  },
  {
    id: "7",
    type: "variableallocatorNode",
    data: {  },
    position: { x: 1500, y: 100 },
  },
];

const initialEdges = [
  { id: "e1-2", source: "1", target: "2" },
  { id: "e2-3", source: "2", target: "3" },
  { id: "e3-4", source: "3", target: "4" },
  { id: "e4-5", source: "4", target: "5", sourceHandle: "elifsource" },
  { id: "e5-6", source: "5", target: "6" },
  { id: "e6-7", source: "6", target: "7", sourceHandle: "handle2" },
];

const nodeTypes = {
  startNode: StartNode,
  llmNode: LlmNode,
  knowledgeNode: KnowledgeNode,
  ifelseNode: IfelseNode,
  answerNode: AnswerNode,
  questionclassifierNode: QuestionClassifierNode,
  variableallocatorNode: VariableAllocatorNode,
};

export default function Page() {
  const [nodes, setNodes] = useState<Node[]>(initialNodes);
  const [edges, setEdges] = useState(initialEdges);
  const [selectedNode, setSelectedNode] = useState<Node | null>(null);
  const [showVariableDetail, setShowVariableDetail] = useState<boolean>(false);
  const [selectedNodeId, setSelectedNodeId] = useState<string | null>(null);

  const onNodesChange = useCallback((changes: NodeChange[]) => {
    setNodes((nds) => applyNodeChanges(changes, nds));
  }, []);

  const onEdgesChange = useCallback((changes: EdgeChange[]) => {
  setEdges((eds) =>
    applyEdgeChanges(changes, eds).map(edge => ({
      ...edge,
      sourceHandle: edge.sourceHandle ?? "" 
    }))
  );
}, []);

  const onNodeClick = useCallback((event: React.MouseEvent, node: Node) => {
    setSelectedNodeId(node.id); 
    setSelectedNode(node);
    setShowVariableDetail(false);
  }, []);

  const handleCloseDetail = useCallback(() => {
    setSelectedNode(null); 
  }, []);

  const handleVariableButtonClick = useCallback(() => {
    setShowVariableDetail((prev) => !prev); 
  }, []);

  // 시작 노드 - 최대 글자 수 업데이트
  const updateMaxChars = useCallback((nodeId: string, newMaxChars: number) => {
    setNodes((nds) =>
      nds.map((node) =>
        node.id === nodeId
          ? { ...node, data: { ...node.data, maxChars: newMaxChars } }
          : node
      )
    );
  }, []);

  // LLM 노드 - 모델 업데이트
  const updateSelectedModel = useCallback((nodeId: string, newModel: string) => {
    setNodes((nds) =>
      nds.map((node) =>
        node.id === nodeId
          ? { ...node, data: { ...node.data, model: newModel } }
          : node
      )
    );
    if (selectedNode && selectedNode.id === nodeId) {
      setSelectedNode((prevNode) => prevNode ? { ...prevNode, data: { ...prevNode.data, model: newModel } } : null);
    }
  }, [selectedNode]);

  // LLM 노드 - 프롬프트 업데이트
  const updateNodePrompts = useCallback(
    (nodeId: string, newPrompts: { type: string; text: string }[]) => {
      setNodes((nds) =>
        nds.map((node) =>
          node.id === nodeId
            ? { ...node, data: { ...node.data, prompts: newPrompts } }
            : node
        )
      );
      if (selectedNode && selectedNode.id === nodeId) {
        setSelectedNode((prevNode) => prevNode ? { ...prevNode, data: { ...prevNode.data, prompts: newPrompts } } : null);
      }
    },
    [selectedNode]
  );

  // LLM 노드 - 프롬프트 삭제
  const removePrompt = useCallback((nodeId: string, index: number) => {
      setNodes((nds) =>
        nds.map((node) =>
          node.id === nodeId
            ? {
                ...node,
                data: {
                  ...node.data,
                  prompts: node.data.prompts.filter((_: any, i: number) => i !== index),
                },
              }
            : node
        )
      );
      
      if (selectedNode && selectedNode.id === nodeId) {
        setSelectedNode((prevNode) =>
          prevNode
            ? {
                ...prevNode,
                data: {
                  ...prevNode.data,
                  prompts: prevNode.data.prompts.filter((_: any, i: number) => i !== index),
                },
              }
            : null
        );
      }
    }, [selectedNode]);

  // 답변 노드 - 답변 업데이트
  const updateAnswer = useCallback((nodeId: string, answer: string) => {
    setNodes((nds) =>
      nds.map((node) =>
        node.id === nodeId
          ? { ...node, data: { ...node.data, answer: answer } }
          : node
      )
    );
  }, []);

  // 질문 분류기 노드 - 클래스 업데이트
  const updateClasses = useCallback(
    (nodeId: string, newClasses: {text: string}[]) => { 
      setNodes((nds) =>
        nds.map((node) =>
          node.id === nodeId
            ? { ...node, data: { ...node.data, classes: newClasses } }
            : node
        )
      );
      if (selectedNode && selectedNode.id === nodeId) {
        setSelectedNode((prevNode) =>
          prevNode ? { ...prevNode, data: { ...prevNode.data, classes: newClasses } } : null
        );
      }
    },
    [selectedNode]
  );

  // 변수 관리
  const [variables, setVariables] = useState<
    { name: string; value: string; type: string; isEditing: boolean }[]
  >([{ name: "변수1", value: "", type: "string", isEditing: false }, { name: "변수2", value: "", type: "string", isEditing: false }]);

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

  const addNode = useCallback(
    (type: string) => {
      if (!selectedNode) return;

      const isPositionOccupied = (x: number, y: number) => {
        return nodes.some(
          (node) =>
            Math.abs(node.position.x - x) < 200 &&
            Math.abs(node.position.y - y) < 160
        );
      };

      let newX = selectedNode.position.x + 200;
      let newY = selectedNode.position.y;

      while (isPositionOccupied(newX, newY)) {
        newY += 160;
      }

      const newNode = {
        id: uuidv4(),
        type,
        data: (() => {
          switch (type) {
            case "startNode":
              return { maxChars: undefined };
            case "llmNode":
              return { prompts: [{ type: "system", text: "" }], model: models[0].id };
            case "knowledgeNode":
              return {};
            case "ifelseNode":
              return {};
            case "answerNode":
              return {};
            case "questionclassifierNode":
              return { classes: [{ text: "" }, { text: "" }] };
            case "variableallocatorNode":
              return {};
            default:
              return {};
          }
        })(),
        position: { x: newX, y: newY },
      };

      const newEdge = {
        id: `e${selectedNode.id}-${newNode.id}`,
        source: selectedNode.id,
        target: newNode.id,
      };

      setNodes((nds) => [...nds, newNode]);
      setEdges((eds) => [...eds, newEdge]);

      setSelectedNode(newNode);
      setSelectedNodeId(newNode.id);
    },
    [selectedNode, nodes]
  );

  useEffect(() => {
    if (selectedNodeId) {
      const selected = nodes.find((node) => node.id === selectedNodeId);
      setSelectedNode(selected || null);
    }
  }, [selectedNodeId, nodes]);




  const renderNodeDetail = () => {
    if (!selectedNode) return null;

    switch (selectedNode.type) {
      case "startNode":
        return (
          <StartNodeDetail
            maxChars={selectedNode.data.maxChars}
            setMaxChars={(newMaxChars: number) =>
              updateMaxChars(selectedNode.id, newMaxChars)
            }
            addNode={addNode}
            onClose={handleCloseDetail}
          />
        );
      case "llmNode":
        return (
          <LlmNodeDetail
            prompts={selectedNode.data.prompts || []}
            setPrompts={(newPrompts) => updateNodePrompts(selectedNode.id, newPrompts)}
            selectedModel={selectedNode.data.model || models[0].id}
            setModel={(newModel: string) => updateSelectedModel(selectedNode.id, newModel)}
            removePrompt={(index: number) => removePrompt(selectedNode.id, index)}
            addNode={addNode}
            onClose={handleCloseDetail}
          />
        );
      case "knowledgeNode":
        return <KnowledgeNodeDetail addNode={addNode} onClose={handleCloseDetail} />;
      case "ifelseNode":
        return <IfelseNodeDetail variables={variables} addNode={addNode} onClose={handleCloseDetail}/>;
      case "answerNode":
        return (
          <AnswerNodeDetail 
            answer={selectedNode.data.answer}
            setAnswer={(newAnswer: string) =>
              updateAnswer(selectedNode.id, newAnswer)
            }
            addNode={addNode}
            onClose={handleCloseDetail}/>
        );
      case "questionclassifierNode":
          return (
            <QuestionClassifierNodeDetail
              classes={selectedNode.data.classes || []}
              setClasses={(newClasses) => updateClasses(selectedNode.id, newClasses)}
              addNode={addNode}
              onClose={handleCloseDetail}
            />
          );
      case "variableallocatorNode":
        return (
          <VariableAllocatorNodeDetail variables={variables} addNode={addNode} onClose={handleCloseDetail}/>
        );
      default:
        return null;
    }
  };

  const renderVariableDetail = () => {
    if (!showVariableDetail) return null;

    return <VariableDetail
        variables={variables}
        handleVariableChange={handleVariableChange}
        handleAddVariable={handleAddVariable}
        handleRemoveVariable={handleRemoveVariable}
        handleEditToggle={handleEditToggle}
        onClose={() => setShowVariableDetail(false)}
      />;
  };

  const [showChatbotCreationModal, setShowChatbotCreationModal] = useState(false);

  const handleChatbotCreationClick = useCallback(() => {
    setShowChatbotCreationModal((prev) => !prev);
  }, []);

  const renderChatbotCreationModal = () => {
    if (!showChatbotCreationModal) return null;

    return (
      <div className="text-[14px] absolute top-[135px] right-[25px] p-4 bg-white shadow-lg rounded-[10px] flex flex-col justify-between gap-3 z-[100] w-[250px] h-[200px]">
        <button className="px-3 py-2.5 bg-[#9A75BF] rounded-[8px] text-white font-bold cursor-pointer">
          업데이트
        </button>
        <div className="flex flex-col gap-3">
          <button className="p-2 bg-[#F2F2F2] rounded-[8px] cursor-pointer text-start flex flex-row items-center gap-1">
            앱 실행<BsArrowUpRight/>
          </button>
          <button className="p-2 bg-[#F2F2F2] rounded-[8px] cursor-pointer text-start flex flex-row items-center gap-1">
            사이트에 삽입<BsArrowUpRight/>
          </button>
        </div>
        
      </div>
    );
  };

  const nodesWithSelection = nodes.map((node) => ({
    ...node, 
    selected: node.id === selectedNodeId,
  }));

  return (
    <>
      <div className="absolute top-[80px] right-[30px] flex flex-row gap-3 z-[10]">
        <button
          className="px-3 py-2.5 bg-white rounded-[10px] text-[#9A75BF] font-bold shadow-[0px_2px_8px_rgba(0,0,0,0.25)] cursor-pointer"
          onClick={handleVariableButtonClick}
        >
          변수
        </button>
        <button onClick={handleChatbotCreationClick} className="flex flex-row gap-1 items-center px-3 py-2.5 bg-[#9A75BF] rounded-[10px] text-white font-bold shadow-[0px_2px_8px_rgba(0,0,0,0.25)] cursor-pointer">
          챗봇 생성 <MdKeyboardArrowDown className="size-4"/>
        </button>
      </div>
      <div className="absolute top-[140px] right-[30px] z-[10] flex flex-row">
        {renderNodeDetail()}
        {renderVariableDetail()}
      </div>
      {renderChatbotCreationModal()}
      <ReactFlowProvider>
        <div style={{ height: "calc(100vh - 60px)", backgroundColor: "#F0EFF1" }}>
          <ReactFlow
            nodes={nodesWithSelection}
            edges={edges}
            onNodesChange={onNodesChange}
            onEdgesChange={onEdgesChange}
            onNodeClick={onNodeClick}
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
          </ReactFlow>
        </div>
      </ReactFlowProvider>
    </>
  );
}

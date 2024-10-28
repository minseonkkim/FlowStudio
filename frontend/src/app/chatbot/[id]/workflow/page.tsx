'use client'

import { useCallback, useState } from "react";
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
import StartNode from "@/components/chatbot/customnode/StartNode";
import LlmNode from "@/components/chatbot/customnode/LlmNode";
import KnowledgeNode from "@/components/chatbot/customnode/KnowledgeNode";
import IfelseNode from "@/components/chatbot/customnode/IfelseNode";
import AnswerNode from "@/components/chatbot/customnode/AnswerNode";
import QuestionClassifierNode from "@/components/chatbot/customnode/QuestionClassifierNode";
import VariableAllocatorNode from "@/components/chatbot/customnode/VariableAllocatorNode";
import StartNodeDetail from "@/components/chatbot/nodedetail/StartNodeDetail";
import LlmNodeDetail from "@/components/chatbot/nodedetail/LlmNodeDetail";
import KnowledgeNodeDetail from "@/components/chatbot/nodedetail/KnowledgeNodeDetail";
import IfelseNodeDetail from "@/components/chatbot/nodedetail/IfelseNodeDetail";
import AnswerNodeDetail from "@/components/chatbot/nodedetail/AnswerNodeDetail";

interface Model {
  id: string;
  name: string;
}

const models: Model[] = [
  { id: "gpt-3.5-turbo", name: "GPT-3.5 Turbo" },
  { id: "gpt-4", name: "GPT-4" },
  { id: "gpt-4-32k", name: "GPT-4 (32k)" },
];

interface Prompt {
  type: string;
  text: string;
}

const initialNodes: Node[] = [
  {
    id: "1",
    type: "startNode",
    data: { label: "1", maxChars: undefined },
    position: { x: 250, y: 100 },
  },
  {
    id: "2",
    type: "llmNode",
    data: { label: "2", prompts: [{type: "system", text: ""}], model: models[0].id },
    position: { x: 460, y: 100 },
  },
  {
    id: "3",
    type: "knowledgeNode",
    data: { label: "3" },
    position: { x: 700, y: 100 },
  },
  {
    id: "4",
    type: "ifelseNode",
    data: { label: "4" },
    position: { x: 900, y: 100 },
  },
  {
    id: "5",
    type: "answerNode",
    data: { label: "5", answer: "" },
    position: { x: 1100, y: 100 },
  },
  {
    id: "6",
    type: "questionclassifierNode",
    data: { label: "6" },
    position: { x: 1300, y: 100 },
  },
  {
    id: "7",
    type: "variableallocatorNode",
    data: { label: "7" },
    position: { x: 1500, y: 100 },
  },
];

const initialEdges = [
  { id: "e1-2", source: "1", target: "2" },
  { id: "e2-3", source: "2", target: "3" },
  { id: "e3-4", source: "3", target: "4" },
  { id: "e4-5", source: "4", target: "5" },
  { id: "e5-6", source: "5", target: "6" },
  { id: "e6-7", source: "6", target: "7" },
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

  const onNodesChange = useCallback((changes: NodeChange[]) => {
    setNodes((nds) => applyNodeChanges(changes, nds));
  }, []);

  const onEdgesChange = useCallback((changes: EdgeChange[]) => {
    setEdges((eds) => applyEdgeChanges(changes, eds));
  }, []);

  const onNodeClick = useCallback((event: React.MouseEvent, node: Node) => {
    setSelectedNode(node);
  }, []);

  const handleCloseDetail = useCallback(() => {
    setSelectedNode(null); 
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
            onClose={handleCloseDetail}
          />
        );
      case "knowledgeNode":
        return <KnowledgeNodeDetail onClose={handleCloseDetail} />;
      case "ifelseNode":
        return <IfelseNodeDetail onClose={handleCloseDetail}/>;
      case "answerNode":
        return (
        <AnswerNodeDetail 
        answer={selectedNode.data.answer}
        setAnswer={(newAnswer: string) =>
          updateAnswer(selectedNode.id, newAnswer)
        }
        onClose={handleCloseDetail}/>);
      default:
        return null;
    }
  };

  return (
    <>
      <div className="absolute top-[80px] right-[30px] flex flex-row gap-3 z-[10]">
        <button className="px-3 py-2.5 bg-white rounded-[10px] text-[#9A75BF] font-bold shadow-[0px_2px_8px_rgba(0,0,0,0.25)] cursor-pointer">
          변수
        </button>
        <button className="px-3 py-2.5 bg-[#9A75BF] rounded-[10px] text-white font-bold shadow-[0px_2px_8px_rgba(0,0,0,0.25)] cursor-pointer">
          챗봇 생성
        </button>
      </div>
      <div className="absolute top-[140px] right-[30px] z-[10]">
        {renderNodeDetail()}
      </div>
      <ReactFlowProvider>
        <div style={{ height: "calc(100vh - 60px)", backgroundColor: "#F0EFF1" }}>
          <ReactFlow
            nodes={nodes}
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

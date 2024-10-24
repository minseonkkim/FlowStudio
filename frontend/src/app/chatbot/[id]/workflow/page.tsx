'use client'

import React, { useCallback, useState } from 'react'
import ReactFlow, {
  Background,
  Controls,
  MiniMap,
  applyNodeChanges,
  applyEdgeChanges,
  NodeChange,
  EdgeChange,
  ReactFlowProvider,
  Node
} from 'reactflow'
import 'reactflow/dist/style.css'
import StartNode from '@/components/chatbot/customnode/StartNode'
import LlmNode from '@/components/chatbot/customnode/LlmNode'
import KnowledgeNode from '@/components/chatbot/customnode/KnowledgeNode'
import IfelseNode from '@/components/chatbot/customnode/IfelseNode'
import AnswerNode from '@/components/chatbot/customnode/AnswerNode'
import QuestionClassifierNode from '@/components/chatbot/customnode/QuestionClassifierNode'
import VariableAllocatorNode from '@/components/chatbot/customnode/VariableAllocatorNode'
import StartNodeDetail from '@/components/chatbot/nodedetail/StartNodeDetail'
import LlmNodeDetail from '@/components/chatbot/nodedetail/LlmNodeDetail'

const models = [
    { id: "gpt-3.5-turbo", name: "GPT-3.5 Turbo" },
    { id: "gpt-4", name: "GPT-4" },
    { id: "gpt-4-32k", name: "GPT-4 (32k)" },
];

const initialNodes: Node[] = [
  {
    id: '1',
    type: 'startNode',
    data: { label: '1',  maxChars: undefined },
    position: { x: 250, y: 100 },
  },
  {
    id: '2',
    type: 'llmNode',
    data: { label: '2' },
    position: { x: 460, y: 100 },
  },
  {
    id: '3',
    type: 'knowledgeNode',
    data: { label: '3' },
    position: { x: 700, y: 100 },
  },
  {
    id: '4',
    type: 'ifelseNode',
    data: { label: '4' },
    position: { x: 900, y: 100 },
  },
  {
    id: '5',
    type: 'answerNode',
    data: { label: '5' },
    position: { x: 1100, y: 100 },
  },
  {
    id: '6',
    type: 'questionclassifierNode',
    data: { label: '6' },
    position: { x: 1300, y: 100 },
  },
  {
    id: '7',
    type: 'variableallocatorNode',
    data: { label: '7' },
    position: { x: 1500, y: 100 },
  },
];


const nodeTypes = {
  startNode: StartNode,
  llmNode: LlmNode,
  knowledgeNode: KnowledgeNode,
  ifelseNode: IfelseNode,
  answerNode: AnswerNode,
  questionclassifierNode: QuestionClassifierNode,
  variableallocatorNode: VariableAllocatorNode
};

const initialEdges = [
  { id: 'e1-2', source: '1', target: '2' },
  { id: 'e2-3', source: '2', target: '3' },
  { id: 'e3-4', source: '3', target: '4' },
  { id: 'e4-5', source: '4', target: '5' },
  { id: 'e5-6', source: '5', target: '6' },
  { id: 'e6-7', source: '6', target: '7' },
];

const Page = () => {
  const [nodes, setNodes] = useState<Node[]>(initialNodes);
  const [edges, setEdges] = useState(initialEdges);

  const onNodesChange = useCallback((changes: NodeChange[]) => {
    setNodes((nds) => applyNodeChanges(changes, nds));
  }, []);

  const onEdgesChange = useCallback((changes: EdgeChange[]) => {
    setEdges((eds) => applyEdgeChanges(changes, eds));
  }, []);

  const [selectedNode, setSelectedNode] = useState<Node | null>(null); 
  
  const onNodeClick = useCallback((event: React.MouseEvent, node: Node) => {
    setSelectedNode(node);
  }, []);


  const updateSelectedModel = useCallback((nodeId: string, newModel: string) => {
    setNodes((nds) =>
      nds.map((node) =>
        node.id === nodeId ? { ...node, data: { ...node.data, model: newModel } } : node
      )
    );
  }, []);


  // 시작 노드 - 최대 글자수 업데이트
  const updateMaxChars = useCallback((nodeId: string, newMaxChars: number) => {
    setNodes((nds) =>
      nds.map((node) =>
        node.id === nodeId
          ? { ...node, data: { ...node.data, maxChars: newMaxChars } }
          : node
      )
    );
  }, []);

  // LLM 노드 - 프롬프트 관리
  const updateNodePrompts = useCallback((nodeId: string, newPrompts: { type: string; text: string }[]) => {
    setNodes((nds) =>
      nds.map((node) =>
        node.id === nodeId
          ? { ...node, data: { ...node.data, prompts: newPrompts } }
          : node
      )
    );
  }, []);


  const renderNodeDetail = () => {
    if (!selectedNode) return null;

    switch (selectedNode.type) {
      case 'startNode':
        return  <StartNodeDetail
            maxChars={selectedNode.data.maxChars}
            setMaxChars={(newMaxChars: number) =>
              updateMaxChars(selectedNode.id, newMaxChars)
            }
          />;
      case 'llmNode':
        return <LlmNodeDetail
            prompts={selectedNode.data.prompts || []}
            setPrompts={(newPrompts) => updateNodePrompts(selectedNode.id, newPrompts)}
            selectedModel={selectedNode.data.model || models[0].id}
            setModel={(newModel) => updateSelectedModel(selectedNode.id, newModel)}
          />
      // case 'knowledgeNode':
      //   return <KnowledgeNodeDetail />;
      // case 'ifelseNode':
      //   return <IfelseNodeDetail />;
      // case 'answerNode':
      //   return <AnswerNodeDetail />;
      // case 'questionclassifierNode':
      //   return <QuestionClassifierNodeDetail />;
      // case 'variableallocatorNode':
      //   return <VariableAllocatorNodeDetail />;
      default:
        return null;
    }
  };

  return (
    <>
      <div className="absolute top-[80px] right-[30px] flex flex-row gap-3 z-[10]">
        <button
          className="px-3 py-2.5 bg-white rounded-[10px] text-[#9A75BF] font-bold shadow-[0px_2px_8px_rgba(0,0,0,0.25)] cursor-pointer">
          변수
        </button>
        <button
          className="px-3 py-2.5 bg-[#9A75BF] rounded-[10px] text-white font-bold shadow-[0px_2px_8px_rgba(0,0,0,0.25)] cursor-pointer">
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
            zoomActivationKeyCode={null}
            nodeTypes={nodeTypes}
            fitView
          >
            <MiniMap
              style={{
                bottom: 5,
                left: 40,
                width: 150,
                height: 100
              }}
            />
            <Controls />
            <Background />
          </ReactFlow>
        </div>
      </ReactFlowProvider>
    </>
  )
}

export default Page;

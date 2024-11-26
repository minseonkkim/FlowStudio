import React from "react";
import { Meta, StoryFn } from "@storybook/react";
import RetrieverNodeDetail from "./RetrieverNodeDetail";
import { NodeData, EdgeData, Knowledge } from "@/types/chatbot";
import { ConnectedNode } from "@/types/workflow";
import { Node, Edge } from "reactflow";

// Storybook 메타 정보 설정
export default {
  title: "ChatFlow/RetrieverNodeDetail",
  component: RetrieverNodeDetail,
  argTypes: {
    chatFlowId: { control: { type: "number" } },
    node: { control: { type: "object" } },
    nodes: { control: { type: "object" } },
    edges: { control: { type: "object" } },
    onClose: { action: "closed" },
  },
} as Meta<typeof RetrieverNodeDetail>;

// Template 생성
const Template: StoryFn<typeof RetrieverNodeDetail> = (args) => <RetrieverNodeDetail {...args} />;

// 기본 스토리
export const Default = Template.bind({});
Default.args = {
  chatFlowId: 123,
  node: {
    id: "1",
    type: "RETRIEVER",
    data: {
      name: "지식1",
      topK: 5,
      scoreThreshold: 0.7,
      knowledge: { knowledgeId: 1, title: "출결및평가.pdf", totalToken: 150 } as Knowledge,
      nodeId: 1,
      isComplete: false,
      isError: false,
    } as NodeData,
  } as Node<NodeData>,
  nodes: [
    {
      id: "1",
      type: "RETRIEVER",
      data: {
        name: "지식1",
        topK: 5,
        scoreThreshold: 0.7,
        knowledge: { knowledgeId: 1, title: "출결및평가.pdf", totalToken: 150 } as Knowledge,
        nodeId: 1,
        isComplete: false,
        isError: false,
      } as NodeData,
    },
  ] as Node<NodeData>[],
  edges: [] as Edge<EdgeData>[],
  setNodes: () => {},
  setEdges: () => {},
  setSelectedNode: () => {},
  onClose: () => console.log("Close button clicked"),
  connectedNodes: [
    {
      nodeId: 2,
      type: "ANSWER",
      name: "답변",
    },
  ] as ConnectedNode[],
};
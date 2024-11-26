import React from "react";
import { Meta, StoryFn } from "@storybook/react";
import StartNodeDetail from "./StartNodeDetail";
import { NodeData, EdgeData } from "@/types/chatbot";
import { ConnectedNode } from "@/types/workflow"; // 경로 수정
import { Node, Edge } from "reactflow";

// Storybook 메타 정보 설정
export default {
  title: "ChatFlow/StartNodeDetail",
  component: StartNodeDetail,
  argTypes: {
    chatFlowId: { control: { type: "number" } },
    node: { control: { type: "object" } },
    nodes: { control: { type: "object" } },
    edges: { control: { type: "object" } },
    onClose: { action: "closed" },
  },
} as Meta<typeof StartNodeDetail>;

// Template 생성
const Template: StoryFn<typeof StartNodeDetail> = (args) => <StartNodeDetail {...args} />;

// 기본 스토리
export const Default = Template.bind({});
Default.args = {
  chatFlowId: 123,
  node: {
    id: "1",
    type: "START",
    data: {
      name: "시작 노드",
      maxLength: 50,
      nodeId: 1,
      isComplete: false,
      isError: false,
    } as NodeData,
  } as Node<NodeData>,
  nodes: [
    {
      id: "1",
      type: "START",
      data: {
        name: "시작",
        maxLength: 50,
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

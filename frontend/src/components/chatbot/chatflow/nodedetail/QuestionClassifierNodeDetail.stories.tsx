// QuestionClassifierNodeDetail.stories.tsx
import React from "react";
import { Meta, StoryFn } from "@storybook/react";
import QuestionClassifierNodeDetail from "./QuestionClassifierNodeDetail";
import { NodeData, EdgeData, QuestionClass } from "@/types/chatbot";
import { ConnectedNode } from "@/types/workflow";
import { Node, Edge } from "reactflow";

// Storybook 메타 정보 설정
export default {
  title: "ChatFlow/QuestionClassifierNodeDetail",
  component: QuestionClassifierNodeDetail,
  argTypes: {
    chatFlowId: { control: { type: "number" } },
    node: { control: { type: "object" } },
    nodes: { control: { type: "object" } },
    edges: { control: { type: "object" } },
    onClose: { action: "closed" },
  },
} as Meta<typeof QuestionClassifierNodeDetail>;

// Template 생성
const Template: StoryFn<typeof QuestionClassifierNodeDetail> = (args) => <QuestionClassifierNodeDetail {...args} />;

// 기본 스토리
export const Default = Template.bind({});
Default.args = {
  chatFlowId: 123,
  node: {
    id: "1",
    type: "QUESTION_CLASSIFIER",
    data: {
      name: "질문 분류기",
      questionClasses: [
        { id: 1, content: "SSAFY, 싸피, 싸피 출결, 싸피 평가 기준과 관련된 모든 질문" },
        { id: 2, content: "그 외" },
      ] as QuestionClass[],
      nodeId: 1,
      isComplete: false,
      isError: false,
    } as NodeData,
  } as Node<NodeData>,
  nodes: [
    {
      id: "1",
      type: "QUESTION_CLASSIFIER",
      data: {
        name: "질문 분류기",
        questionClasses: [
          { id: 1, content: "SSAFY, 싸피, 싸피 출결, 싸피 평가 기준과 관련된 모든 질문" },
          { id: 2, content: "그 외" },
        ] as QuestionClass[],
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
      sourceConditionId: 1,
    },
  ] as ConnectedNode[],
};

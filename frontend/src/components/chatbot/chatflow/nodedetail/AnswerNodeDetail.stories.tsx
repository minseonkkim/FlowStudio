// AnswerNodeDetail.stories.tsx
import React from "react";
import { Meta, StoryFn } from "@storybook/react";
import AnswerNodeDetail from "./AnswerNodeDetail";
import { NodeData, EdgeData } from "@/types/chatbot";
import { Node, Edge } from "reactflow";

// Storybook 메타 정보 설정
export default {
  title: "ChatFlow/AnswerNodeDetail",
  component: AnswerNodeDetail,
  argTypes: {
    node: { control: { type: "object" } },
    nodes: { control: { type: "object" } },
    edges: { control: { type: "object" } },
    onClose: { action: "closed" },
  },
} as Meta<typeof AnswerNodeDetail>;

// Template 생성
const Template: StoryFn<typeof AnswerNodeDetail> = (args) => <AnswerNodeDetail {...args} />;

// 기본 스토리
export const Default = Template.bind({});
Default.args = {
  node: {
    id: "1",
    type: "ANSWER",
    data: {
      name: "답변",
      outputMessage: `<span contenteditable="false" data-value="{{308}}" style="font-family: monospace; background-color: rgb(234, 242, 255); padding: 2px 4px; border-radius: 3px; box-shadow: rgb(59, 130, 246) 0px 0px 0px 0.25px; display: inline-block; margin-right: 2px;">LLM</span>`,
      nodeId: 1,
      isComplete: false,
      isError: false,
    } as NodeData,
  } as Node<NodeData>,
  nodes: [
    {
      id: "1",
      type: "ANSWER",
      data: {
        name: "답변",
        outputMessage: `<span contenteditable="false" data-value="{{308}}" style="font-family: monospace; background-color: rgb(234, 242, 255); padding: 2px 4px; border-radius: 3px; box-shadow: rgb(59, 130, 246) 0px 0px 0px 0.25px; display: inline-block; margin-right: 2px;">LLM</span>`,
        nodeId: 1,
        isComplete: false,
        isError: false,
      } as NodeData,
    },
  ] as Node<NodeData>[],
  edges: [] as Edge<EdgeData>[],
  setNodes: () => {},
  onClose: () => console.log("Close button clicked"),
};
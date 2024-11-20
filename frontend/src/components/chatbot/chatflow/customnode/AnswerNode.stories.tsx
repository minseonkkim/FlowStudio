import React from "react";
import { Meta, StoryFn } from "@storybook/react";
import AnswerNode from "./AnswerNode";
import { NodeData } from "@/types/chatbot";
import { ReactFlowProvider } from "reactflow";

// Storybook 메타 정보 설정
export default {
  title: "ChatFlow/AnswerNode",
  component: AnswerNode,
  decorators: [
    (Story) => (
      <ReactFlowProvider>
        <Story />
      </ReactFlowProvider>
    ),
  ],
  argTypes: {
    // NodeData 속성에 따라 매개변수 설정
    data: {
      control: { type: "object" },
    },
    selected: {
      control: { type: "boolean" },
    },
  },
} as Meta<typeof AnswerNode>;

// Template 생성
const Template: StoryFn<typeof AnswerNode> = (args) => <AnswerNode {...args} />;

// 기본 스토리
export const Default = Template.bind({});
Default.args = {
  id: "1",
  data: {
    name: "답변",
    isComplete: false,
    isError: false,
    renderOutputMessage: { __html: `<span contenteditable="false" data-value="{{310}}" style="font-family: monospace; background-color: rgb(234, 242, 255); padding: 2px 4px; border-radius: 3px; box-shadow: rgb(59, 130, 246) 0px 0px 0px 0.25px; display: inline-block; margin-right: 2px;">LLM</span>` },
    onDelete: (id: string) => alert(`Delete node with id: ${id}`),
  } as NodeData,
  selected: false,
};

// 선택된 상태 스토리
export const Selected = Template.bind({});
Selected.args = {
  id: "2",
  data: {
    name: "답변",
    isComplete: true,
    isError: false,
    renderOutputMessage: { __html: `<span contenteditable="false" data-value="{{310}}" style="font-family: monospace; background-color: rgb(234, 242, 255); padding: 2px 4px; border-radius: 3px; box-shadow: rgb(59, 130, 246) 0px 0px 0px 0.25px; display: inline-block; margin-right: 2px;">LLM</span>` },
    onDelete: (id: string) => alert(`Delete node with id: ${id}`),
  } as NodeData,
  selected: true,
};

// 에러 상태 스토리
export const ErrorState = Template.bind({});
ErrorState.args = {
  id: "3",
  data: {
    name: "답변",
    isComplete: false,
    isError: true,
    renderOutputMessage: { __html: `<span contenteditable="false" data-value="{{310}}" style="font-family: monospace; background-color: rgb(234, 242, 255); padding: 2px 4px; border-radius: 3px; box-shadow: rgb(59, 130, 246) 0px 0px 0px 0.25px; display: inline-block; margin-right: 2px;">LLM</span>` },
    onDelete: (id: string) => alert(`Delete node with id: ${id}`),
  } as NodeData,
  selected: false,
};

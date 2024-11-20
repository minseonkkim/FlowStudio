import React from "react";
import { Meta, StoryFn } from "@storybook/react";
import StartNode from "./StartNode";
import { NodeData } from "@/types/chatbot";
import { ReactFlowProvider } from "reactflow";

// Storybook의 메타 정보 설정
export default {
  title: "ChatFlow/StartNode",
  component: StartNode,
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
} as Meta<typeof StartNode>;

// Template 생성
const Template: StoryFn<typeof StartNode> = (args) => <StartNode {...args} />;

// 기본 스토리
export const Default = Template.bind({});
Default.args = {
  data: {
    name: "시작",
    maxLength: 100,
    isComplete: false,
    isError: false,
  } as NodeData,
  selected: false,
};

// 선택된 상태 스토리
export const Selected = Template.bind({});
Selected.args = {
  data: {
    name: "시작",
    maxLength: 100,
    isComplete: true,
    isError: false,
  } as NodeData,
  selected: true,
};

// 에러 상태 스토리
export const ErrorState = Template.bind({});
ErrorState.args = {
  data: {
    name: "시작",
    maxLength: 100,
    isComplete: false,
    isError: true,
  } as NodeData,
  selected: false,
};

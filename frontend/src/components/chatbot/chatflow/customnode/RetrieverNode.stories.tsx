import React from "react";
import { Meta, StoryFn } from "@storybook/react";
import RetrieverNode from "./RetrieverNode";
import { NodeData } from "@/types/chatbot";
import { ReactFlowProvider } from "reactflow";

export default {
  title: "ChatFlow/RetrieverNode",
  component: RetrieverNode,
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
} as Meta<typeof RetrieverNode>;

// Template 생성
const Template: StoryFn<typeof RetrieverNode> = (args) => <RetrieverNode {...args} />;

// 기본 스토리
export const Default = Template.bind({});
Default.args = {
  id: "1",
  data: {
    name: "지식 검색",
    isComplete: false,
    isError: false,
    knowledge: {
      title: "출결및평가.pdf",
    },
    onDelete: (id: string) => alert(`Delete node with id: ${id}`),
  } as NodeData,
  selected: false,
};

// 선택된 상태 스토리
export const Selected = Template.bind({});
Selected.args = {
  id: "2",
  data: {
    name: "지식 검색",
    isComplete: true,
    isError: false,
    knowledge: {
      title: "출결및평가.pdf",
    },
    onDelete: (id: string) => alert(`Delete node with id: ${id}`),
  } as NodeData,
  selected: true,
};

// 에러 상태 스토리
export const ErrorState = Template.bind({});
ErrorState.args = {
  id: "3",
  data: {
    name: "지식 검색",
    isComplete: false,
    isError: true,
    knowledge: {
      title: "출결및평가.pdf",
    },
    onDelete: (id: string) => alert(`Delete node with id: ${id}`),
  } as NodeData,
  selected: false,
};

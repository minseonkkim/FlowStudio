import React from "react";
import { Meta, StoryFn } from "@storybook/react";
import QuestionClassifierNode from "./QuestionClassifierNode";
import { NodeData, QuestionClass } from "@/types/chatbot";
import { ReactFlowProvider } from "reactflow";

// Storybook 메타 정보 설정
export default {
  title: "ChatFlow/QuestionClassifierNode",
  component: QuestionClassifierNode,
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
} as Meta<typeof QuestionClassifierNode>;

// Template 생성
const Template: StoryFn<typeof QuestionClassifierNode> = (args) => <QuestionClassifierNode {...args} />;

// 기본 스토리
export const Default = Template.bind({});
Default.args = {
  id: "1",
  data: {
    name: "질문 분류기",
    isComplete: false,
    isError: false,
    questionClasses: [
      { id: 1, content: "SSAFY, 싸피, 싸피 출결, 싸피 평가 기준과 관련된 모든 질문" },
      { id: 2, content: "그 외" },
    ] as QuestionClass[],
    onDelete: (id: string) => alert(`Delete node with id: ${id}`),
  } as NodeData,
  selected: false,
};

// 선택된 상태 스토리
export const Selected = Template.bind({});
Selected.args = {
  id: "2",
  data: {
    name: "질문 분류기",
    isComplete: true,
    isError: false,
    questionClasses: [
      { id: 1, content: "SSAFY, 싸피, 싸피 출결, 싸피 평가 기준과 관련된 모든 질문" },
      { id: 2, content: "그 외" },
    ] as QuestionClass[],
    onDelete: (id: string) => alert(`Delete node with id: ${id}`),
  } as NodeData,
  selected: true,
};

// 에러 상태 스토리
export const ErrorState = Template.bind({});
ErrorState.args = {
  id: "3",
  data: {
    name: "질문 분류기",
    isComplete: false,
    isError: true,
    questionClasses: [
      { id: 1, content: "SSAFY, 싸피, 싸피 출결, 싸피 평가 기준과 관련된 모든 질문" },
      { id: 2, content: "그 외" },
    ] as QuestionClass[],
    onDelete: (id: string) => alert(`Delete node with id: ${id}`),
  } as NodeData,
  selected: false,
};

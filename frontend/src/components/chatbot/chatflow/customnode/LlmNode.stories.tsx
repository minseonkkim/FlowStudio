import React from "react";
import { Meta, StoryFn } from "@storybook/react";
import LlmNode from "./LlmNode";
import { NodeData } from "@/types/chatbot";
import { ReactFlowProvider } from "reactflow";

// Storybook 메타 정보 설정
export default {
  title: "ChatFlow/LlmNode",
  component: LlmNode,
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
} as Meta<typeof LlmNode>;

// Template 생성
const Template: StoryFn<typeof LlmNode> = (args) => <LlmNode {...args} />;

// 기본 스토리
export const Default = Template.bind({});
Default.args = {
  id: "1",
  data: {
    name: "LLM",
    isComplete: false,
    isError: false,
    modelName: "gpt-4o-mini",
    promptSystem: "시스템 프롬프트 예제",
    promptUser: "유저 프롬프트 예제",
    renderPromptSystem: {
      __html: `<span contenteditable="false" data-value="{{312}}" style="font-family: monospace; background-color: rgb(255, 243, 235); padding: 2px 4px; border-radius: 3px; box-shadow: rgb(249, 115, 22) 0px 0px 0px 0.25px; display: inline-block; margin-right: 2px;">지식 검색</span>
위의 문서에 기반해서 답변해줘`,
    },
    renderPromptUser: {
      __html: `<span contenteditable="false" data-value="{{INPUT_MESSAGE}}" style="font-family: monospace; background-color: rgb(236, 243, 224); padding: 2px 4px; border-radius: 3px; box-shadow: rgb(149, 196, 71) 0px 0px 0px 0.25px; display: inline-block; margin-right: 2px;">시작</span>`,
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
    name: "LLM",
    isComplete: true,
    isError: false,
    modelName: "gpt-4o-mini",
    promptSystem: "선택된 시스템 프롬프트 예제",
    promptUser: "선택된 유저 프롬프트 예제",
    renderPromptSystem: {
      __html: `<span contenteditable="false" data-value="{{312}}" style="font-family: monospace; background-color: rgb(255, 243, 235); padding: 2px 4px; border-radius: 3px; box-shadow: rgb(249, 115, 22) 0px 0px 0px 0.25px; display: inline-block; margin-right: 2px;">지식 검색</span>
위의 문서에 기반해서 답변해줘`,
    },
    renderPromptUser: {
      __html: `<span contenteditable="false" data-value="{{INPUT_MESSAGE}}" style="font-family: monospace; background-color: rgb(236, 243, 224); padding: 2px 4px; border-radius: 3px; box-shadow: rgb(149, 196, 71) 0px 0px 0px 0.25px; display: inline-block; margin-right: 2px;">시작</span>`,
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
    name: "LLM",
    isComplete: false,
    isError: true,
    modelName: "gpt-4o-mini",
    promptSystem: "에러 시스템 프롬프트 예제",
    promptUser: "에러 유저 프롬프트 예제",
    renderPromptSystem: {
      __html: `<span contenteditable="false" data-value="{{312}}" style="font-family: monospace; background-color: rgb(255, 243, 235); padding: 2px 4px; border-radius: 3px; box-shadow: rgb(249, 115, 22) 0px 0px 0px 0.25px; display: inline-block; margin-right: 2px;">지식 검색</span>
위의 문서에 기반해서 답변해줘`,
    },
    renderPromptUser: {
      __html: `<span contenteditable="false" data-value="{{INPUT_MESSAGE}}" style="font-family: monospace; background-color: rgb(236, 243, 224); padding: 2px 4px; border-radius: 3px; box-shadow: rgb(149, 196, 71) 0px 0px 0px 0.25px; display: inline-block; margin-right: 2px;">시작</span>`,
    },
    onDelete: (id: string) => alert(`Delete node with id: ${id}`),
  } as NodeData,
  selected: false,
};

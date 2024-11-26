import React from "react";
import { Meta, StoryFn } from "@storybook/react";
import LlmNodeDetail from "./LlmNodeDetail";
import { NodeData, EdgeData } from "@/types/chatbot";
import { ConnectedNode } from "@/types/workflow";
import { Node, Edge } from "reactflow";

// Storybook 메타 정보 설정
export default {
  title: "ChatFlow/LlmNodeDetail",
  component: LlmNodeDetail,
  argTypes: {
    chatFlowId: { control: { type: "number" } },
    node: { control: { type: "object" } },
    nodes: { control: { type: "object" } },
    edges: { control: { type: "object" } },
    onClose: { action: "closed" },
  },
} as Meta<typeof LlmNodeDetail>;

// Template 생성
const Template: StoryFn<typeof LlmNodeDetail> = (args) => <LlmNodeDetail {...args} />;

// 기본 스토리
export const Default = Template.bind({});
Default.args = {
  chatFlowId: 123,
  node: {
    id: "1",
    type: "LLM",
    data: {
      name: "LLM 노드",
      maxTokens: 1500,
      temperature: 0.7,
      promptSystem: `<span contenteditable="false" data-value="{{312}}" style="font-family: monospace; background-color: rgb(255, 243, 235); padding: 2px 4px; border-radius: 3px; box-shadow: rgb(249, 115, 22) 0px 0px 0px 0.25px; display: inline-block; margin-right: 2px;">지식 검색</span>
위의 문서에 기반해서 답변해줘`,
      promptUser: `<span contenteditable="false" data-value="{{INPUT_MESSAGE}}" style="font-family: monospace; background-color: rgb(236, 243, 224); padding: 2px 4px; border-radius: 3px; box-shadow: rgb(149, 196, 71) 0px 0px 0px 0.25px; display: inline-block; margin-right: 2px;">시작</span>`,
      nodeId: 1,
      isComplete: false,
      isError: false,
    } as NodeData,
  } as Node<NodeData>,
  nodes: [
    {
      id: "1",
      type: "LLM",
      data: {
        name: "LLM 노드",
        maxTokens: 1500,
        temperature: 0.7,
        promptSystem: `<span contenteditable="false" data-value="{{312}}" style="font-family: monospace; background-color: rgb(255, 243, 235); padding: 2px 4px; border-radius: 3px; box-shadow: rgb(249, 115, 22) 0px 0px 0px 0.25px; display: inline-block; margin-right: 2px;">지식 검색</span>
위의 문서에 기반해서 답변해줘`,
        promptUser: `<span contenteditable="false" data-value="{{INPUT_MESSAGE}}" style="font-family: monospace; background-color: rgb(236, 243, 224); padding: 2px 4px; border-radius: 3px; box-shadow: rgb(149, 196, 71) 0px 0px 0px 0.25px; display: inline-block; margin-right: 2px;">시작</span>`,
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
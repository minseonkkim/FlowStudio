import { NodeData } from "@/types/workflow";
import axiosInstance from "./token/axiosInstance";
import { Dispatch, SetStateAction } from "react";
import { Edge, Node } from "reactflow";
import { EdgeData } from "@/types/chatbot";

// 노드 생성
export async function postNode(data: NodeData){
  try {
    const response = await axiosInstance.post('chat-flows/nodes', data)
    if (response.status === 200) {
      return response.data.data;
    } else {
      throw new Error('Failed to post node');
    }
  } catch (error) {
    console.error(error)
    throw error
  }
}

// 노드 상세 조회
export async function getNodeDetail(nodeId: number){
  try {
    const response = await axiosInstance.get(`chat-flows/nodes/${nodeId}`)
    if (response.status === 200) {
      return response.data.data;
    } else {
      throw new Error('Failed to get node detail');
    }
  } catch (error) {
    console.error(error)
    throw error
  }
}


// 노드 삭제
export async function deleteNode(nodeId: number){
  try {
    const response = await axiosInstance.delete(`chat-flows/nodes/${nodeId}`)
    console.log(response)
    if (response.status === 200) {
      return response.data;
    } else {
      throw new Error('Failed to delete node');
    }
  } catch (error) {
    console.error(error)
    throw error
  }
}

// 노드 수정
export async function putNode(nodeId: number, data: any){
  try {
    console.log("CALL NODE UPDATE : "+ {...data});
    const response = await axiosInstance.put(`chat-flows/nodes/${nodeId}/${data.type.toLowerCase()}`, data)
    if (response.status === 200) {
      return response.data.data;
    } else {
      throw new Error('Failed to put node');
    }
  } catch (error) {
    console.error(error)
    throw error
  }
}

// 질문 분류 클래스 수정
export async function putQuestionClassNode(questionClassId: number, data: {"content" : string}){
  try {
    const response = await axiosInstance.put(`chat-flows/nodes/question-classes/${questionClassId}`, data)
    if (response.status === 200) {
      return response.data.data;
    } else {
      throw new Error('Failed to put question-class node');
    }
  } catch (error) {
    console.error(error)
    throw error
  }
}

// 질문 분류 클래스 생성
export async function postQuestionClassNode(nodeId: number, data: {"content" : string}){
  try {
    const response = await axiosInstance.post(`chat-flows/nodes/${nodeId}/question-classes`, data)
    if (response.status === 200) {
      return response.data.data;
    } else {
      throw new Error('Failed to post question-class node');
    }
  } catch (error) {
    console.error(error)
    throw error
  }
}

// 간선 생성
export async function postEdge(chatFlowId: number, data: EdgeData): Promise<EdgeData>{
  try {
    const response = await axiosInstance.post(`chat-flows/${chatFlowId}/edges`, data)
    console.log("CALL EDGE CREATE");
    
    if (response.status === 200) {
      
      return response.data.data as EdgeData;
    } else {
      throw new Error('Failed to post edge');
    }
  } catch (error) {
    console.error(error)
    throw error
  }
}

// 간선 수정
export async function putEdge(chatFlowId: number, edgeId: number, data: EdgeData){
  try {
    const response = await axiosInstance.put(`chat-flows/${chatFlowId}/edges/${edgeId}`, data)
    if (response.status === 200) {
      return response.data.data;
    } else {
      throw new Error('Failed to put edge');
    }
  } catch (error) {
    console.error(error)
    throw error
  }
}

// 간선 삭제
export async function deleteEdge(chatFlowId: number, edgeId: number){
  console.log("chatFlowId", chatFlowId, "edgeId", edgeId)
  try {
    const response = await axiosInstance.delete(`chat-flows/${chatFlowId}/edges/${edgeId}`)
    if (response.status === 200) {
      return response.data;
    } else {
      throw new Error('Failed to delete edge');
    }
  } catch (error) {
    console.error(error)
    throw error
  }
}
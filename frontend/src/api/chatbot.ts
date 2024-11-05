import axiosInstance from '@/api/token/axiosInstance'
import { ChatFlowData } from '@/types/chatbot'

// 챗플로우 목록 조회
export async function getAllChatFlows(){
  try {
    const response = await axiosInstance.get('chat-flows')
    if (response.status === 200) {
      return response.data.data;
    } else {
      throw new Error('Failed to get chat-flows');
    }
  } catch (error) {
    console.error(error)
    throw error
  }
}

// 챗플로우 생성
export async function postChatFlow(data: ChatFlowData){
  try {
    const response = await axiosInstance.post('chat-flows', data)
    if (response.status === 200) {
      return response.data.data;
    } else {
      throw new Error('Failed to post chat-flow');
    }
  } catch (error) {
    console.error(error)
    throw error
  }
}

// 챗플로우 삭제
export async function deleteChatFlow(chatFlowId: number){
  try {
    const response = await axiosInstance.delete(`chat-flows/${chatFlowId}`)
    if (response.status === 200) {
      return response.data;
    } else {
      throw new Error('Failed to delete chat-flow');
    }
  } catch (error) {
    console.error(error)
    throw error
  }
}

// 챗플로우 수정
export async function patchChatFlow(chatFlowId: number, data: ChatFlowData){
  try {
    const response = await axiosInstance.patch(`chat-flows/${chatFlowId}`, data)
    if (response.status === 200) {
      return response.data.data;
    } else {
      throw new Error('Failed to patch chat-flow');
    }
  } catch (error) {
    console.error(error)
    throw error
  }
}
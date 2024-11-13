import axiosInstance from '@/api/token/axiosInstance';
import noAuthAxios from './token/noAuthAxios';

// 채팅 목록 조회
export async function getChattingList(chatFlowId: string) {
  try {
    const response = await axiosInstance.get(`/chat-flows/${chatFlowId}/chats`);
    if (response.status === 200) {
      return response.data.data;
    } else {
      throw new Error('Failed to fetch chat list');
    }
  } catch (error) {
    console.error(error);
    throw error;
  }
}

// 채팅 상세 조회
export async function getChatting(chatFlowId: string, chatId: string) {
  try {
    const response = await axiosInstance.get(`/chat-flows/${chatFlowId}/chats/${chatId}`);
    if (response.status === 200) {
      return response.data.data; 
    } else {
      throw new Error('Failed to fetch chat details');
    }
  } catch (error) {
    console.error(error);
    throw error;
  }
}

// 채팅 생성
export async function postChatting(chatFlowId: string, data: { isPreview: boolean }) {
  try {
    const response = await axiosInstance.post(`chat-flows/${chatFlowId}/chats`, data);
    if (response.status === 200) {
      return response
    } else {
      throw new Error('Failed to create chat');
    }
  } catch (error) {
    console.error(error);
    throw error;
  }
}

// 발행한 채팅방 삭제
export async function deleteChatting(chatFlowId: string, chatId: string) {
  try {
    const response = await axiosInstance.delete(`/chat-flows/${chatFlowId}/chats/${chatId}`);
    if (response.status === 200) {
      return response.data;
    } else {
      throw new Error('Failed to delete published chat');
    }
  } catch (error) {
    console.error(error);
    throw error;
  }
}

// 챗봇 발행 -> 아직 api 안나옴
export async function postChatBot(chatFlowId: string) {
  try {
    const response = await axiosInstance.post(`/chat-flows/${chatFlowId}/publish`);
    if (response.status === 200) {
      return response.data;
    } else {
      throw new Error('Failed to publish chatbot');
    }
  } catch (error) {
    console.error(error);
    throw error;
  }
}

// 챗봇에게 메시지 보내기
export async function postMessage(chatId: string, data: { message: string }) {
  try {
    const response = await noAuthAxios.post(`/chats/${chatId}`, data);
    if (response.status === 200) {
      return response.data.data;
    } else {
      throw new Error('Failed to send message');
    }
  } catch (error) {
    console.error(error);
    throw error;
  }
}

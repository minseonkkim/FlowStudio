import axiosInstance from '@/api/token/axiosInstance';


// 채팅 조회
export async function getChatting() {
  // 적어주세요
}

// 채팅 생성
export async function postChatting(chatFlowId: string, data: { isPreview: boolean }) {
  try {
    const response = await axiosInstance.post(`chat-flows/${chatFlowId}/chats`, data);
    if (response.status === 200) {
      return response.data.data;
    } else {
      throw new Error('Failed to create chat');
    }
  } catch (error) {
    console.error(error);
    throw error;
  }
}


// 발행한 채팅방 삭제
export async function deleteChatting() {
  //적어주세요
}

// 챗봇 발행
export async function postChatBot() {
  // 적어주세요
}

// 챗봇에게 메시지 보내기
export async function postMessage(chatId: string, data: { message : string }) {
  try {
    const response = await axiosInstance.post(`/chats/${chatId}`, data);
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

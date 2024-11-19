import axiosInstance from '@/api/token/axiosInstance';

// 테스트 완료한 챗플로우 목록 조회
export async function getChatFlowTestList(test:boolean) {
  try {
    let url = 'chat-flows';
    if (test) {
      url += '?test=true';
    }
    const response = await axiosInstance.get(url);
    if (response.status === 200) {
      return response.data.data;
    } else {
      throw new Error('Failed to get chat-flows');
    }
  } catch (error) {
    console.error(error);
    throw error;
  }
}


// 챗플로우 테스트 목록 조회
export async function getChatTestList(chatFlowId: string) {
  try {
    const response = await axiosInstance.get(`/chat-flows/${chatFlowId}/tests`);
    if (response.status === 200) {
      return response.data.data 
    } else {
      throw new Error('Failed to get knowledges');
    }
  } catch (error) {
    console.error(error);
    throw error;
  }
}

// 챗플로우 테스트 상세 조회
export async function getChatTestDetail(chatFlowId: string, chatFlowTestId: string) {
  try {
    const response = await axiosInstance.get(`/chat-flows/${chatFlowId}/tests/${chatFlowTestId}`);
    if (response.status === 200) {
      return response.data.data.chatFlowTestCases 
    } else {
      throw new Error('Failed to get knowledges');
    }
  } catch (error) {
    console.error(error);
    throw error;
  }
}


// 챗플로우 테스트 생성
export async function postChatFlowTest(chatFlowId: string, data: { testQuestion: string; groundTruth: string }[]) {
  try {
    const response = await axiosInstance.post(`/chat-flows/${chatFlowId}/tests`, data);
    if (response.status === 200) {
      return response.data.data 
    } else {
      throw new Error('Failed to get knowledges');
    }
  } catch (error) {
    console.error(error);
    throw error;
  }
}


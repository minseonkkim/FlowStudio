import axiosInstance from "./token/axiosInstance";

// 챗플로우 업로드
export async function postUploadChatFlow(chatFlowId: number){
  try {
    const response = await axiosInstance.post(`chat-flows/${chatFlowId}/upload`);
    if (response.status === 200) {
      console.log(response);
      return response.data.data;
    } else {
      throw new Error('Failed to upload chatflow');
    }
  } catch (error) {
    console.error(error)
    throw error
  }
}

// 챗플로우 다운로드
export async function postDownloadChatFlow(chatFlowId: number){
  try {
    console.log('다운로드')
    const response = await axiosInstance.post(`chat-flows/${chatFlowId}/download`);
    if (response.status === 200) {
      return response.data.data;
    } else {
      throw new Error('Failed to download chatflow');
    }
  } catch (error) {
    console.error(error)
    throw error
  }
}

// 공유된 챗플로우 전체 목록 조회
export async function getSharedChatFlows(){
  try {
    const response = await axiosInstance.get('chat-flows/shares');
    if (response.status === 200) {
      console.log(response.data);
      return response.data.data;
    } else {
      throw new Error('Failed to get shared chatflows');
    }
  } catch (error) {
    console.error(error)
    throw error
  }
}
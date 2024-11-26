import axiosInstance from "./token/axiosInstance";

// 모델 목록 조회
export async function getModels() {
  try {
    const response = await axiosInstance.get('chat-flows/nodes/models');
    if (response.status === 200) {
      console.log('모델 목록', response.data.data)
      return response.data.data;
    } else {
      throw new Error('Failed to get models');
    }
  } catch (error) {
    console.error(error);
    throw error;
  }
}
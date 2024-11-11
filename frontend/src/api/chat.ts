import axiosInstance from '@/api/token/axiosInstance';


// SSE 연결
export async function getSse() {
  try {
    const response = await axiosInstance.get('/sse/connect');
    if (response.status === 200) {
      return response.data.data;
    } else {
      throw new Error('Failed to get knowledges');
    }
  } catch (error) {
    console.error(error);
    throw error;
  }
}

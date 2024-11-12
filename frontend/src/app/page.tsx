"use client"; 

import { setAuthorizationToken } from '@/api/token/axiosInstance';
import { getAllChatFlows } from '@/api/chatbot'; 
import { postChatting } from '@/api/chat';
import { useMutation } from "@tanstack/react-query";

export default function Home() {
  const chatFlowId = '4';
  const data = { isPreview: true };

  const handleTokenRefresh = async () => {
    try {
      const newToken = await setAuthorizationToken();
      if (newToken) {
        alert('Token refreshed successfully. Check localStorage for the new token.');
        console.log('New Token:', newToken); 
      } else {
        alert('Token refresh failed.');
      }
    } catch (error) {
      console.error('Token refresh error:', error);
      alert('Token refresh failed. Check the console for details.');
    }
  };

  const handleGetChatFlows = async () => {
    try {
      const chatFlows = await getAllChatFlows();
      alert('Chat flows loaded successfully. Check the console for data.');
      console.log('Chat Flows:', chatFlows); 
    } catch (error) {
      console.error('Error fetching chat flows:', error);
      alert('Failed to load chat flows. Check the console for details.');
    }
  };

  const createMutation = useMutation({
    mutationFn: ({ chatFlowId, data }: { chatFlowId: string; data: { isPreview: boolean } }) =>
      postChatting(chatFlowId, data),
      
      onSuccess: (response) => {
        const accessToken = response.headers.get('access-token');
        
        if (accessToken) {
          localStorage.setItem('fake-access-token', accessToken);
          console.log('Access Token saved to localStorage as fake-access-token:', accessToken);
        } else {
          console.warn('Access token not found in response headers.');
        }
      },
      onError: () => {
        alert("채팅 생성에 실패했습니다. 다시 시도해 주세요.");
      },
  });

  const handelPostChat = () => {
    createMutation.mutate({ chatFlowId, data });
  };

  return (
    <div>
      <button onClick={handleTokenRefresh} className="border p-2 m-12 bg-red-300">
        Refresh Token
      </button>
      <button onClick={handleGetChatFlows} className="border p-2 m-12 bg-blue-300">
        Load Chat Flows
      </button>
      <button onClick={handelPostChat} className="border p-2 m-12 bg-blue-300">
        Create Chat
      </button>
    </div>
  );
}

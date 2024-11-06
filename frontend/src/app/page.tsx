"use client"; 

import { setAuthorizationToken } from '@/api/token/axiosInstance';
import { getAllChatFlows } from '@/api/chatbot'; // 파일 경로를 확인하고 수정하세요

export default function Home() {
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

  return (
    <div>
      <button onClick={handleTokenRefresh} className="border p-2 m-12 bg-red-300">
        Refresh Token
      </button>
      <button onClick={handleGetChatFlows} className="border p-2 m-12 bg-blue-300">
        Load Chat Flows
      </button>
    </div>
  );
}

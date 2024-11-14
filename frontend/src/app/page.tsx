"use client"; 

import { postChatting } from '@/api/chat';
import { useRouter } from 'next/navigation';
import ChatPage from './chat/[id]/page';
import { useState } from 'react';
import { useMutation } from "@tanstack/react-query";


export default function Home() {
  const chatFlowId = "1"; // 페이지에서 params.id로 받아서 넘겨주기
  const router = useRouter();
  const [isPreview, setIsPreview] = useState(false); 
  const [, setData] = useState<{ isPreview: boolean }>({ isPreview: false }); 
  const [chatId, setChatId] = useState<number>()

  const createMutation = useMutation({
    mutationFn: ({ chatFlowId, data }: { chatFlowId: string; data: { isPreview: boolean } }) =>
      postChatting(chatFlowId, data),
  
    onSuccess: (response) => {
      const newChatId = response.data.data.id; 
      setChatId(newChatId); 
      if (response.headers['authorization']) {
        const accessToken = response.headers['authorization'];
        localStorage.setItem("fake-access-token", accessToken);
        console.log("Fake access token 저장:", accessToken);
      }
    },
  
    onError: () => {
      alert("채팅 생성에 실패했습니다. 다시 시도해 주세요.");
    },
  });
  

  const handelPostPreviewChat = () => {
    setIsPreview(!isPreview);
    setData({ isPreview: true }); 
    createMutation.mutate({ chatFlowId, data: { isPreview: true } }); 
  };

  const handelPostDefaultChat = () => {
    // setIsPreview(false);
    // setData({ isPreview: false }); 
    // createMutation.mutate({ chatFlowId, data: { isPreview: false } });
    router.push(`/chat/${chatFlowId}`);
  };

  return (
    <div>
      <button onClick={handelPostPreviewChat} className="border p-2 m-12 bg-blue-300">
        Create PreviewChat
      </button>
      <button onClick={handelPostDefaultChat} className="border p-2 m-12 bg-blue-300">
        Create DefaultChat
      </button>

      {isPreview && chatId && (
        <ChatPage customStyle={true} params={{ id: chatFlowId }} chatId={chatId}/> 
      )}
    </div>
  );
}

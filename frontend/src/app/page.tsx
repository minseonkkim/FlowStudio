"use client"; 

import { postChatting } from '@/api/chat';
import { useRouter } from 'next/navigation';
import ChatPage from './chat/[id]/page';
import { useState } from 'react';
import { useMutation } from "@tanstack/react-query";


export default function Home() {
  const chatFlowId = "9"; // 페이지에서 params.id로 받아서 넘겨주기
  const router = useRouter();
  const [isPreview, setIsPreview] = useState(false); 

  const handelPostPreviewChat = () => {
    setIsPreview(!isPreview);
  };

  const handelPostDefaultChat = () => {
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

      {isPreview && (
        <ChatPage customStyle={'preview'} params={{ id: chatFlowId }} /> 
      )}
    </div>
  );
}

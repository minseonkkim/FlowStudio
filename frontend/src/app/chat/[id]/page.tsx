"use client";

import React, { useState, useRef, useEffect } from "react";
import { useMutation, useQueryClient } from '@tanstack/react-query';
import { AiOutlineSend } from "@react-icons/all-files/ai/AiOutlineSend";
import { EventSourcePolyfill } from "event-source-polyfill";
import { setAuthorizationToken } from "@/api/token/axiosInstance";
import { postChatting, postMessage } from "@/api/chat"

type ChatPageProps = {
  customStyle?: boolean;
  params: {
    id: number;
  };
};


const ChatPage: React.FC<ChatPageProps> = ({ customStyle = false, params}) => {
  const chatId = String(params.id);
  const chatFlowId = 28 // 임시로 지정
  const [messages, setMessages] = useState<string[]>([]);
  const [input, setInput] = useState("");
  const inputRef = useRef<HTMLTextAreaElement>(null);
  const chatEndRef = useRef<HTMLDivElement>(null);
  const eventSourceRef = useRef<EventSource | null>(null);
  const queryClient = useQueryClient();

  const BASE_URL = process.env.NEXT_PUBLIC_BASE_URL;

  const initializeSSE = async () => {
    try {
      let token = localStorage.getItem("accessToken") || (await setAuthorizationToken());
      const sse = new EventSourcePolyfill(`${BASE_URL}/sse/connect?duration=300`, {
        headers: { Authorization: `Bearer ${token}` },
        withCredentials: true,
      });
      sse.onopen = () => console.log("SSE 연결이 성공적으로 열렸습니다.");
      sse.addEventListener("connect", (e) => {
        if ((e as MessageEvent).data === "connected!") console.log("서버에서 연결 성공 메시지 수신");
      });
      sse.onmessage = (event) => setMessages((prev) => [...prev, JSON.parse((event as MessageEvent).data).content]);
      sse.onerror = () => console.error("SSE 연결 오류: 자동 재연결 시도 중...");
      eventSourceRef.current = sse;
    } catch (error) {
      console.error("Failed to initialize SSE:", error);
    }
  };

  useEffect(() => {
    initializeSSE();
    return () => eventSourceRef.current?.close();
  }, []);

  useEffect(() => chatEndRef.current?.scrollIntoView({ behavior: "smooth" }), [messages]);

  const createMutation = useMutation({
    mutationFn: ({ chatId, data }: { chatId: string, data: { message: string } }) =>
      postMessage(chatId, data),

    onError: () => {
      alert("댓글 등록에 실패했습니다. 다시 시도해 주세요.");
    },
  });

  const startNewChat = () => setMessages([]);

  const sendMessage = () => {
    if (input.trim()) {
      setMessages((prev) => [...prev, input]);
      const data = { message: input };
      createMutation.mutate({ chatId, data });
      setInput("");
      if (inputRef.current) inputRef.current.style.height = "auto";
    }
  };

  const handleInput = (e: React.ChangeEvent<HTMLTextAreaElement>) => {
    setInput(e.target.value);
    if (inputRef.current) {
      inputRef.current.style.height = "auto";
      inputRef.current.style.height = `${inputRef.current.scrollHeight}px`;
    }
  };

  const handleKeyDown = (e: React.KeyboardEvent<HTMLTextAreaElement>) => {
    if (e.key === "Enter") {
      e.preventDefault();
      e.shiftKey ? setInput(input + "\n") : sendMessage();
    }
  };

  return (
    <div className={`flex ${customStyle ? "ml-[20px] flex-col gap-4 w-[320px] h-[calc(100vh-170px)] rounded-[20px] p-[20px] bg-white bg-opacity-40 backdrop-blur-[15px] shadow-[0px_2px_8px_rgba(0,0,0,0.25)] overflow-hidden" : "h-screen"}`}>
      {!customStyle && (
        <div className="w-1/6 p-6 border-r bg-white">
          <div className="flex items-center mb-8">
            <div className="w-10 h-10 rounded-lg bg-gray-300 mr-4"></div>
            <div className="text-lg font-semibold break-words max-w-[150px]">대출금리 상담</div>
          </div>
          <button onClick={startNewChat} className="mb-4 w-full py-2 px-4 border border-gray-300 rounded-xl text-gray-700 hover:bg-gray-100">
            새 채팅
          </button>
        </div>
      )}
      <div className="flex flex-col flex-grow bg-gray-50">
        <div className="border-b p-4 text-[18px] bg-white">{customStyle ? "미리보기" : "새 대화"}</div>
        <div className={`flex-grow h-0 p-6 space-y-4 overflow-y-auto ${customStyle ? "scrollbar-hide" : ""}`}>
          {messages.map((msg, index) => (
            <div key={index} className="flex items-start justify-end space-x-4">
              <div className={`bg-white border border-gray-300 rounded-lg px-4 py-2 whitespace-pre-wrap text-gray-800 shadow-sm ${customStyle ? "text-[10px]" : "text-[15px]"}`} style={{ maxWidth: "80%", width: "fit-content" }}>
                {msg}
              </div>
              <div className={`rounded-full ${customStyle ? "w-5 h-5" : "w-10 h-10"} bg-gray-300`}></div>
            </div>
          ))}
          <div ref={chatEndRef} />
        </div>
        <div className="border-t p-6 flex items-center bg-white">
          <textarea
            ref={inputRef}
            placeholder="메시지를 입력하세요..."
            value={input}
            onChange={handleInput}
            onKeyDown={handleKeyDown}
            className={`flex-1 border border-gray-300 rounded-lg py-2 px-6 mr-3 resize-none overflow-y-hidden shadow-sm focus:outline-none focus:ring-1 focus:ring-[#9A75BF] ${customStyle ? "text-[12px]" : ""}`}
            rows={1}
          />
          <button onClick={sendMessage} className="text-white bg-[#9A75BF] hover:bg-[#7C5DAF] rounded-lg p-2">
            <AiOutlineSend size={20} />
          </button>
        </div>
      </div>
    </div>
  );
};

export default ChatPage;

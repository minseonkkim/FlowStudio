"use client";

import React, { useState, useRef, useEffect } from "react";
import { AiOutlineSend } from "@react-icons/all-files/ai/AiOutlineSend";
import { EventSourcePolyfill } from "event-source-polyfill";
import { setAuthorizationToken } from '@/api/token/axiosInstance';

const ChatPage = () => {
  const [messages, setMessages] = useState<string[]>([]);
  const [input, setInput] = useState("");
  const inputRef = useRef<HTMLTextAreaElement>(null);
  const chatEndRef = useRef<HTMLDivElement>(null);
  const BASE_URL = process.env.NEXT_PUBLIC_BASE_URL;
  // const [connectionId, setConnectionId] = useState<string | null>(null);
  const eventSourceRef = useRef<EventSource | null>(null);

  // SSE 연결 설정
  const initializeSSE = async () => {
    try {
      let token = localStorage.getItem("accessToken");
      if (!token) {
        token = await setAuthorizationToken();
      }

      const sse = new EventSourcePolyfill(`${BASE_URL}/sse/connect?duration=10`, {
        headers: { Authorization: `Bearer ${token}` },
        withCredentials: true,
      });

      sse.onopen = () => {
        console.log("SSE 연결이 성공적으로 열렸습니다.");
      };

      sse.addEventListener('connect', (e) => {
        const receivedConnectData = (e as MessageEvent).data;
        if (receivedConnectData === "connected!") {
          console.log('서버에서 연결 성공 메시지 수신:', receivedConnectData);
          
        }
      });

      sse.onmessage = (event) => {
        const serverMessage = JSON.parse((event as MessageEvent).data);
        setMessages((prevMessages) => [...prevMessages, serverMessage.content]);
      };

      // 연결 오류가 발생했을 때 에러 로그만 남김 (자동 재연결에 의존)
      sse.onerror = () => {
        console.error("SSE 연결 오류: 자동 재연결 시도 중...");
      };

      eventSourceRef.current = sse;
    } catch (error) {
      console.error("Failed to initialize SSE:", error);
    }
  };

  useEffect(() => {
    initializeSSE();

    return () => {
      eventSourceRef.current?.close();
    };
  }, []);

  const startNewChat = () => {
    setMessages([]);
  };

  const sendMessage = () => {
    if (input.trim()) {
      setMessages([...messages, input]);
      setInput("");
      if (inputRef.current) {
        inputRef.current.style.height = "auto";
      }
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
      if (e.shiftKey) {
        setInput(input + "\n");
      } else {
        e.preventDefault();
        sendMessage();
      }
    }
  };

  return (
    <div className="flex h-screen">
      <div className="w-1/6 p-6 border-r bg-white">
        <div className="flex items-center mb-8">
          <div className="w-10 h-10 rounded-lg bg-gray-300 mr-4"></div>
          <div className="text-lg font-semibold break-words max-w-[150px]">
            대출금리 상담
          </div>
        </div>
        <button
          onClick={startNewChat}
          className="mb-4 w-full py-2 px-4 border border-gray-300 rounded-xl text-gray-700 hover:bg-gray-100"
        >
          새 채팅
        </button>
      </div>

      <div className="flex flex-col flex-grow bg-gray-50">
        <div className="border-b p-4 text-[18px] bg-white">
          새 대화
        </div>

        <div className="flex-grow p-6 space-y-4 overflow-y-auto">
          {messages.map((msg, index) => (
            <div key={index} className="flex items-start justify-end space-x-4">
              <div className="bg-white border border-gray-300 rounded-lg px-4 py-2 whitespace-pre-wrap text-gray-800 shadow-sm"
                style={{ maxWidth: "80%", width: "fit-content" }}>
                {msg}
              </div>
              <div className="w-10 h-10 rounded-full bg-gray-300"></div>
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
            className="flex-1 border border-gray-300 rounded-lg py-2 px-6 mr-3 resize-none overflow-y-hidden shadow-sm focus:outline-none focus:ring-1 focus:ring-[#9A75BF]"
            rows={1}
          />
          <button
            onClick={sendMessage}
            className="text-white bg-[#9A75BF] hover:bg-[#7C5DAF] rounded-lg p-2"
          >
            <AiOutlineSend size={20} />
          </button>
        </div>
      </div>
    </div>
  );
};

export default ChatPage;

"use client";

import React, { useState, useRef, useEffect } from "react";
import { AiOutlineSend } from "@react-icons/all-files/ai/AiOutlineSend";
import { useQuery, useMutation, UseQueryOptions } from "@tanstack/react-query";
import { EventSourcePolyfill } from "event-source-polyfill";
import { postMessage, postChatting, getChatting } from "@/api/chat";
import { getChatDetailList } from "@/types/chat"
import SideBar from "@/components/chat/SideBar";
import ReactMarkdown from "react-markdown";
import remarkGfm from "remark-gfm";

type Message = {
  text: string;
  sender: "user" | "server";
};

type DefaultChatProps = {
  chatFlowId: string;
};

export default function DefaultChat({ chatFlowId }: DefaultChatProps) {
  const [messages, setMessages] = useState<Message[]>([]);
  const [input, setInput] = useState("");
  const [defaultChatId, setDefaultChatId] = useState<number>();
  const [isSSEConnected, setIsSSEConnected] = useState(false);
  const [pendingMessage, setPendingMessage] = useState<string | null>(null);
  const inputRef = useRef<HTMLTextAreaElement>(null);
  const chatEndRef = useRef<HTMLDivElement>(null);
  const BASE_URL = process.env.NEXT_PUBLIC_BASE_URL;
  const [title, setTitle] = useState<string>("새 대화");
  const [selectedChatId, setSelectedChatId] = useState<number>();

  useEffect(() => {
    chatEndRef.current?.scrollIntoView({ behavior: "smooth" });
  }, [messages]);

  const initializeSSE = (token: string) => {
    const sse = new EventSourcePolyfill(`${BASE_URL}/sse/connect`, {
      headers: { Authorization: `Bearer ${token}` },
      withCredentials: true,
    });

    sse.onopen = () => {
      console.log("SSE 연결이 성공적으로 열렸습니다.");
      setIsSSEConnected(true);
    };
    
    sse.addEventListener("heartbeat", (event) => {
      const data = (event as MessageEvent).data;
      console.log(data)
    
    });

    sse.addEventListener("title", (event) => {
      const data = JSON.parse((event as MessageEvent).data);
      console.log(data);
    });


    sse.addEventListener("node", (event) => {
      const data = JSON.parse((event as MessageEvent).data);
      console.log(data);
      if (data.type === "ANSWER") {
        setMessages((prev) => [...prev.slice(0, -1), { text: data.message, sender: "server" }]);
      }
    });

    sse.onerror = () => {
      console.error("SSE 연결 오류: 자동 재연결 시도 중...");
      setIsSSEConnected(false);
    };
  };

  const createChatMutation = useMutation({
    mutationFn: (data: { isPreview: boolean }) => postChatting(chatFlowId, data),
    onSuccess: (response) => {
      const newChatId = response.data.data.id;
      setDefaultChatId(newChatId);

      const accessToken = response.headers["authorization"] || localStorage.getItem("accessToken");
      console.log(accessToken);
      if (accessToken) {
        initializeSSE(accessToken);
        if (pendingMessage) {
          setMessages((prev) => [...prev, { text: pendingMessage, sender: "user" }, { text: "로딩 중...", sender: "server" }]);
          sendMessageMutation.mutate({ chatId: String(newChatId), message: pendingMessage });
          setPendingMessage(null);
        }
      } else {
        console.error("SSE 연결을 위한 토큰이 없습니다.");
      }
    },
    onError: () => {
      alert("채팅 생성에 실패했습니다. 다시 시도해 주세요.");
      setPendingMessage(null);
    },
  });

  const handleSendMessage = () => {
    if (input.trim()) {
      const message = input.trim();
      setInput(""); 
      if (!defaultChatId || !isSSEConnected) {
        setPendingMessage(message);
        createChatMutation.mutate({ isPreview: false });
      } else {
        setMessages((prev) => [...prev, { text: message, sender: "user" }, { text: "로딩 중...", sender: "server" }]);
        sendMessageMutation.mutate({ chatId: String(defaultChatId), message });
      }

      if (inputRef.current) inputRef.current.style.height = "auto";
    }
  };

  const sendMessageMutation = useMutation({
    mutationFn: (data: { chatId: string; message: string }) => postMessage(data.chatId, { message: data.message }),
    onError: () => {
      alert("메시지 전송에 실패했습니다. 다시 시도해 주세요.");
    },
  });

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
      e.shiftKey ? setInput(input + "\n") : handleSendMessage();
    }
  };


// 채팅 상세보기 
  const { isError, error, data: chatDetail } = useQuery<getChatDetailList>({
      queryKey: ["chatDetail", chatFlowId, selectedChatId],
      queryFn: () => getChatting(chatFlowId, String(selectedChatId!)),
      enabled: !!selectedChatId,
    });

  useEffect(() => {
    if (isError && error) {
      alert("채팅내역을 불러오는 중 오류가 발생했습니다. 다시 시도해 주세요.");
    }
  }, [isError, error]);
  
  useEffect(() => {
    if (chatDetail) {
      try {
        const parsedMessageList = JSON.parse(chatDetail.messageList);
        console.log(parsedMessageList)
        
        setMessages(
          parsedMessageList
            .map((msg: { question: string; answer: string; }) => [
              { text: msg.question, sender: "user" as const },
              { text: msg.answer, sender: "server" as const },
            ])
            .flat()
        );
  
        setTitle(chatDetail.title);
      } catch (error) {
        console.error("messageList 파싱 오류:", error);
      }
    }
  }, [chatDetail]);
  
  
  
  // Sidebar에서 선택된 채팅 ID를 설정하는 함수
  const handleSelectChat = (chatId: number) => {
    setSelectedChatId(chatId);
    setDefaultChatId(chatId);
  };

  
  const onNewChat = () => {
    setMessages([]); 
    createChatMutation.mutate({ isPreview: false }); // 새 채팅 생성 로직을 호출합니다
  };

  return (
    <div className="flex h-screen">
      <div className="w-64 flex-shrink-0">
        <SideBar onNewChat={onNewChat} chatFlowId={chatFlowId} onSelectChat={handleSelectChat} />
      </div>

      {/* 채팅 영역 */}
      <div className="flex flex-col flex-grow bg-gray-50">
        <div className="border-b p-4 bg-white text-[18px]">{title}</div>
        <div className="flex-grow h-0 px-14 pt-6 space-y-8 overflow-y-auto">
          {messages.map((msg, index) => (
            <div key={index} className={`flex items-start ${msg.sender === "user" ? "justify-end" : "justify-start"} space-x-6`}>
              {msg.sender === "server" && <div className="rounded-full w-10 h-10 bg-gray-500"></div>}
              <div
                className={`rounded-lg px-4 py-2 whitespace-pre-wrap text-gray-800 shadow-sm ${
                  msg.sender === "server" ? "bg-[#f9f9f9] border border-gray-300 max-w-[80%]" : "bg-[#f3f3f3] max-w-[60%]"
                }`}
              >
                {msg.sender === "server" ? (
                  <ReactMarkdown remarkPlugins={[remarkGfm]}>{msg.text}</ReactMarkdown>
                ) : (
                  msg.text
                )}
              </div>
              {msg.sender === "user" && <div className="rounded-full w-10 h-10 bg-gray-300"></div>}
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
            className="flex-1 border border-gray-300 rounded-lg py-2 px-6 mr-3 resize-none overflow-y-hidden shadow-sm focus:outline-none focus:ring-1 focus:ring-[#9A75BF] text-base"
            rows={1}
          />
          <button onClick={handleSendMessage} className="text-white bg-[#9A75BF] hover:bg-[#7C5DAF] rounded-lg p-2">
            <AiOutlineSend size={15} />
          </button>
        </div>
      </div>
    </div>
  );
}

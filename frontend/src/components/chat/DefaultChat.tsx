"use client";

import React, { useState, useRef, useEffect } from "react";
import { AiOutlineSend } from "@react-icons/all-files/ai/AiOutlineSend";
import { FiMenu } from "@react-icons/all-files/fi/FiMenu";
import { FiX } from "@react-icons/all-files/fi/FiX"; 
import { useQuery, useMutation, useQueryClient } from "@tanstack/react-query";
import { EventSourcePolyfill } from "event-source-polyfill";
import { postMessage, postChatting, getChatting, getChattingList } from "@/api/chat";
import { getChatDetailList, getChatListData, Message } from "@/types/chat";
import SideBar from "@/components/chat/SideBar";
import ReactMarkdown from "react-markdown";
import remarkGfm from "remark-gfm";
import { useRecoilValue } from "recoil";
import { profileImageAtom } from "@/store/profileAtoms";
import Image from 'next/image';
import one from '../../../public/chatbot-icon/1.jpg';
import two from '../../../public/chatbot-icon/2.jpg';
import three from '../../../public/chatbot-icon/3.jpg';
import four from '../../../public/chatbot-icon/4.jpg';
import five from '../../../public/chatbot-icon/5.jpg';
import six from '../../../public/chatbot-icon/6.jpg';
import { StaticImageData } from 'next/image';

type DefaultChatProps = {
  chatFlowId: string;
};

export default function DefaultChat({ chatFlowId }: DefaultChatProps) {
  const [messages, setMessages] = useState<Message[]>([]);
  const [input, setInput] = useState("");
  const [defaultChatId, setDefaultChatId] = useState<number | null>(null);
  const [isSSEConnected, setIsSSEConnected] = useState(false);
  const [pendingMessage, setPendingMessage] = useState<string | null>(null);
  const [title, setTitle] = useState<string>("새 대화");
  const [isSidebarOpen, setIsSidebarOpen] = useState(false);
  const [, setChatlist] = useState<getChatListData | undefined>();
  const profileImage = useRecoilValue(profileImageAtom);
  const [isLogin, setIsLogin] = useState<boolean>(false)

  const thumbnailImages: Record<number, StaticImageData> = {
    1: one,
    2: two,
    3: three,
    4: four,
    5: five,
    6: six,
  };
  const chatlistThumbnail = 1; 

  const inputRef = useRef<HTMLTextAreaElement>(null);
  const chatEndRef = useRef<HTMLDivElement>(null);
  const queryClient = useQueryClient();
  const BASE_URL = process.env.NEXT_PUBLIC_BASE_URL;
 

  useEffect(() => {
    const token = localStorage.getItem("accessToken");
    setIsLogin(!!token); 
  }, []);
  
  useEffect(() => {
    chatEndRef.current?.scrollIntoView({ behavior: "smooth" });
  }, [messages]);

  const toggleSidebar = () => {
    setIsSidebarOpen(!isSidebarOpen);
  };

  const closeSidebar = () => {
    setIsSidebarOpen(false);
  };

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
      console.log("Received heartbeat:", (event as MessageEvent).data);
    });

    sse.addEventListener("title", async (event) => {
      const data = JSON.parse((event as MessageEvent).data);
      setTitle(data.title);
    
      const accessToken = localStorage.getItem("accessToken");
      if (accessToken) {
       
        const updatedChatlist = await getChattingList({
          chatFlowId,
          page: "0",
          limit: "1",
        });
    
        queryClient.setQueryData(["chatlist", chatFlowId], (oldData: any) => {
          if (!oldData) {
            return {
              pageParams: [0],
              pages: [updatedChatlist],
            };
          }
    
          return {
            ...oldData,
            pages: [
              {
                ...oldData.pages[0],
                chats: [
                  ...updatedChatlist.chats, 
                  ...oldData.pages[0].chats, 
                ],
                totalCount: updatedChatlist.totalCount, 
              },
              ...oldData.pages.slice(1), 
            ],
          };
        });
      }
    });
    

    sse.addEventListener("node", (event) => {
      const data = JSON.parse((event as MessageEvent).data);
      console.log(data)
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
    
      if (accessToken) {
        initializeSSE(accessToken);
        if (pendingMessage) {
          setMessages((prev) => [
            ...prev,
            { text: pendingMessage, sender: "user" },
            { text: "로딩 중...", sender: "server" },
          ]);
          sendMessageMutation.mutate({ chatId: String(newChatId), message: pendingMessage });
          setPendingMessage(null);
        }
        setTitle("새 대화");
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
        setMessages((prev) => [
          ...prev,
          { text: message, sender: "user" },
          { text: "로딩 중...", sender: "server" },
        ]);
        sendMessageMutation.mutate({ chatId: String(defaultChatId), message });
      }

      if (inputRef.current) inputRef.current.style.height = "auto";
    }
  };


  const sendMessageMutation = useMutation({
    mutationFn: (data: { chatId: string; message: string }) =>
      postMessage(data.chatId, { message: data.message }),
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

  const { isError, error, data: chatDetail } = useQuery<getChatDetailList>({
    queryKey: ["chatDetail", chatFlowId, defaultChatId],
    queryFn: () => getChatting(chatFlowId, String(defaultChatId!)),
    enabled: !!defaultChatId && typeof window !== "undefined" && !!localStorage.getItem("accessToken"),
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
        if (parsedMessageList && parsedMessageList.length > 0) {
          setMessages(
            parsedMessageList
              .map((msg: { question: string; answer: string }) => [
                { text: msg.question, sender: "user" as const },
                { text: msg.answer, sender: "server" as const },
              ])
              .flat()
          );
        }
  
        if (chatDetail.title != null) {
          setTitle(chatDetail.title);
        } else {
          setTitle("새 대화");
        }
      } catch (error) {
        console.error("messageList 파싱 오류:", error);
      }
    }
  }, [chatDetail]);
    
  const handleSelectChat = (chatId: number) => {
    setDefaultChatId(chatId);
  };


  const onNewChat = () => {
    setMessages([]);
    createChatMutation.mutate({ isPreview: false });
  };

  const onDeleteNewChat = () => {
    setMessages([]);
    setDefaultChatId(null);
    handleSendMessage();
    setTitle("새 대화");
  };

  return (
    <div className="flex h-screen flex-col md:flex-row">
      <div className="md:hidden p-4 bg-white border-b flex justify-between items-center">
        {isLogin ? <button onClick={()=>toggleSidebar()} className="text-gray-600">
          <FiMenu size={24} />
        </button> : <div></div>}
        
        <div className="text-lg font-semibold">{title}</div>
        {isLogin ?
        <button onClick={()=>onNewChat()} className="text-[#9A75BF] font-medium">
          새 채팅
        </button>: <div></div>}
      </div>

      {isSidebarOpen && (
        <div
          className="fixed inset-0 bg-black bg-opacity-50 z-40"
          onClick={() => closeSidebar()}
        />
      )}

      {isLogin && (
      <div
        className={`fixed top-0 left-0 h-full bg-white shadow-lg transform ${
          isSidebarOpen ? "translate-x-0" : "-translate-x-full"
        } transition-transform md:relative md:translate-x-0 md:shadow-none z-50 w-64`}
      >
        <div className="p-4 flex justify-between items-center md:hidden">
          <button onClick={() => closeSidebar()} className="text-gray-600">
            <FiX size={24} />
          </button>
        </div>
        
          <SideBar
            onNewChat={onNewChat}
            chatFlowId={chatFlowId}
            onSelectChat={handleSelectChat}
            onDeleteNewChat={onDeleteNewChat}
            selectedChatId={defaultChatId}
          />
        

      </div>)}

      <div className={`flex flex-col flex-grow ${!isLogin && "w-full"} bg-gray-50`}>
        <div className="hidden md:block border-b p-4 bg-white text-[18px]">{title}</div>
        <div className="flex-grow h-0 px-4 md:px-14 pt-6 space-y-4 md:space-y-8 overflow-y-auto">
          {messages.map((msg, index) => (
            <div
              key={index}
              className={`flex items-start ${
                msg.sender === "user" ? "justify-end" : "justify-start"
              } space-x-3 md:space-x-6`}
            >
              {msg.sender === "server" && chatlistThumbnail && thumbnailImages[chatlistThumbnail] &&
                <Image 
                  src={thumbnailImages[chatlistThumbnail]} 
                  alt={`Thumbnail ${chatlistThumbnail}`} 
                  className="rounded-full w-8 h-8 md:w-10 md:h-10" 
                />}
              <div
                className={`rounded-lg px-3 py-2 md:px-4 md:py-2 whitespace-pre-wrap text-gray-800 shadow-sm ${
                  msg.sender === "server"
                    ? "bg-[#f9f9f9] border border-gray-300 max-w-[80%]"
                    : "bg-[#f3f3f3] max-w-[60%]"
                }`}
              >
                {msg.sender === "server" ? (
                  <ReactMarkdown remarkPlugins={[remarkGfm]}>{msg.text}</ReactMarkdown>
                ) : (
                  msg.text
                )}
              </div>
              {msg.sender === "user" && (
                profileImage ? (
                  <img src={profileImage} className="w-8 h-8 md:w-10 md:h-10 rounded-full" />
                ) : (
                  <div className="rounded-full w-8 h-8 md:w-10 md:h-10 bg-gray-300"></div>
                )
              )}

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
            className="flex-1 border border-gray-300 rounded-lg py-1 md:py-2 px-4 md:px-6 mr-2 md:mr-3 resize-none overflow-y-hidden shadow-sm focus:outline-none focus:ring-1 focus:ring-[#9A75BF] text-sm md:text-base"
            rows={1}
          />
          <button
            onClick={()=>handleSendMessage()}
            className="text-white bg-[#9A75BF] hover:bg-[#7C5DAF] rounded-lg p-2 md:p-2.5"
          >
            <AiOutlineSend size={15} />
          </button>
        </div>
      </div>
    </div>
  );
}
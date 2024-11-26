"use client";

import React, { useState, useRef, useEffect, Dispatch, SetStateAction } from "react";
import { AiOutlineSend } from "@react-icons/all-files/ai/AiOutlineSend";
import { useMutation } from "@tanstack/react-query";
import { EventSourcePolyfill } from "event-source-polyfill";
import { postMessage, postChatting } from "@/api/chat";
import { Message } from "@/types/chat";
import ReactMarkdown from "react-markdown";
import remarkGfm from "remark-gfm";
import { CgClose } from "@react-icons/all-files/cg/CgClose";
import { useRecoilValue } from "recoil";
import { profileImageAtom } from "@/store/profileAtoms";
import { chatbotThumbnailState } from "@/store/chatbotAtoms"; import one from '../../../public/chatbot-icon/1.jpg';
import two from '../../../public/chatbot-icon/2.jpg';
import three from '../../../public/chatbot-icon/3.jpg';
import four from '../../../public/chatbot-icon/4.jpg';
import five from '../../../public/chatbot-icon/5.jpg';
import six from '../../../public/chatbot-icon/6.jpg';
import { Bounce, toast } from "react-toastify";
import { Node, useReactFlow } from "reactflow";
import { NodeData } from "@/types/chatbot";

type PreviewChatProps = {
  chatFlowId: string;
  onClose: () => void;
  nodes?: Node<NodeData, string>[];
  setNodes?: Dispatch<SetStateAction<Node<NodeData, string>[]>>;
};

const thumbnailImages: { [key: number]: string } = {
  1: one.src,
  2: two.src,
  3: three.src,
  4: four.src,
  5: five.src,
  6: six.src,
};

export default function PreviewChat({ chatFlowId, onClose, nodes, setNodes }: PreviewChatProps) {
  const [messages, setMessages] = useState<Message[]>([]);
  const [input, setInput] = useState("");
  const [previewChatId, setPreviewChatId] = useState<number>();
  const inputRef = useRef<HTMLTextAreaElement>(null);
  const chatEndRef = useRef<HTMLDivElement>(null);
  const sseRef = useRef<EventSourcePolyfill | null>(null); // SSE 인스턴스 추적
  const BASE_URL = process.env.NEXT_PUBLIC_BASE_URL;
  const profileImage = useRecoilValue(profileImageAtom);
  const thumbnail = useRecoilValue(chatbotThumbnailState);
  const [currentEventNodeId, setCurrentEventNodeId] = useState<string>();
  const resetTimeoutRef = useRef<NodeJS.Timeout | null>(null);
  const { getViewport, setCenter } = useReactFlow();


  // SSE 연결 초기화 함수
  const initializeSSE = (token: string) => {
    if (sseRef.current) {
      console.log("이미 연결된 SSE");
      sseRef.current.close(); // 기존 연결 닫기
    }

    const sse = new EventSourcePolyfill(`${BASE_URL}/sse/connect`, {
      headers: { Authorization: `Bearer ${token}` },
      withCredentials: true,
    });

    sse.onopen = () => console.log("SSE 연결이 성공적으로 열렸습니다.");

    sse.addEventListener("heartbeat", (event) => {
      console.log("Received heartbeat:", (event as MessageEvent).data);
    });

    sse.addEventListener("node", (event) => {
      const data = JSON.parse((event as MessageEvent).data);
      console.log("Node Event Received:", data);
      setCurrentEventNodeId(data.nodeId);
      console.log(data.nodeId);

      setNodes((prevNodes) => {
        return prevNodes.map((n) =>
          n.id == data.nodeId ? { ...n, data: { ...n.data, isComplete: true }, } : n
        )}
      );
      
       // React Flow instance가 초기화된 상태인지 확인 후 실행
       const currentEventNode = nodes.find((n) => n.id == data.nodeId);
       if (currentEventNode) {
        const viewport = getViewport();
        setCenter(currentEventNode.position.x, currentEventNode.position.y, {zoom: viewport.zoom, duration: 500});
      }

      if (data.type === "ANSWER") {
        setMessages((prev) => [...prev.slice(0, -1), { text: data.message, sender: "server" }]);
      }

      if (resetTimeoutRef.current) {
        clearTimeout(resetTimeoutRef.current); // 이전 타이머 초기화
      }
    
      resetTimeoutRef.current = setTimeout(() => {
        setNodes((prevNodes) =>
          prevNodes.map((n) => ({
            ...n,
            data: { ...n.data, isComplete: false, isError: false },
          }))
        );
      }, 5000);
    });

    sse.onerror = (error) => {
      console.error("SSE 연결 오류:", error);
      sse.close(); // 오류 시 기존 연결 닫기
      sseRef.current = null; // 레퍼런스 초기화
    };

    sseRef.current = sse; // SSE 레퍼런스 업데이트
  };

  // 채팅 ID 생성 및 토큰 저장 후 SSE 연결
  const createChatMutation = useMutation({
    mutationFn: (data: { isPreview: boolean }) => postChatting(chatFlowId, data),
    onSuccess: (response) => {
      const newChatId = response.data.data.id;
      setPreviewChatId(newChatId);

      const accessToken = response.headers["authorization"] || localStorage.getItem("accessToken");
      console.log("Access Token:", accessToken);
      if (accessToken) {
        initializeSSE(accessToken);
      } else {
        console.error("SSE 연결을 위한 토큰이 없습니다.");
      }
    },
    onError: () => {
      toast.error(`채팅 생성에 실패했습니다. 다시 시도해 주세요.`, {
        position: "top-right",
        autoClose: 5000,
        hideProgressBar: false,
        closeOnClick: true,
        pauseOnHover: false,
        draggable: true,
        progress: undefined,
        theme: "light",
        transition: Bounce,
      });
    },
  });

  // 초기 설정: 채팅 ID 생성 후 토큰 저장 및 SSE 연결
  useEffect(() => {
    if (!sseRef.current && !previewChatId) {
      createChatMutation.mutate({ isPreview: true });
    }
  }, [previewChatId]);

  useEffect(() => chatEndRef.current?.scrollIntoView({ behavior: "smooth" }), [messages]);

  const sendMessageMutation = useMutation({
    mutationFn: (data: { chatId: string; message: string }) => {
      setNodes((prevNodes) =>
        prevNodes.map((n) => ({
          ...n,
          data: { ...n.data, isComplete: false, isError: false },
        }))
      );
      return postMessage(data.chatId, { message: data.message });
    },
    onError: (error) => {
      const serverErrorMessage = error?.message || error?.message || "메시지를 보낼 수 없습니다. 다시 확인해주세요.";

      const prevEventNode = nodes.find((n) => n.id == currentEventNodeId);
      const errorEventNodeId = prevEventNode.data?.outputEdges[0]?.targetNodeId.toString(); // 연결 노드가 1개라는 가정
      
      setNodes((prevNodes) => {
        return prevNodes.map((n) =>
          n.id == errorEventNodeId ? { ...n, data: { ...n.data, isError: true }, } : n
        )}
      );

      const currentEventNode = nodes.find((n) => n.id == errorEventNodeId);
       if (currentEventNode) {
        const viewport = getViewport();
        setCenter(currentEventNode.position.x, currentEventNode.position.y, {zoom: viewport.zoom, duration: 500});
      }

      if (resetTimeoutRef.current) {
        clearTimeout(resetTimeoutRef.current); // 이전 타이머 초기화
      }
    
      resetTimeoutRef.current = setTimeout(() => {
        setNodes((prevNodes) =>
          prevNodes.map((n) => ({
            ...n,
            data: { ...n.data, isComplete: false, isError: false },
          }))
        );
      }, 5000);

      toast.error(
        <>
          {serverErrorMessage}
        </>,
        {
          position: "top-right",
          autoClose: 5000,
          hideProgressBar: false,
          closeOnClick: true,
          pauseOnHover: false,
          draggable: true,
          progress: undefined,
          theme: "light",
          transition: Bounce,
        }
      );
      
    },
  });

  const handleSendMessage = () => {
    if (input.trim() && previewChatId) {
      const message = input.trim();
      setMessages((prev) => [...prev, { text: message, sender: "user" }]);
      sendMessageMutation.mutate({ chatId: String(previewChatId), message });
      setMessages((prev) => [...prev, { text: "로딩 중...", sender: "server" }]);
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
      e.shiftKey ? setInput(input + "\n") : handleSendMessage();
    }
  };

  return (
    <div className="ml-[20px] flex flex-col gap-4 w-[320px] h-[calc(100vh-170px)] rounded-[20px] p-[10px] bg-white bg-opacity-40 backdrop-blur-[15px] shadow-[0px_2px_8px_rgba(0,0,0,0.25)] overflow-y-auto">
      <div className="flex flex-row justify-between items-center mb-2 p-4">
        <div className="border-b text-[18px] font-semibold">미리보기</div>
        <CgClose className="size-6 cursor-pointer" onClick={onClose} />

      </div>
      <div className="flex-grow h-0 p-5 space-y-4 overflow-y-auto scrollbar-hide">
        {messages.map((msg, index) => (
          <div key={index} className={`flex items-start text-[12px] ${msg.sender === "user" ? "justify-end" : "justify-start"} space-x-4`}>
            {msg.sender === "server" && (thumbnail ? <img src={thumbnailImages[thumbnail]} className="w-6 h-6 rounded-full" /> : <div className="rounded-full w-6 h-6 bg-gray-500"></div>)}
            <div
              className={`rounded-lg px-4 py-2 whitespace-pre-wrap text-gray-800 shadow-sm ${msg.sender === "server" ? "bg-[#f9f9f9] border border-gray-300 max-w-[80%]" : "bg-[#f3f3f3] max-w-[60%]"
                }`}
            >
              {msg.sender === "server" ? (
                <ReactMarkdown remarkPlugins={[remarkGfm]}>{msg.text}</ReactMarkdown>
              ) : (
                msg.text
              )}
            </div>
            {msg.sender === "user" &&
              (profileImage ?
                <img src={profileImage} className="w-6 h-6 rounded-full" /> :
                <div className="rounded-full w-6 h-6 bg-gray-300"></div>)
            }
          </div>
        ))}
        <div ref={chatEndRef} />
      </div>
      <div className="border-t p-5 flex items-center">
        <textarea
          ref={inputRef}
          placeholder="메시지를 입력하세요..."
          value={input}
          onChange={handleInput}
          onKeyDown={handleKeyDown}
          className="flex-1 border border-gray-300 rounded-lg py-2 px-6 mr-3 resize-none overflow-y-hidden shadow-sm focus:outline-none focus:ring-1 focus:ring-[#9A75BF] text-sm"
          rows={1}
        />
        <button onClick={handleSendMessage} className="text-white bg-[#9A75BF] hover:bg-[#7C5DAF] rounded-lg p-2">
          <AiOutlineSend size={15} />
        </button>
      </div>
    </div>
  );
}

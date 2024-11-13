

"use client";

import React, { useState, useRef, useEffect } from "react";
import { useMutation } from "@tanstack/react-query";
import { AiOutlineSend } from "@react-icons/all-files/ai/AiOutlineSend";
import { EventSourcePolyfill } from "event-source-polyfill";
import { postMessage } from "@/api/chat";
import SideBar from "@/components/chat/SideBar";
import ReactMarkdown from "react-markdown";
import remarkGfm from "remark-gfm";

type ChatPageProps = {
  customStyle?: boolean;
  params: {
    id: number;
  };
};

const ChatPage: React.FC<ChatPageProps> = ({ customStyle = false, params }) => {
  const chatId = String(params.id);
  const [messages, setMessages] = useState<{ text: string; sender: "user" | "server" }[]>([]);
  const [input, setInput] = useState("");
  const inputRef = useRef<HTMLTextAreaElement>(null);
  const chatEndRef = useRef<HTMLDivElement>(null);
  const BASE_URL = process.env.NEXT_PUBLIC_BASE_URL;
  

  const initializeSSE = async () => {
    try {
      const accessToken = localStorage.getItem("accessToken") || localStorage.getItem("fake-access-token");
      if (!accessToken) return;
      console.log(chatId)

      const sse = new EventSourcePolyfill(`${BASE_URL}/sse/connect`, {
        headers: { Authorization: `Bearer ${accessToken}` },
        withCredentials: true,
      });

      sse.onopen = () => console.log("SSE 연결이 성공적으로 열렸습니다.");


      sse.addEventListener("node", (event) => {
        const data = JSON.parse((event as MessageEvent).data);
        if (data.type === "ANSWER") {
          setMessages((prev) => [...prev.slice(0, -1), { text: data.message, sender: "server" }]);
        }
      });

      sse.onerror = () => console.error("SSE 연결 오류: 자동 재연결 시도 중...");
    } catch (error) {
      console.error("Failed to initialize SSE:", error);
    }
  };

  useEffect(() => {
    initializeSSE();
  }, []);

  useEffect(() => chatEndRef.current?.scrollIntoView({ behavior: "smooth" }), [messages]);

  const createMutation = useMutation({
    mutationFn: ({ chatId, data }: { chatId: string; data: { message: string } }) => postMessage(chatId, data),
    onError: () => {
      alert("메시지 전송에 실패했습니다. 다시 시도해 주세요.");
    },
  });

  const sendMessage = () => {
    if (input.trim()) {
      setMessages((prev) => [...prev, { text: input, sender: "user" }]);
      createMutation.mutate({ chatId, data: { message: input } });
      setInput("");
      setMessages((prev) => [...prev, { text: "로딩 중...", sender: "server" }]);
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
    <div
      className={`flex ${customStyle ? "flex-col w-[320px] p-4 rounded-lg bg-white shadow-md" : "h-screen"}`}
    >
      {!customStyle && (
        <SideBar onNewChat={() => setMessages([])} />
      )}
      <div className={`flex flex-col flex-grow ${customStyle ? "bg-gray-100 p-4" : "bg-gray-50"}`}>
        <div className="border-b p-4 text-[18px] bg-white">
          {customStyle ? "미리보기" : "새 대화"}
        </div>
        <div className={`flex-grow h-0 p-6 space-y-4 overflow-y-auto ${customStyle ? "scrollbar-hide" : ""}`}>
          {messages.map((msg, index) => (
            <div key={index} className={`flex items-start ${msg.sender === "user" ? "justify-end" : "justify-start"} space-x-4`}>
              {msg.sender === "server" && <div className="rounded-full w-10 h-10 bg-gray-500"></div>}
              <div
                className={`rounded-lg px-4 py-2 whitespace-pre-wrap text-gray-800 shadow-sm max-w-[60%] ${
                  msg.sender === "server" ? "bg-gray-200" : "bg-blue-200"
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
            className={`flex-1 border border-gray-300 rounded-lg py-2 px-6 mr-3 resize-none overflow-y-hidden shadow-sm focus:outline-none focus:ring-1 focus:ring-blue-500 ${customStyle ? "text-sm" : "text-base"}`}
            rows={1}
          />
          <button onClick={sendMessage} className="text-white bg-blue-600 hover:bg-blue-500 rounded-lg p-2">
            <AiOutlineSend size={20} />
          </button>
        </div>
      </div>
    </div>
  );
};

export default ChatPage;



// "use client";
// "use client";

// import React, { useState, useRef, useEffect } from "react";
// import { useRecoilState } from "recoil";
// import { EventSourcePolyfill } from "event-source-polyfill";
// import { postMessage } from "@/api/chat";
// import DefaultChat from "@/components/chat/DefaultChat";
// import PreviewChat from "@/components/chat/PreviewChat";
// import { messagesState, isLoadingState } from "@/store/chatAtoms";
// import { usePathname } from "next/navigation";
// import { useMutation } from "@tanstack/react-query";

// type ChatPageProps = {
//   customStyle?: boolean;
//   params: {
//     id: number;
//   };
// };

// const ChatPage: React.FC<ChatPageProps> = ({ customStyle = false, params }) => {
//   const chatId = "1";
//   const [input, setInput] = useState("");
//   const [messages, setMessages] = useRecoilState(messagesState);
//   const [isLoading, setIsLoading] = useRecoilState(isLoadingState);
//   const inputRef = useRef<HTMLTextAreaElement>(null);
//   const chatEndRef = useRef<HTMLDivElement>(null);

//   const BASE_URL = process.env.NEXT_PUBLIC_BASE_URL;

//   const getAccessToken = () => {
//     return localStorage.getItem("accessToken") || localStorage.getItem("fake-access-token");
//   };

//   // Remove async and return EventSourcePolyfill directly
//   const initializeSSE = () => {
//     const accessToken = getAccessToken();
//     if (!accessToken) {
//       console.error("No access token found in local storage or cookies.");
//       return null;
//     }

//     const sse = new EventSourcePolyfill(`${BASE_URL}/sse/connect`, {
//       headers: { Authorization: `Bearer ${accessToken}` },
//       withCredentials: true,
//     });

//     sse.onopen = () => console.log("SSE 연결이 성공적으로 열렸습니다.");

//     sse.addEventListener("node", (event) => {
//       const data = JSON.parse((event as MessageEvent).data);
//       if (data.type === "ANSWER") {
//         setMessages((prev) =>
//           prev.filter((msg) => msg.text !== "로딩 중...").concat({ text: data.message, sender: "server" })
//         );
//         setIsLoading(false);
//       }
//     });

//     sse.onerror = () => console.error("SSE 연결 오류: 자동 재연결 시도 중...");
//     return sse; 
//   };

//   const startNewChat = () => setMessages([]);

//   useEffect(() => {
//     const sse = initializeSSE();

//     return () => {
//       if (sse) sse.close(); 
//     };
//   }, []);

//   const createMutation = useMutation({
//     mutationFn: ({ chatId, data }: { chatId: string; data: { message: string } }) => postMessage(chatId, data),
//     onError: () => {
//       alert("댓글 등록에 실패했습니다. 다시 시도해 주세요.");
//     },
//   });

//   const sendMessage = () => {
//     if (input.trim()) {
//       setMessages((prev) => [...prev, { text: input, sender: "user" }]);
//       const data = { message: input };
//       createMutation.mutate({ chatId, data });
//       setInput("");
//       setIsLoading(true);
//       setMessages((prev) => [...prev, { text: "로딩 중...", sender: "server" }]);
//       if (inputRef.current) inputRef.current.style.height = "auto";
//     }
//   };

//   const handleInput = (e: React.ChangeEvent<HTMLTextAreaElement>) => {
//     setInput(e.target.value);
//     if (inputRef.current) {
//       inputRef.current.style.height = "auto";
//       inputRef.current.style.height = `${inputRef.current.scrollHeight}px`;
//     }
//   };

//   const handleKeyDown = (e: React.KeyboardEvent<HTMLTextAreaElement>) => {
//     if (e.key === "Enter") {
//       e.preventDefault();
//       e.shiftKey ? setInput(input + "\n") : sendMessage();
//     }
//   };

//   return (
//     <div className="flex h-screen overflow-hidden">
//       {customStyle ? (
//         <PreviewChat
//           input={input}
//           handleInput={handleInput}
//           sendMessage={sendMessage}
//           chatEndRef={chatEndRef}
//           handleKeyDown={handleKeyDown}
//         />
//       ) : (
//         <DefaultChat
//           input={input}
//           handleInput={handleInput}
//           sendMessage={sendMessage}
//           chatEndRef={chatEndRef}
//           onNewChat={startNewChat}
//           handleKeyDown={handleKeyDown}
//         />
//       )}
//     </div>
//   );
// };

// export default ChatPage;

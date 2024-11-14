// "use client";

// import React, { useState, useRef, useEffect } from "react";
// import { useMutation } from "@tanstack/react-query";
// import { AiOutlineSend } from "@react-icons/all-files/ai/AiOutlineSend";
// import { EventSourcePolyfill } from "event-source-polyfill";
// import { postMessage, postChatting } from "@/api/chat";
// import SideBar from "@/components/chat/SideBar";
// import ReactMarkdown from "react-markdown";
// import remarkGfm from "remark-gfm";




// type ChatPageProps = {
//   customStyle?: boolean;
//   params: {
//     id: string;
//   };
//   chatId: number
// };

// const ChatPage: React.FC<ChatPageProps> = ({ customStyle = false, params, chatId}) => {
//   const chatFlowId = params.id;
//   const [messages, setMessages] = useState<{ text: string; sender: "user" | "server" }[]>([]);
//   const [input, setInput] = useState("");
//   const [defaultChatId, setDefaultChatId] = useState<number>()
//   const inputRef = useRef<HTMLTextAreaElement>(null);
//   const chatEndRef = useRef<HTMLDivElement>(null);
//   const BASE_URL = process.env.NEXT_PUBLIC_BASE_URL;

//   const creatChateMutation = useMutation({
//     mutationFn: ({ chatFlowId, data }: { chatFlowId: string; data: { isPreview: boolean } }) =>
//       postChatting(chatFlowId, data),
  
//     onSuccess: (response) => {
//       const newChatId = response.data.data.id; 
//       setDefaultChatId(newChatId); 
//       if (response.headers['authorization']) {
//         const accessToken = response.headers['authorization'];
//         localStorage.setItem("fake-access-token", accessToken);
//         console.log("Fake access token 저장:", accessToken);
//       }
//     },
  
//     onError: () => {
//       alert("채팅 생성에 실패했습니다. 다시 시도해 주세요.");
//     },
//   });
  


//   const initializeSSE = async () => {
//     try {
//       const accessToken = localStorage.getItem("accessToken") || localStorage.getItem("fake-access-token");
//       if (!accessToken) return;
      
//       const sse = new EventSourcePolyfill(`${BASE_URL}/sse/connect`, {
//         headers: { Authorization: `Bearer ${accessToken}` },
//         withCredentials: true,
//       });
      
//       sse.onopen = () => console.log("SSE 연결이 성공적으로 열렸습니다. " );

//       sse.addEventListener("node", (event) => {
//         const data = JSON.parse((event as MessageEvent).data);
//         console.log(data)
//         if (data.type === "ANSWER") {
//           console.log(123)
//           setMessages((prev) => [...prev.slice(0, -1), { text: data.message, sender: "server" }]);
//         }
//       });
//       sse.onerror = () => console.error("SSE 연결 오류: 자동 재연결 시도 중...");
//     } catch (error) {
//       console.error("Failed to initialize SSE:", error);
//     }
//   };

//   useEffect(() => {
//     initializeSSE();
//   }, []);

//   useEffect(() => chatEndRef.current?.scrollIntoView({ behavior: "smooth" }), [messages]);

//   const createMutation = useMutation({
//     mutationFn: ({ chatId, data }: { chatId: string; data: { message: string } }) => postMessage(chatId, data),
//     onError: () => {
//       alert("메시지 전송에 실패했습니다. 다시 시도해 주세요.");
//     },
//   });

//   const sendMessage = () => {
//     if (input.trim()) {
//       setMessages((prev) => [...prev, { text: input, sender: "user" }]);
//       createMutation.mutate({ chatId: String(chatId), data: { message: input } });
//       setInput("");
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
//     <div
//       className={`${
//         customStyle
//           ? "ml-[20px] flex flex-col gap-4 w-[320px] h-[calc(100vh-170px)] rounded-[20px] p-[20px] bg-white bg-opacity-40 backdrop-blur-[15px] shadow-[0px_2px_8px_rgba(0,0,0,0.25)] overflow-y-auto"
//           : "flex h-screen"
//       }`}
//     >
//       {!customStyle && <SideBar onNewChat={() => setMessages([])} chatFlowId={chatFlowId}/>}
//       <div className={`flex flex-col flex-grow ${customStyle ? "" : "bg-gray-50"}`}>
//         <div className={`border-b p-4 bg-white ${customStyle ? "text-[14px]" : "text-[18px]"}`}>
//           {customStyle ? "미리보기" : "새 대화"}
//         </div>
//         <div className={`flex-grow h-0 p-6 space-y-4 overflow-y-auto ${customStyle ? "scrollbar-hide" : ""}`}>
//           {messages.map((msg, index) => (
//             <div key={index} className={`flex items-start ${customStyle ? "text-[12px]" : ""} ${msg.sender === "user" ? "justify-end " : "justify-start"} space-x-4`}>
//               {msg.sender === "server" && <div className={`rounded-full ${customStyle ? "w-5 h-5" : "w-10 h-10"} bg-gray-500`}></div>}
//               <div
//                 className={`rounded-lg px-4 py-2 whitespace-pre-wrap text-gray-800 shadow-sm ${
//                   msg.sender === "server" ? "bg-[#f9f9f9] border border-gray-300 max-w-[80%]" : "bg-[#f3f3f3] max-w-[60%]"
//                 }`}
//               >
//                 {msg.sender === "server" ? (
//                   <ReactMarkdown remarkPlugins={[remarkGfm]}>{msg.text}</ReactMarkdown>
//                 ) : (
//                   msg.text
//                 )}
//               </div>
//               {msg.sender === "user" && <div className={`rounded-full ${customStyle ? "w-5 h-5" :"w-10 h-10"} bg-gray-300`}></div>}
//             </div>
//           ))}
//           <div ref={chatEndRef} />
//         </div>
//         <div className="border-t p-6 flex items-center bg-white">
//           <textarea
//             ref={inputRef}
//             placeholder="메시지를 입력하세요..."
//             value={input}
//             onChange={handleInput}
//             onKeyDown={handleKeyDown}
//             className={`flex-1 border border-gray-300 rounded-lg py-2 px-6 mr-3 resize-none overflow-y-hidden shadow-sm focus:outline-none focus:ring-1 focus:ring-[#9A75BF] ${
//               customStyle ? "text-sm" : "text-base"
//             }`}
//             rows={1}
//           />
//           <button onClick={sendMessage} className="text-white bg-[#9A75BF] hover:bg-[#7C5DAF] rounded-lg p-2">
//             <AiOutlineSend size={15} />
//           </button>
//         </div>
//       </div>
//     </div>
//   );
// };

// export default ChatPage; 


"use client";

import React from "react";
import PreviewChat from "@/components/chat/PreviewChat";
import DefaultChat from "@/components/chat/DefaultChat";

type ChatPageProps = {
  customStyle?: string;
  params: {
    id: string;
  };
};

export default function chatpage({ customStyle = "default", params }: ChatPageProps) {
  const chatFlowId = params.id;
  

  return customStyle === "preview" ? (
    <PreviewChat chatFlowId={chatFlowId} />
  ) : (
    <DefaultChat chatFlowId={chatFlowId} />
  );
}

import React from "react";
import SideBar from "@/components/chat/SideBar";
import ReactMarkdown from "react-markdown";
import remarkGfm from "remark-gfm";
import { AiOutlineSend } from "@react-icons/all-files/ai/AiOutlineSend";
import { useRecoilValue } from "recoil";
import { messagesState } from "@/store/chatAtoms";

type DefaultChatProps = {
  input: string;
  handleInput: (e: React.ChangeEvent<HTMLTextAreaElement>) => void;
  sendMessage: () => void;
  chatEndRef: React.RefObject<HTMLDivElement>;
  onNewChat: () => void;
  handleKeyDown: (e: React.KeyboardEvent<HTMLTextAreaElement>) => void;
};

const DefaultChat: React.FC<DefaultChatProps> = ({
  input,
  handleInput,
  sendMessage,
  chatEndRef,
  onNewChat,
  handleKeyDown,
}) => {
  const messages = useRecoilValue(messagesState);

  return (
    <div className="flex w-full h-screen overflow-hidden">
      <SideBar onNewChat={onNewChat} />
      <div className="flex flex-col flex-grow bg-gray-50">
        <div className="border-b p-4 text-[18px] bg-white">새 대화</div>
        <div className="flex-grow h-0 p-6 overflow-y-auto space-y-4">
          {messages.map((msg, index) => (
            <div
              key={index}
              className={`flex ${msg.sender === "user" ? "justify-end" : "justify-start"} space-x-4`}
            >
              <div
                className={`rounded-lg px-4 py-2 whitespace-pre-wrap text-gray-800 shadow-sm max-w-[60%] ${
                  msg.sender === "server" ? "bg-[#f9f9f9] border border-gray-300" : "bg-[#f3f3f3]"
                }`}
              >
                {msg.sender === "server" ? (
                  <ReactMarkdown remarkPlugins={[remarkGfm]}>{msg.text}</ReactMarkdown>
                ) : (
                  msg.text
                )}
              </div>
            </div>
          ))}
          <div ref={chatEndRef} />
        </div>
        <div className="border-t p-6 flex items-center bg-white">
          <textarea
            value={input}
            onChange={handleInput}
            onKeyDown={handleKeyDown}
            rows={1}
            placeholder="메시지를 입력하세요..."
            className="flex-1 border border-gray-300 rounded-lg py-2 px-4 resize-none overflow-y-hidden shadow-sm focus:outline-none focus:ring-1 focus:ring-[#9A75BF]"
          />
          <button onClick={sendMessage} className="ml-3 p-2 bg-[#9A75BF] hover:bg-[#7C5DAF] rounded text-white">
            <AiOutlineSend size={20} />
          </button>
        </div>
      </div>
    </div>
  );
};

export default DefaultChat;

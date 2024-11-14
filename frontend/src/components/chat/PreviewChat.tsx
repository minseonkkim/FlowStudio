import React from "react";
import ReactMarkdown from "react-markdown";
import remarkGfm from "remark-gfm";
import { AiOutlineSend } from "@react-icons/all-files/ai/AiOutlineSend";
import { useRecoilValue } from "recoil";
import { messagesState } from "@/store/chatAtoms";

type PreviewChatProps = {
  input: string;
  handleInput: (e: React.ChangeEvent<HTMLTextAreaElement>) => void;
  sendMessage: () => void;
  chatEndRef: React.RefObject<HTMLDivElement>;
  handleKeyDown: (e: React.KeyboardEvent<HTMLTextAreaElement>) => void;
};

const PreviewChat: React.FC<PreviewChatProps> = ({ input, handleInput, sendMessage, chatEndRef, handleKeyDown }) => {
  const messages = useRecoilValue(messagesState);

  return (
    <div className="flex flex-col h-screen overflow-hidden bg-white">
      <div className="flex-grow p-6 overflow-y-auto space-y-4">
        {messages.map((msg, index) => (
          <div
            key={index}
            className={`flex ${msg.sender === "user" ? "justify-end" : "justify-start"} space-x-4`}
          >
            <div
              className={`px-4 py-2 shadow-sm max-w-[60%] rounded-lg text-gray-800 border ${
                msg.sender === "server" ? "bg-[#f9f9f9] border-gray-300" : "bg-[#e1f5fe] border-gray-300"
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
      <div className="flex items-center border-t p-6 bg-gray-50">
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
  );
};

export default PreviewChat;

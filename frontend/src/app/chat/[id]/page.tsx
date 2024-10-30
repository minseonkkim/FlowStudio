"use client";

import React, { useState, useRef, useEffect } from "react";
import { AiOutlineSend } from "@react-icons/all-files/ai/AiOutlineSend"; 

const ChatPage = () => {
  const [messages, setMessages] = useState<string[]>([]); // 대화 메시지 리스트
  const [input, setInput] = useState(""); // 입력창 상태
  const inputRef = useRef<HTMLTextAreaElement>(null);
  const chatEndRef = useRef<HTMLDivElement>(null); // 스크롤 하단 참조

  // 새 채팅 시작
  const startNewChat = () => {
    setMessages([]); // 기존 메시지 초기화
  };

  // 메시지 전송 핸들러
  const sendMessage = () => {
    if (input.trim()) {
      setMessages([...messages, input]); // 메시지 리스트에 추가
      setInput(""); // 입력창 초기화
      if (inputRef.current) {
        inputRef.current.style.height = "auto"; // 전송 후 높이 초기화
      }
    }
  };

  // 스크롤을 자동으로 가장 아래로 내리기
  useEffect(() => {
    chatEndRef.current?.scrollIntoView({ behavior: "smooth" });
  }, [messages]);

  // 텍스트 입력 시 높이 자동 조절
  const handleInput = (e: React.ChangeEvent<HTMLTextAreaElement>) => {
    setInput(e.target.value);
    if (inputRef.current) {
      inputRef.current.style.height = "auto"; // 높이 초기화
      inputRef.current.style.height = `${inputRef.current.scrollHeight}px`; // 내용에 맞게 높이 조절
    }
  };

  // 키 다운 핸들러
  const handleKeyDown = (e: React.KeyboardEvent<HTMLTextAreaElement>) => {
    if (e.key === "Enter") {
      if (e.shiftKey) {
        // Shift + Enter: 줄바꿈 추가
        setInput(input + "\n");
      } else {
        // Enter: 메시지 전송
        e.preventDefault(); // 엔터로 줄바꿈 되는 것을 막음
        sendMessage();
      }
    }
  };

  return (
    <div className="flex h-screen">
      {/* 왼쪽 사이드바 */}
      <div className="w-1/6 p-6 border-r bg-white">
        {/* 챗봇 이름 */}
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
        <button className="w-full py-2 px-4 bg-purple-100 text-purple-600 font-semibold rounded-xl">
          새 대화
        </button>
      </div>

      {/* 채팅 영역 */}
      <div className="flex flex-col flex-grow bg-gray-50">
        <div className="border-b p-4 text-[18px] bg-white">
          새 대화
        </div>

        <div className="flex-grow p-6 space-y-4 overflow-y-auto">
          {messages.map((msg, index) => (
            <div
              key={index}
              className="flex items-start justify-end space-x-4"
            >
              <div
                className="bg-white border border-gray-300 rounded-lg px-4 py-2 whitespace-pre-wrap text-gray-800 shadow-sm"
                style={{
                  maxWidth: "80%", 
                  width: "fit-content", 
                }}
              >
                {msg}
              </div>
              <div className="w-10 h-10 rounded-full bg-gray-300"></div>
            </div>
          ))}
          <div ref={chatEndRef} />
        </div>

        {/* 메시지 입력 영역 */}
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

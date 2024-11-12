import React from "react";
import { useState } from "react";

type SidebarProps = {
  onNewChat: () => void;
};

export default function Sidebar({ onNewChat }: SidebarProps) {
  const [title, setTitle] = useState<String>("대출금리 상담");

  return (
    <div className="w-64 p-6 border-r bg-white"> 
      <div className="flex items-center mb-8">
        {/* 카테고리 이미지 넣어주기 */}
        <div className="w-10 h-10 rounded-lg bg-gray-300 mr-4"></div> 
        <div className="text-lg font-semibold break-words max-w-[150px]">{title}</div>
      </div>
      <button
        onClick={onNewChat}
        className="mb-4 w-full py-2 px-4 border border-gray-300 rounded-xl text-gray-700 hover:bg-gray-100"
      >
        새 채팅
      </button>

      {/* 이 밑으로는 전체 채팅 목록 보여주기 */}
    </div>
  );
}

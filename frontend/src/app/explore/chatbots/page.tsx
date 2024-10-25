"use client";

import ChatbotCard from "@/components/chatbot/ChatbotCard";
import PopularChatbotCard from "@/components/chatbot/PopularChatbotCard";
import { useState } from "react";

export default function Page() {
  const [selectedCategory, setSelectedCategory] = useState<string>("모든 챗봇");

  const categorys = [
    "모든 챗봇",
    "금융",
    "헬스케어",
    "전자 상거래",
    "여행",
    "교육",
    "엔터테이먼트",
    "기타",
  ];

  const handleCategoryClick = (label: string) => {
    setSelectedCategory(label);
  };

  return (
    <div className="px-12 py-10">
      <div>
        <p className="mb-6 text-[22px]">가장 인기있는 챗봇</p>
        <div className="flex flex-row justify-between w-full gap-8">
          <PopularChatbotCard />
          <PopularChatbotCard />
          <PopularChatbotCard />
          <PopularChatbotCard />
        </div>
      </div>

      <div className="mt-16 mb-4">
        <p className="mb-6 text-[22px]">챗봇 라우지</p>
        {categorys.map((label) => (
          <button
            key={label}
            onClick={() => handleCategoryClick(label)}
            className={`mr-6 mb-6 ${selectedCategory === label ? "font-semibold" : "text-gray-600"}`}
          >
            {label}
          </button>
        ))}

        <div>
          <ChatbotCard />
          <ChatbotCard />
          <ChatbotCard />
          <ChatbotCard />
          <ChatbotCard />
        </div>
      </div>
    </div>
  );
}

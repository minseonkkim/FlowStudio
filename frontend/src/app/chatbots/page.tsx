"use client";

import CreateChatbotModal from "@/components/chatbot/CreateChatbotModal";
import PopularChatbotCard from "@/components/chatbot/PopularChatbotCard";
import { useState } from "react";

export default function Page() {
  const [selectedCategory, setSelectedCategory] = useState<string>("모든 챗봇");
  const [isCreateModalOpen, setIsCreateModalOpen] = useState(false);

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
      <div className="flex flex-col">
        <div className="mb-4 flex items-center">
          <p className="text-[22px] mr-6">나의 챗봇</p>
          <button
            onClick={() => {
              setIsCreateModalOpen(true);
            }}
            className="py-2 px-4 text-[14px] bg-[#9A75BF] text-white rounded-lg"
          >
            챗봇 만들기
          </button>
        </div>

        <div className="mb-10">
          {categorys.map((label) => (
            <button
              key={label}
              onClick={() => handleCategoryClick(label)}
              className={`mr-6 ${selectedCategory === label ? "font-semibold" : "text-gray-600"}`}
            >
              {label}
            </button>
          ))}
        </div>
        <div className="grid grid-cols-4 w-full w-full gap-6">
          <PopularChatbotCard />
          <PopularChatbotCard />
          <PopularChatbotCard />
          <PopularChatbotCard />
          <PopularChatbotCard />
          <PopularChatbotCard />
          <PopularChatbotCard />
          <PopularChatbotCard />
          <PopularChatbotCard />
          <PopularChatbotCard />
        </div>
      </div>

      {/* 모달 */}
      {isCreateModalOpen && (
        <div className="fixed inset-0 flex items-center justify-center bg-black bg-opacity-50">
          <CreateChatbotModal />
        </div>
      )}
    </div>
  );
}

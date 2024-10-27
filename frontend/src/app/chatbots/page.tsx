"use client";

import CreateChatbotModal from "@/components/chatbot/CreateChatbotModal";
import ShareChatbotModal from "@/components/chatbot/ShareChatbotModal";
import PopularChatbotCard from "@/components/chatbot/PopularChatbotCard";
import { useState } from "react";

interface Chatbot {
  id: number;
  title: string;
  description: string;
  category: string;
  iconId: number;
}

const chatbots: Chatbot[] = [
  {
    id: 1,
    title: "Workflow Planning Assistant",
    description: "An assistant that helps you plan and select the right node for a workflow (v0.6.0).",
    category: "교육",
    iconId: 1,
  },
  {
    id: 2,
    title: "Financial Advisor Bot",
    description: "Provides insights and suggestions for better financial planning (v1.2.3).",
    category: "금융",
    iconId: 1,
  },
  {
    id: 3,
    title: "Health Tracker Assistant",
    description: "Tracks your daily health metrics and offers tips to improve your well-being (v2.0.1).",
    category: "헬스케어",
    iconId: 1,
  },
  {
    id: 4,
    title: "E-Commerce Helper",
    description: "Assists in finding the best deals and manages your online shopping lists (v1.0.5).",
    category: "전자 상거래",
    iconId: 1,
  },
  {
    id: 5,
    title: "Travel Itinerary Planner",
    description: "Helps you create and organize your travel plans with ease (v0.8.7).",
    category: "여행",
    iconId: 1,
  },
];


export default function Page() {
  const [selectedCategory, setSelectedCategory] = useState<string>("모든 챗봇");
  const [isCreateModalOpen, setIsCreateModalOpen] = useState(false);
  const [isShareModalOpen, setIsShareModalOpen] = useState(false);

  const categories = [
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

  const filteredChatbots = selectedCategory === "모든 챗봇"
    ? chatbots
    : chatbots.filter((bot) => bot.category === selectedCategory);

  return (
    <div className="px-12 py-10">
      <div className="flex flex-col">
        <div className="mb-4 flex items-center">
          <p className="text-[22px] font-semibold mr-6">나의 챗봇</p>
          <button
            onClick={() => setIsCreateModalOpen(true)}
            className="py-2 px-4 text-[14px] bg-[#9A75BF] text-white rounded-lg"
          >
            챗봇 만들기
          </button>
        </div>

        <div className="mb-10">
          {categories.map((label) => (
            <button
              key={label}
              onClick={() => handleCategoryClick(label)}
              className={`mr-6 ${selectedCategory === label ? "font-semibold" : "text-gray-600"}`}
            >
              {label}
            </button>
          ))}
        </div>

        <div className="grid grid-cols-4 w-full gap-6">
          {filteredChatbots.map((bot) => (
            <PopularChatbotCard
              key={bot.id}
              title={bot.title}
              description={bot.description}
              buttonText="공유"
              onButtonClick={() => setIsShareModalOpen(true)}
            />
          ))}
        </div>
      </div>

      {/* 챗봇 생성 모달 */}
      {isCreateModalOpen && (
        <div
          className="fixed inset-0 flex items-center justify-center bg-black bg-opacity-50"
          onClick={() => setIsCreateModalOpen(false)}
        >
          <div onClick={(e) => e.stopPropagation()}>
            <CreateChatbotModal onClose={() => setIsCreateModalOpen(false)} />
          </div>
        </div>
      )}

      {/* 챗봇 공유 모달 */}
      {isShareModalOpen && (
        <div
          className="fixed inset-0 flex items-center justify-center bg-black bg-opacity-50"
          onClick={() => setIsShareModalOpen(false)}
        >
          <div onClick={(e) => e.stopPropagation()}>
            <ShareChatbotModal onClose={() => setIsShareModalOpen(false)} />
          </div>
        </div>
      )}
    </div>
  );
}

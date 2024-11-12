"use client";

import { useRouter } from "next/navigation";
import React, { useState } from "react";
import ChatbotCard from "@/components/chatbot/ChatbotCard";
import Search from "@/components/common/Search";
import PopularChatbotCard from "@/components/chatbot/PopularChatbotCard";
import PurpleButton from "@/components/common/PurpleButton";
import { ChatFlow } from "@/types/chatbot";

const chatFlows: ChatFlow[] = [
  {
    chatFlowId: 1,
    title: "챗봇 1",
    description: "챗봇 1 묘사",
    author: {
      id: 1,
      username: "김싸피",
      nickname: "김싸피",
      profileImage: "kim.png",
    },
    thumbnail: "1",
    categories: [
      { categoryId: 1, name: "교육" },
      { categoryId: 2, name: "금융" },
    ],
    public: true,
  },
  {
    chatFlowId: 2,
    title: "챗봇 2",
    description: "챗봇 2 묘사",
    author: {
      id: 2,
      username: "정싸피",
      nickname: "정싸피",
      profileImage: "jeong.png",
    },
    thumbnail: "2",
    categories: [
      { categoryId: 1, name: "금융" },
      { categoryId: 3, name: "교육" },
    ],
    public: true,
  },
];

export default function Page() {
  const router = useRouter();
  const categories = [
    "모든 챗봇",
    "금융",
    "헬스케어",
    "전자상거래",
    "여행",
    "교육",
    "엔터테인먼트",
    "기타",
  ];

  const [selectedCategory, setSelectedCategory] = useState<string>("모든 챗봇");
  const [searchTerm, setSearchTerm] = useState<string>("");

  const handleCategoryClick = (label: string) => {
    setSelectedCategory(label);
  };

  const filteredChatFlows = chatFlows.filter((bot) => {
    const matchesCategory =
      selectedCategory === "모든 챗봇" ||
      bot.categories.some((category) => category.name === selectedCategory);
    const matchesSearch = bot.title
      .toLowerCase()
      .includes(searchTerm.toLowerCase());
    return matchesCategory && matchesSearch;
  });

  const handleEvaluationClick = () => {
    router.push("/evaluation");
  };

  const handleResultClick = (id: number) => {
    router.push(`/evaluation/${id}/result`);
  };

  return (
    <div className="px-4 md:px-12 py-10">
      <div className="flex items-center mb-2">
        <p className="font-semibold text-[24px] text-gray-700 mr-6">
          챗봇 평가 결과
        </p>
        <PurpleButton
          text="챗봇 평가하기"
          onHandelButton={() => handleEvaluationClick()}
        />
      </div>

      {/* 카테고리 선택 */}
      <div className="flex justify-between items-center mb-6">
        <div className="hidden md:flex">
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
        <div className="md:hidden w-full mr-2">
          <select
            onChange={(e) => handleCategoryClick(e.target.value)}
            className="w-full p-2 border rounded-md"
            value={selectedCategory}
          >
            {categories.map((label) => (
              <option key={label} value={label}>
                {label}
              </option>
            ))}
          </select>
        </div>
        <Search onSearchChange={setSearchTerm} />
      </div>

      <div className="hidden md:flex flex-col gap-1">
        {filteredChatFlows.map((bot) => (
          <ChatbotCard
            key={bot.chatFlowId}
            title={bot.title}
            description={bot.description}
            iconId={bot.thumbnail}
            category={bot.categories.map((cat) => cat.name)}
            onCardClick={() => handleResultClick(bot.chatFlowId)}
            type="eval"
          />
        ))}
      </div>

      <div className="md:hidden flex flex-col gap-4">
        {filteredChatFlows.map((bot) => (
          <PopularChatbotCard
            key={bot.chatFlowId}
            title={bot.title}
            description={bot.description}
            iconId={bot.thumbnail}
            category={bot.categories.map((cat) => cat.name)}
            type="eval"
            onCardClick={() => handleResultClick(bot.chatFlowId)}
          />
        ))}
      </div>
    </div>
  );
}

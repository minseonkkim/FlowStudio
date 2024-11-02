"use client";

import CreateChatbotModal from "@/components/chatbot/CreateChatbotModal";
import ShareChatbotModal from "@/components/chatbot/ShareChatbotModal";
import PopularChatbotCard from "@/components/chatbot/PopularChatbotCard";
import Search from "@/components/common/Search";
import { useRouter } from "next/navigation";
import { useState } from "react";
import { useRecoilState } from "recoil";
import { selectedChatbotState } from "@/store/chatbotAtoms";
import PurpleButton from "@/components/common/PurpleButton";

interface Chatbot {
  id: number;
  title: string;
  description: string;
  category: string[];
  iconId: number;
}

const chatbots: Chatbot[] = [
  {
    id: 1,
    title: "Workflow Planning Assistant",
    description:
      "An assistant that helps you plan and select the right node for a workflow (v0.6.0).",
    category: ["교육", "금융"],
    iconId: 1,
  },
  {
    id: 2,
    title: "Financial Advisor Bot",
    description:
      "Provides insights and suggestions for better financial planning (v1.2.3).",
    category: ["교육", "금융"],
    iconId: 2,
  },
  {
    id: 3,
    title: "Health Tracker Assistant",
    description:
      "Tracks your daily health metrics and offers tips to improve your well-being (v2.0.1).",
    category: ["헬스케어"],
    iconId: 3,
  },
  {
    id: 4,
    title: "E-Commerce Helper",
    description:
      "Assists in finding the best deals and manages your online shopping lists (v1.0.5).",
    category: ["전자 상거래", "헬스케어"],
    iconId: 4,
  },
  {
    id: 5,
    title: "Travel Itinerary Planner",
    description:
      "Helps you create and organize your travel plans with ease (v0.8.7).",
    category: ["여행"],
    iconId: 5,
  },
];

export default function Page() {
  const [selectedCategory, setSelectedCategory] = useState<string>("모든 챗봇");
  const [searchTerm, setSearchTerm] = useState<string>("");
  const [isCreateModalOpen, setIsCreateModalOpen] = useState(false);
  const [isShareModalOpen, setIsShareModalOpen] = useState(false);
  const [, setSelectedChatbot] = useRecoilState(selectedChatbotState);
  const router = useRouter();

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

  const handleCreateClick = () => {
    setSelectedChatbot(null); 
    setIsCreateModalOpen(true); 
  };

  const handleUpdateClick = (chatbot: Chatbot) => {
    setSelectedChatbot(chatbot); 
    setIsCreateModalOpen(true); 
  };

  const filteredChatbots = chatbots.filter((bot) => {
    const matchesCategory =
      selectedCategory === "모든 챗봇" ||
      bot.category.includes(selectedCategory);
    const matchesSearch = bot.title
      .toLowerCase()
      .includes(searchTerm.toLowerCase());
    return matchesCategory && matchesSearch;
  });

  return (
    <div className="px-4 md:px-12 py-10">
      <div className="flex flex-col">
        <div className="mb-2 flex items-center">
          <p className="font-semibold text-[24px] text-gray-700 mr-6">나의 챗봇</p>
          <PurpleButton text='챗봇 만들기' onHandelButton={handleCreateClick} />
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
      </div>

      <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 xl:grid-cols-4 w-full gap-4">
        {filteredChatbots.map((bot) => (
          <PopularChatbotCard
            key={bot.id}
            iconId={bot.iconId}
            title={bot.title}
            description={bot.description}
            type="my"
            category={bot.category}
            onCardClick={() => router.push(`/chatbot/${bot.id}/workflow`)}
            onButtonUpdateClick={() => handleUpdateClick(bot)}
            onButtonShareClick={() => setIsShareModalOpen(true)}
          />
        ))}
      </div>
    

      {/* 챗봇 생성 모달 */}
      {isCreateModalOpen && (
        <div
          className="z-30 fixed inset-0 flex items-center justify-center bg-black bg-opacity-50"
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
          className="z-30 fixed inset-0 flex items-center justify-center bg-black bg-opacity-50"
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

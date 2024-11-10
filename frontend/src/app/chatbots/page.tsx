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
import { ChatFlow } from "@/types/chatbot"

const chatFlows: ChatFlow[] = [
  {
    chatFlowId: 1,
    title: "챗봇 1",
    description:
      "챗봇 1 묘사",
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
    description:
      "챗봇 2 묘사",
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

  const handleUpdateClick = (chatFlow: ChatFlow) => {
    setSelectedChatbot(chatFlow); 
    setIsCreateModalOpen(true); 
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
        {filteredChatFlows.map((bot) => (
          <PopularChatbotCard
            key={bot.chatFlowId}
            iconId={bot.thumbnail}
            title={bot.title}
            description={bot.description}
            type="my"
            category={bot.categories.map((cat) => cat.name)}
            onCardClick={() => router.push(`/chatbot/${bot.chatFlowId}/workflow`)}
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

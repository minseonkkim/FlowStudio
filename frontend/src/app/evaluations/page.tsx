"use client";

import { useRouter } from "next/navigation";
import React, { useState, useEffect } from "react";
import ChatbotCard from "@/components/chatbot/ChatbotCard";
import Search from "@/components/common/Search";
import PopularChatbotCard from "@/components/chatbot/PopularChatbotCard";
import PurpleButton from "@/components/common/PurpleButton";
import { ChatFlow } from "@/types/chatbot";
import { getChatFlowTestList } from "@/api/evaluation"
import { useQuery } from '@tanstack/react-query';
import Loading from "@/components/common/Loading";
import { categories } from "@/constants/chatbotCategories";

export default function Page() {
  const router = useRouter();

  const [selectedCategory, setSelectedCategory] = useState<string>("모든 챗봇");
  const [searchTerm, setSearchTerm] = useState<string>("");
  const [itemsToLoad, setItemsToLoad] = useState(10);
  const [isCategoryFixed, setIsCategoryFixed] = useState(false);
  const { isLoading, isError, error, data: chatFlows } = useQuery<ChatFlow[]>({
    queryKey: ['chatFlows'],
    queryFn: () => getChatFlowTestList(true),
    
  });

  useEffect(() => {
    if (isError && error) {
      alert("테스트 완료한 챗플로우 목록을 불러오는 중 오류가 발생했습니다. 다시 시도해 주세요.");
    }
  }, [isError, error]);

  useEffect(() => {
    const handleScroll = () => {
      if (
        window.innerHeight + document.documentElement.scrollTop >=
        document.documentElement.offsetHeight - 100
      ) {
        setItemsToLoad((prev) => prev + 16);
      }

      if (window.scrollY >= 57) {
        setIsCategoryFixed(true);
      } else {
        setIsCategoryFixed(false);
      }
    };

    window.addEventListener("scroll", handleScroll);
    return () => window.removeEventListener("scroll", handleScroll);
  }, []);

  const handleCategoryClick = (label: string) => {
    setSelectedCategory(label);
  };

  const filteredChatFlows = (chatFlows || []).filter((bot) => {
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

  if(isLoading) return <Loading/>;

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
      <div className={`flex justify-between items-center mb-6 ${
            isCategoryFixed ? "fixed top-[57px] left-0 right-0 bg-white z-10 px-4 md:px-12 py-2" : ""
          }`}>
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
        {filteredChatFlows.reverse().slice(0, itemsToLoad).map((bot) => (
          <ChatbotCard
            key={bot.chatFlowId}
            chatbotId={bot.chatFlowId}
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
        {filteredChatFlows.reverse().slice(0, itemsToLoad).map((bot) => (
          <PopularChatbotCard
            key={bot.chatFlowId}
            chatbotId={bot.chatFlowId}
            title={bot.title}
            description={bot.description}
            iconId={bot.thumbnail}
            category={bot.categories.map((cat) => cat.name)}
            type="eval"
            onCardClick={() => handleResultClick(bot.chatFlowId)}/>
        ))}
      </div>
    </div>
  );
}

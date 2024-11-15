"use client";

import PopularChatbotCard from "@/components/chatbot/PopularChatbotCard";
import ChatbotCard from "@/components/chatbot/ChatbotCard";
import Search from "@/components/common/Search";
import { useState } from "react";
import { Swiper, SwiperSlide } from "swiper/react";
import "swiper/css";
import { SharedChatFlow } from "@/types/chatbot";
import { getSharedChatFlows } from "@/api/share";
import { useQuery } from "@tanstack/react-query";


export default function Page() {
  const { isLoading, isError, error, data: chatFlows } = useQuery<SharedChatFlow[]>({
    queryKey: ['sharedChatFlows'],
    queryFn: () => getSharedChatFlows(),
  });

  const [selectedCategory, setSelectedCategory] = useState<string>("모든 챗봇");
  const [searchTerm, setSearchTerm] = useState<string>("");
  const [activeSlide, setActiveSlide] = useState<number>(0);

  const categories = [
    "모든 챗봇",
    "금융",
    "헬스케어",
    "전자 상거래",
    "여행",
    "교육",
    "엔터테인먼트",
    "기타",
  ];

  const popularChatbots = chatFlows ? [...chatFlows]
    .sort((a, b) => b.shareNum - a.shareNum)
    .slice(0, 4) : [];

  const filteredChatFlows = chatFlows ? chatFlows.filter((bot) => {
    const matchesCategory =
      selectedCategory === "모든 챗봇" ||
      bot.categories.some((category) => category.name === selectedCategory);
    const matchesSearch = bot.title
      .toLowerCase()
      .includes(searchTerm.toLowerCase());
    return matchesCategory && matchesSearch;
  }) : [];

  const handleCategoryClick = (label: string) => {
    setSelectedCategory(label);
  };

  return (
    <div className="px-4 md:px-12 py-10">
      <div>
        <p className="mb-4 font-semibold text-[24px] text-gray-700">
          가장 인기있는 챗봇
        </p>
        <div className="md:hidden">
          <Swiper
            spaceBetween={16}
            slidesPerView={1}
            onSlideChange={(swiper) => setActiveSlide(swiper.activeIndex)}
          >
            {popularChatbots?.slice().reverse().map((chatbot) => (
              <SwiperSlide key={chatbot.chatFlowId}>
                <PopularChatbotCard
                  chatbotId={chatbot.chatFlowId}
                  title={chatbot.title}
                  description={chatbot.description}
                  iconId={chatbot.thumbnail}
                  type="all"
                  category={chatbot.categories.map((cat) => cat.name)}
                />
              </SwiperSlide>
            ))}
          </Swiper>
          {/* 현재 슬라이드 인덱스 표시 */}
          <div className="flex justify-center mt-2">
            {popularChatbots?.slice().reverse().map((_, index) => (
              <span
                key={index}
                className={`h-2 w-2 rounded-full mx-1 ${
                  index === activeSlide ? "bg-[#9A75BF]" : "bg-gray-300"
                }`}
              />
            ))}
          </div>
        </div>
        <div className="hidden md:grid md:grid-cols-2 xl:grid-cols-4 w-full gap-4">
          {popularChatbots?.slice().reverse().map((chatbot) => (
            <PopularChatbotCard
              key={chatbot.chatFlowId}
              chatbotId={chatbot.chatFlowId}
              title={chatbot.title}
              description={chatbot.description}
              iconId={chatbot.thumbnail}
              type="all"
              category={chatbot.categories.map((cat) => cat.name)}
            />
          ))}
        </div>
      </div>

      <div className="mt-16">
        <p className="mb-2 font-semibold text-[24px] text-gray-700">
          챗봇 라운지
        </p>

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
          {filteredChatFlows?.slice().reverse().map((bot) => (
            <ChatbotCard
              key={bot.chatFlowId}
              chatbotId={bot.chatFlowId}
              title={bot.title}
              description={bot.description}
              iconId={bot.thumbnail}
              category={bot.categories.map((cat) => cat.name)}
              type="all"
            />
          ))}
        </div>

        <div className="md:hidden flex flex-col gap-4">
          {filteredChatFlows?.slice().reverse().map((bot) => (
            <PopularChatbotCard
              key={bot.chatFlowId}
              chatbotId={bot.chatFlowId}
              title={bot.title}
              description={bot.description}
              iconId={bot.thumbnail}
              category={bot.categories.map((cat) => cat.name)}
              type="all"
            />
          ))}
        </div>
      </div>
    </div>
  );
}

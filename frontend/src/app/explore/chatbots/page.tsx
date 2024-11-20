"use client";

import PopularChatbotCard from "@/components/chatbot/PopularChatbotCard";
import ChatbotCard from "@/components/chatbot/ChatbotCard";
import Search from "@/components/common/Search";
import { useState, useEffect, useMemo } from "react";
import { Swiper, SwiperSlide } from "swiper/react";
import "swiper/css";
import { SharedChatFlow } from "@/types/chatbot";
import { getSharedChatFlows } from "@/api/share";
import { useQuery } from "@tanstack/react-query";
import Loading from "@/components/common/Loading";
import { categories } from "@/constants/chatbotCategories";
import { useRouter } from "next/navigation";

export default function Page() {
  const { isLoading, data: chatFlows } = useQuery<SharedChatFlow[]>({
    queryKey: ["sharedChatFlows"],
    queryFn: () => getSharedChatFlows(),
  });

  const [selectedCategory, setSelectedCategory] = useState<string>("모든 챗봇");
  const [searchTerm, setSearchTerm] = useState<string>("");
  const [activeSlide, setActiveSlide] = useState<number>(0);
  const [itemsToLoad, setItemsToLoad] = useState<number>(8);
  const [isCategoryFixed, setIsCategoryFixed] = useState<boolean>(false); 
  const router = useRouter();

  const popularChatbots = useMemo(() => {
    return chatFlows ? [...chatFlows].sort((a, b) => b.shareCount - a.shareCount).slice(0, 4) : [];
  }, [chatFlows]);

  const filteredChatFlows = useMemo(() => {
    return chatFlows
      ? chatFlows.filter((bot) => {
          const matchesCategory =
            selectedCategory === "모든 챗봇" ||
            bot.categories.some((category) => category.name === selectedCategory);
          const matchesSearch = bot.title.toLowerCase().includes(searchTerm.toLowerCase());
          return matchesCategory && matchesSearch;
        })
      : [];
  }, [chatFlows, selectedCategory, searchTerm]);

  useEffect(() => {
    const handleScroll = () => {
      if (window.scrollY > 100) {
        setIsCategoryFixed(true);
      } else {
        setIsCategoryFixed(false);
      }
    };

    window.addEventListener("scroll", handleScroll);
    return () => window.removeEventListener("scroll", handleScroll);
  }, []);

  useEffect(() => {
    const handleScroll = () => {
      if (
        window.innerHeight + document.documentElement.scrollTop >=
        document.documentElement.offsetHeight - 100
      ) {
        setItemsToLoad((prev) => prev + 8);
      }
    };

    window.addEventListener("scroll", handleScroll);
    return () => window.removeEventListener("scroll", handleScroll);
  }, []);

  const handleCategoryClick = (label: string) => {
    setSelectedCategory(label);
  };

  if (isLoading) 
  return <Loading/>;

  return (
    <div className="px-4 md:px-12 py-10">
      <div>
        <p className="mb-4 font-semibold text-[24px] text-gray-700">가장 인기있는 챗봇</p>
        <div className="md:hidden">
          <Swiper
            spaceBetween={16}
            slidesPerView={1}
            onSlideChange={(swiper) => setActiveSlide(swiper.activeIndex)}
          >
            {popularChatbots?.slice().map((chatbot) => (
              <SwiperSlide key={chatbot.chatFlowId}>
                <PopularChatbotCard
                  chatbotId={chatbot.chatFlowId}
                  title={chatbot.title}
                  description={chatbot.description}
                  iconId={chatbot.thumbnail}
                  type="all"
                  authorNickName={chatbot.author.nickname}
                  authorProfile={chatbot.author.profileImage}
                  shareNum={chatbot.shareCount}
                  category={chatbot.categories.map((cat) => cat.name)}
                />
              </SwiperSlide>
            ))}
          </Swiper>
          <div className="flex justify-center mt-2">
            {popularChatbots?.slice().map((_, index) => (
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
          {popularChatbots?.slice().map((chatbot) => (
            <PopularChatbotCard
              key={chatbot.chatFlowId}
              chatbotId={chatbot.chatFlowId}
              title={chatbot.title}
              description={chatbot.description}
              iconId={chatbot.thumbnail}
              type="all"
              authorNickName={chatbot.author.nickname}
              authorProfile={chatbot.author.profileImage}
              onCardClick={() => {
                router.push(`/chatbot/${chatbot.chatFlowId}/chatflow?isEditable=false`);
  
              }}
              shareNum={chatbot.shareCount}
              category={chatbot.categories.map((cat) => cat.name)}
            />
          ))}
        </div>
      </div>

      <div className="mt-16">
        <p className="mb-2 font-semibold text-[24px] text-gray-700">챗봇 라운지</p>

        <div
          className={`flex justify-between items-center mb-6 ${
            isCategoryFixed
              ? "fixed top-[57px] left-0 right-0 bg-white z-10 px-4 md:px-12 py-2"
              : ""
          }`}
        >
          <div className="hidden md:flex">
            {categories.map((label) => (
              <button
                key={label}
                onClick={() => handleCategoryClick(label)}
                className={`mr-6 ${
                  selectedCategory === label ? "font-semibold" : "text-gray-600"
                }`}
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
          {filteredChatFlows?.slice().reverse().slice(0, itemsToLoad).map((bot) => (
            <ChatbotCard
              key={bot.chatFlowId}
              chatbotId={bot.chatFlowId}
              title={bot.title}
              description={bot.description}
              iconId={bot.thumbnail}
              authorNickName={bot.author.nickname}
              authorProfile={bot.author.profileImage}
              onCardClick={() => {
                router.push(`/chatbot/${bot.chatFlowId}/chatflow?isEditable=false`);
  
              }}
              shareNum={bot.shareCount}
              category={bot.categories.map((cat) => cat.name)}
              type="all"
            />
          ))}
        </div>

        <div className="md:hidden flex flex-col gap-4">
          {filteredChatFlows?.slice().reverse().slice(0, itemsToLoad).map((bot) => (
            <PopularChatbotCard
              key={bot.chatFlowId}
              chatbotId={bot.chatFlowId}
              title={bot.title}
              description={bot.description}
              iconId={bot.thumbnail}
              authorNickName={bot.author.nickname}
              authorProfile={bot.author.profileImage}
              shareNum={bot.shareCount}
              category={bot.categories.map((cat) => cat.name)}
              type="all"
            />
          ))}
        </div>
      </div>
    </div>
  );
}

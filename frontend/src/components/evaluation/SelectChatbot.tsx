'use client'
import { useState, useEffect } from "react";
import PopularChatbotCard from "@/components/chatbot/PopularChatbotCard";
import Search from "../common/Search";
import { ChatFlow } from "@/types/chatbot";
import { useQuery } from '@tanstack/react-query';
import { getAllChatFlows } from "@/api/chatbot";
import { useSetRecoilState } from "recoil";
import { chatbotIdState } from "@/store/evaluationAtoms";
import { categories } from "@/constants/chatbotCategories";

interface SelectChatbotProps {
  onNext: () => void; 
  selectedTab: string
}

export default function SelectChatbot({ onNext, selectedTab }: SelectChatbotProps) {
  const [selectedCategory, setSelectedCategory] = useState<string>("모든 챗봇");
  const [searchTerm, setSearchTerm] = useState<string>("");
  const setChatbotId = useSetRecoilState(chatbotIdState);

  // 챗 플로우 목록 조회
  const { isError, error, data: chatFlows } = useQuery<ChatFlow[]>({
    queryKey: ['chatFlows', { isShared: false }], 
    queryFn: () => getAllChatFlows(false), 
  });

  useEffect(() => {
    if (isError && error) {
      alert("챗봇 목록을 불러오는 중 오류가 발생했습니다. 다시 시도해 주세요.");
    }
  }, [isError, error]);

  const filteredChatFlows = chatFlows?.filter((bot) => {
    const matchesCategory =
      selectedCategory === "모든 챗봇" ||
      bot.categories.some((category) => category.name === selectedCategory);
    const matchesSearch = bot.title
      .toLowerCase()
      .includes(searchTerm.toLowerCase());
    return matchesCategory && matchesSearch;
  });

  const handleCategoryClick = (label: string) => {
    setSelectedCategory(label);
  };
  const handleChatbotSelect = (chatbotId: number) => {
    setChatbotId(chatbotId); // 선택한 챗봇 ID를 Recoil에 저장
    onNext(); // 다음 단계로 이동
  };


  return (
    <div>
      <div className="flex justify-between mb-8 h-[15px]">
        <p className="text-[22px]">{selectedTab}</p>
      </div>
      <div className="flex justify-between items-center mb-6">
        <div>
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
        <Search onSearchChange={setSearchTerm} />
      </div>

      <div className="grid grid-cols-3 gap-6 mb-8">
        {filteredChatFlows?.slice().reverse().map((chatbot) => (
          <PopularChatbotCard
            key={chatbot.chatFlowId}
            chatbotId={chatbot.chatFlowId}
            title={chatbot.title}
            description={chatbot.description}
            category={chatbot.categories.map((cat) => cat.name)}
            iconId={chatbot.thumbnail}
            onCardClick={() => handleChatbotSelect(chatbot.chatFlowId)}
          />
        ))}
      </div>
    </div>
  );
}

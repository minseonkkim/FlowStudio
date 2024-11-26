'use client'
import { useState, useEffect } from "react";
import PopularChatbotCard from "@/components/chatbot/PopularChatbotCard";
import { IoIosInformationCircleOutline } from '@react-icons/all-files/io/IoIosInformationCircleOutline';
import { Tooltip } from 'react-tooltip';
import Search from "../common/Search";
import { ChatFlow } from "@/types/chatbot";
import { useQuery } from '@tanstack/react-query';
import { getChatFlowTestList } from "@/api/evaluation";
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
    queryKey: ['chatFlows', { executable: true }], 
    queryFn: () => getChatFlowTestList(true), 
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
        <div className="flex gap-2">
          <p className="text-[22px]">{selectedTab}</p>
          <IoIosInformationCircleOutline 
            data-tooltip-id="info-tooltip1" 
            className="w-4 h-4 text-[#4F4F4F] mt-2" 
          />
        </div>
        <Tooltip 
          id="info-tooltip1" 
          content="평가 가능한 챗봇만 보여집니다." 
          place="right"
          style={{
            backgroundColor: 'white',
            color: 'black',
            border: '1px solid #D1D5DB', 
            boxShadow: '0 10px 15px rgba(0, 0, 0, 0.1), 0 4px 6px rgba(0, 0, 0, 0.05)', 
            borderRadius: '0.375rem',  
            padding: '0.5rem 0.75rem', 
            width: '22rem',
            zIndex: 50          
          }}
        />
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

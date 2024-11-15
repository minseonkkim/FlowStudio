'use client'
import { useState, useEffect } from "react";
import PopularChatbotCard from "@/components/chatbot/PopularChatbotCard";
import Search from "../common/Search";
import { ChatFlow } from "@/types/chatbot";
import { useQuery } from '@tanstack/react-query';
import { getAllChatFlows } from "@/api/chatbot";
import { useSetRecoilState } from "recoil";
import { chatbotIdState } from "@/store/evaluationAtoms";

interface SelectChatbotProps {
  onNext: () => void; 
}

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

export default function SelectChatbot({ onNext }: SelectChatbotProps) {
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

  return (
    <div>
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
        {filteredChatFlows?.map((chatbot) => (
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

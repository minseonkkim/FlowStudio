import { useState } from "react";
import PopularChatbotCard from "@/components/chatbot/PopularChatbotCard";
import Search from "../common/Search";
import { ChatFlow } from "@/types/chatbot";

interface SelectChatbotProps {
  onNext: () => void;
}

const chatFlows: ChatFlow[] = [
  {
    chatFlowId: "1",
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
    chatFlowId: "2",
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

  const filteredChatFlows = chatFlows.filter((bot) => {
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
        {filteredChatFlows.map((chatbot) => (
          <PopularChatbotCard
            key={chatbot.chatFlowId}
            title={chatbot.title}
            description={chatbot.description}
            category={chatbot.categories.map((cat) => cat.name)}
            iconId={chatbot.thumbnail}
            onCardClick={() => {
              onNext();
            }}
          />
        ))}
      </div>
    </div>
  );
}

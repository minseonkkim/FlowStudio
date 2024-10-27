import { useState } from "react";
import PopularChatbotCard from "@/components/chatbot/PopularChatbotCard";

interface SelectChatbotProps {
  onNext: () => void;
}

interface Chatbot {
  id: number;
  title: string;
  description: string;
  category: string;
  iconId: number;
  shareNum: number;
}

const chatbots: Chatbot[] = [
  {
    id: 1,
    title: "Workflow Planning Assistant",
    description: "An assistant that helps you plan and select the right node for a workflow (v0.6.0).",
    category: "교육",
    iconId: 1,
    shareNum: 120,
  },
  {
    id: 2,
    title: "Financial Advisor Bot",
    description: "Provides insights and suggestions for better financial planning (v1.2.3).",
    category: "금융",
    iconId: 1,
    shareNum: 200,
  },
  {
    id: 3,
    title: "Health Tracker Assistant",
    description: "Tracks your daily health metrics and offers tips to improve your well-being (v2.0.1).",
    category: "헬스케어",
    iconId: 1,
    shareNum: 180,
  },
  {
    id: 4,
    title: "E-Commerce Helper",
    description: "Assists in finding the best deals and manages your online shopping lists (v1.0.5).",
    category: "전자 상거래",
    iconId: 1,
    shareNum: 150,
  },
  {
    id: 5,
    title: "Travel Itinerary Planner",
    description: "Helps you create and organize your travel plans with ease (v0.8.7).",
    category: "여행",
    iconId: 1,
    shareNum: 90,
  },
];

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

export default function SelectChatbot({ onNext }: SelectChatbotProps) {
  const [selectedCategory, setSelectedCategory] = useState<string>("모든 챗봇");

  const filteredChatbots = selectedCategory === "모든 챗봇"
    ? chatbots
    : chatbots.filter((bot) => bot.category === selectedCategory);

  const handleCategoryClick = (label: string) => {
    setSelectedCategory(label);
  };
  
  return (
    <div>
      {/* 카테고리 필터 */}
      <div className="flex items-center mb-6">
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

      <div className="grid grid-cols-3 gap-6 mb-8">
        {filteredChatbots.map((chatbot) => (
          <PopularChatbotCard
            key={chatbot.id}
            title={chatbot.title}
            description={chatbot.description}
          />
        ))}
      </div>
    </div>
  );
}

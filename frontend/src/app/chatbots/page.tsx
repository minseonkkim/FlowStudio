"use client";

import { useState, useEffect } from "react";
import { useRecoilState } from "recoil";
import { useQuery, useMutation, useQueryClient } from "@tanstack/react-query";
import { useRouter } from "next/navigation";
import { CgArrowsExchangeAltV } from "@react-icons/all-files/cg/CgArrowsExchangeAltV";

import CreateChatbotModal from "@/components/chatbot/CreateChatbotModal";
import ShareChatbotModal from "@/components/chatbot/ShareChatbotModal";
import PopularChatbotCard from "@/components/chatbot/PopularChatbotCard";
import Search from "@/components/common/Search";
import PurpleButton from "@/components/common/PurpleButton";
import { selectedChatbotState } from "@/store/chatbotAtoms";
import { ChatFlow } from "@/types/chatbot";
import { getAllChatFlows, deleteChatFlow } from "@/api/chatbot";

export default function Page() {
  const [selectedCategory, setSelectedCategory] = useState<string>("모든 챗봇");
  const [searchTerm, setSearchTerm] = useState<string>("");
  const [isCreateModalOpen, setIsCreateModalOpen] = useState(false);
  const [isShareModalOpen, setIsShareModalOpen] = useState(false);
  const [isViewingShared, setIsViewingShared] = useState(false);
  const [selectedChatbot, setSelectedChatbot] = useRecoilState(selectedChatbotState);
  const [itemsToLoad, setItemsToLoad] = useState(20); // Number of items to load
  const router = useRouter();
  const queryClient = useQueryClient();

  const { isLoading, isError, error, data: chatFlows } = useQuery<ChatFlow[]>({
    queryKey: ["chatFlows", isViewingShared],
    queryFn: () => getAllChatFlows(isViewingShared),
  });

  useEffect(() => {
    if (isError && error) {
      alert("챗봇을 불러오는 중 오류가 발생했습니다. 다시 시도해 주세요.");
    }
  }, [isError, error]);

  const deleteMutation = useMutation({
    mutationFn: deleteChatFlow,
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ["chatFlows"] });
    },
    onError: () => {
      alert("챗봇 삭제에 실패했습니다. 다시 시도해 주세요.");
    },
  });

  // Infinite scroll logic
  useEffect(() => {
    const handleScroll = () => {
      if (
        window.innerHeight + document.documentElement.scrollTop >=
        document.documentElement.offsetHeight - 100
      ) {
        setItemsToLoad((prev) => prev + 16); // Load 20 more items
      }
    };

    window.addEventListener("scroll", handleScroll);
    return () => window.removeEventListener("scroll", handleScroll);
  }, []);

  if (isLoading) return <div>is Loading...</div>;

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

  const handleDeleteClick = (chatFlowId: number) => {
    deleteMutation.mutate(chatFlowId);
  };

  const handleSharedClick = () => {
    setIsViewingShared(!isViewingShared);
  };

  const filteredChatFlows = chatFlows
    ? chatFlows.filter((bot) => {
        const matchesCategory =
          selectedCategory === "모든 챗봇" ||
          bot.categories.some((category) => category.name === selectedCategory);
        const matchesSearch = bot.title.toLowerCase().includes(searchTerm.toLowerCase());
        return matchesCategory && matchesSearch;
      })
    : [];

  return (
    <div className="px-4 md:px-12 py-10">
      <div className="flex flex-col">
        <div className="mb-2 flex items-center gap-4">
          <div className="flex flex-row items-center">
            <p className="font-semibold text-[24px] text-gray-700 mr-1">
              {isViewingShared ? "공유된 챗봇" : "나의 챗봇"}
            </p>
            <CgArrowsExchangeAltV
              className="size-6 text-gray-500 cursor-pointer"
              onClick={handleSharedClick}
            />
          </div>
          <PurpleButton text="챗봇 만들기" onHandelButton={handleCreateClick} />
        </div>

        {/* 카테고리 선택 */}
        <div className="flex justify-between items-center mb-6">
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
      </div>

      <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 xl:grid-cols-4 w-full gap-4">
        {filteredChatFlows.reverse().slice(0, itemsToLoad).map((bot) => (
          <PopularChatbotCard
            key={bot.chatFlowId}
            chatbotId={bot.chatFlowId}
            iconId={bot.thumbnail}
            title={bot.title}
            description={bot.description}
            type={isViewingShared ? "shared" : "my"}
            category={bot.categories.map((cat) => cat.name)}
            onCardClick={() => {
              router.push(`/chatbot/${bot.chatFlowId}/workflow`);
            }}
            onButtonUpdateClick={() => handleUpdateClick(bot)}
            onButtonDeleteClick={() => handleDeleteClick(bot.chatFlowId)}
            onButtonShareClick={() => {
              setSelectedChatbot(bot);
              setIsShareModalOpen(true);
            }}
          />
        ))}
      </div>

      {/* 챗봇 생성 및 모달 */}
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
            <ShareChatbotModal
              onClose={() => setIsShareModalOpen(false)}
              chatFlowId={selectedChatbot?.chatFlowId || 0}
            />
          </div>
        </div>
      )}
    </div>
  );
}

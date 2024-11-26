"use client";

import { useState, useEffect, useMemo, useCallback } from "react";
import { useRecoilState, useSetRecoilState } from "recoil";
import { useQueryClient, useInfiniteQuery, useMutation } from "@tanstack/react-query";
import { useRouter } from "next/navigation";
import { CgArrowsExchangeAltV } from "@react-icons/all-files/cg/CgArrowsExchangeAltV";

import CreateChatbotModal from "@/components/chatbot/CreateChatbotModal";
import ShareChatbotModal from "@/components/chatbot/ShareChatbotModal";
import PopularChatbotCard from "@/components/chatbot/PopularChatbotCard";
import Search from "@/components/common/Search";
import PurpleButton from "@/components/common/PurpleButton";
import { selectedChatbotState } from "@/store/chatbotAtoms";
import { ChatFlow } from "@/types/chatbot";
import { deleteChatFlow, getAllChatFlows } from "@/api/chatbot";
import Loading from "@/components/common/Loading";
import { chatbotThumbnailState } from "@/store/chatbotAtoms";
import { debounce } from "@/utils/node";
import { categories } from "@/constants/chatbotCategories";

export default function Page() {
  const [selectedCategory, setSelectedCategory] = useState<string>("모든 챗봇");
  const [searchTerm, setSearchTerm] = useState<string>("");
  const [isCreateModalOpen, setIsCreateModalOpen] = useState(false);
  const [isShareModalOpen, setIsShareModalOpen] = useState(false);
  const [isViewingShared, setIsViewingShared] = useState(false);
  const [selectedChatbot, setSelectedChatbot] = useRecoilState(selectedChatbotState);
  const [isCategoryFixed, setIsCategoryFixed] = useState(false);
  const [isDeleteModalOpen, setIsDeleteModalOpen] = useState(false);
  const [chatFlowIdToDelete, setChatFlowIdToDelete] = useState<number | null>(null);
  const [chatFlowTitleToDelete, setChatFlowTitleToDelete] = useState<string>("");

  const router = useRouter();
  const queryClient = useQueryClient();

  const setChatbotThumbnail = useSetRecoilState(chatbotThumbnailState);

  const {
    data,
    isLoading,
    isFetchingNextPage,
    fetchNextPage,
    hasNextPage,
    isError,
    error,
  } = useInfiniteQuery<ChatFlow[]>({
    queryKey: ["chatFlows", isViewingShared],
    queryFn: ({ pageParam = 0 }) => getAllChatFlows(isViewingShared, pageParam.toString(), "20"), // 페이지 번호와 limit 설정
    getNextPageParam: (lastPage, allPages) => {
      // 마지막 페이지에 데이터가 있으면 다음 페이지 번호를 반환하고, 없으면 undefined 반환
      return lastPage.length > 0 ? allPages.length : undefined;
    },
    initialPageParam: 0, // 초기 페이지 파라미터 설정
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

  useEffect(() => {
    const handleScroll = debounce(() => {
      if (
        window.innerHeight + document.documentElement.scrollTop >=
        document.documentElement.offsetHeight - 100
      ) {
        if (hasNextPage && !isFetchingNextPage) {
          fetchNextPage(); // 무한 스크롤을 위한 다음 페이지 데이터 로드
        }
      }

      if (window.scrollY >= 57) {
        setIsCategoryFixed(true);
      } else {
        setIsCategoryFixed(false);
      }
    }, 100);

    window.addEventListener("scroll", handleScroll);
    return () => window.removeEventListener("scroll", handleScroll);
  }, [hasNextPage, isFetchingNextPage, fetchNextPage]);

  const handleCategoryClick = useCallback((label: string) => {
    setSelectedCategory(label);
  }, []);

  const filteredChatFlows = useMemo(() => {
    if (!data) return [];
    const allChatFlows = data.pages.flat();
    return allChatFlows.filter((bot) => {
      const matchesCategory =
        selectedCategory === "모든 챗봇" ||
        bot.categories.some((category) => category.name === selectedCategory);
      const matchesSearch = bot.title.toLowerCase().includes(searchTerm.toLowerCase());
      return matchesCategory && matchesSearch;
    });
  }, [data, selectedCategory, searchTerm]);

  if (isLoading) return <Loading />;

  const handleCreateClick = () => {
    setSelectedChatbot(null);
    setIsCreateModalOpen(true);
  };

  const handleUpdateClick = (chatFlow: ChatFlow) => {
    setSelectedChatbot(chatFlow);
    setIsCreateModalOpen(true);
  };

  const handleDeleteClick = (chatFlowId: number, chatFlowTitle: string) => {
    setChatFlowIdToDelete(chatFlowId);
    setChatFlowTitleToDelete(chatFlowTitle);
    setIsDeleteModalOpen(true);
  };

  const confirmDelete = () => {
    if (chatFlowIdToDelete !== null) {
      deleteMutation.mutate(chatFlowIdToDelete);
      setIsDeleteModalOpen(false);
    }
  };

  const cancelDelete = () => {
    setIsDeleteModalOpen(false);
  };

  const handleSharedClick = () => {
    setIsViewingShared(!isViewingShared);
  };

  const ConfirmDeleteModal = ({
    onConfirm,
    onCancel,
  }: {
    onConfirm: () => void;
    onCancel: () => void;
  }) => (
    <div
      className="z-30 fixed inset-0 flex items-center justify-center bg-black bg-opacity-50"
      onClick={onCancel}
    >
      <div className="bg-white p-6 rounded-lg shadow-md w-96" onClick={(e) => e.stopPropagation()}>
        <p className="mb-6">
          <b>{chatFlowTitleToDelete}</b>을(를) 삭제하시겠습니까? <br />
          이 작업은 되돌릴 수 없습니다.
        </p>
        <div className="flex justify-end gap-4">
          <button className="px-4 py-2 bg-gray-200 rounded hover:bg-gray-300" onClick={onCancel}>
            취소
          </button>
          <button
            className="px-4 py-2 bg-red-500 text-white bg-[#874aa5] rounded hover:bg-red-700"
            onClick={onConfirm}
          >
            삭제
          </button>
        </div>
      </div>
    </div>
  );

  return (
    <div className="px-4 md:px-12 py-8">
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

        <div
          className={`flex justify-between items-center mb-6 ${isCategoryFixed ? "fixed top-[57px] left-0 right-0 bg-white z-10 px-4 md:px-12 py-2" : ""}`}
        >
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
            chatbotId={bot.chatFlowId}
            iconId={bot.thumbnail}
            title={bot.title}
            description={bot.description}
            type={isViewingShared ? "shared" : "my"}
            category={bot.categories.map((cat) => cat.name)}
            onCardClick={() => {
              setChatbotThumbnail(bot.thumbnail);
              router.push(`/chatbot/${bot.chatFlowId}/chatflow`);
            }}
            onButtonUpdateClick={() => handleUpdateClick(bot)}
            onButtonDeleteClick={() => handleDeleteClick(bot.chatFlowId, bot.title)}
            onButtonShareClick={() => {
              setSelectedChatbot(bot);
              setIsShareModalOpen(true);
            }}
          />
        ))}
      </div>

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

      {isDeleteModalOpen && <ConfirmDeleteModal onConfirm={confirmDelete} onCancel={cancelDelete} />}
      
      {/* 무한 스크롤 로딩 표시 */}
      {/* {isFetchingNextPage && (
        <div className="text-center py-4">로딩 중...</div>
      )} */}
    </div>
  );
}

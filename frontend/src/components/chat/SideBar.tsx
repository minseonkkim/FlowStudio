import React, { useEffect, useState, useRef } from "react";
import { getChattingList, deleteChatting } from '@/api/chat';
import { useInfiniteQuery, useMutation, useQueryClient } from '@tanstack/react-query';
import { CgTrash } from '@react-icons/all-files/cg/CgTrash';
import Image from 'next/image';
import one from '../../../public/chatbot-icon/1.jpg';
import two from '../../../public/chatbot-icon/2.jpg';
import three from '../../../public/chatbot-icon/3.jpg';
import four from '../../../public/chatbot-icon/4.jpg';
import five from '../../../public/chatbot-icon/5.jpg';
import six from '../../../public/chatbot-icon/6.jpg';

type SidebarProps = {
  onNewChat: () => void;
  chatFlowId: string;
  onSelectChat: (chatId: number) => void;
  onDeleteNewChat: () => void;
  selectedChatId: number | null;
};

export default function Sidebar({ onNewChat, chatFlowId, onSelectChat, onDeleteNewChat, selectedChatId}: SidebarProps) {
  const queryClient = useQueryClient();
  const [localSelectedChatId, setLocalSelectedChatId] = useState<number | null>(null); 
  const observerTarget = useRef(null);
  const limitCnt = 10;

  useEffect(() => {
    setLocalSelectedChatId(selectedChatId);
  }, [selectedChatId]);

  const {
    data: chatlist,
    error,
    isError,
    fetchNextPage,
    hasNextPage,
    isFetchingNextPage,
  } = useInfiniteQuery({
    queryKey: ["chatlist", chatFlowId], 
    queryFn: ({ pageParam = 0 }) =>
      getChattingList({
        chatFlowId,
        page: pageParam.toString(), 
        limit: '5',
      }),
    getNextPageParam: (lastPage: { totalCount: number }, allPages: any[]) => {
      const totalPages = Math.ceil(lastPage.totalCount / limitCnt); // 총 페이지 수 계산
      const currentPage = allPages.length - 1; // 현재 불러온 페이지 수
      return currentPage < totalPages ? currentPage + 1 : undefined; // 다음 페이지 번호 반환
    },
    initialPageParam: 0,
  });
  

  useEffect(() => {
    const observer = new IntersectionObserver(
      (entries) => {
        if (entries[0].isIntersecting && hasNextPage && !isFetchingNextPage) {
          fetchNextPage();
        }
      },
      {threshold: 0.7 }
    );

    if (observerTarget.current) {
      observer.observe(observerTarget.current);
    }

    return () => {
      if (observerTarget.current) {
        observer.unobserve(observerTarget.current);
      }
    };
  }, [observerTarget, hasNextPage, isFetchingNextPage, fetchNextPage]);

  useEffect(() => {
    if (isError && error) {
      alert("채팅목록을 불러오는 중 오류가 발생했습니다. 다시 시도해 주세요.");
    }
  }, [isError, error]);

  useEffect(() => {
    if (isError && error) {
      alert("채팅목록을 불러오는 중 오류가 발생했습니다. 다시 시도해 주세요.");
    }
  }, [isError, error]);

  const deleteMutation = useMutation({
    mutationFn: (chatId: string) => deleteChatting(chatFlowId, chatId),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ['chatlist', chatFlowId] });
      setLocalSelectedChatId(null); 
      onDeleteNewChat();
    },
    onError: () => {
      alert("채팅 삭제에 실패했습니다. 다시 시도해 주세요.");
    },
  });

  const handleDeleteClick = (chatId: string) => {
    deleteMutation.mutate(chatId);
  };

  const handleChatClick = (chatId: number) => {
    setLocalSelectedChatId(chatId);
    onSelectChat(chatId);
  };

  const thumbnailImages: { [key: number]: string } = {
    1: one.src,
    2: two.src,
    3: three.src,
    4: four.src,
    5: five.src,
    6: six.src,
  };

  return (
    <div className="w-64 p-6 border-r bg-white h-screen flex flex-col">
      <div className="flex items-center mb-8">
      {chatlist?.pages[0]?.thumbnail &&
          thumbnailImages[Number(chatlist.pages[0].thumbnail)] && (
            <Image
              src={thumbnailImages[Number(chatlist.pages[0].thumbnail)]}
              width={50}
              height={50}
              alt={`Thumbnail ${chatlist.pages[0].thumbnail}`}
              className="rounded-lg mr-4"
            />
          )}
        <div className="text-lg font-semibold break-words max-w-[150px]">
          {chatlist?.pages[0]?.title}
        </div>
      </div>
      
      <button
        onClick={() => {
          onNewChat();
          setLocalSelectedChatId(null); 
        }}
        className="mb-4 w-full py-2 px-4 border border-gray-300 rounded-xl text-gray-700 hover:bg-gray-100"
      >
        새 채팅
      </button>
      
      <div className="flex-grow overflow-y-auto pr-2 scrollbar-thin scrollbar-thumb-gray-400 scrollbar-track-gray-100 space-y-4">
      {chatlist?.pages
          .flatMap((page) => page.chats)
          .filter((chat) => chat.title !== null)
          .map((chat) => (
            <div
              key={chat.id}
              className={`cursor-pointer p-2 border rounded-lg shadow-sm flex justify-between items-center hover:bg-[#E1D5F2] group ${
                localSelectedChatId === chat.id ? "bg-[#E1D5F2]" : "bg-gray-50"
              }`}
              onClick={() => handleChatClick(chat.id)}
            >
              <div className="text-gray-700">{chat.title}</div>
              <CgTrash
                className="text-gray-500 opacity-0 group-hover:opacity-100 transition-opacity"
                onClick={(e) => {
                  e.stopPropagation();
                  handleDeleteClick(String(chat.id));
                }}
              />
            </div>
          ))}
        {hasNextPage && <div ref={observerTarget} className="h-20"></div>}
      </div>
    </div>
  );
}

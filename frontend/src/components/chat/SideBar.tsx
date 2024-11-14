import React, { useEffect } from "react";
import { getChattingList, deleteChatting } from '@/api/chat';
import { useQuery, useMutation, useQueryClient } from '@tanstack/react-query';
import { getChatListData } from '@/types/chat';
import { CgTrash } from '@react-icons/all-files/cg/CgTrash';
import Image, { StaticImageData } from 'next/image';
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
};

export default function Sidebar({ onNewChat, chatFlowId, onSelectChat }: SidebarProps) {
  const queryClient = useQueryClient();

  const { isError, error, data: chatlist } = useQuery<getChatListData>({
    queryKey: ['chatlist', chatFlowId],
    queryFn: () => getChattingList(chatFlowId), 
  });

  useEffect(() => {
    if (isError && error) {
      alert("채팅목록을 불러오는 중 오류가 발생했습니다. 다시 시도해 주세요.");
    }
  }, [isError, error]);

  const deleteMutation = useMutation({
    mutationFn: (chatId: string) => deleteChatting(chatFlowId, chatId),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ['chatlist', chatFlowId] });
    },
    onError: () => {
      alert("채팅 삭제에 실패했습니다. 다시 시도해 주세요.");
    },
  });
  
  const handleDeleteClick = (chatId: string) => {
    deleteMutation.mutate(chatId);
  };

  const handleChatClick = (chatId: number) => {
    onSelectChat(chatId);
  }

  const thumbnailImages: { [key: number]: StaticImageData } = {
    1: one,
    2: two,
    3: three,
    4: four,
    5: five,
    6: six,
  };

  return (
    <div className="w-64 p-6 border-r bg-white h-screen flex flex-col">
      <div className="flex items-center mb-8">
        {chatlist?.thumbnail && thumbnailImages[chatlist.thumbnail] && (
          <Image 
            src={thumbnailImages[chatlist.thumbnail]} 
            alt={`Thumbnail ${chatlist.thumbnail}`} 
            className="rounded-lg w-10 h-10 mr-4" 
          />
        )}
        <div className="text-lg font-semibold break-words max-w-[150px]">{chatlist?.title}</div>
      </div>
      
      <button
        onClick={onNewChat}
        className="mb-4 w-full py-2 px-4 border border-gray-300 rounded-xl text-gray-700 hover:bg-gray-100"
      >
        새 채팅
      </button>
      
      <div className="flex-grow overflow-y-auto pr-2 scrollbar-thin scrollbar-thumb-gray-400 scrollbar-track-gray-100 space-y-4">
        {chatlist?.chats?.map((chat) => (
          <div 
            key={chat.id} 
            className="p-2 border rounded-lg shadow-sm bg-gray-50 flex justify-between items-center hover:bg-[#E1D5F2] group"
            onClick={() => handleChatClick(chat.id)}
          >
            <div className="text-gray-700">{chat.title}</div>
            <CgTrash  
              className="text-gray-500 opacity-0 group-hover:opacity-100 transition-opacity"
              onClick={() => handleDeleteClick(String(chat.id))}
            />
          </div>
        ))}
      </div>
    </div>
  );
}

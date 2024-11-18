import { BsDownload } from "@react-icons/all-files/bs/BsDownload";
import Image from "next/image";
import { useState } from "react";
import { useMutation, useQueryClient } from "@tanstack/react-query";
import { postDownloadChatFlow } from "@/api/share";
import { useRouter } from 'next/navigation';

interface ChatbotCardProps {
  chatbotId: number;
  title: string;
  description: string;
  category: string[];
  iconId: string;
  authorNickName?: string;
  authorProfile?: string;
  shareNum?: number;
  onCardClick?: () => void;
  type: "all" | "eval";
}

export default function ChatbotCard({
  chatbotId,
  title,
  description,
  category,
  iconId,
  authorNickName,
  authorProfile,
  shareNum,
  onCardClick,
  type,
}: ChatbotCardProps) {
  const [isModalOpen, setIsModalOpen] = useState(false);
  const queryClient = useQueryClient();
  const router = useRouter();

  const downloadChatFlowMutation = useMutation({
    mutationFn: postDownloadChatFlow,
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ['chatFlows'] });
    },
    onError: () => {
      alert('챗봇 다운로드에 실패했습니다. 다시 시도해 주세요.');
    },
  });

  const handleDownloadClick = () => {
    setIsModalOpen(true);
  };

  const handleConfirmDownload = () => {
    downloadChatFlowMutation.mutate(chatbotId); 
    router.push('/chatbots');
    setIsModalOpen(false);
  };

  const handleCloseModal = () => {
    setIsModalOpen(false);
  };

  return (
    <div
      onClick={onCardClick}
      className="mb-4 flex items-center justify-between w-full py-3.5 px-6 rounded-xl border-2 group hover:border-[#9A75BF] hover:bg-[#B99AD9] hover:bg-opacity-10 cursor-pointer"
    >
      <div className="flex items-center w-full">
        <Image
          src={`/chatbot-icon/${iconId}.jpg`}
          alt="Selected Icon"
          width={51}
          height={51}
          className="mr-6 rounded-lg border border-gray-300"
        />
        <div className="flex-1 flex-col">
          <div className="flex flex-row justify-between items-center mb-1">
            <p className="mb-1 text-[16px] text-[#1D2939]">{title}</p>
            { type === "all" &&
              <p className="text-[12px] text-gray-400 flex flex-row items-center">
                made by&nbsp;<Image src={authorProfile} width={19} height={19} alt="author profile" className="rounded-full"/>&nbsp;<span className="text-[#242426] font-semibold text-[13px]">{authorNickName}</span>&nbsp;&nbsp;|&nbsp;&nbsp;<span className="text-[#242426] font-semibold text-[13px]">{shareNum}</span>번 공유됨
              </p>
            }
            
            
          </div>

          <div className="flex justify-between items-center">
            <p className="flex-grow text-[14px] text-[#667085] line-clamp-1 overflow-hidden">{description}</p>
            <div className="w-[46%] flex gap-1 flex-row">
              <div className="w-[98%] line-clamp-1 overflow-hidden text-end">
                {category.map((cat) => (
                  <span key={cat} className="text-[13px] p-2 py-1 text-[#667085]">
                    # {cat}
                  </span>
                ))}
              </div>
              
              {type === "all" && (
                <button onClick={handleDownloadClick}>
                  <BsDownload
                    size={18}
                    className="ml-6 text-[#667085] group-hover:scale-125 group-hover:text-[#9A75BF]"
                  />
                </button>
              )}
            </div>
          </div>
        </div>
      </div>

      {/* Modal */}
      {isModalOpen && (
        <div className="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center z-50">
          <div className="bg-white p-6 rounded-lg shadow-lg">
            <p className="mb-4 text-lg">나의 챗봇에 <b>{title}</b>을(를) 추가하시겠습니까?</p>
            <div className="flex justify-end gap-4">
              <button
                className="px-4 py-2 bg-gray-200 rounded hover:bg-gray-300"
                onClick={handleCloseModal}
              >
                취소
              </button>
              <button
                className="px-4 py-2 bg-[#874aa5] text-white rounded hover:bg-[#6e3a85]"
                onClick={handleConfirmDownload}
              >
                확인
              </button>
            </div>
          </div>
        </div>
      )}
    </div>
  );
}

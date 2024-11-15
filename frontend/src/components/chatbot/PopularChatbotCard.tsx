import { BsDownload } from "@react-icons/all-files/bs/BsDownload";
import { BsThreeDots } from "@react-icons/all-files/bs/BsThreeDots";
import { useState, useEffect, useRef } from "react";
import Image from "next/image";
import { useMutation, useQueryClient } from "@tanstack/react-query";
import { postDownloadChatFlow } from "@/api/share";
import { useRouter } from 'next/navigation';

interface PopularChatbotCardProps {
  chatbotId: number;
  title: string;
  description: string;
  category: string[];
  iconId: string;
  type?: "my" | "all" | "eval" | "shared";
  onCardClick?: () => void;
  onButtonUpdateClick?: () => void;
  onButtonDeleteClick?: () => void;
  onButtonShareClick?: () => void;
}

export default function PopularChatbotCard({
  chatbotId,
  title,
  description,
  category,
  iconId,
  type,
  onCardClick,
  onButtonUpdateClick,
  onButtonDeleteClick,
  onButtonShareClick,
}: PopularChatbotCardProps) {
  const [isDropdownOpen, setIsDropdownOpen] = useState(false);
  const [isModalOpen, setIsModalOpen] = useState(false);
  const dropdownRef = useRef<HTMLDivElement>(null);

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

  useEffect(() => {
    const handleClickOutside = (event: MouseEvent) => {
      if (dropdownRef.current && !dropdownRef.current.contains(event.target as Node)) {
        setIsDropdownOpen(false);
      }
    };

    document.addEventListener("mousedown", handleClickOutside);

    return () => {
      document.removeEventListener("mousedown", handleClickOutside);
    };
  }, []);

  return (
    <div
      onClick={onCardClick}
      className={`w-full h-[194px] px-6 py-5 rounded-xl border-2 cursor-pointer hover:border-[#9A75BF] hover:bg-[#B99AD9] hover:bg-opacity-10 ${
        type === "all" ? "group" : ""
      }`}
    >
      <div className="mb-3 flex items-center gap-4">
        <Image
          src={`/chatbot-icon/${iconId}.jpg`}
          alt="Selected Icon"
          width={40}
          height={40}
          className="rounded-lg border border-gray-300"
        />
        <p className="text-[16px] line-clamp-1 overflow-hidden">{title}</p>
      </div>

      <div className="flex flex-col h-[108px] justify-between">
        <p className="text-[14px] text-[#667085] line-clamp-3 overflow-hidden">{description}</p>


        <div className="flex justify-between items-center h-[40px]">
          <div className="line-clamp-1 overflow-hidden">
            {category.map((cat) => (
              <span key={cat} className="text-[13px] pr-2 text-[#667085]">
                # {cat}
              </span>
            ))}
          </div>

          {type === "all" && (
            <div className="flex items-center p-2" onClick={handleDownloadClick}>
              <button>
                <BsDownload
                  size={18}
                  className="text-[#667085] group-hover:scale-125 group-hover:text-[#9A75BF]"
                />
              </button>
            </div>
          )}
          {type === "my" && (
            <div
              ref={dropdownRef}
              onClick={(e) => {
                e.stopPropagation();
                setIsDropdownOpen(!isDropdownOpen);
              }}
              className="relative flex items-center p-2 rounded-lg hover:bg-[#B99AD9] hover:bg-opacity-40"
            >
              <button>
                <BsThreeDots size={18} className="text-[#667085]" />
              </button>

              {/* Dropdown menu */}
              {isDropdownOpen && (
                <div className="absolute right-0 top-10 w-40 bg-white shadow-lg rounded-lg border border-gray-200 z-10">
                  <ul className="text-sm text-gray-700">
                    <li
                      className="px-4 py-2 hover:bg-gray-100 cursor-pointer"
                      onClick={() => {
                        if (onButtonUpdateClick) {
                          onButtonUpdateClick();
                        }
                        setIsDropdownOpen(false);
                      }}
                    >
                      챗봇 수정
                    </li>
                    <li
                      className="px-4 py-2 hover:bg-gray-100 cursor-pointer"
                      onClick={() => {
                        if (onButtonDeleteClick) {
                          onButtonDeleteClick();
                        }
                        setIsDropdownOpen(false);
                      }}
                    >
                      챗봇 삭제
                    </li>
                    <li
                      className="px-4 py-2 hover:bg-gray-100 cursor-pointer"
                      onClick={() => {
                        if (onButtonShareClick) {
                          onButtonShareClick();
                        }
                        setIsDropdownOpen(false);
                      }}
                    >
                      챗봇 공유
                    </li>
                  </ul>
                </div>
              )}
            </div>
          )}
        </div>
      </div>

      {/* Modal */}
      {isModalOpen && (
        <div className="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center z-50">
          <div className="bg-white p-6 rounded-lg shadow-lg">
            <p className="mb-4 text-[17px]">나의 챗봇에 {title} 챗봇을 추가하시겠습니까?</p>
            <div className="flex justify-end gap-4">
              <button
                className="px-4 py-2 bg-gray-200 rounded hover:bg-gray-300"
                onClick={handleCloseModal}
              >
                취소
              </button>
              <button
                className="px-4 py-2 bg-purple-600 text-white rounded hover:bg-purple-700"
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

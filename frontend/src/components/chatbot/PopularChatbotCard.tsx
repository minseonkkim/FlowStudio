import { FiShare } from "@react-icons/all-files/fi/FiShare";
import { BsThreeDots } from "@react-icons/all-files/bs/BsThreeDots";
import { useState } from "react";

interface PopularChatbotCardProps {
  title: string;
  description: string;
  category: string[];
  type?: "my" | "all";
  onCardClick?: () => void;
  onButtonUpdateClick?: () => void;
  onButtonShareClick?: () => void;
}

export default function PopularChatbotCard({
  title,
  description,
  category,
  type,
  onCardClick,
  onButtonUpdateClick,
  onButtonShareClick,
}: PopularChatbotCardProps) {
  const [isDropdownOpen, setIsDropdownOpen] = useState(false);

  return (
    <div
      onClick={onCardClick}
      className={`w-full h-[190px] px-6 py-4 rounded-xl border-2 cursor-pointer hover:border-[#9A75BF] hover:bg-[#B99AD9] hover:bg-opacity-10 ${
        type === "all" ? "group" : ""
      }`}
    >
      <div className="mb-3 flex items-center gap-2">
        <div className="mr-2 w-[36px] h-[36px] rounded-md bg-gray-200"></div>
        <p className="text-[16px]">{title}</p>
      </div>

      <div className="flex flex-col h-[108px] justify-between">
        <p className="text-[14px] text-[#667085]">{description}</p>

        <div className="flex justify-between items-center">
          <div className="flex gap-1 flex-wrap">
            {category.map((cat) => (
              <span key={cat} className="text-[13px] pr-2 text-[#667085]">
                # {cat}
              </span>
            ))}
          </div>

          {type === "all" && (
            <div className="flex items-center p-2">
              <button>
                <FiShare
                  size={18}
                  className="text-[#667085] group-hover:scale-125 group-hover:text-[#9A75BF]"
                />
              </button>
            </div>
          )}
          {type === "my" && (
            <div
              onClick={(e) => {
                e.stopPropagation();
                setIsDropdownOpen(!isDropdownOpen);
              }}
              className="relative flex items-center p-2 rounded-lg hover:bg-[#B99AD9] hover:bg-opacity-40"
            >
              <button>
                <BsThreeDots
                  size={18}
                  className="text-[#667085] group-hover:scale-125 group-hover:text-[#9A75BF]"
                />
              </button>

              {/* 드롭다운 메뉴 */}
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
    </div>
  );
}

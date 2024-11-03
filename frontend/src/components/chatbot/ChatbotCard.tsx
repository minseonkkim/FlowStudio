import { FiShare } from "@react-icons/all-files/fi/FiShare";
import Image from "next/image";

interface ChatbotCardProps {
  title: string;
  description: string;
  category: string[];
  iconId: number;
  onCardClick?: () => void;
  type: "all" | "eval"
}

export default function ChatbotCard({
  title,
  description,
  category,
  iconId,
  onCardClick,
  type,
}: ChatbotCardProps) {
  return (
    <div
      onClick={onCardClick}
      className="mb-4 flex items-center justify-between w-full py-4 px-6 rounded-xl border-2 group hover:border-[#9A75BF] hover:bg-[#B99AD9] hover:bg-opacity-10 cursor-pointer"
    >
      <div className="flex items-center w-full">
        <Image
          src={`/chatbot-icon/${iconId}.jpg`}
          alt="Selected Icon"
          width={48}
          height={48}
          className="mr-6 rounded-lg border border-gray-300"
        />
        <div className="flex-1 flex-col">
          <p className="mb-1 text-[16px] text-[#1D2939]">{title}</p>

          <div className="flex justify-between items-center">
            <p className="text-[14px] text-[#667085]">{description}</p>
            <div className="flex gap-1 flex-wrap">
              {category.map((cat) => (
                <span
                  key={cat}
                  className="text-[13px] p;-2 py-1 text-[#667085]"
                >
                  # {cat}
                </span>
              ))}
              {type === "all" && (
                <button>
                  <FiShare
                    size={18}
                    className="ml-6 text-[#667085] group-hover:scale-125 group-hover:text-[#9A75BF]"
                  />
                </button>
              )}
            </div>
          </div>
        </div>
      </div>
    </div>
  );
}

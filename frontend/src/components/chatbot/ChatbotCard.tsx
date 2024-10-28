import { FiShare } from '@react-icons/all-files/fi/FiShare';

interface ChatbotCardProps {
  title: string;
  description: string;
  buttonText: string;
  category: string[];
  onButtonClick: () => void;
}

export default function ChatbotCard({
  title,
  description,
  buttonText,
  category,
  onButtonClick,
}: ChatbotCardProps) {
  return (
    <div className="mb-4 flex items-center justify-between w-full py-4 px-6 rounded-xl border-2 group hover:border-[#9A75BF] hover:bg-[#9A75BF] hover:bg-opacity-5 hover:border-opacity-70 cursor-pointer">
      <div className="flex w-full">
        <div className="mr-6 w-[48px] h-[48px] rounded-lg bg-gray-200"></div>
        <div className="flex-1 flex-col">
    
          <p className="mb-1 text-[16px] text-[#1D2939]">{title}</p>
 
      
          <div className="flex justify-between items-center">
          <p className="text-[14px] text-[#667085]">{description}</p>
          <div className="flex gap-1 flex-wrap">
            {category.map((cat) => (
              <span key={cat} className="text-[13px] p;-2 py-1 text-[#667085]">
                # {cat}
              </span>
              ))}
            {onButtonClick && (
            <button
              onClick={onButtonClick}
            >
              <FiShare size={18} className="ml-4 text-[#667085] group-hover:scale-110 group-hover:text-[#9A75BF]" />
            </button>
          )}
          </div>
          </div>
        </div>
      </div>
    </div>
  );
}

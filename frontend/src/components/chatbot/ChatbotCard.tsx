interface ChatbotCardProps {
  title: string;
  description: string;
  buttonText: string;
  onButtonClick: () => void;
}

export default function ChatbotCard({
  title,
  description,
  buttonText,
  onButtonClick,
}: ChatbotCardProps) {
  return (
    <div className="mb-4 flex items-center justify-between w-full py-4 px-6 rounded-xl border-2 hover:border-[#9A75BF] hover:bg-[#9A75BF] hover:bg-opacity-5 cursor-pointer">
      <div className="flex">
        <div className="mr-6 w-[48px] h-[48px] rounded-lg bg-gray-200"></div>
        <div className="flex flex-col">
          <p className="text-[16px] text-[#1D2939] font-semibold mb-1">{title}</p>
          <p className="text-[14px] text-[#667085]">{description}</p>
        </div>
      </div>
      <button
        onClick={onButtonClick}
        className="py-3 px-8 bg-[#9A75BF] text-white text-[14px] rounded-lg"
      >
        {buttonText}
      </button>
    </div>
  );
}

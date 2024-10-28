interface PopularChatbotCardProps {
  title: string;
  description: string;
  buttonText?: string;
  onButtonClick?: () => void;
}

export default function PopularChatbotCard({
  title,
  description,
  buttonText,
  onButtonClick,
}: PopularChatbotCardProps) {
  return (
    <div className="w-full h-[200px] px-6 py-4 rounded-xl border-2 cursor-pointer hover:border-[#9A75BF] hover:bg-[#9A75BF] hover:bg-opacity-5">
      <div className="mb-3 flex items-center gap-2">
        <div className="mr-2 w-[36px] h-[36px] rounded-md bg-gray-200"></div>
        <p className="text-[16px] text-[#1D2939] font-semibold">{title}</p>
      </div>
      <div className="flex flex-col h-[118px] justify-between">
        <p className="text-[14px] text-[#667085]">{description}</p>

        {buttonText && onButtonClick && (
          <button
            onClick={onButtonClick}
            className="flex items-center justify-center p-2 bg-[#9A75BF] text-white text-[14px] rounded-lg"
          >
            {buttonText}
          </button>
        )}
      </div>
    </div>
  );
}

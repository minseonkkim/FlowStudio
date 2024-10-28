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
    <div className="mb-4 flex items-center justify-between w-full py-4 px-6 rounded-xl shadow-[0px_2px_8px_rgba(0,0,0,0.25)] cursor-pointer">
      <div className="flex">
        <div className="mr-6 w-[48px] h-[48px] rounded-lg bg-gray-200"></div>
        <div className="flex flex-col">
          <p className="text-[18px] mb-1">{title}</p>
          <p className="text-[#333333]">{description}</p>
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

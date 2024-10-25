export default function ChatbotCard() {
  return (
    <div className="mb-4 flex items-center justify-between w-full py-4 px-6 rounded-xl shadow-[0px_2px_8px_rgba(0,0,0,0.25)] cursor-pointer">
      <div className="flex">
        <div className="mr-6 w-[48px] h-[48px] rounded-lg bg-gray-200"></div>
        <div className="flex flex-col">
          <p className="text-[18px] mb-1">Workflow Planning Assistant</p>
          <p className="text-[#333333]">
            An assistant that helps you plan and select the right node for a
            workflow (V0.6.0).
          </p>
        </div>
      </div>
      <button className="py-3 px-8 bg-[#9A75BF] text-white text-[14px] rounded-lg">
        작업 공간에 추가
      </button>
    </div>
  );
}

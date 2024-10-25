export default function PopularChatbotCard() {
  return (
    <div className="w-full h-[204px] px-6 py-4 rounded-xl shadow-[0px_2px_8px_rgba(0,0,0,0.25)] group cursor-pointer">
      <div className="mb-3 flex items-center gap-2">
        <div className="mr-2 w-[36px] h-[36px] rounded-md bg-gray-200"></div>
        <p className="text-[18px]">Workflow Planning Assistant</p>
      </div>
      <div className="flex flex-col h-[122px] justify-between">
        <p className='text-[#333333]'>
          An assistant that helps you plan and select the right node for a
          workflow (V0.6.0).
        </p>
        <button className="flex items-center justify-center p-2 bg-[#9A75BF] text-white text-[14px] rounded-lg opacity-0 group-hover:opacity-100 transition-opacity duration-300">
          작업 공간에 추가
        </button>
      </div>
    </div>
  );
}
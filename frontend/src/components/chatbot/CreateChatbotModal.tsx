export default function CreateChatbotModal() {
  return (
    <div className="flex flex-col bg-white w-[500px] h-[400px] p-8 rounded-xl">
      <p className="mb-4 text-[22px]">챗봇 만들기</p>
      <div className="flex flex-col gap-4">
        <div className="flex flex-col">
          <p className="mb-2">카테고리 선택</p>
          <div></div>
        </div>

        <div className="flex flex-col">
          <p className="mb-2">앱 아이콘과 이름</p>
          <div className="flex w-full">
            <div className="mr-6 w-[40px] h-[40px] rounded-lg bg-gray-200"></div>
            <input type="text" className="flex-1 px-2 border rounded-md" />
          </div>
        </div>

        <div className="flex flex-col">
          <p className="mb-2">설명</p>
          <input type="text" className="border h-[100px] p-1 rounded-md" />
        </div>
      </div>
    </div>
  );
}

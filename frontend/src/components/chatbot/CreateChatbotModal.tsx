import { useState } from "react";
import Image from "next/image";

interface CreateChatbotModalProps {
  onClose: () => void;
}

export default function CreateChatbotModal({ onClose }: CreateChatbotModalProps) {
  const [selectedCategories, setSelectedCategories] = useState<string[]>([]);
  const [selectedIcon, setSelectedIcon] = useState<string>("1");
  const [name, setName] = useState("");
  const [description, setDescription] = useState("");

  const categories = [
    "금융",
    "헬스케어",
    "전자상거래",
    "여행",
    "교육",
    "엔터테인먼트",
    "기타",
  ];
  const icons = ["1", "2", "3", "4", "5", "6"];

  // 카테고리 선택 및 해제
  const toggleCategory = (category: string) => {
    setSelectedCategories((prevCategories) =>
      prevCategories.includes(category)
        ? prevCategories.filter((cat) => cat !== category)
        : [...prevCategories, category]
    );
  };

  const handleCreate = () => {
    // 추가 로직을 추가할 수 있습니다.
    onClose();
  };

  return (
    <div className="flex flex-col bg-white w-[500px] h-[600px] p-8 rounded-xl shadow-lg">
      <p className="mb-4 text-[22px] text-gray-800">챗봇 만들기</p>

      <div className="flex flex-col flex-grow">
        {/* 카테고리 선택 */}
        <div className="flex flex-col mb-4">
          <p className="mb-2 text-gray-700 font-medium">카테고리 선택</p>
          <div className="flex flex-wrap gap-2">
            {categories.map((cat) => (
              <button
                key={cat}
                onClick={() => toggleCategory(cat)}
                className={`text-[12px] px-3 py-1 rounded-full transition-all duration-200 ease-in-out ${
                  selectedCategories.includes(cat)
                    ? "border-2 font-semibold border-[#9A75BF] bg-[#F3E8FF] text-[#9A75BF]"
                    : "border border-gray-300 bg-gray-100 text-gray-700 hover:bg-gray-200"
                }`}
              >
                # {cat}
              </button>
            ))}
          </div>
        </div>

        {/* 아이콘 선택 */}
        <div className="flex flex-col mb-4">
          <p className="mb-2 text-gray-700 font-medium">앱 아이콘 선택</p>
          <div className="flex gap-2">
            {icons.map((icon) => (
              <div
                key={icon}
                onClick={() => setSelectedIcon(icon)}
                style={{
                  backgroundImage: `url(/chatbot-icon/${icon}.jpg)`,
                  backgroundSize: "cover",
                  backgroundPosition: "center",
                }}
                className={`w-[45px] h-[45px] rounded-lg cursor-pointer ${
                  selectedIcon === icon
                    ? "border-2 border-[#9A75BF]"
                    : "border border-gray-300"
                }`}
              />
            ))}
          </div>
        </div>

        {/* 앱 이름 입력 */}
        <div className="flex flex-col mb-4">
          <p className="mb-2 text-gray-700 font-medium">앱 이름</p>
          <div className="flex items-center gap-4">
            <div className="w-[40px] h-[40px] rounded-lg">
              <Image
                src={`/chatbot-icon/${selectedIcon}.jpg`}
                alt="Selected Icon"
                width={40}
                height={40}
                className="rounded-lg"
              />
            </div>
            <input
              type="text"
              value={name}
              onChange={(e) => setName(e.target.value)}
              placeholder="앱 이름을 입력하세요"
              className="flex-1 px-2 py-1 border border-gray-300 rounded-md"
            />
          </div>
        </div>

        {/* 챗봇 설명 */}
        <div className="flex flex-col mb-4">
          <p className="mb-2 text-gray-700 font-medium">설명</p>
          <textarea
            value={description}
            onChange={(e) => setDescription(e.target.value)}
            placeholder="챗봇 설명을 입력하세요"
            className="w-full h-[110px] p-2 border border-gray-300 rounded-md bg-gray-100 resize-none"
          />
        </div>
      </div>

      {/* 취소 및 생성 버튼 */}
      <div className="flex justify-end gap-4">
        <button
          onClick={onClose}
          className="w-[70px] h-[38px] border-2 border-[#9A75BF] text-[#9A75BF] rounded-lg hover:bg-[#f3e8ff] active:bg-[#e3d1f7]"
        >
          취소
        </button>
        <button
          onClick={handleCreate}
          className="w-[70px] h-[38px] bg-[#9A75BF] text-white rounded-lg hover:bg-[#874aa5] active:bg-[#733d8a]"
        >
          생성
        </button>
      </div>
    </div>
  );
}

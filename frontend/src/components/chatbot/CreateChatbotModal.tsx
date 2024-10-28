import { useState } from 'react';

interface CreateChatbotModalProps {
  onClose: () => void;
}

export default function CreateChatbotModal({ onClose }: CreateChatbotModalProps) {
  // const [category, setCategory] = useState("");
  const [selectedCategories, setSelectedCategories] = useState<string[]>([]);
  const [name, setName] = useState("");
  const [description, setDescription] = useState("");

  const categories = ["금융", "헬스케어", "전자상거래", "여행", "교육", "엔터테인먼트", "기타"];

  const toggleCategory = (category: string) => {
    setSelectedCategories((prevCategories) =>
      prevCategories.includes(category)
        ? prevCategories.filter((cat) => cat !== category)
        : [...prevCategories, category]
    );
  };

  const handleCreate = () => {
    onClose(); 
  };

  return (
    <div className="flex flex-col bg-white w-[500px] h-[550px] p-8 rounded-xl shadow-lg">
      <p className="mb-4 text-[22px] font-semibold">챗봇 만들기</p>
      
      <div className="flex flex-col flex-grow">
        <div className="flex flex-col mb-4">
          <p className="mb-2 text-gray-700">카테고리 선택</p>
          <div className="flex flex-wrap gap-2">
            {categories.map((cat) => (
              <button
                key={cat}
                onClick={() => toggleCategory(cat)}
                className={`text-[12px] px-4 py-2 rounded-full transition-all duration-200 ease-in-out ${
                  selectedCategories.includes(cat)
                    ? 'border-2 font-semibold border-[#9A75BF] bg-[#F3E8FF] text-[#9A75BF]'
                    : 'border-2 border-gray-300 bg-gray-100 text-gray-700 hover:bg-gray-200'
                }`}
              >
                {cat}
              </button>
            ))}
          </div>
        </div>

        <div className="flex flex-col mb-4">
          <p className="mb-2 text-gray-700">앱 아이콘과 이름</p>
          <div className="flex items-center gap-4">
            <div className="w-[40px] h-[40px] rounded-lg bg-gray-200"></div>
            <input
              type="text"
              value={name}
              onChange={(e) => setName(e.target.value)}
              placeholder="앱 이름을 입력하세요"
              className="flex-1 px-2 py-1 border border-gray-300 rounded-md"
            />
          </div>
        </div>

        <div className="flex flex-col mb-4">
          <p className="mb-2 text-gray-700">설명</p>
          <textarea
            value={description}
            onChange={(e) => setDescription(e.target.value)}
            placeholder="챗봇 설명을 입력하세요"
            className="w-full h-[110px] p-2 border border-gray-300 rounded-md bg-gray-100 resize-none"
          />
        </div>
      </div>

      <div className="flex justify-end gap-4">
        <button
          onClick={onClose}
          className="w-[80px] h-[40px] border-2 border-[#9A75BF] text-[#9A75BF] rounded-lg"
        >
          취소
        </button>
        <button
          onClick={handleCreate}
          className="w-[80px] h-[40px] bg-[#9A75BF] text-white rounded-lg"
        >
          생성
        </button>
      </div>
    </div>
  );
}

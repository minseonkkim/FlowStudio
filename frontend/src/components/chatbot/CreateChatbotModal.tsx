import { useState, useEffect } from "react";
import Image from "next/image";
import { useRecoilState } from "recoil";
import { selectedChatbotState } from "@/store/chatbotAtoms";
import PurpleButton from "../common/PurpleButton";
import WhiteButton from "../common/whiteButton";
import { postChatFlow, patchChatFlow } from "@/api/chatbot"; 
import { useMutation, useQueryClient } from "@tanstack/react-query";
import { ChatFlowData } from "@/types/chatbot";

interface CreateChatbotModalProps {
  onClose: () => void;
}

export default function CreateChatbotModal({
  onClose,
}: CreateChatbotModalProps) {
  const [selectedChatbot, setSelectedChatbot] =
    useRecoilState(selectedChatbotState);

  const [selectedCategories, setSelectedCategories] = useState<string[]>([]);
  const [selectedIcon, setSelectedIcon] = useState<string>("1");
  const [name, setName] = useState("");
  const [description, setDescription] = useState("");
  const categoryMap: { [key: string]: number } = {
    "금융": 1,
    "헬스케어": 2,
    "전자상거래": 3,
    "여행": 4,
    "교육": 5,
    "엔터테인먼트": 6,
    "기타": 7,
  };
  const categories = Object.keys(categoryMap);
  const icons = ["1", "2", "3", "4", "5", "6"];
  const queryClient = useQueryClient();

  const createMutation = useMutation({
    mutationFn: postChatFlow,
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ["chatFlows"] });
    },
    onError: () => {
      alert("챗봇 생성에 실패했습니다. 다시 시도해 주세요.");
    },
  });

  const updateMutation = useMutation({
    mutationFn: ({ chatFlowId, data }: { chatFlowId: number; data: ChatFlowData }) =>
      patchChatFlow(chatFlowId, data),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ["chatFlows"] });
    },
    onError: () => {
      alert("챗봇 수정에 실패했습니다. 다시 시도해 주세요.");
    },
  });


  const handleCreateOrUpdate = () => {
    const categoryIds = selectedCategories.map(cat => categoryMap[cat]);

    const chatbotData = {
      title: name,
      description,
      categoryIds: categoryIds,
      thumbnail: selectedIcon,
    };

    if (selectedChatbot) {
      updateMutation.mutate({ chatFlowId: selectedChatbot.chatFlowId, data: chatbotData });
      setSelectedChatbot(null);
    } else {
      createMutation.mutate(chatbotData);
    }
    onClose();
  };

  // 수정 모드일 경우 선택된 챗봇 데이터로 초기화
  useEffect(() => {
    if (selectedChatbot) {
      setName(selectedChatbot.title);
      setDescription(selectedChatbot.description);
      setSelectedCategories(selectedChatbot.categories.map(category => category.name));
      setSelectedIcon(String(selectedChatbot.thumbnail));
    }
  }, [selectedChatbot]);

  // 카테고리 선택 및 해제
  const toggleCategory = (category: string) => {
    setSelectedCategories((prevCategories) =>
      prevCategories.includes(category)
        ? prevCategories.filter((cat) => cat !== category)
        : [...prevCategories, category]
    );
  };

  return (
    <div className="flex flex-col bg-white w-[500px] h-[600px] p-8 rounded-xl shadow-lg">
      <p className="mb-4 text-[22px] text-gray-800">
        {selectedChatbot ? "챗봇 수정" : "챗봇 만들기"}
      </p>

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
                    ? "border font-semibold border-[#9A75BF] bg-[#F3E8FF] text-[#9A75BF]"
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
          <p className="mb-2 text-gray-700">앱 아이콘 선택</p>
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
          <p className="mb-2 text-gray-700">앱 이름</p>
          <div className="flex items-center gap-4">
            <div className="w-[40px] h-[40px] rounded-lg">
              <Image
                src={`/chatbot-icon/${selectedIcon}.jpg`}
                alt="Selected Icon"
                width={40}
                height={40}
                className="rounded-lg border border-gray-300"
              />
            </div>
            <input
              type="text"
              value={name}
              onChange={(e) => setName(e.target.value)}
              placeholder="앱 이름을 입력하세요"
              className="flex-1 px-2 py-1 border border-gray-300 rounded-md focus:border-2 focus:border-[#9A75BF] focus:outline-none"
            />
          </div>
        </div>

        {/* 챗봇 설명 */}
        <div className="flex flex-col mb-4">
          <p className="mb-2 text-gray-700">설명</p>
          <textarea
            value={description}
            onChange={(e) => setDescription(e.target.value)}
            placeholder="챗봇 설명을 입력하세요"
            className="w-full h-[110px] p-2 border border-gray-300 rounded-md bg-gray-100 resize-none focus:border-2 focus:border-[#9A75BF] focus:outline-none"
          />
        </div>
      </div>

      {/* 취소 및 생성/수정 버튼 */}
      <div className="flex justify-end gap-4">
        <WhiteButton text='취소' onHandelButton={onClose} />
        <PurpleButton text={selectedChatbot ? "수정" : "생성"} onHandelButton={handleCreateOrUpdate} />
      </div>
    </div>
  );
}

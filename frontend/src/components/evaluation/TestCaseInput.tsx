"use client";

import { useState, useEffect } from "react";
import WhiteButton from "../common/whiteButton";
import PurpleButton from "../common/PurpleButton";
import { useRecoilValue } from "recoil";
import { chatbotIdState } from "@/store/evaluationAtoms";
import { postChatFlowTest } from "@/api/evaluation";
import { useMutation } from "@tanstack/react-query";

interface TestCase {
  testQuestion: string;
  groundTruth: string;
}

interface TestCaseInputProps {
  onNext: () => void;
  onPrevious: () => void;
}



export default function TestCaseInput({
  onNext,
  onPrevious,
}: TestCaseInputProps) {
 
  const chatbotId = useRecoilValue(chatbotIdState);
  useEffect(()=>{
    console.log(chatbotId)
  },[])

  // Mutation 설정
  const createMutation = useMutation({
    mutationFn: ({ chatbotId, data }: { chatbotId: string; data: TestCase[] }) =>
      postChatFlowTest(chatbotId, data),
    onSuccess: (res) => {
      console.log(res)
      //sse연결
      alert("테스트 케이스가 성공적으로 전송되었습니다!");
      onNext(); // 성공 시 다음 단계로 이동
    },
    onError: () => {
      alert("테스트 케이스 전송에 실패했습니다. 다시 시도해주세요.");
    },
  });

  // 테스트케이스 상태 관리
  const [items, setItems] = useState<TestCase[]>([
    { testQuestion: "", groundTruth: "" },
  ]);

  // 테스트케이스 추가
  const addItem = () => {
    setItems([...items, { testQuestion: "", groundTruth: "" }]);
  };

  // 테스트케이스 삭제
  const deleteItem = (index: number) => {
    setItems((prevItems) => prevItems.filter((_, i) => i !== index));
  };

  // 테스트케이스 업데이트
  const updateItem = (
    index: number,
    field: "testQuestion" | "groundTruth",
    value: string
  ) => {
    setItems(
      items.map((item, i) =>
        i === index ? { ...item, [field]: value } : item
      )
    );
  };

  // 테스트케이스 전송
  const handleSubmit = () => {
    if (!chatbotId) {
      alert("챗봇 ID를 가져오지 못했습니다. 다시 시도해주세요.");
      return;
    }

    const data = items.map(({ testQuestion, groundTruth }) => ({
      testQuestion,
      groundTruth,
    }));
    createMutation.mutate({ chatbotId: String(chatbotId), data });
  };

  return (
    <div className="container">
      {/* 테스트케이스 렌더링 */}
      {items.map((item, index) => (
        <div key={index} className="border-2 rounded-xl mb-4">
          <details open className="py-4 px-6">
            <summary className="font-semibold">테스트 케이스 {index + 1}</summary>
            <div className="mt-4">
              <label className="block mb-2">테스트 질문 (Test Question)</label>
              <input
                type="text"
                value={item.testQuestion}
                onChange={(e) =>
                  updateItem(index, "testQuestion", e.target.value)
                }
                className="w-full p-2 border rounded-md bg-gray-100 focus:border-2 focus:border-[#9A75BF] focus:outline-none"
                placeholder="Enter test question"
              />
            </div>
            <div className="mt-4">
              <label className="block mb-2">정답 (Ground Truth)</label>
              <textarea
                value={item.groundTruth}
                onChange={(e) =>
                  updateItem(index, "groundTruth", e.target.value)
                }
                className="w-full p-2 border rounded-md bg-gray-100 focus:border-2 focus:border-[#9A75BF] focus:outline-none"
                placeholder="Enter ground truth"
              />
            </div>
            <div className="flex items-center justify-end">
              <button
                onClick={() => deleteItem(index)}
                className="mt-4 px-4 py-2 bg-[#E1D5F2] text-[#9A75BF] font-semibold rounded-md"
              >
                삭제
              </button>
            </div>
          </details>
        </div>
      ))}

      {/* 버튼 */}
      <div className="flex justify-between items-center mt-4">
        <button
          onClick={addItem}
          className="mt-4 px-4 py-2 bg-[#E1D5F2] text-[#9A75BF] font-semibold rounded-md"
        >
          추가
        </button>

        <div className="flex gap-4">
          <WhiteButton text="이전" onHandelButton={() => onPrevious()} />
          <PurpleButton text="테스트 시작" onHandelButton={handleSubmit} />
        </div>
      </div>
    </div>
  );
}

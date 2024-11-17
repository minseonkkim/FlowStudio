"use client";

import { useState, useEffect } from "react";
import WhiteButton from "../common/whiteButton";
import PurpleButton from "../common/PurpleButton";
import { useRecoilValue, useSetRecoilState } from "recoil";
import { chatbotIdState, parsedTestDataState } from "@/store/evaluationAtoms";
import { postChatFlowTest } from "@/api/evaluation";
import { useMutation } from "@tanstack/react-query";
import Loading from "@/components/common/Loading"; // Loading 컴포넌트 임포트

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
  const [isLoading, setIsLoading] = useState(false);

  const chatbotId = useRecoilValue(chatbotIdState);
  // const BASE_URL = process.env.NEXT_PUBLIC_BASE_URL;
  // const accessToken = localStorage.getItem("accessToken");
  const setParsedTestData = useSetRecoilState(parsedTestDataState);

  useEffect(() => {
    setParsedTestData([]);
  }, []);

  const createMutation = useMutation({
    mutationFn: ({ chatbotId, data }: { chatbotId: string; data: TestCase[] }) =>
      postChatFlowTest(chatbotId, data),
    onSuccess: (res) => {
      setParsedTestData((prev) => {
        const updatedData = [...prev];
        res.forEach((item) => {
          const existingItemIndex = updatedData.findIndex(
            (data) => data.chatId === item.chatId
          );

          if (existingItemIndex !== -1) {
            updatedData[existingItemIndex] = {
              ...updatedData[existingItemIndex],
              testQuestion: item.testQuestion,
              groundTruth: item.groundTruth,
            };
          } else {
            updatedData.push({
              chatId: item.chatId,
              testQuestion: item.testQuestion,
              groundTruth: item.groundTruth,
              prediction: "",
              embeddingDistance: 0,
              rougeMetric: 0,
              crossEncoder: 0,
            });
          }
        });
        return updatedData;
      });
      setIsLoading(false); // 로딩 상태 해제
      onNext();
    },
    onError: () => {
      setIsLoading(false); // 로딩 상태 해제
      alert("테스트 케이스 전송에 실패했습니다. 다시 시도해주세요.");
    },
  });

  const [items, setItems] = useState<TestCase[]>([
    { testQuestion: "", groundTruth: "" },
  ]);

  const addItem = () => {
    setItems([...items, { testQuestion: "", groundTruth: "" }]);
  };

  const deleteItem = (index: number) => {
    setItems((prevItems) => prevItems.filter((_, i) => i !== index));
  };

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

  const handleSubmit = () => {
    if (!chatbotId) {
      alert("챗봇 ID를 가져오지 못했습니다. 다시 시도해주세요.");
      return;
    }
    setIsLoading(true); // 로딩 상태 활성화
    const data = items.map(({ testQuestion, groundTruth }) => ({
      testQuestion,
      groundTruth,
    }));
    createMutation.mutate({ chatbotId: String(chatbotId), data });
  };

  return (
    <div className="relative">
      {isLoading && (
        
          <Loading />
     
      )}

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

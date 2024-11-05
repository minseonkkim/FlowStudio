"use client";

import { useState } from "react";
import WhiteButton from "../common/whiteButton";
import PurpleButton from "../common/PurpleButton";

interface TestCase {
  id: number;
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
  const [items, setItems] = useState<TestCase[]>([
    { id: 1, testQuestion: "", groundTruth: "" },
  ]);

  const addItem = () => {
    setItems([
      ...items,
      { id: items.length + 1, testQuestion: "", groundTruth: "" },
    ]);
  };

  const deleteItem = (id: number) => {
    setItems((prevItems) =>
      prevItems
        .filter((item) => item.id !== id) 
        .map((item, index) => ({ ...item, id: index + 1 })) 
    );
  };

  const updateItem = (
    id: number,
    field: "testQuestion" | "groundTruth",
    value: string
  ) => {
    setItems(
      items.map((item) => (item.id === id ? { ...item, [field]: value } : item))
    );
  };

  return (
    <div className="container">
      {items.map((item) => (
        <div key={item.id} className="border-2 rounded-xl mb-4">
          <details open className="py-4 px-6">
            <summary className="font-semibold">테스트 케이스 {item.id}</summary>
            <div className="mt-4">
              <label className="block mb-2">
                테스트 질문 (Test Question)
              </label>
              <input
                type="text"
                value={item.testQuestion}
                onChange={(e) =>
                  updateItem(item.id, "testQuestion", e.target.value)
                }
                className="w-full p-2 border rounded-md bg-gray-100 focus:border-2 focus:border-[#9A75BF] focus:outline-none"
                placeholder="Enter test question"
              />
            </div>
            <div className="mt-4">
              <label className="block mb-2">
                정답 (Ground Truth)
              </label>
              <textarea
                value={item.groundTruth}
                onChange={(e) =>
                  updateItem(item.id, "groundTruth", e.target.value)
                }
                className="w-full p-2 border rounded-md bg-gray-100 focus:border-2 focus:border-[#9A75BF] focus:outline-none"
                placeholder="Enter ground truth"
              />
            </div>
            <div className="flex items-center justify-end">
              <button
                onClick={() => deleteItem(item.id)}
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
          <WhiteButton text='이전' onHandelButton={() => onPrevious()} />
          <PurpleButton text='테스트 시작' onHandelButton={() => onNext()} />
        </div>
      </div>
    </div>
  );
}

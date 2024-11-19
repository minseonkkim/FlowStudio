"use client";

import { EventSourcePolyfill } from "event-source-polyfill";
import { useState, useEffect } from "react";
import WhiteButton from "../common/whiteButton";
import PurpleButton from "../common/PurpleButton";
import { useRecoilValue, useSetRecoilState } from "recoil";
import { chatbotIdState, parsedTestDataState, isLoadingState } from "@/store/evaluationAtoms";
import { postChatFlowTest } from "@/api/evaluation";
import { useMutation } from "@tanstack/react-query";

interface TestCase {
  testQuestion: string;
  groundTruth: string;
}

interface TestCaseInputProps {
  onNext: () => void;
  onPrevious: () => void;
  selectedTab: string
}

export default function TestCaseInput({
  onNext,
  onPrevious,
  selectedTab
}: TestCaseInputProps) {
  
  // 챗플로우 아이디
  const chatbotId = useRecoilValue(chatbotIdState);
  const BASE_URL = process.env.NEXT_PUBLIC_BASE_URL;
  const accessToken = localStorage.getItem("accessToken");
  const setParsedTestData = useSetRecoilState(parsedTestDataState);
  const setisLoadingState = useSetRecoilState(isLoadingState);


  useEffect(()=>{
    setParsedTestData([])
  },[])

  // 테스트 케이스와 정답 값 보내기
  const createMutation = useMutation({
    mutationFn: ({ chatbotId, data }: { chatbotId: string; data: TestCase[] }) =>
      postChatFlowTest(chatbotId, data),
      onSuccess: (res) => {
      setParsedTestData((prev) => {
        const updatedData = [...prev];
        res.forEach((item) => {
          const existingItemIndex = updatedData.findIndex((data) => data.chatId === item.chatId);

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
      setisLoadingState(false)
      onNext(); 
        },
        onError: () => {
          setisLoadingState(false)
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

    // SSE 연결 함수 
    const initializeSSE = (token: string) => {
      const sse = new EventSourcePolyfill(`${BASE_URL}/sse/connect`, {
        headers: { Authorization: `Bearer ${token}` },
        withCredentials: true,
      });
  
      sse.onopen = () => {
        console.log("SSE 연결이 성공적으로 열렸습니다.");
      };
  
      sse.addEventListener("prediction", (event) => {
        const predictionData = JSON.parse((event as MessageEvent).data);
        console.log(predictionData);
      
        setParsedTestData((prev) => {
          const updatedData = [...prev];
      
          const existingItemIndex = updatedData.findIndex(
            (data) => data.chatId === predictionData.chatId
          );
      
          if (existingItemIndex !== -1) {
            updatedData[existingItemIndex] = {
              ...updatedData[existingItemIndex],
              prediction: predictionData.prediction,
            };
          } else {
            updatedData.push({
              chatId: predictionData.chatId,
              testQuestion: "",
              groundTruth: "",
              prediction: predictionData.prediction,
              embeddingDistance: 0,
              rougeMetric: 0,
              crossEncoder: 0,
            });
          }
      
          return updatedData;
        });
      });
      

      sse.addEventListener("testCase", (event) => {
        const testCaseData = JSON.parse((event as MessageEvent).data);
        console.log(testCaseData)
      
        setParsedTestData((prev) => {
          const updatedData = [...prev];
      
          const existingItemIndex = updatedData.findIndex(
            (data) => data.chatId === testCaseData.chatId
          );
      
          if (existingItemIndex !== -1) {
            updatedData[existingItemIndex] = {
              ...updatedData[existingItemIndex],
              embeddingDistance: testCaseData.embeddingDistance,
              rougeMetric: testCaseData.rougeMetric,
              crossEncoder: testCaseData.crossEncoder,
            };
          } else {
            updatedData.push({
              chatId: testCaseData.chatId,
              testQuestion: "",
              groundTruth: "",
              prediction: "",
              embeddingDistance: testCaseData.embeddingDistance,
              rougeMetric: testCaseData.rougeMetric,
              crossEncoder: testCaseData.crossEncoder,
            });
          }
      
          return updatedData;
        });
      });
      
      sse.onerror = () => {
        console.error("SSE 연결 오류: 자동 재연결 시도 중...");
      };
    };
  
  // 테스트케이스 전송
  const handleSubmit = () => {
    if (!chatbotId) {
      alert("챗봇 ID를 가져오지 못했습니다. 다시 시도해주세요.");
      return;
    }
    setisLoadingState(true); 
    initializeSSE(accessToken as string)
    const data = items.map(({ testQuestion, groundTruth }) => ({
      testQuestion,
      groundTruth,
    }));
    createMutation.mutate({ chatbotId: String(chatbotId), data });
  };

  return (
    <>
    <div className="flex justify-between mb-10 h-[15px] ">
    <p className="text-[22px]">{selectedTab}</p>
  </div>
  <div className="relative">
    <>
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
    </>
  </div>
</>

  );
}



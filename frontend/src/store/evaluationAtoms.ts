import { atom } from "recoil";

// 챗플로우 아이디
export const chatbotIdState = atom<number | null>({
  key: "chatbotIdState", 
  default: null, 
});

// 테스트 결과
export const parsedTestDataState = atom<
  {
    chatId: number;
    testQuestion: string;
    groundTruth: string;
    prediction: string;
    embeddingDistance: number;
    rougeMetric: number;
    crossEncoder: number;
  }[]
>({
  key: "parsedTestDataState",
  default: [],
});

// 테스트 결과 로딩 상태
export const isLoadingState = atom<boolean>({
  key: 'isLoadingState',  
  default: false,  
});
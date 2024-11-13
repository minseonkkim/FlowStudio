import { atom } from 'recoil';

// 메시지 상태 관리
export const messagesState = atom<{ text: string; sender: "user" | "server" }[]>({
  key: "messagesState",
  default: [],
});

// 로딩 상태 관리
export const isLoadingState = atom<boolean>({
  key: "isLoadingState",
  default: false,
});

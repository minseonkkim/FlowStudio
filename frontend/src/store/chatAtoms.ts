import { atom } from "recoil";

export const chatModeAtom = atom<"preview" | "default">({
  key: "chatModeAtom",
  default: "default", // 기본 모드
});

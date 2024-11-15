import { atom } from "recoil";

export const chatbotIdState = atom<number | null>({
  key: "chatbotIdState", 
  default: null, 
});
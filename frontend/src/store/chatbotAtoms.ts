import { atom } from "recoil";

interface Chatbot {
  id: number;
  title: string;
  description: string;
  category: string[];
  iconId: number;
}

export const selectedChatbotState = atom<Chatbot | null>({
  key: "selectedChatbotState",
  default: null, 
});

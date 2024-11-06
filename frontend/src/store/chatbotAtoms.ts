import { ChatFlow } from "@/types/chatbot";
import { atom } from "recoil";

export const selectedChatbotState = atom<ChatFlow | null>({
  key: "selectedChatbotState",
  default: null, 
});

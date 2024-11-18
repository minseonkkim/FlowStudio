import { ChatFlow } from "@/types/chatbot";
import { atom } from "recoil";

export const selectedChatbotState = atom<ChatFlow | null>({
  key: "selectedChatbotState",
  default: null, 
});

export const chatbotThumbnailState = atom<string | null>({
  key: "chatbotThumbnailState",
  default: null,
});
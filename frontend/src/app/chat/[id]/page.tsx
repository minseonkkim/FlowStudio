"use client";

import React from "react";
import { useRecoilValue} from "recoil";
import { chatModeAtom } from "@/store/chatAtoms";
import PreviewChat from "@/components/chat/PreviewChat";
import DefaultChat from "@/components/chat/DefaultChat";

type ChatPageProps = {
  params: {
    id: string;
  };
};

export default function ChatPage({ params }: ChatPageProps) {
  const chatFlowId = params.id;
  const mode = useRecoilValue(chatModeAtom);



  return mode === "preview" ? (
    <PreviewChat chatFlowId={chatFlowId} />
  ) : (
    <DefaultChat chatFlowId={chatFlowId} />
  );
}

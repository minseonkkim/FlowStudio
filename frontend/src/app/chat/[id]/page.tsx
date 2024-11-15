"use client";

import React from "react";
import PreviewChat from "@/components/chat/PreviewChat";
import DefaultChat from "@/components/chat/DefaultChat";

type ChatPageProps = {
  customStyle?: string;
  params: {
    id: string;
  };
};

export default function chatpage({ customStyle = "default", params }: ChatPageProps) {
  const chatFlowId = params.id;

  return customStyle === "preview" ? (
    <PreviewChat chatFlowId={chatFlowId} />
  ) : (
    <DefaultChat chatFlowId={chatFlowId} />
  );
}

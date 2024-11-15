"use client";

import React, { useState, useRef, useEffect } from "react";
import { useMutation } from "@tanstack/react-query";
import { AiOutlineSend } from "@react-icons/all-files/ai/AiOutlineSend";
import { EventSourcePolyfill } from "event-source-polyfill";
import { postMessage } from "@/api/chat";
import SideBar from "@/components/chat/SideBar";
import ReactMarkdown from "react-markdown";
import remarkGfm from "remark-gfm";




type ChatPageProps = {
  customStyle?: string;
  params: {
    id: string;
  };
};

export default function chatpage({ customStyle = "default", params }: ChatPageProps) {
  const chatFlowId = params.id;
  const [messages, setMessages] = useState<{ text: string; sender: "user" | "server" }[]>([]);
  const [input, setInput] = useState("");
  const inputRef = useRef<HTMLTextAreaElement>(null);
  const chatEndRef = useRef<HTMLDivElement>(null);
  const BASE_URL = process.env.NEXT_PUBLIC_BASE_URL;

  return customStyle === "preview" ? (
    <PreviewChat chatFlowId={chatFlowId} />
  ) : (
    <DefaultChat chatFlowId={chatFlowId} />
  );
<<<<<<< Updated upstream
};

export default ChatPage; 
=======
}
>>>>>>> Stashed changes

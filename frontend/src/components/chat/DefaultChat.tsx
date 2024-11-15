import React from "react";
import SideBar from "@/components/chat/SideBar";
import ReactMarkdown from "react-markdown";
import remarkGfm from "remark-gfm";
import { AiOutlineSend } from "@react-icons/all-files/ai/AiOutlineSend";
import { useRecoilValue } from "recoil";
import { messagesState } from "@/store/chatAtoms";

type DefaultChatProps = {
  input: string;
  handleInput: (e: React.ChangeEvent<HTMLTextAreaElement>) => void;
  sendMessage: () => void;
  chatEndRef: React.RefObject<HTMLDivElement>;
  onNewChat: () => void;
  handleKeyDown: (e: React.KeyboardEvent<HTMLTextAreaElement>) => void;
};

<<<<<<< Updated upstream
const DefaultChat: React.FC<DefaultChatProps> = ({
  input,
  handleInput,
  sendMessage,
  chatEndRef,
  onNewChat,
  handleKeyDown,
}) => {
  const messages = useRecoilValue(messagesState);

  return (
    <div className="flex w-full h-screen overflow-hidden">
      <SideBar onNewChat={onNewChat} />
      <div className="flex flex-col flex-grow bg-gray-50">
        <div className="border-b p-4 text-[18px] bg-white">새 대화</div>
        <div className="flex-grow h-0 p-6 overflow-y-auto space-y-4">
=======
export default function DefaultChat({ chatFlowId }: DefaultChatProps) {
  const [messages, setMessages] = useState<Message[]>([]);
  const [input, setInput] = useState("");
  const [defaultChatId, setDefaultChatId] = useState<number | null>(null);
  const [isSSEConnected, setIsSSEConnected] = useState(false);
  const [pendingMessage, setPendingMessage] = useState<string | null>(null);
  const [title, setTitle] = useState<string>("새 대화");
  const [, setChatlist] = useState<getChatListData | undefined>();
  
  const inputRef = useRef<HTMLTextAreaElement>(null);
  const chatEndRef = useRef<HTMLDivElement>(null);
  const queryClient = useQueryClient();
  const BASE_URL = process.env.NEXT_PUBLIC_BASE_URL;


  useEffect(() => {
    chatEndRef.current?.scrollIntoView({ behavior: "smooth" });
  }, [messages]);

  // SSE 연결 함수
  const initializeSSE = (token: string) => {
    const sse = new EventSourcePolyfill(`${BASE_URL}/sse/connect`, {
      headers: { Authorization: `Bearer ${token}` },
      withCredentials: true,
    });

    sse.onopen = () => {
      console.log("SSE 연결이 성공적으로 열렸습니다.");
      setIsSSEConnected(true);
    };
    
    sse.addEventListener("heartbeat", (event) => {
      console.log("Received heartbeat:", (event as MessageEvent).data);
    });

    sse.addEventListener("title", async (event) => {
      const data = JSON.parse((event as MessageEvent).data);
      setTitle(data.title);
    
      const accessToken = localStorage.getItem("accessToken");
      if (accessToken) {
        const updatedChatlist = await queryClient.fetchQuery<getChatListData>({
          queryKey: ["chatlist", chatFlowId],
          queryFn: () => getChattingList(chatFlowId),
        });
        setChatlist(updatedChatlist);
      }
    });
    
    
    sse.addEventListener("node", (event) => {
      const data = JSON.parse((event as MessageEvent).data);
      if (data.type === "ANSWER") {
        setMessages((prev) => [...prev.slice(0, -1), { text: data.message, sender: "server" }]);
      }
    });

    sse.onerror = () => {
      console.error("SSE 연결 오류: 자동 재연결 시도 중...");
      setIsSSEConnected(false);
    };
  };

  // 새로운 채팅 생성
  const createChatMutation = useMutation({
    mutationFn: (data: { isPreview: boolean }) => postChatting(chatFlowId, data),
    onSuccess: (response) => {
      const newChatId = response.data.data.id;
      setDefaultChatId(newChatId);


      const accessToken = response.headers["authorization"] || localStorage.getItem("accessToken");
      if (accessToken) {
        initializeSSE(accessToken);
        if (pendingMessage) {
          setMessages((prev) => [...prev, { text: pendingMessage, sender: "user" }, { text: "로딩 중...", sender: "server" }]);
          sendMessageMutation.mutate({ chatId: String(newChatId), message: pendingMessage });
          setPendingMessage(null);
        }
        setTitle('새 대화')
      } else {
        console.error("SSE 연결을 위한 토큰이 없습니다.");
      }
    },
    onError: () => {
      alert("채팅 생성에 실패했습니다. 다시 시도해 주세요.");
      setPendingMessage(null);
    },
  });

  const handleSendMessage = () => {
    if (input.trim()) {
      const message = input.trim();
      setInput(""); 
      if (!defaultChatId || !isSSEConnected) {
        setPendingMessage(message);
        createChatMutation.mutate({ isPreview: false });
      } else {
        setMessages((prev) => [...prev, { text: message, sender: "user" }, { text: "로딩 중...", sender: "server" }]);
        sendMessageMutation.mutate({ chatId: String(defaultChatId), message });
      }

      if (inputRef.current) inputRef.current.style.height = "auto";
    }
  };



  const sendMessageMutation = useMutation({
    mutationFn: (data: { chatId: string; message: string }) => postMessage(data.chatId, { message: data.message }),
    onError: () => {
      alert("메시지 전송에 실패했습니다. 다시 시도해 주세요.");
    },
  });

  const handleInput = (e: React.ChangeEvent<HTMLTextAreaElement>) => {
    setInput(e.target.value);
    if (inputRef.current) {
      inputRef.current.style.height = "auto";
      inputRef.current.style.height = `${inputRef.current.scrollHeight}px`;
    }
  };

  const handleKeyDown = (e: React.KeyboardEvent<HTMLTextAreaElement>) => {
    if (e.key === "Enter") {
      e.preventDefault();
      e.shiftKey ? setInput(input + "\n") : handleSendMessage();
    }
  };

  const { isError, error, data: chatDetail } = useQuery<getChatDetailList>({
    queryKey: ["chatDetail", chatFlowId, defaultChatId],
    queryFn: () => getChatting(chatFlowId, String(defaultChatId!)),
    enabled: !!defaultChatId && typeof window !== "undefined" && !!localStorage.getItem("accessToken"),
  });
  
  useEffect(() => {
    if (isError && error) {
      alert("채팅내역을 불러오는 중 오류가 발생했습니다. 다시 시도해 주세요.");
    }
  }, [isError, error]);

  useEffect(() => {
    if (chatDetail) {
      try {
        const parsedMessageList = JSON.parse(chatDetail.messageList);
        
        setMessages(
          parsedMessageList
            .map((msg: { question: string; answer: string; }) => [
              { text: msg.question, sender: "user" as const },
              { text: msg.answer, sender: "server" as const },
            ])
            .flat()
        );
        if(chatDetail.title != null) {
          setTitle(chatDetail.title);
        } else {
          setTitle('새 대화');
        }
      } catch (error) {
        console.error("messageList 파싱 오류:", error);
      }
    }
  }, [chatDetail]);

  const handleSelectChat = (chatId: number) => {
    setDefaultChatId(chatId);
  };

  const onNewChat = () => {
    setMessages([]);
    createChatMutation.mutate({ isPreview: false });
  };

  const onDeleteNewChat = () =>{
    setMessages([]); 
    setDefaultChatId(null)
    handleSendMessage()
    setTitle('새 대화')
    
  }

  return (
    <div className="flex h-screen">
      {typeof window !== "undefined" && localStorage.getItem("accessToken") ? (
        <div className="w-64 flex-shrink-0">
          <SideBar 
            onNewChat={onNewChat} 
            chatFlowId={chatFlowId} 
            onSelectChat={handleSelectChat} 
            onDeleteNewChat={onDeleteNewChat} 
            selectedChatId={defaultChatId} 
          />
        </div>
      ) : null}
      
      <div className={`flex flex-col ${typeof window !== "undefined" && localStorage.getItem("accessToken") ? 'flex-grow' : 'w-full'} bg-gray-50`}>
        <div className="border-b p-4 bg-white text-[18px]">{title}</div>
        <div className="flex-grow h-0 px-14 pt-6 space-y-8 overflow-y-auto">
>>>>>>> Stashed changes
          {messages.map((msg, index) => (
            <div
              key={index}
              className={`flex ${msg.sender === "user" ? "justify-end" : "justify-start"} space-x-4`}
            >
              <div
                className={`rounded-lg px-4 py-2 whitespace-pre-wrap text-gray-800 shadow-sm max-w-[60%] ${
                  msg.sender === "server" ? "bg-[#f9f9f9] border border-gray-300" : "bg-[#f3f3f3]"
                }`}
              >
                {msg.sender === "server" ? (
                  <ReactMarkdown remarkPlugins={[remarkGfm]}>{msg.text}</ReactMarkdown>
                ) : (
                  msg.text
                )}
              </div>
            </div>
          ))}
          <div ref={chatEndRef} />
        </div>
        <div className="border-t p-6 flex items-center bg-white">
          <textarea
            value={input}
            onChange={handleInput}
            onKeyDown={handleKeyDown}
            rows={1}
            placeholder="메시지를 입력하세요..."
            className="flex-1 border border-gray-300 rounded-lg py-2 px-4 resize-none overflow-y-hidden shadow-sm focus:outline-none focus:ring-1 focus:ring-[#9A75BF]"
          />
          <button onClick={sendMessage} className="ml-3 p-2 bg-[#9A75BF] hover:bg-[#7C5DAF] rounded text-white">
            <AiOutlineSend size={20} />
          </button>
        </div>
      </div>
    </div>
  );
<<<<<<< Updated upstream
};

export default DefaultChat;
=======
}  
>>>>>>> Stashed changes

import React, { forwardRef, useImperativeHandle, useState } from "react";
import { BsArrowUpRight } from "@react-icons/all-files/bs/BsArrowUpRight";
import { publishChatFlow } from "@/api/chatbot";

export const ChatFlowPublishMenu = forwardRef(
    (
        { chatFlowId }: { chatFlowId: number },
        ref: React.Ref<{ toggleChatFlowPublishModal: () => void }>
    ) => {
        const [showChatbotCreationModal, setShowChatbotCreationModal] = useState(false);

        // 부모 컴포넌트에서 제어할 수 있도록 함수 노출
        useImperativeHandle(ref, () => ({
            toggleChatFlowPublishModal: () => {
                setShowChatbotCreationModal((prev) => !prev);
            },
        }));

        const handlePublishButtonClick = () => {
            publishChatFlow(chatFlowId).then((success) => {
                if (success) alert("발행 성공");
            });
        };

        return (<>
            {showChatbotCreationModal && (
                <div className="text-[14px] absolute top-[135px] right-[25px] p-4 bg-white shadow-lg rounded-[10px] flex flex-col justify-between gap-3 z-[100] w-[250px] h-[200px]">
                    <button
                        onClick={handlePublishButtonClick}
                        className="px-3 py-2.5 bg-[#9A75BF] hover:bg-[#8D64B6] rounded-[8px] text-white font-bold cursor-pointer"
                    >
                        업데이트
                    </button>
                    <div className="flex flex-col gap-3">
                        <a
                            href={`${process.env.NEXT_PUBLIC_FRONT_URL}/chat/${chatFlowId}`}
                            target="_blank"
                            className="p-2 bg-[#F2F2F2] hover:bg-[#ECECEC] rounded-[8px] cursor-pointer text-start flex flex-row items-center gap-1"
                        >
                            앱 실행<BsArrowUpRight />
                        </a>
                        <button
                            onClick={() => setShowChatbotCreationModal(true)}
                            className="p-2 bg-[#F2F2F2] hover:bg-[#ECECEC] rounded-[8px] cursor-pointer text-start flex flex-row items-center gap-1"
                        >
                            사이트에 삽입<BsArrowUpRight />
                        </button>
                    </div>
                </div>

            )}</>);

    });
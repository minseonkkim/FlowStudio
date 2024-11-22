import React, { Dispatch, forwardRef, SetStateAction, useImperativeHandle, useState } from "react";
import { BsArrowUpRight } from "@react-icons/all-files/bs/BsArrowUpRight";
import { publishChatFlow, unPublishChatFlow } from "@/api/chatbot";
import { PublishChatFlowData } from "@/types/workflow";
import { timeDifferenceFromNow } from "@/utils/node";
import { Bounce, toast } from 'react-toastify';
import ModalIframe from "./ModalIframe";
import { getApiKeys } from "@/api/profile";
import { toastError, toastSuccess } from "@/utils/toast";

const ChatFlowPublishMenu = forwardRef(
    ({ publishedChatFlowData,
        setPublishedChatFlowData,
    }: {
        publishedChatFlowData: PublishChatFlowData,
        setPublishedChatFlowData: Dispatch<SetStateAction<PublishChatFlowData>>
    },
        ref: React.Ref<{ toggleChatFlowPublishModal: () => void }>
    ) => {
        const [showChatbotCreationModal, setShowChatbotCreationModal] = useState(false);
        const [timeDiff, setTimeDiff] = useState(timeDifferenceFromNow(publishedChatFlowData.publishedAt))
        const [showImportModal, setShowImportModal] = useState(false);

        // 부모 컴포넌트에서 제어할 수 있도록 함수 노출
        useImperativeHandle(ref, () => ({
            toggleChatFlowPublishModal: () => {
                setTimeDiff(timeDifferenceFromNow(publishedChatFlowData.publishedAt));
                setShowChatbotCreationModal((prev) => !prev);
            },
        }));

        /**
         * 발행 버튼
         */
        const handlePublishButtonClick = async () => {
            let isPublishPossible = false;

            await getApiKeys().then((data) => {
                isPublishPossible = (data.openAiKey && data.openAiKey?.length != 0);                
            });

            if (!isPublishPossible) {
                toastError('API 키를 등록해야 합니다.');
                return;
            }

            publishChatFlow(publishedChatFlowData.chatFlowId).then((data: PublishChatFlowData) => {
                setPublishedChatFlowData(data);
                setTimeDiff(timeDifferenceFromNow(data.publishedAt));
                const msg = publishedChatFlowData.publishUrl && publishedChatFlowData.publishedAt
                    ? "업데이트"
                    : "발행";
                    toastSuccess(`챗봇 ${msg} 성공`);
            });
        };
        
        const handleUnPublishButtonClick = async () => {
            unPublishChatFlow(publishedChatFlowData.chatFlowId).then((data) => {
                console.log(data);
                if (Boolean(data)) { 
                    setPublishedChatFlowData({
                        ...publishedChatFlowData,
                        publishUrl: '',
                        publishedAt: '',
                    })
                    setTimeDiff(null);
                    toastSuccess('챗봇 발행이 취소되었습니다.');
                }
            })
        }

        const handleImportModal = () => {
            setShowImportModal((prev) => !prev);
        }

        return (<>
            {showChatbotCreationModal && (
                <div className="text-[14px] absolute top-[135px] right-[25px] p-4 bg-white shadow-lg rounded-[10px] flex flex-col justify-between gap-3 z-[100] w-[250px]">
                    <button
                        onClick={handlePublishButtonClick}
                        className="px-3 py-2.5 bg-[#9A75BF] hover:bg-[#8D64B6] rounded-[8px] text-white font-bold cursor-pointer"
                    >
                        {publishedChatFlowData.publishUrl && publishedChatFlowData.publishedAt
                            ? "업데이트"
                            : "발행"}
                    </button>
                    {publishedChatFlowData.publishUrl && publishedChatFlowData.publishedAt &&<button
                        onClick={handleUnPublishButtonClick}
                        className="px-3 py-2.5 bg-[#9A75BF] hover:bg-[#8D64B6] rounded-[8px] text-white font-bold cursor-pointer"
                    >발행 취소
                    </button>
                    }
                    <div>
                        {timeDiff ? `${timeDiff} 발행` : "챗봇을 발행하세요"}
                    </div>
                    <div className="relative flex flex-col gap-3">
                        {/* 비활성화 시 덮는 레이어 */}
                        {!publishedChatFlowData.publishUrl || !publishedChatFlowData.publishedAt ? (
                            <div
                                style={{
                                    position: "absolute",
                                    top: 0,
                                    left: 0,
                                    width: "100%",
                                    height: "100%",
                                    backgroundColor: "rgba(255, 255, 255, 0.6)", // 반투명 배경
                                    zIndex: 10,
                                    cursor: "not-allowed", // 금지 표시
                                }}
                            />
                        ) : null}
                        <a
                            href={`${process.env.NEXT_PUBLIC_FRONT_URL}/chat/${publishedChatFlowData.chatFlowId}`}
                            target="_blank"
                            className="p-2 bg-[#F2F2F2] hover:bg-[#ECECEC] rounded-[8px] cursor-pointer text-start flex flex-row items-center gap-1"
                        >
                            앱 실행<BsArrowUpRight />
                        </a>
                        <button
                            onClick={handleImportModal}
                            className="p-2 bg-[#F2F2F2] hover:bg-[#ECECEC] rounded-[8px] cursor-pointer text-start flex flex-row items-center gap-1"
                        >
                            사이트에 삽입<BsArrowUpRight />
                        </button>
                    </div>
                    {showImportModal && <ModalIframe chatFlowId={publishedChatFlowData.chatFlowId} handleModalOpen={handleImportModal} />}
                </div>
            )}</>);

    });

ChatFlowPublishMenu.displayName = "ChatFlowPublishMenu";

export default ChatFlowPublishMenu;
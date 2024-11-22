'use client';

import { toastSuccess } from '@/utils/toast';
import { FaRegCopy } from '@react-icons/all-files/fa/FaRegCopy';
import { FiX } from '@react-icons/all-files/fi/FiX';
import { Bounce, toast } from 'react-toastify';

interface ModalIframeProps {
  chatFlowId: number;
  handleModalOpen: () => void;
}

export default function ModalIframe({ 
  chatFlowId,
  handleModalOpen, 
}: ModalIframeProps) {
  const handleCopy = () => {
    navigator.clipboard.writeText(iframeCode);
    toastSuccess('iframe 코드가 복사되었습니다.');
  };

  const iframeCode = `
    <iframe
      src="${process.env.NEXT_PUBLIC_FRONT_URL}/chat/${chatFlowId}"
      style="width: 100%; height: 100%; min-height: 700px"
      frameborder="0"
      allow="microphone">
    </iframe>`;

  return (
    <>
      {/* 오버레이 */}
      <div className="fixed inset-0 bg-black bg-opacity-20 z-20"></div>
      {/* 모달 */}
      <div className="fixed inset-0 z-30 flex items-center justify-center">
        <div className="bg-white p-6 rounded-lg shadow-lg w-[640px] flex flex-col items-center">
          {/* 닫기 아이콘 */}
          <div className="ml-auto">
            <FiX onClick={handleModalOpen} className="cursor-pointer" />
          </div>

          {/* 모달 헤더 */}
          <p className="text-[20px] font-bold mb-4 text-center">웹사이트에 임베드하기</p>

          {/* 설명 */}
          <p className="text-[13px] text-gray-500 mb-4 text-center">
            웹사이트의 원하는 위치에 챗봇 앱을 추가하려면 이 Iframe을 HTML 코드에 추가하세요.
          </p>

          {/* 아이프레임 코드 */}
          <div className="bg-gray-100 p-4 rounded-md w-full mb-4">
            {/* 복사 아이콘 */}
            <div className="flex justify-end">
              <FaRegCopy className="text-gray-800 cursor-pointer" onClick={handleCopy} />
            </div>

            {/* 코드 텍스트 */}
            <div className="flex items-center mb-8">
              <pre className="text-[13px] text-gray-800 whitespace-pre-wrap flex-1">
                {iframeCode}
              </pre>
            </div>
          </div>
        </div>
      </div>
    </>
  );
}

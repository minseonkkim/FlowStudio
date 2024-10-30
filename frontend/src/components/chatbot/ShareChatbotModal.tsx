import { useState } from 'react';

interface ShareChatbotModalProps {
  onClose: () => void;
}

const documents = [
  { id: 1, name: "파일이름.txt", isPublic: true },
  { id: 2, name: "파일이름.txt", isPublic: false },
  { id: 3, name: "파일이름.txt", isPublic: true },
];

export default function ShareChatbotModal({ onClose }: ShareChatbotModalProps) {
  const [documentStates, setDocumentStates] = useState(
    documents.map((doc) => ({ ...doc, isPublic: doc.isPublic }))
  );

  const handleTogglePublic = (id: number) => {
    setDocumentStates((prevStates) =>
      prevStates.map((doc) =>
        doc.id === id ? { ...doc, isPublic: !doc.isPublic } : doc
      )
    );
  };

  const handleShare = () => {
    onClose();
  };

  return (
    <div className="flex flex-col bg-white w-[500px] h-[400px] p-8 rounded-xl shadow-lg">
      <p className="mb-4 text-[22px]">챗봇 공유하기</p>
      <p className="mb-2 text-gray-700">포함된 문서</p>
      
      <div className="flex-grow overflow-y-auto">
        <table className="w-full mb-6">
          <thead>
            <tr className="text-left text-gray-500">
              <th className="w-1/6">번호</th>
              <th className="w-4/6">문서 이름</th>
              <th className="w-1/6 text-right">공개 여부</th>
            </tr>
          </thead>
          <tbody>
            {documentStates.map((doc, index) => (
              <tr key={doc.id} className="border-b text-gray-800">
                <td className="py-3">{index + 1}</td>
                <td className="py-3">{doc.name}</td>
                <td className="py-3 flex justify-end">
                  <div
                    onClick={() => handleTogglePublic(doc.id)}
                    className={`relative w-12 h-6 flex items-center cursor-pointer ${
                      doc.isPublic ? 'bg-[#9A75BF]' : 'bg-gray-400'
                    } rounded-full p-1 transition-colors duration-300 ease-in-out`}
                  >
                    <div
                      className={`h-4 w-4 bg-white rounded-full shadow-md transform ${
                        doc.isPublic ? 'translate-x-6' : 'translate-x-0'
                      } transition-transform duration-300 ease-in-out`}
                    />
                  </div>
                </td>
              </tr>
            ))}
          </tbody>
        </table>
      </div>

      <div className="flex justify-end gap-4 mt-4">
      <button
          onClick={onClose}
          className="w-[70px] h-[38px] border-2 border-[#9A75BF] text-[#9A75BF] rounded-lg hover:bg-[#f3e8ff] active:bg-[#e3d1f7]"
        >
          취소
        </button>
        <button
          onClick={handleShare}
          className="w-[70px] h-[38px] bg-[#9A75BF] text-white rounded-lg hover:bg-[#874aa5] active:bg-[#733d8a]"
        >
          공유
        </button>
      </div>
    </div>
  );
}

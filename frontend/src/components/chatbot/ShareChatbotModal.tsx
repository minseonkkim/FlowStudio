import { getAllChatFlows, getChatFlow } from '@/api/chatbot';
import { putDocKnowledge } from '@/api/knowledge';
import { postUploadChatFlow } from '@/api/share';
import {ChatFlowDetail, NodeData } from '@/types/chatbot';
import { useMutation, useQuery, useQueryClient } from '@tanstack/react-query';
import { useEffect, useState } from 'react';

interface ShareChatbotModalProps {
  chatFlowId: number;
  onClose: () => void;
}

export default function ShareChatbotModal({ onClose, chatFlowId }: ShareChatbotModalProps) {
  const queryClient = useQueryClient();

  const { isLoading, isError, error, data: chatFlow } = useQuery<ChatFlowDetail>({
    queryKey: ["chatFlow"],
    queryFn: () => getChatFlow(chatFlowId),
  });

  const updateKnowledgeMutation = useMutation<void, unknown, { knowledgeId: number; data: { title: string; isPublic: boolean } }>({
    mutationFn: ({ knowledgeId, data }) => putDocKnowledge(knowledgeId, data),
    onSuccess: (_, { knowledgeId }) => {
      setKnowledgeArray((prev) =>
        prev.map((doc) =>
          doc.knowledgeId === knowledgeId
            ? { ...doc, isPublic: !doc.isPublic }
            : doc
        )
      );
      queryClient.invalidateQueries({ queryKey: ['knowledgeList'] });
    },
    onError: (error) => {
      console.error('Error updating knowledge:', error);
      alert('Knowledge update failed. Please try again.');
  },
});


  const createMutation = useMutation({
    mutationFn: postUploadChatFlow,
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ["chatFlows"] });
    },
    onError: () => {
      alert("챗봇 업로드에 실패했습니다. 다시 시도해 주세요.");
    },
  });

  const [knowledgeArray, setKnowledgeArray] = useState<any[]>([]);

  useEffect(() => {
    if (chatFlow && chatFlow.edges) {
      const knowledgeArray = chatFlow.nodes
            .filter((node: NodeData) => node.type === "RETRIEVER")
            .map((node: NodeData) => node.knowledge);
      console.log('지식리스트', knowledgeArray);
      setKnowledgeArray(knowledgeArray);
    }
  }, [chatFlow, chatFlowId]);

  const handleTogglePublic = (id: number, title: string, isPublic: boolean) => {
    console.log(isPublic)
    updateKnowledgeMutation.mutate({
      knowledgeId: id,
      data: {
        title: title,
        isPublic: !isPublic,
      },
    });
  };

  const handleShare = () => {
    createMutation.mutate(chatFlowId);
    onClose();
  };

  if (isLoading) return <div>Loading...</div>;
  if (isError && error) return <div>Error: {error.message}</div>;

  return (
    <div className="flex flex-col bg-white w-[500px] h-[400px] p-8 rounded-xl shadow-lg">
      <p className="mb-4 text-[22px]">챗봇 공유</p>
      <p className="mb-2 text-gray-700">포함된 문서</p>

      <div className="flex-grow overflow-y-auto">
        <table className="w-full mb-6">
          <thead>
            <tr className="text-left text-gray-500">
              <th className="w-1/6">번호</th>
              <th className="w-4/6">문서 이름</th>
              <th className="w-1/6">공개 여부</th>
            </tr>
          </thead>
          <tbody>
            {knowledgeArray.map((doc, index) => (
              <tr key={doc.id} className="border-b text-gray-800">
                <td className="py-3">{index + 1}</td>
                <td className="py-3">{doc?.title}</td>
                <td className="py-3 flex justify-end">
                  <div
                    onClick={() => handleTogglePublic(doc.knowledgeId, doc.title, doc.isPublic)}
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

'use client';
import { useState, useEffect } from "react";
import ModalTestResult from "@/components/evaluation/ModalTestResult";
import { getChatTestList, getChatTestDetail } from '@/api/evaluation';
import { useQuery, useMutation } from '@tanstack/react-query';
import { ChatFlowTestResult, ChatFlowTestCase } from '@/types/evaluation';

interface EvaluationPageProps {
  params: {
    id: number;
  };
}

export default function Page({ params }: EvaluationPageProps) {
  const chatFlowId = String(params.id);
  const [testList, setTestList] = useState<ChatFlowTestResult[]>([]);
  const [testResultDetail, setTestResultDetail] = useState<ChatFlowTestCase[]>([]);
  const [isModalOpen, setIsModalOpen] = useState(false);

  useEffect(() => {
    if (isModalOpen) {
      document.body.style.overflow = "hidden"; // 모달 열림 시 스크롤 비활성화
    } else {
      document.body.style.overflow = "auto"; // 모달 닫힘 시 스크롤 활성화
    }
    return () => {
      document.body.style.overflow = "auto"; // 컴포넌트 언마운트 시 스크롤 복구
    };
  }, [isModalOpen]);

  const { isError, error, data: testResultList } = useQuery<ChatFlowTestResult[]>({
    queryKey: ['testResultList'],
    queryFn: () => getChatTestList(chatFlowId),
  });

  const testDetailMutation = useMutation({
    mutationFn: ({ chatFlowId, chatFlowTestId }: { chatFlowId: string; chatFlowTestId: string }) =>
      getChatTestDetail(chatFlowId, chatFlowTestId),
    onSuccess: (data) => {
      setTestResultDetail(data); // 세부 정보 저장
      setIsModalOpen(true); // 모달 열기
    },
    onError: () => {
      alert("테스트케이스 내용 가져오기에 실패했습니다. 다시 시도해 주세요.");
    },
  });

  const handleRowClick = (testId: number) => {
    testDetailMutation.mutate({
      chatFlowId,
      chatFlowTestId: String(testId),
    });
  };

  const closeModal = () => {
    setIsModalOpen(false);
    setTestResultDetail([]); // 세부 정보 초기화
  };

  useEffect(() => {
    if (isError && error) {
      alert("테스트 완료한 챗플로우 목록을 불러오는 중 오류가 발생했습니다. 다시 시도해 주세요.");
    }
  }, [isError, error]);

  useEffect(() => {
    if (testResultList) {
      setTestList(testResultList);
    }
  }, [testResultList]);

  return (
    <div className="relative">
      <div className="rounded-xl border-2 border-[#9A75BF] p-12 my-16 mx-36">
        <div className="p-8 text-[#333]">
          <h2 className="text-2xl font-semibold mb-12">Workflow Planning Assistant</h2>

          <table className="w-full text-[14px] border border-collapse border-gray-300 mb-8">
            <thead>
              <tr className="bg-gray-100">
                <th className="border px-6 py-2">번호</th>
                <th className="border px-6 py-4">Embedding Distance 평균</th>
                <th className="border px-6 py-2">Embedding Distance 분산</th>
                <th className="border px-6 py-2">ROUGE Metric 평균</th>
                <th className="border px-6 py-2">ROUGE Metric 분산</th>
                <th className="border px-6 py-2">Cross Encoder 평균</th>
                <th className="border px-6 py-2">Cross Encoder 분산</th>
                <th className="border px-8 py-2">테스트 케이스 횟수</th>
              </tr>
            </thead>
            <tbody>
              {testList.map((item, index) => (
                <tr
                  key={index}
                  className="cursor-pointer hover:bg-gray-100"
                  onClick={() => handleRowClick(item.id)}
                >
                  <td className="border px-6 py-2 text-center">{index + 1}</td>
                  <td className="border px-6 py-2 text-center">{item.embeddingDistanceMean.toFixed(5)}</td>
                  <td className="border px-6 py-2 text-center">{item.embeddingDistanceVariance.toFixed(5)}</td>
                  <td className="border px-6 py-2 text-center">{item.rougeMetricMean.toFixed(5)}</td>
                  <td className="border px-6 py-2 text-center">{item.rougeMetricVariance.toFixed(5)}</td>
                  <td className="border px-6 py-2 text-center">{item.crossEncoderMean.toFixed(5)}</td>
                  <td className="border px-6 py-2 text-center">{item.crossEncoderVariance.toFixed(5)}</td>
                  <td className="border px-6 py-2 text-center">{item.totalTestCount}</td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>

        {/* 설명 텍스트 */}
        <div className="text-[14px] text-[#757575] mb-8">
          <p>
            ROUGE Metric과 Embedding Distance는 텍스트 유사도를 측정하는 데 사용되었으며,
            Cross Encoder는 문장 간 의미적 일치도를 평가하기 위한 지표로 활용되었습니다.
          </p>
          <ul className="list-disc list-inside ml-5">
            <li>Embedding Distance: 텍스트를 벡터공간에 매핑하고 거리를 측정하여 유사도를 평가</li>
            <li>Cross-Encoder: 두 테스트를 동시에 입력받아 직접적인 관련성 점수를 출력</li>
            <li>Rouge Metric: 생성된 텍스트와 참조 텍스트 간의 겹치는 n-gram을 기반으로 평가</li>
          </ul>
        </div>
      </div>

      {/* 모달 */}
      {isModalOpen && testResultDetail.length > 0 && (
        <div
          className="fixed inset-0 bg-black bg-opacity-50 flex justify-center items-center z-50"
          onClick={closeModal} 
        >
          <div
            className="bg-white rounded-lg shadow-lg p-8 max-w-3xl w-full relative"
            onClick={(e) => e.stopPropagation()} 
          >
            <ModalTestResult testResultDetail={testResultDetail} />
            {/* <TiDeleteOutline onClick={closeModal} className="absolute top-4 right-4 w-6 h-6" /> */}
          </div>
        </div>
      )}
    </div>
  );
}

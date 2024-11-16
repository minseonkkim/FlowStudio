'use client';
import { useState } from "react";
import ModalTestResult from "@/components/evaluation/ModalTestResult";
import { TiDeleteOutline } from '@react-icons/all-files/ti/TiDeleteOutline';

export default function Page() {
  const [isModalOpen, setIsModalOpen] = useState(false); // 모달 상태 관리

  const openModal = () => {
    setIsModalOpen(true); // 모달 열기
  };

  const closeModal = () => {
    setIsModalOpen(false); // 모달 닫기
  };

  return (
    <div className="relative">
      <div className="rounded-xl border-2 border-[#9A75BF] p-12 my-16 mx-36" >
        <div className="p-8 text-[#333]">
          <h2 className="text-2xl font-semibold mb-12">Workflow Planning Assistant</h2>

          {/* 표 */}
          <table className="w-full text-[14px] border border-collapse border-gray-300 mb-8">
            <thead>
              <tr className="bg-gray-100">
                <th className="border px-4 py-2">번호</th>
                <th className="border px-4 py-2">Embedding Distance 평균</th>
                <th className="border px-4 py-2">Embedding Distance 분산</th>
                <th className="border px-4 py-2">ROUGE Metric 평균</th>
                <th className="border px-4 py-2">ROUGE Metric 분산</th>
                <th className="border px-4 py-2">Cross Encoder 평균</th>
                <th className="border px-4 py-2">Cross Encoder 분산</th>
                <th className="border px-4 py-2 min-w-[150px]">DateTime</th>
              </tr>
            </thead>
            <tbody>
              {[...Array(5)].map((_, index) => (
                <tr
                  key={index}
                  className="cursor-pointer hover:bg-gray-100"
                  onClick={openModal} 
                >
                  <td className="border px-4 py-2 text-center">{index + 1}</td>
                  <td className="border px-4 py-2 text-center">0.85</td>
                  <td className="border px-4 py-2 text-center">0.02</td>
                  <td className="border px-4 py-2 text-center">0.6</td>
                  <td className="border px-4 py-2 text-center">0.01</td>
                  <td className="border px-4 py-2 text-center">0.7</td>
                  <td className="border px-4 py-2 text-center">0.03</td>
                  <td className="border px-4 py-2 text-center">2024-10-22 10:00</td>
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

        {/* 구분선 */}
        <hr className="border-t-2 border-gray-300 my-8" />

        {/* 기타 사항 */}
        <div>
          <h3 className="font-semibold text-[14px] mb-2">챗봇 관련 기타사항</h3>
          <ul className="list-decimal list-inside text-[14px] text-[#333] ml-5 space-y-2">
            <li>텍스트 분석 및 다중 언어 지원 기능을 포함</li>
            <li>자연어 처리 개선 후 비정형 질문에 대한 챗봇 응답의 정확성 테스트.</li>
            <li>긴 문장을 처리하는 로직을 추가 후 응답 정확성 테스트.</li>
            <li>다중 질문 처리 로직 추가 후, 다중 질문 응답 정확성 테스트.</li>
            <li>중복된 질문 응답 필터링 로직 추가 후 성능 테스트.</li>
          </ul>
        </div>
      </div>

      {/* 모달 */}
      {isModalOpen && (
        <div className="fixed inset-0 bg-black bg-opacity-50 flex justify-center items-center z-50">
          <div className="bg-white rounded-lg shadow-lg p-8 max-w-3xl w-full relative">
            <ModalTestResult />
            <TiDeleteOutline onClick={closeModal} className="absolute top-4 right-4 w-6 h-6"/>
          </div>
        </div>
      )}
    </div>
  );
}

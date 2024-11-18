"use client";

import React, { useState } from "react";
import EvaluationFirst from "@/components/evaluation/SelectChatbot";
import EvaluationSecond from "@/components/evaluation/TestCaseInput";
import EvaluationThird from "@/components/evaluation/TestResult";
import { useRecoilValue } from "recoil";
import { isLoadingState } from "@/store/evaluationAtoms";
import Loading from "@/components/common/Loading";

const tabNames = ["챗봇 선택", "테스트케이스 입력", "테스트 결과"];

export default function Page() {
  const [selectedTab, setSelectedTab] = useState<string>("챗봇 선택");
  const isLoading = useRecoilValue(isLoadingState);

  const renderContent = () => {
    switch (selectedTab) {
      case "챗봇 선택":
        return (
          <EvaluationFirst
            selectedTab={selectedTab}
            onNext={() => {
              setSelectedTab("테스트케이스 입력");
            }}
          />
        );
      case "테스트케이스 입력":
        return (
          <EvaluationSecond
            selectedTab={selectedTab}
            onNext={() => setSelectedTab("테스트 결과")}
            onPrevious={() => setSelectedTab("챗봇 선택")}
          />
        );
      case "테스트 결과":
        return <EvaluationThird selectedTab={selectedTab} />;
      default:
        return null;
    }
  };

  return (
    <div className="relative h-screen">
      {/* 메인 콘텐츠 */}
      <div className="flex h-full">
        {/* 왼쪽 사이드바 */}
        <div className="w-[270px] p-4 border-r bg-gray-50 pl-12 pt-12 h-[calc(100vh-57px)] fixed top-[57px] left-0">
          <h2 className="text-[18px] mb-4">챗봇 평가 단계</h2>
          <ul className="space-y-3">
            {tabNames.map((tab, index) => (
              <li
                key={index}
                className={`flex items-center py-2 rounded-lg text-[14px] ${
                  selectedTab === tab
                    ? "text-[#9A75BF] font-semibold"
                    : " "
                }`}
              >
                <span
                  className={`flex items-center justify-center w-6 h-6 mr-3 rounded-full ${
                    selectedTab === tab
                      ? "bg-[#9A75BF] text-white"
                      : "bg-gray-200 text-gray-500"
                  }`}
                >
                  {index + 1}
                </span>
                {tab}
              </li>
            ))}
          </ul>
        </div>

        {/* 메인 콘텐츠 */}
        <div className="flex-1 p-[38px] ml-[270px]">{renderContent()}</div>
      </div>

      {/* 로딩 상태일 때 오버레이와 모달 */}
      {isLoading && (
        <>
          {/* 오버레이 */}
          <div className="fixed inset-0 bg-black bg-opacity-50 z-20"></div>

          {/* 로딩 모달 */}
          <div className="fixed inset-0 z-30 flex items-center justify-center">
            <div className="bg-white p-6 rounded-lg shadow-lg w-[350px] h-[250px] flex flex-col items-center justify-center">
              <Loading />
              <p className="text-[20px] font-bold mb-2 text-center">
                테스트 케이스를 평가하는 중입니다
              </p>
              <p className="text-[13px] text-gray-500 mb-4 text-center">
                잠시만 기다려 주세요.
              </p>
            </div>
          </div>
        </>
      )}
    </div>
  );
}

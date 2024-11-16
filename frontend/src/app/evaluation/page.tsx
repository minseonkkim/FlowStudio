"use client";

import React, { useState } from "react";
import EvaluationFirst from "@/components/evaluation/SelectChatbot";
import EvaluationSecond from "@/components/evaluation/TestCaseInput";
import EvaluationThird from "@/components/evaluation/TestResult";

const tabNames = ["챗봇 선택", "테스트케이스 입력", "테스트 결과"];

export default function Page() {
  const [selectedTab, setSelectedTab] = useState<string>("챗봇 선택");



  const renderContent = () => {
    switch (selectedTab) {
      case "챗봇 선택":
        return <EvaluationFirst
        onNext={() => {
          setSelectedTab("테스트케이스 입력");
        }}
      />;
      case "테스트케이스 입력":
        return (
          <EvaluationSecond
            onNext={() => setSelectedTab("테스트 결과")}
            onPrevious={() => setSelectedTab("챗봇 선택")}
          />
        );
      case "테스트 결과":
        return <EvaluationThird />;
      default:
        return null;
    }
  };

  return (
    <div className="flex h-full">
    {/* 왼쪽 사이드바 */}
    <div className="w-1/6 p-4 border-r bg-gray-50 pl-12 pt-12">
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
                selectedTab === tab ? "bg-[#9A75BF] text-white" : "bg-gray-200 text-gray-500"
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
      <div className="flex-1 p-12">
        <div className="flex justify-between mb-8">
          <p className="text-[22px]">{selectedTab}</p>
        </div>
        {renderContent()}
      </div>
    </div>
  );
}

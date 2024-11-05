"use client"; 

import React from 'react';
import { useRecoilState } from 'recoil';
import { currentStepState } from '@/store/knoweldgeAtoms'; 

export default function CreateStep() {
  const [currentStep, ] = useRecoilState(currentStepState); 

  return (
    <div className="border-r border-r-[#E0E0E0] bg-gray-50">
      <p className="text-lg font-semibold pt-10 pl-8 text-[#333333]">지식 생성</p>
      <div className="mt-4">
        <div className={`flex items-center pl-8 py-3 ${currentStep === 1 ? "text-[#9A75BF]" : "text-[#757575]"}`}>
          <span className={`flex justify-center items-center w-6 h-6 rounded-full text-xs font-medium ${currentStep === 1 ? "bg-[#9A75BF] text-white" : "bg-gray-300 text-gray-700"}`}>1</span>
          <span className="ml-3 text-sm">데이터 소스 선택</span>
        </div>
        <div className={`flex items-center pl-8 py-3 ${currentStep === 2 ? "text-[#9A75BF]" : "text-[#757575]"}`}>
          <span className={`flex justify-center items-center w-6 h-6 rounded-full text-xs font-medium ${currentStep === 2 ? "bg-[#9A75BF] text-white" : "bg-gray-300 text-gray-700"}`}>2</span>
          <span className="ml-3 text-sm">데이터 전처리 및 클리닝</span>
        </div>
        <div className={`flex items-center pl-8 py-3 ${currentStep === 3 ? "text-[#9A75BF]" : "text-[#757575]"}`}>
          <span className={`flex justify-center items-center w-6 h-6 rounded-full text-xs font-medium ${currentStep === 3 ? "bg-[#9A75BF] text-white" : "bg-gray-300 text-gray-700"}`}>3</span>
          <span className="ml-3 text-sm">실행 및 완료</span>
        </div>
      </div>
    </div>
  );
}

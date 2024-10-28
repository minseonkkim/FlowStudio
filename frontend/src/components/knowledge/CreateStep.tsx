"use client"; 

import React from 'react';
import { useRecoilState } from 'recoil';
import { currentStepState } from '@/store/atoms'; 

export default function CreateStep() {
  const [currentStep, setCurrentStep] = useRecoilState(currentStepState); 

  return (
    <div className="border-r-[#EAECF0] border-r">
      <p className="text-xl font-medium pt-14 pl-8">지식 생성</p>
      <div>
        <div className={`flex items-center pl-8 pt-4 ${currentStep === 1 ? "text-[#9A75BF]" : "text-[#757575]"}`}>
          <span className={`flex justify-center items-center w-5 h-5 rounded-full text-sm ${currentStep === 1 ? "bg-[#9A75BF] text-white" : "bg-[#E4E6EB] text-black"}`}>1</span>
          <span className="ml-2">데이터 소스 선택</span>
        </div>
        <div className={`flex items-center pl-8 pt-2 ${currentStep === 2 ? "text-[#9A75BF]" : "text-[#757575]"}`}>
          <span className={`flex justify-center items-center w-5 h-5 rounded-full text-sm ${currentStep === 2 ? "bg-[#9A75BF] text-white" : "bg-[#E4E6EB] text-black"}`}>2</span>
          <span className="ml-2">데이터 전처리 및 클리닝</span>
        </div>
        <div className={`flex items-center pl-8 pt-2 ${currentStep === 3 ? "text-[#9A75BF]" : "text-[#757575]"}`}>
          <span className={`flex justify-center items-center w-5 h-5 rounded-full text-sm ${currentStep === 3 ? "bg-[#9A75BF] text-white" : "bg-[#E4E6EB] text-black"}`}>3</span>
          <span className="ml-2">실행 및 완료</span>
        </div>
      </div>
    </div>
  );
}

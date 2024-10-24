'use client';

import React from 'react';
import CreateStep from '@/components/knowledge/CreateStep';
import CreateFirst from '@/components/knowledge/CreateFirst';
import CreateSecond from '@/components/knowledge/CreateSecond';
import { useRecoilState } from 'recoil';
import { currentStepState } from '@/store/atoms'; 

const Page = () => {
  const [currentStep, setCurrentStep] = useRecoilState(currentStepState); // Recoil 상태 사용

  return (
      <div className="flex">
        <div className="w-60" style={{ height: 'calc(100vh - 60px)' }}>
          <CreateStep />
        </div>
        
        <div className="flex-grow">
          {currentStep === 1 ? <CreateFirst /> : (currentStep === 2 && <CreateSecond />)}
        </div>
      </div>
  );
}

export default Page;

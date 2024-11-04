'use client';

import CreateStep from '@/components/knowledge/CreateStep';
import CreateFirst from '@/components/knowledge/CreateFirst';
import CreateSecond from '@/components/knowledge/CreateSecond';
import { useRecoilState } from 'recoil';
import { currentStepState } from '@/store/atoms'; 
import { useRouter } from 'next/navigation'; 
import { IoCheckmarkCircle } from '@react-icons/all-files/io5/IoCheckmarkCircle';
import PurpleButton from '@/components/common/PurpleButton';

export default function Page() {
  const [currentStep, setCurrentStep] = useRecoilState(currentStepState); // Recoil 상태 사용
  const router = useRouter(); // useRouter 사용
  const goToListPage = (): void => {
    router.push('/knowledges'); 
    setCurrentStep(1)
  };

  return (
    <>
      <div className="flex relative">
        <div className="w-60" style={{ height: 'calc(100vh - 57px)' }}>
          <CreateStep />
        </div>

        <div className="flex-grow">
          {currentStep === 1 ? <CreateFirst /> : <CreateSecond />}
        </div>

        {currentStep === 3 && (
          <>
            <div className="fixed inset-0 bg-black bg-opacity-50 z-20"></div>
            
            <div className="fixed inset-0 z-30 flex items-center justify-center bg-black bg-opacity-20">
              <div className="bg-white p-6 rounded-lg shadow-lg w-[350px] h-[250px] flex flex-col items-center justify-center">
                <IoCheckmarkCircle className="text-[#9A75BF] w-16 h-16 mb-4"/> 
                <p className="text-[22px] font-bold mb-2 text-center">지식이 생성되었습니다</p> 
                <p className="text-[14px] text-gray-500 mb-4 text-center">지식 목록으로 이동합니다.</p> 
                <PurpleButton text="문서로 이동" onHandelButton={goToListPage}/> 
              </div>
            </div>
          </>
        )}
      </div>
    </>
  );
};

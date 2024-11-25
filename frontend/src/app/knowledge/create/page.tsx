'use client';
import { useQueryClient } from '@tanstack/react-query';
import CreateStep from '@/components/knowledge/CreateStep';
import CreateFirst from '@/components/knowledge/CreateFirst';
import CreateSecond from '@/components/knowledge/CreateSecond';
import { useRecoilState, useRecoilValue } from 'recoil';
import { currentStepState, fileState, isLoadingState } from '@/store/knoweldgeAtoms'; 
import { useRouter } from 'next/navigation'; 
import { IoCheckmarkCircle } from '@react-icons/all-files/io5/IoCheckmarkCircle';
import PurpleButton from '@/components/common/PurpleButton';
import { useEffect } from 'react';
import Loading from '@/components/common/Loading';

export default function Page() {
  const [currentStep,] = useRecoilState(currentStepState); 
  const router = useRouter(); 
  const [, setfile] = useRecoilState(fileState); 
  const isLoading = useRecoilValue(isLoadingState);



  useEffect(()=>{
    console.log(isLoading)

  },[isLoading])

  const goToListPage = () => {
    router.push('/knowledges');  
    setfile(null);
  };

  useEffect(() => {
    console.log('커런트 스텝: ' + currentStep);
  }, [currentStep]);

  return (
    <>
      <div className="flex relative">
        <div className="w-[270px] fixed top-[57px]" style={{ height: 'calc(100vh - 57px)' }}>
          <CreateStep />
        </div>

        <div className="ml-[270px] w-full">
          {currentStep !== 1 ? <CreateSecond /> : <CreateFirst />}
        </div>


        {/* 커런트 스텝이 2이고 로딩 중일 때 */}
        {currentStep === 2 && isLoading && (
          <>
            {/* 오버레이 */}
            <div className="fixed inset-0 bg-black bg-opacity-20 z-20"></div>

            {/* 로딩 모달 */}
            <div className="fixed inset-0 z-30 flex items-center justify-center">
              <div className="bg-white p-6 rounded-lg shadow-lg w-[350px] h-[250px] flex flex-col items-center justify-center">
                <Loading /> 
                <p className="text-[20px] font-bold mb-2 text-center">지식이 생성되는 중입니다</p>
                <p className="text-[13px] text-gray-500 mb-4 text-center">잠시만 기다려 주세요.</p>
              </div>
            </div>
          </>
        )}

        {/* 커런트 스텝이 3일 때 */}
        {currentStep === 3 && (
          <>
            {/* 오버레이 */}
            <div className="fixed inset-0 bg-black bg-opacity-20 z-20"></div>

            {/* 완료 모달 */}
            <div className="fixed inset-0 z-30 flex items-center justify-center">
              <div className="bg-white p-6 rounded-lg shadow-lg w-[350px] h-[250px] flex flex-col items-center justify-center">
                <IoCheckmarkCircle className="text-[#9A75BF] w-12 h-12 mb-4" />
                <p className="text-[20px] font-bold mb-2 text-center">지식이 생성되었습니다</p>
                <p className="text-[13px] text-gray-500 mb-4 text-center">지식 목록으로 이동합니다.</p>
                <PurpleButton text="문서로 이동" onHandelButton={goToListPage} />
              </div>
            </div>
          </>
        )}
      </div>
    </>
  );
};

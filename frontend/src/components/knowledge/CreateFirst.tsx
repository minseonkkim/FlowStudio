"use client";

import { useEffect } from 'react';
import { useRecoilState } from 'recoil';
import { currentStepState, fileNameState, fileState } from '@/store/knoweldgeAtoms';
import PurpleButton from '../common/PurpleButton';
import { IoCloudDownloadOutline } from '@react-icons/all-files/io5/IoCloudDownloadOutline';
import { CgTrash } from '@react-icons/all-files/cg/CgTrash';

export default function CreateFirst() {
  const [, setCurrentStep] = useRecoilState(currentStepState); 
  const [file, setFile] = useRecoilState(fileState);  
  const [fileName, setFileName] = useRecoilState(fileNameState);  

  // 파일 선택 시 호출되는 함수
  const handleFileSelect = (event: React.ChangeEvent<HTMLInputElement>) => {
    const selectedFile = event.target.files ? event.target.files[0] : null;
    setFile(selectedFile);
    setFileName(selectedFile ? selectedFile.name : '');
  };

  // 드래그앤드롭 이벤트 핸들러
  const handleDrop = (event: React.DragEvent<HTMLDivElement>) => {
    event.preventDefault();
    const droppedFile = event.dataTransfer.files[0];
    setFile(droppedFile);
    setFileName(droppedFile.name);
  };

  const handleDragOver = (event: React.DragEvent<HTMLDivElement>) => {
    event.preventDefault();
  };

  // 파일 초기화 (삭제)
  const clearFile = () => {
    setFile(null);
    setFileName('');
  };

  // 다음 단계로 이동
  const onChange2Step = () => {
    setCurrentStep(2);
  };

  useEffect(()=>{
    console.log(file)

  },[file])

  return (
    <div className="pl-10">
      <p className="text-2xl font-medium pt-14">데이터 소스 선택</p>
      <p className="text-base font-medium pt-14 pb-6 text-[#757575]">텍스트 파일 업로드</p>
      
      {!file ? (
        <>
          <label htmlFor="file-upload" className="cursor-pointer">
            <div className="w-[995px] h-[160px] bg-gray-200 border rounded-lg p-4 flex flex-col justify-center items-center gap-2 mb-6"
                onDrop={handleDrop}
                onDragOver={handleDragOver}>
              <div className='flex gap-2 justify-center items-center'>
                <IoCloudDownloadOutline className='h-8 w-8' />
                <p className="opacity-80 text-center">파일을 끌어다 놓거나 찾아보기</p>
                <input id="file-upload" type="file" className="hidden" onChange={handleFileSelect} />
              </div>
              <div className='text-center'>
                <p className="opacity-50 text-base">지원 파일 형식: txt, pdf, 등...</p>
                <p className="opacity-50 text-sm">파일당 최대 크기 15MB</p>
              </div>
            </div>
          </label>
   
          <p className="text-sm text-red-500 mb-2">파일을 추가해주세요</p>
          <button
            disabled
            className='py-2 px-4 text-[14px] bg-[#c4b2d6] text-white rounded-lg cursor-not-allowed'
          >
            다음
          </button>
        </>
      ) : (
        <>
          <div className='relative w-[830px] h-[55px] border rounded-lg flex items-center justify-between px-4 mb-6 group hover:bg-[#E1D5F2]'>
            <span className=' group-hover:text-[#757575]'>{fileName}</span>
            <CgTrash className='h-6 w-6 cursor-pointer hidden group-hover:flex text-[#9A75BF]' onClick={clearFile} />
          </div>
          <PurpleButton text='다음' onHandelButton={onChange2Step} />
        </>
      )}
    </div>
  );
}

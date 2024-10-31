'use client';

import { useState } from 'react';
import { IoCloudDownloadOutline } from '@react-icons/all-files/io5/IoCloudDownloadOutline';
import { CgTrash } from '@react-icons/all-files/cg/CgTrash';
import ColorButton from '../common/ColorButton';
import WhiteButton from '../common/whiteButton';
import { useRecoilState } from 'recoil';
import { currentStepState } from '@/store/atoms';
import { fileNameState } from '@/store/atoms';  

export default function CreateFirst() {
  const [file, setFile] = useState<File | null>(null);
  const [, setCurrentStep] = useRecoilState(currentStepState); 
  const [fileName, setFileName] = useRecoilState(fileNameState);  

  // 파일 선택 시 호출되는 함수
  const handleFileSelect = (event: React.ChangeEvent<HTMLInputElement>) => {
    const file = event.target.files ? event.target.files[0] : null;
    setFile(file);
    setFileName(file ? file.name : '');
  };

  // 드래그앤드롭 이벤트 핸들러
  const handleDrop = (event: React.DragEvent<HTMLDivElement>) => {
    event.preventDefault();
    event.stopPropagation();
    const file = event.dataTransfer.files[0];
    setFile(file);
    setFileName(file.name);
  };

  const handleDragOver = (event: React.DragEvent<HTMLDivElement>) => {
    event.preventDefault();
    event.stopPropagation();
  };

  // 파일 초기화 (삭제)
  const clearFile = () => {
    setFile(null);
    setFileName('');
  };

  // 단계 변경 함수 (Step 2로 이동)
  const onChange2Step = () => {
    setCurrentStep(2);
  };

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
                  찾아보기
                <input id="file-upload" type="file" className="hidden" onChange={handleFileSelect} />
              </div>
              <div className='text-center'>
                <p className="opacity-50 text-base">TXT, MARKDOWN, PDF, HTML, XLSX, XLS, DOCX, CSV, EML, MSG, PPTX, PPT, XML, EPUB을(를) 지원합니다.</p>
                <p className="opacity-50 text-sm">파일당 최대 크기는 15MB입니다.</p>
              </div>
            </div>
          </label>
          <WhiteButton w='80px' h='40px' text='다음' borderColor='#9A75BF' textColor='#9A75BF'/>
        </>
      ) : (
        <>
          <div className='relative w-[830px] h-[55px] border rounded-lg flex items-center justify-between px-4 mb-6 group hover:bg-[#E1D5F2]'>
            <span className=' group-hover:text-[#757575]'>{fileName}</span>
            <CgTrash className='h-6 w-6 cursor-pointer hidden group-hover:flex text-[#9A75BF]' onClick={clearFile} />
          </div>
          <ColorButton w='80px' h='40px' text='다음' onHandelButton={onChange2Step} />
        </>
      )}
    </div>
  );
}

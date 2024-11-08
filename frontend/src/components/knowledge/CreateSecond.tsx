'use client';

import { useMutation, useQueryClient } from '@tanstack/react-query';
import { useState, useEffect } from 'react';
import { VscSettings } from '@react-icons/all-files/vsc/VscSettings';
import { FaFile } from '@react-icons/all-files/fa/FaFile';
import { TiDeleteOutline } from '@react-icons/all-files/ti/TiDeleteOutline';
import { IoIosInformationCircleOutline } from '@react-icons/all-files/io/IoIosInformationCircleOutline';
import { useRecoilState } from 'recoil';
import { fileNameState, fileState } from '@/store/knoweldgeAtoms';
import { currentStepState } from '@/store/knoweldgeAtoms';
import WhiteButton from '../common/whiteButton';
import { Tooltip } from 'react-tooltip';
import PurpleButton from '../common/PurpleButton';
import { postKnowledge, postKnowledgeChunk } from "@/api/knowledge";
import { ChunkList, KnowledgeData } from '@/types/knowledge'
import { useRouter } from 'next/navigation';


export default function CreateSecond() {
  const [segmentIdentifier, setSegmentIdentifier] = useState<string>('\\n\\n');  
  const [maxChunkLength, setMaxChunkLength] = useState<number>(500); 
  const [file] = useRecoilState(fileState); 
  const [chunkOverlap, setChunkOverlap] = useState<number>(50); 
  const [predictedChunkCount, setPredictedChunkCount] = useState<number>(0); 
  const [chunks, setChunks] = useState<ChunkList[]>([])
  const [fileName] = useRecoilState(fileNameState); 
  const [, setCurrentStep] = useRecoilState(currentStepState); 
  const [isPreviewOpen, setIsPreviewOpen] = useState(false); 
	const queryClient = useQueryClient();
  const router = useRouter()


  useEffect(() => {
    if (!file) return;
    createChunkMutation.mutate({
      file,
      chunkSize: maxChunkLength.toString(),
      chunkOverlap: chunkOverlap.toString(),
      separator: segmentIdentifier,
    });
  }, [file]);

  // 미리보기 버튼 클릭 시 청크 요청
  const onChangePreview = () => {
    if (!file) return;
    setIsPreviewOpen(true);
    createChunkMutation.mutate({
      file,
      chunkSize: maxChunkLength.toString(),
      chunkOverlap: chunkOverlap.toString(),
      separator: segmentIdentifier,
    });
  };

  // 지식 등록 후 다음 단계로 이동
  const onChange3Step = () => {
    if (!file) {
      alert("파일을 업로드해주세요.");
      return;
    }

    createMutation.mutate({
      file,
      chunkSize: maxChunkLength.toString(),
      chunkOverlap: chunkOverlap.toString(),
      separator: segmentIdentifier,
    });
    queryClient.invalidateQueries({ queryKey: ["knowledgeList"] }).then(() => {
      router.refresh();  
    })
    setCurrentStep(3);
  };

  const onChangeBack = () => {
    setCurrentStep(1);
  };

  const createMutation = useMutation({
    mutationFn: postKnowledge,
    onSuccess: (newKnowledge: KnowledgeData) => {
      queryClient.setQueryData<KnowledgeData[]>(['knowledgeList'], (oldData = []) => [
        ...oldData,
        newKnowledge
      ]);
      setCurrentStep(3); 
    },
    onError: () => {
      alert("지식 생성에 실패했습니다. 다시 시도해 주세요.");
    },
  });;
  

  // 청크 미리보기 요청 및 결과 처리
  const createChunkMutation = useMutation({
    mutationFn: postKnowledgeChunk,
    onSuccess: (data) => {
      setPredictedChunkCount(data.chunkCount); 
      setChunks(data.chunkList); 
    },
    onError: () => {
      alert("청크 미리보기에 실패했습니다. 다시 시도해 주세요.");
    },
  });

  return (
    <>
      <div className="pl-10 flex relative">
        <div className="flex-grow">
          <p className="text-2xl font-medium pt-14 pb-8">텍스트 전처리 및 클리닝</p>
          <div className="border border-[#5D2973] w-[640px] h-[425px] rounded-xl">
            <div className="p-4 flex gap-1">
              <div className="border w-[33px] h-[33px] bg-[#E1D5FE] rounded-lg relative mr-4">  
                <VscSettings className="absolute top-2 left-2" />
              </div>
              <div>
                <p className="font-semibold">사용자 설정</p> 
                <p className="text-sm text-gray-500">
                  청크 규칙, 청크 길이, 전처리 규칙 등을 사용자 정의합니다.
                </p>
              </div>
            </div>
            <div className="border-b border-b-[#EAECF0]"></div>

            <div className='flex flex-col items-center text-base font-normal mt-4'>
              <div className="w-[490px]">
                <div className='flex gap-2 items-center'>
                  <p>세그먼트 식별자</p>
                  <IoIosInformationCircleOutline 
                    data-tooltip-id="info-tooltip" 
                    className='w-4 h-4 mt-1 text-[#4F4F4F]' 
                  />
                  <Tooltip 
                    id="info-tooltip" 
                    content="구분 기호는 텍스트를 구분하는 데 사용되는 문자입니다." 
                    style={{
                      backgroundColor: 'white',
                      color: 'black',
                      border: '1px solid #D1D5DB', 
                      boxShadow: '0 10px 15px rgba(0, 0, 0, 0.1), 0 4px 6px rgba(0, 0, 0, 0.05)', 
                      borderRadius: '0.375rem',  
                      padding: '0.5rem 0.75rem', 
                      width: '15rem'            
                    }}
                  />
                </div>
                <input 
                  value={segmentIdentifier} 
                  onChange={(e) => setSegmentIdentifier(e.target.value)}  
                  type="text" 
                  spellCheck="false" 
                  className='pl-4 border w-full h-[40px] rounded-md bg-[#D9D9D9] opacity-50 mt-2 focus:bg-white focus:border-gray-400 focus:outline-none' 
                />
              </div>
              <div className="w-[490px] mt-4">
                <p>최대 청크 길이</p>
                <input 
                  value={maxChunkLength}
                  onChange={(e) => setMaxChunkLength(parseInt(e.target.value, 10))}  
                  type="number" 
                  spellCheck="false" 
                  className='pl-4 border w-full h-[40px] rounded-md bg-[#D9D9D9] opacity-50 mt-2 focus:bg-white focus:border-gray-400 focus:outline-none' 
                />
              </div>
              <div className="w-[490px] mt-4">
                <div className='flex gap-2 items-center'>
                  <p>청크 중첩</p>
                  <IoIosInformationCircleOutline 
                    data-tooltip-id="info-tooltip1" 
                    className="w-4 h-4 mt-1 text-[#4F4F4F]" 
                  />
                  <Tooltip 
                    id="info-tooltip1" 
                    content="청크 중첩은 데이터 청크가 겹쳐지는 부분을 설정하여 텍스트 처리 및 검색결과의 일관성과 정확성을 높입니다." 
                    style={{
                      backgroundColor: 'white',
                      color: 'black',
                      border: '1px solid #D1D5DB', 
                      boxShadow: '0 10px 15px rgba(0, 0, 0, 0.1), 0 4px 6px rgba(0, 0, 0, 0.05)', 
                      borderRadius: '0.375rem',  
                      padding: '0.5rem 0.75rem', 
                      width: '13rem'            
                    }}
                  />
                </div>
                <input 
                  value={chunkOverlap}
                  onChange={(e) => setChunkOverlap(parseInt(e.target.value, 10))} 
                  type="number" 
                  spellCheck="false" 
                  className='pl-4 border w-full h-[40px] rounded-md bg-[#D9D9D9] opacity-50 mt-2 focus:bg-white focus:border-gray-400 focus:outline-none' 
                />
              </div>
              <div className="w-[490px] mt-4">
                <PurpleButton text='미리보기' onHandelButton={onChangePreview}/>
              </div>
            </div>
          </div>
          <div className='w-[640px] h-[72px] rounded-lg mt-10 bg-[rgba(217,217,217,0.2)] shadow-md flex items-center justify-between p-4'>
            <div className="flex items-center">
              <div className='py-3'>
                <p className="text-[#757575]">문서 전처리</p>
                <div className='flex gap-2 mt-2'>
                  <FaFile className='text-[#757575]' />
                  <p className="text-sm text-[#757575]"> - {fileName}</p> 
                </div>
              </div>
            </div>
            <div className="flex items-center border-l pl-4">
              <div>
                <p className="text-sm text-[#757575]">예상 청크 수</p> 
                <p className="text-lg font-semibold text-[#757575]">{predictedChunkCount}</p> 
              </div>
            </div>
          </div>
          <div className='flex gap-3 mt-6 mb-8'>
            <WhiteButton text='이전' onHandelButton={onChangeBack}/>
            <PurpleButton text='저장하고 처리' onHandelButton={onChange3Step}/>
          </div>
        </div>

        {isPreviewOpen && (
          <div className="w-[520px] h-full bg-white shadow-lg absolute right-0 top-0 p-4">
            <div className="p-4 flex justify-between items-center">
              <p className="font-bold">미리보기</p>
              <TiDeleteOutline className='w-6 h-6' onClick={() => setIsPreviewOpen(false)}/>
            </div>

            <div className='overflow-y-auto max-h-[calc(100vh-150px)] pt-4'> 
              {chunks.map((chunk) => (
                <div key={chunk.chunkId} className="mb-4 p-4 w-[420px] min-h-[180px] h-auto rounded-lg bg-[rgba(217,217,217,0.2)] shadow-md">
                  <div className='border border-gray-400 w-[60px] h-[24px] rounded-lg text-center'>
                    <p># {String(chunk.chunkId).padStart(3, '0')}</p>
                  </div>
                  <p className="mt-4">{chunk.content}</p>
                </div>
              ))}
            </div>
          </div>
        )}
      </div>
    </>
  );
}

// 모달 컴포넌트
'use client';
import { useQuery, useMutation, useQueryClient } from '@tanstack/react-query';
import { ChunkData, ChunkList } from "@/types/knowledge";
import { getChunkDetail, postChunkDetail } from '@/api/knowledge';
import React, { useRef, useEffect, useState } from 'react';
import { HiOutlinePencilAlt } from '@react-icons/all-files/hi/HiOutlinePencilAlt';
import { BsTextareaT } from '@react-icons/all-files/bs/BsTextareaT';
import { TiDeleteOutline } from '@react-icons/all-files/ti/TiDeleteOutline';

interface ChunkDetailModalProps {
  knowledgeId: number;
  chunkId: string;
  onClose: () => void;
}

export default function ChunkDetailModal({
  knowledgeId,
  chunkId,
  onClose,
}: ChunkDetailModalProps) {
  const textareaRef = useRef<HTMLTextAreaElement>(null);
  const [isEditing, setIsEditing] = useState<boolean>(false);
  const [chunkContent, setChunkContent] = useState<string>(''); 
  const [originalContent, setOriginalContent] = useState<string>(''); 
  const [textLength, setTextLength] = useState<number>(0);

  const queryClient = useQueryClient();

  useEffect(() => {
    if (isEditing && textareaRef.current) {
      textareaRef.current.focus();
    }
  }, [isEditing]);

  const handleEditClick = () => {
    setOriginalContent(chunkContent); 
    setIsEditing(true);
  };

  const { isLoading, isError, error, data: chunkdetail } = useQuery<ChunkList>({
    queryKey: ['chunkdetail', knowledgeId, chunkId],
    queryFn: () => getChunkDetail(knowledgeId, chunkId), 
  });

  useEffect(() => {
    if (chunkdetail) {
      setChunkContent(chunkdetail.content); 
      setTextLength(chunkdetail.content.length);
    }
  }, [chunkdetail]);

  useEffect(() => {
    setTextLength(chunkContent.length);
  }, [chunkContent]);

  useEffect(() => {
    if (isError && error) {
      alert("청크 세부내용을 불러오는 중 오류가 발생했습니다. 다시 시도해 주세요.");
    }
  }, [isError, error]);

  const formattedContentId = chunkId.padStart(3, '0'); 

  const updateMutation = useMutation({
    mutationFn: ({ knowledgeId, chunkId, data }: { knowledgeId: number, chunkId: string; data: { content: string } }) =>
      postChunkDetail(knowledgeId, chunkId, data),
    onSuccess: (_, variables) => {
      queryClient.setQueryData(['chunkdetail', knowledgeId, chunkId], (oldData: ChunkList | undefined) => {
        if (oldData) {
          return { ...oldData, content: variables.data.content };
        }
        return { chunkId: parseInt(chunkId), content: variables.data.content }; 
      });

      queryClient.setQueryData(['chunklist', knowledgeId], (oldList: ChunkData | undefined) => {
        if (oldList) {
          return {
            ...oldList,
            chunkList: oldList.chunkList.map((item: ChunkList) =>
              item.chunkId === parseInt(chunkId)
                ? { ...item, content: variables.data.content }
                : item
            ),
          };
        }
        return oldList;
      });
      setChunkContent(variables.data.content);
    },
    onError: () => {
      alert("청크 내용 수정에 실패했습니다. 다시 시도해 주세요.");
    },
  });

  const handleCancelClick = () => {
    setIsEditing(false);
    setChunkContent(originalContent); 
  };

  const handleUpdateClick = () => {
    setIsEditing(false);
    const contentData = {                                                                                                             
      content: chunkContent
    };
    updateMutation.mutate({ knowledgeId: knowledgeId, chunkId: chunkId, data: contentData });
  };

  if (isLoading) return <></>;

  return (
    <div className="fixed inset-0 flex items-center justify-center z-50">
      <div className="absolute inset-0 bg-black opacity-50" onClick={onClose}></div>
      <div className="relative bg-white w-[450px] h-[530px] p-6 rounded-lg shadow-lg">
        <div className="flex justify-between items-center mb-4">
          <div className="border border-gray-400 w-[55px] h-[24px] rounded-lg text-center"># {formattedContentId}</div>
          <div className='flex gap-3'>
            {isEditing ? (
              <>
                <button 
                  className='py-1 px-2 border border-[#9A75BF] text-[#9A75BF] hover:bg-[#f3e8ff] active:bg-[#e3d1f7] rounded-md text-sm'
                  onClick={handleCancelClick}>
                  취소
                </button>
                <button
                  className='py-1 px-2 border rounded-md text-sm bg-[#9A75BF] text-white hover:bg-[#874aa5] active:bg-[#733d8a]'
                  onClick={handleUpdateClick}>
                  수정
                </button>
              </>
            ) : (
              <button onClick={handleEditClick} className="text-gray-500 hover:text-gray-700">
                <HiOutlinePencilAlt className='w-5 h-5'/>
              </button>
            )}
            <div className="text-gray-500">|</div>
            <TiDeleteOutline className="text-gray-500 w-6 h-6" onClick={onClose}/>
          </div>
        </div>
        {!isEditing ? (
          <p className="text-base pt-4 w-full h-[400px]">{chunkContent}</p>
        ) : (
          <textarea
            ref={textareaRef}
            value={chunkContent}
            spellCheck="false"
            onChange={(e) => {
              setChunkContent(e.target.value);
            }}
            className="text-base pt-4 w-full h-[400px] resize-none focus:outline-none overflow-y-auto"
          />
        )}        
        <hr />
        <div className='flex gap-3 items-center mt-2'>
          <div className='flex gap-2 items-center'>
            <BsTextareaT className='text-gray-500 w-4 h-4'/>
            <p className='text-gray-500 text-[15px]'>{textLength} 문자</p> 
          </div>
        </div>
      </div>
    </div>
  );
}

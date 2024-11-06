'use client';
import { useQuery, useMutation, useQueryClient } from '@tanstack/react-query';
import { ChunkList } from "@/types/knowledge";
import { getChunkDetail, postChunkDetail } from '@/api/knowledge';
import React, { useRef, useEffect, useState } from 'react';
import { HiOutlinePencilAlt } from '@react-icons/all-files/hi/HiOutlinePencilAlt';
import { BsTextareaT } from '@react-icons/all-files/bs/BsTextareaT';
import { FiTarget } from '@react-icons/all-files/fi/FiTarget';

interface ChunkDetailModalProps {
  knowledgeId : string ;
  chunkId: string;
  onClose: () => void;
  // onEdit: () => void;
  // isEditing: boolean;
  editedContent: string;
  onSave: () => void;
}

export default function ChunkDetailModal({
  knowledgeId,
  chunkId,
  onClose,
  // onEdit,
  // isEditing,
  editedContent,
  onSave
}: ChunkDetailModalProps) {
  const textareaRef = useRef<HTMLTextAreaElement>(null);
  const [content, setContent] = useState<string>(editedContent);
  const [isEditing, setIsEditing] = useState<boolean>(false);

  useEffect(() => {
    if (isEditing && textareaRef.current) {
      textareaRef.current.focus();
    }
  }, [isEditing]);

  const handleEditClick = () => {
    setIsEditing(true);
    // onEdit();
  };

  const handleSaveClick = () => {
    onSave();
  };

  const handleCancelClick = () => {
    onSave();
  };

  const { isLoading, isError, error, data: chunkdetail } = useQuery<ChunkList>({
    queryKey: ['chunkdetail', knowledgeId, chunkId],
    queryFn: () => getChunkDetail(knowledgeId, chunkId), 
  });

  useEffect(() => {
    if (isError && error) {
      alert("청크 세부내용을 불러오는 중 오류가 발생했습니다. 다시 시도해 주세요.");
    }
  }, [isError, error]);

  
  // // chunkdetail가 로드된 후 조건부로 textLength를 설정
  // const textLength = chunkdetail?.content.length || 0;
  // const [searchCount] = useState(0); 
  const formattedContentId = chunkId.padStart(3, '0');
  
  const queryClient = useQueryClient();
  
  const updateMutation = useMutation({
    mutationFn: ({ knowledgeId, chunkId, data }: { knowledgeId: string, chunkId: string; data: {content: string} }) =>
      postChunkDetail(knowledgeId, chunkId, data),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ['chunkdetail', knowledgeId, chunkId] });
      queryClient.invalidateQueries({ queryKey: ['chunklist', knowledgeId] });
    },
    onError: () => {
      alert("청크 내용 수정에 실패했습니다. 다시 시도해 주세요.");
    },
  });
  
  const handleUpdateClick = () => {
    // onEdit();
    setIsEditing(false);
    const contentData = {
      content: content
    }
    updateMutation.mutate({ knowledgeId: knowledgeId, chunkId: chunkId, data: contentData });
    onSave()
 };
  
  // 로딩 중일 때 다른 훅이 실행되지 않도록 초기 반환
  if (isLoading) return <div>Loading...</div>;
  

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
            <button className="text-gray-500" onClick={onClose}>X</button>
          </div>
        </div>
        {!isEditing ? (
          <p className="text-base pt-4 w-full h-[400px]">{chunkdetail?.content}</p>
        ) : (
          <textarea
            ref={textareaRef}
            value={content}
            spellCheck="false"
            onChange={(e) => {
              setContent(e.target.value);
              // setTextLength(e.target.value.length);
            }}
            className="text-base pt-4 w-full h-[400px] resize-none focus:outline-none overflow-y-auto"
          />
          
        )}        
        <hr />
        <div className='flex gap-3 items-center mt-2'>
          <div className='flex gap-2 items-center'>
            <BsTextareaT className='text-gray-500 w-4 h-4'/>
            {/* <p className='text-gray-500 text-[15px]'>{textLength} 문자</p>  */}
          </div>
          <div className='flex gap-2 items-center'>
            <FiTarget className='text-gray-500 w-4 h-4'/>
            {/* <p className='text-gray-500 text-[15px]'>{searchCount} 검색 횟수</p> */}
          </div>
        </div>
      </div>
    </div>
  );
}

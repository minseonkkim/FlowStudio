'use client'
import React, { useRef, useEffect, useState } from 'react';
import { HiOutlinePencilAlt } from '@react-icons/all-files/hi/HiOutlinePencilAlt';
import { BsTextareaT } from '@react-icons/all-files/bs/BsTextareaT';
import { FiTarget } from '@react-icons/all-files/fi/FiTarget';
import ColorButton from '../common/ColorButton';
import WhiteButton from '../common/whiteButton';

interface ChunkDetailModalProps {
  contentId: string;
  content: string;
  onClose: () => void;
  onEdit: () => void;
  isEditing: boolean;
  editedContent: string;
  onChangeEditedContent: (content: string) => void;
  onSave: () => void;
}

export default function ChunkDetailModal({
  contentId,
  content,
  onClose,
  onEdit,
  isEditing,
  editedContent,
  onChangeEditedContent,
  onSave
}: ChunkDetailModalProps) {
  const textareaRef = useRef<HTMLTextAreaElement>(null);

  useEffect(() => {
    if (isEditing && textareaRef.current) {
      textareaRef.current.focus();
    }
  }, [isEditing]);

  const [textLength, setTextLength] = useState(content.length); // 초기 글자 수
  const [searchCount, setSearchCount] = useState(0); // 검색횟수

  const handleEditClick = () => {
    onEdit();
  };

  const handleSaveClick = () => {
    onSave();
  };

  const handleCancelClick = () => {
    onChangeEditedContent(content); 
    onSave();
  };

  return (
    <div className="fixed inset-0 flex items-center justify-center z-50">
      <div className="absolute inset-0 bg-black opacity-50" onClick={onClose}></div>
      <div className="relative bg-white w-[450px] h-[530px] p-6 rounded-lg shadow-lg">
        <div className="flex justify-between items-center mb-4">
          <div className="border border-gray-400 w-[55px] h-[24px] rounded-lg text-center">{contentId}</div>
          <div className='flex gap-3'>
            {isEditing ? (
              <>
              <button 
                className='py-1 px-2 border border-gray-800 rounded-md text-sm'
                onClick={handleCancelClick}>
                취소
              </button>
              <button
                className='py-1 px-2 border rounded-md text-sm text-white bg-gray-800 border-gray-800'
                onClick={handleSaveClick}>
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
          <p className="text-base pt-4 w-full h-[400px]">{content}</p>
        ) : (
          <textarea
            ref={textareaRef}
            value={editedContent}
            spellCheck="false"
            onChange={(e) => {
              onChangeEditedContent(e.target.value);
              setTextLength(e.target.value.length);
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
          <div className='flex gap-2 items-center'>
            <FiTarget className='text-gray-500 w-4 h-4'/>
            <p className='text-gray-500 text-[15px]'>{searchCount} 검색 횟수</p>
          </div>
        </div>
      </div>
    </div>
  );
}

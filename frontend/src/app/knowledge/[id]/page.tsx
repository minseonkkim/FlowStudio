'use client'

import React, { useState } from 'react';
import Search from '@/components/common/Search';
import ChunkDetail from '@/components/knowledge/ChunkDetaill';
import ChunkDetailModal from '@/components/knowledge/ChunkDetailModal';
import { IoIosArrowBack  } from '@react-icons/all-files/io/IoIosArrowBack';
import { useRouter } from 'next/navigation';


const data = [
  { id: '#001', content: '지구 온난화 문제 해결을 위한 다양한 방안을 모색합니다.' },
  { id: '#002', content: 'AI의 발전이 우리 생활에 미치는 영향에 대해 알아봅니다.' },
  { id: '#003', content: '대체 에너지원의 가능성과 미래에 대해 논의합니다.' },
  { id: '#004', content: '우주 탐사가 인류에게 가져다줄 이점과 그 한계를 분석합니다.' },
  { id: '#005', content: '물 부족 문제와 그로 인한 사회적 영향에 대해 연구합니다.' },
  { id: '#006', content: '기후 변화가 농업에 미치는 영향에 대해 알아봅니다.' }
];

const Page = () => {
  const [searchTerm, setSearchTerm] = useState('');
  const [isModalOpen, setModalOpen] = useState(false);
  const [selectedContentId, setSelectedContentId] = useState('')
  const [selectedContent, setSelectedContent] = useState('');
  const [isEditing, setEditing] = useState(false);
  const [editedContent, setEditedContent] = useState('');
  const router = useRouter()

  const handleSearchChange = (term: string) => {
    setSearchTerm(term);
  };

  const handleOpenModal = (content: string, id: string) => {
    setSelectedContentId(id)
    setSelectedContent(content);
    setEditedContent(content);
    setModalOpen(true);
  };

  const handleCloseModal = () => {
    setModalOpen(false);
    setSelectedContentId('')
    setSelectedContent('');
    setEditing(false);
  };

  const handleEditClick = () => {
    setEditing(true);
  };

  const handleSave = () => {
    setSelectedContent(editedContent);
    setEditing(false);
  };

  const filteredData = data.filter((item) =>
    item.content.toLowerCase().includes(searchTerm.toLowerCase())
  );

  return (
    <div className="px-12 py-10 relative">
      <div className='flex gap-4 items-center'>
        <IoIosArrowBack className='w-6 h-6 ' onClick={()=>router.push('/knowledges')}/>
        <p className="font-semibold text-[24px] text-gray-700">파일이름 1</p>
      </div>
      <div className="flex mb-4 xl:justify-end justify-center mt-4">
        <Search onSearchChange={handleSearchChange} />
      </div>

      <ChunkDetail data={filteredData} onItemSelect={handleOpenModal} />
      {isModalOpen && (
        <ChunkDetailModal
          contentId = {selectedContentId}
          content={selectedContent}
          onClose={handleCloseModal}
          onEdit={handleEditClick}
          isEditing={isEditing}
          editedContent={editedContent}
          onChangeEditedContent={setEditedContent}
          onSave={handleSave}
        />
      )}
    </div>
  );
};

export default Page;

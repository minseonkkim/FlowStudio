'use client';

import { useQuery } from '@tanstack/react-query';
import { getAllChunks } from "@/api/knowledge";
import { ChunkData } from "@/types/knowledge";
import React, { useState, useEffect } from 'react';
import Search from '@/components/common/Search';
import ChunkDetail from '@/components/knowledge/ChunkDetaill';
import ChunkDetailModal from '@/components/knowledge/ChunkDetailModal';
import { IoIosArrowBack } from '@react-icons/all-files/io/IoIosArrowBack';
import { useRouter } from 'next/navigation';

const Page = () => {
  const knowledgeId = typeof window !== "undefined" ? window.location.pathname.split("/").pop() ?? null : null;
  const [searchTerm, setSearchTerm] = useState('');
  const [isModalOpen, setModalOpen] = useState(false);
  const [selectedContentId, setSelectedContentId] = useState('');
  // const [selectedContent, setSelectedContent] = useState('');
  const [isEditing, setEditing] = useState(false);
  const [editedContent, setEditedContent] = useState('');
  const router = useRouter();

  const { isLoading, isError, error, data: chunklist } = useQuery<ChunkData>({
    queryKey: ['chunklist', knowledgeId],
    queryFn: () => {
      if (knowledgeId !== "undefined" ) {
        return getAllChunks(String(knowledgeId));
      }
      return Promise.reject("Invalid knowledge ID");
    },
    enabled: knowledgeId !== "undefined" 
  });
  
  useEffect(() => {
    if (isError && error) {
      alert("청크목록을 불러오는 중 오류가 발생했습니다. 다시 시도해 주세요.");
    }
  }, [isError, error]);

  if (isLoading) return <div>is Loading...</div>;

  const handleSearchChange = (term: string) => {
    setSearchTerm(term);
  };

  const handleOpenModal = (content: string, id: string) => {
    setSelectedContentId(id);
    // setSelectedContent(content);
    setEditedContent(content);
    setModalOpen(true);
  };

  const handleCloseModal = () => {
    setModalOpen(false);
    setSelectedContentId('');
    // setSelectedContent('');
    setEditing(false);
  };

  const handleEditClick = () => {
    setEditing(true);
  };

  const handleSave = () => {
    // setSelectedContent(editedContent);
    setEditing(false);
  };

  const filteredData = chunklist?.chunkList.map(item => ({
    id: item.chunkId.toString(), 
    content: item.content
  })).filter((item) =>
    item.content.toLowerCase().includes(searchTerm.toLowerCase())
  ) || [];

  return (
    <div className="px-12 py-10 relative">
      <div className="flex gap-4 items-center">
        <IoIosArrowBack className="w-6 h-6" onClick={() => router.push('/knowledges')} />
        <p className="font-semibold text-[24px] text-gray-700">파일이름 1</p>
      </div>
      <div className="flex mb-4 xl:justify-end justify-center mt-4">
        <Search onSearchChange={handleSearchChange} />
      </div>

      <ChunkDetail data={filteredData} onItemSelect={handleOpenModal} />
      {isModalOpen && (
        <ChunkDetailModal
          knowledgeId={knowledgeId ?? ""}
          chunkId={selectedContentId}
          onClose={handleCloseModal}

          // contentId={selectedContentId}
          // content={selectedContent}
          // onEdit={handleEditClick}
          // isEditing={isEditing}
          editedContent={editedContent}
          // onChangeEditedContent={setEditedContent}
          onSave={handleSave}
        />
      )}
    </div>
  );
};

export default Page;

'use client';

import { useQuery, useMutation, useQueryClient } from '@tanstack/react-query';
import { getAllChunks, deleteChunkDetail } from "@/api/knowledge";
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
  const router = useRouter();

  const queryClient = useQueryClient();
  
  const { isLoading, isError, error, data: chunklist } = useQuery<ChunkData>({
    queryKey: ['chunklist', knowledgeId],
    queryFn: () => {
      if (knowledgeId !== "undefined") {
        return getAllChunks(String(knowledgeId));
      }
      return Promise.reject("Invalid knowledge ID");
    },
    enabled: knowledgeId !== "undefined"
  });

  const deleteMutation = useMutation({
    mutationFn: (chunkId: string) => deleteChunkDetail(knowledgeId as string, chunkId),
    onSuccess: (_, variables) => {
      queryClient.setQueryData(['chunklist', knowledgeId], (oldData: ChunkData | undefined) => {
        if (!oldData) return oldData;
        return {
          ...oldData,
          chunkList: oldData.chunkList.filter(chunk => chunk.chunkId.toString() !== variables)
        };
      });
    },
    onError: () => {
      alert("청크 삭제에 실패했습니다. 다시 시도해 주세요.");
    },
  });

  const handleDelete = (chunkId: string) => {
    deleteMutation.mutate(chunkId);
  };

  useEffect(() => {
    if (isError && error) {
      alert("청크목록을 불러오는 중 오류가 발생했습니다. 다시 시도해 주세요.");
    }
  }, [isError, error]);

  if (isLoading) return <div>Loading...</div>;

  const handleSearchChange = (term: string) => {
    setSearchTerm(term);
  };

  const handleOpenModal = (id: string) => {
    setSelectedContentId(id);
    setModalOpen(true);
  };

  const handleCloseModal = () => {
    setModalOpen(false);
  };

  const filteredData = chunklist?.chunkList
    .map(item => ({
      id: item.chunkId.toString(), 
      content: item.content
    }))
    .filter((item) =>
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

      <ChunkDetail data={filteredData} onItemSelect={(id) => handleOpenModal(id)} onDelete={handleDelete} />
      
      {isModalOpen && (
        <ChunkDetailModal
          knowledgeId={knowledgeId ?? ""}
          chunkId={selectedContentId}
          onClose={handleCloseModal}
        />
      )}
    </div>
  );
};

export default Page;

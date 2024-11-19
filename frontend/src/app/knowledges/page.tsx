"use client";

import { useQuery, useMutation, useQueryClient } from '@tanstack/react-query';
import { useState, useEffect, useRef } from 'react';
import { useRouter } from 'next/navigation';
import Search from '@/components/common/Search';
import PurpleButton from '@/components/common/PurpleButton';
import { getAllKnowledges, putDocKnowledge, deleteKnowledge } from "@/api/knowledge";
import { KnowledgeData, KnowledgeIsPublic } from "@/types/knowledge";
import { useRecoilState } from 'recoil';
import { chunkFileNameState } from '@/store/knoweldgeAtoms';
import { currentStepState } from '@/store/knoweldgeAtoms'; 
import Loading from '@/components/common/Loading';

export default function Page() {
  const [searchTerm, setSearchTerm] = useState('');
  const [, setKnowledgeTitle] = useRecoilState(chunkFileNameState);
  const [, setCurrentStepState] = useRecoilState(currentStepState);
  const router = useRouter();
  const queryClient = useQueryClient();
  const observerTarget = useRef(null);

  const [currentPage, setCurrentPage] = useState(1);
  const itemsPerPage = 12;
  const [displayedData, setDisplayedData] = useState<KnowledgeData[]>([]);

  const { isLoading, isError, error, data: knowledgeList } = useQuery<KnowledgeData[]>({
    queryKey: ['knowledgeList'],
    queryFn: getAllKnowledges,
  });

  useEffect(() => {
    if (isError && error) {
      alert("지식목록을 불러오는 중 오류가 발생했습니다. 다시 시도해 주세요.");
    }
  }, [isError, error]);

  useEffect(() => {
    if (knowledgeList) {
      const newItems = knowledgeList.slice(0, currentPage * itemsPerPage);
      setDisplayedData(newItems);
    }
  }, [knowledgeList, currentPage]);

  const putMutation = useMutation({
    mutationFn: ({ knowledgeId, data }: { knowledgeId: number; data: KnowledgeIsPublic }) =>
      putDocKnowledge(knowledgeId, data),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ["knowledgeList"] });
    },
    onError: () => {
      alert("문서 수정에 실패했습니다. 다시 시도해 주세요.");
    },
  });

  const deleteMutation = useMutation({
    mutationFn: deleteKnowledge,
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ["knowledgeList"] });
    },
    onError: () => {
      alert("문서 삭제에 실패했습니다. 다시 시도해 주세요.");
    },
  });

  useEffect(() => {
    const observer = new IntersectionObserver((entries) => {
      if (entries[0].isIntersecting && knowledgeList && displayedData.length < knowledgeList.length) {
        setCurrentPage((prev) => prev + 1);
      }
    }, {
      threshold: 1.0,
    });

    if (observerTarget.current) {
      observer.observe(observerTarget.current);
    }

    return () => {
      if (observerTarget.current) {
        observer.unobserve(observerTarget.current);
      }
    };
  }, [observerTarget, knowledgeList, displayedData]);

  if (isLoading) return <Loading/>;

  const goToCreatePage = (): void => {
    setCurrentStepState(1)
    router.push('/knowledge/create');
  };

  const goToKnowledgeDetail = (knowledgeId: string, title: string): void => {
    setKnowledgeTitle(title);
    router.push(`/knowledge/${knowledgeId}`);
  };

  const togglePublicStatus = (file: KnowledgeData) => {
    const knowledgeData = {
      title: file.title,
      isPublic: !file.isPublic,
    };
    putMutation.mutate({ knowledgeId: file.knowledgeId, data: knowledgeData });
  };

  const handleDeleteClick = (knowledgeId: number) => {
    deleteMutation.mutate(knowledgeId);
  };

  const filteredData = displayedData?.filter((file) =>
    file.title.toLowerCase().includes(searchTerm.toLowerCase())
  );

  return (
    <div className="px-4 md:px-12 py-10">
      <div className="flex gap-2 mb-4">
        <p className="font-semibold text-[24px] text-gray-700 mr-6">문서</p>
        <PurpleButton text="파일 추가" onHandelButton={goToCreatePage} />
      </div>

      <div className="flex flex-col lg:flex-row justify-between mb-6 lg:mb-8">
        <p className="text-sm lg:text-base text-[#757575] mb-4 lg:mb-0 lg:pt-2">
          지식의 모든 파일이 여기에 표시되며, 전체 지식이 FlowStudio의 인용문이나 챗 플러그인을 통해 링크되거나 색인화될 수 있습니다.
        </p>
        <Search onSearchChange={setSearchTerm} />
      </div>

      <div className="overflow-x-hidden">
        <table className="w-full table-auto border-collapse">
          <thead>
            <tr className="border-b">
              <th className="text-left p-1 sm:p-2 lg:p-4 text-[10px] sm:text-xs lg:text-base md:w-1/12">#</th>
              <th className="text-left p-1 sm:p-2 lg:p-4 text-[10px] sm:text-xs lg:text-base md:w-3/12">파일명</th>
              <th className="text-left p-1 sm:p-2 lg:p-4 text-[10px] sm:text-xs lg:text-base md:w-2/12 whitespace-nowrap">단어 수</th>
              <th className="text-left p-1 sm:p-2 lg:p-4 text-[10px] sm:text-xs lg:text-base md:w-3/12 whitespace-nowrap">업로드 시간</th>
              <th className="text-left p-1 sm:p-2 lg:p-4 text-[10px] sm:text-xs lg:text-base md:w-2/12 whitespace-nowrap">문서 공개</th>
              <th className="text-left p-1 sm:p-2 lg:p-4 text-[10px] sm:text-xs lg:text-base md:w-1/12"></th>
            </tr>
          </thead>
          <tbody>
            {filteredData?.reverse().map((file, index) => (
              <tr key={file.knowledgeId} className="border-b cursor-pointer hover:bg-gray-100">
                <td className="p-1 sm:p-2 lg:p-4 text-[10px] sm:text-xs lg:text-base">{index+1}</td>
                <td
                  className="p-1 sm:p-2 lg:p-4 max-w-[150px] w-full md:max-w-none md:w-auto"
                  onClick={() => goToKnowledgeDetail(String(file.knowledgeId), file.title)}
                >
                  <div className="flex items-center w-full">
                    <p className="text-[10px] sm:text-xs lg:text-base truncate overflow-hidden whitespace-nowrap">
                      {file.title}
                    </p>
                  </div>
                </td>
                <td className="p-1 sm:p-2 lg:p-4 text-[10px] sm:text-xs lg:text-base">{file.totalToken}</td>
                <td className="p-1 sm:p-2 lg:p-4 text-[10px] sm:text-xs lg:text-base">{file.createdAt}</td>
                <td className="p-1 sm:p-2 lg:p-4">
                  <div
                    onClick={() => togglePublicStatus(file)}
                    className={`relative w-8 sm:w-10 lg:w-12 h-4 sm:h-5 lg:h-6 flex items-center cursor-pointer ${
                      file.isPublic ? 'bg-[#9A75BF]' : 'bg-gray-400'
                    } rounded-full p-1 transition-colors duration-300 ease-in-out`}
                  >
                    <div
                      className={`h-3 sm:h-4 w-3 sm:w-4 bg-white rounded-full shadow-md transform ${
                        file.isPublic ? 'translate-x-4 sm:translate-x-5 lg:translate-x-6' : 'translate-x-0'
                      } transition-transform duration-300 ease-in-out`}
                    />
                  </div>
                </td>
                <td className="p-1 sm:p-2 lg:p-4">
                  <button
                    className="text-[10px] sm:text-xs lg:text-[13px] w-[36px] sm:w-[40px] lg:w-[50px] h-[24px] sm:h-[28px] lg:h-[32px] bg-[#9A75BF] text-white rounded-lg shadow-sm hover:bg-[#874aa5] active:bg-[#733d8a] transition-all duration-200 ease-in-out"
                    onClick={() => { handleDeleteClick(file.knowledgeId); }}
                  >
                    삭제
                  </button>
                </td>
              </tr>
            ))}
          </tbody>
        </table>
        <div ref={observerTarget} className="h-10" />
      </div>
    </div>
  );
}
"use client";

import { useState } from 'react';
import { useRouter } from 'next/navigation';
import Search from '@/components/common/Search';
import PurpleButton from '@/components/common/PurpleButton';

interface FileData {
  id: number;
  fileName: string;
  wordCount: string;
  uploadTime: string;
  isPublic: boolean;
}

const dummyData: FileData[] = [
  {
    id: 1,
    fileName: 'sdfghjkhgfdsafaklsdjflkhFLKDJSF;KLjdslkfjLKFJ;lSDFGHGFflkFJL;KDJF.txt',
    wordCount: '1.1k',
    uploadTime: '2024/10/17 20:17',
    isPublic: true,
  },
  {
    id: 2,
    fileName: '파일이름2.txt',
    wordCount: '950',
    uploadTime: '2024/10/18 14:45',
    isPublic: false,
  },
  {
    id: 3,
    fileName: '파일이름3.txt',
    wordCount: '1.5k',
    uploadTime: '2024/10/19 08:30',
    isPublic: true,
  },
];

export default function Page() {
  const [fileData, setFileData] = useState<FileData[]>(dummyData);
  const [searchTerm, setSearchTerm] = useState('');
  const router = useRouter();

  const goToCreatePage = (): void => {
    router.push('/knowledge/create');
  };

  const goToKnoweldgeDetail = (id: number): void => {
    router.push(`/knowledge/${id}`);
  };

  const togglePublicStatus = (id: number) => {
    setFileData((prevData) =>
      prevData.map((file) =>
        file.id === id ? { ...file, isPublic: !file.isPublic } : file
      )
    );
  };

  const filteredData = fileData.filter((file) =>
    file.fileName.toLowerCase().includes(searchTerm.toLowerCase())
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
      {filteredData.map((file) => (
        <tr key={file.id} className="border-b cursor-pointer hover:bg-gray-100">
          <td className="p-1 sm:p-2 lg:p-4 text-[10px] sm:text-xs lg:text-base">{file.id}</td>
          <td 
            className="p-1 sm:p-2 lg:p-4 max-w-[150px] w-full md:max-w-none md:w-auto"
            onClick={() => goToKnoweldgeDetail(file.id)}>
            <div className="flex items-center w-full">
              <p className="text-[10px] sm:text-xs lg:text-base truncate overflow-hidden whitespace-nowrap">
                {file.fileName}
              </p>
            </div>
          </td>
          <td className="p-1 sm:p-2 lg:p-4 text-[10px] sm:text-xs lg:text-base">{file.wordCount}</td>
          <td className="p-1 sm:p-2 lg:p-4 text-[10px] sm:text-xs lg:text-base">{file.uploadTime}</td>
          <td className="p-1 sm:p-2 lg:p-4">
            <div
              onClick={() => togglePublicStatus(file.id)}
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
            onClick={(event) => {
              event.stopPropagation(); 
              setFileData((prevData) =>
                prevData.filter((f) => f.id !== file.id)
              );
            }}
          >
            삭제
          </button>
          </td>
        </tr>
      ))}
    </tbody>
  </table>
</div>

    </div>
  );
}

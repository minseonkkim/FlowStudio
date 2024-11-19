
import React from 'react';
import { CgTrash } from '@react-icons/all-files/cg/CgTrash';

interface ChunkDetailProps {
  data: { id: string; content: string }[];
  onItemSelect: (id: string) => void;
  onDelete: (chunkId: string) => void; 
}

export default function ChunkDetail({ data, onItemSelect, onDelete }: ChunkDetailProps) {
  return (
    <div className="grid grid-cols-1 sm:grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-4 p-4">
      {data.map((item) => (
        <div
          key={item.id}
          onClick={() => onItemSelect(item.id)}
          className="w-full h-[190px] px-6 py-4 rounded-xl border-2 cursor-pointer
                     hover:border-[#9A75BF] hover:bg-[#B99AD9] hover:bg-opacity-10 group relative"
        >
          <div className='flex justify-between'>
            <div className="border border-gray-400 w-[60px] h-[24px] rounded-lg text-center mb-4">
              <p># {item.id.padStart(3, '0')}</p>
            </div>
            <CgTrash
              className='text-gray-400 opacity-0 group-hover:opacity-100 w-4 h-4 cursor-pointer'
              onClick={(e) => {
                e.stopPropagation(); 
                onDelete(item.id); 
              }}
            />
          </div>
          <div className="flex flex-col h-[122px] justify-between">
            <p className="text-[#333333] truncate">{item.content}</p>
          </div>
        </div>
      ))}
    </div>
  );
}

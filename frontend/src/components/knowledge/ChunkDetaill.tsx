import React from 'react';

interface ChunkDetailProps {
  data: { id: string; content: string }[];
  onItemSelect: (content: string, id: string) => void;
}

export default function ChunkDetail({ data, onItemSelect }: ChunkDetailProps) {
  return (
    <div className="grid grid-cols-1 sm:grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-4 p-4">
      {data.map((item) => (
        <div
          key={item.id}
          onClick={() => onItemSelect(item.content, item.id)}
          className="w-full h-[190px] px-6 py-4 rounded-xl border-2 cursor-pointer
                     hover:border-[#9A75BF] hover:bg-[#B99AD9] hover:bg-opacity-10"
        >
          <div className="border border-gray-400 w-[60px] h-[24px] rounded-lg text-center mb-4">
            <p># {item.id.padStart(3, '0')}</p>
          </div>
          <div className="flex flex-col h-[122px] justify-between">
            <p className="text-[#333333] truncate">{item.content}</p>
          </div>
        </div>
      ))}
    </div>
  );
}

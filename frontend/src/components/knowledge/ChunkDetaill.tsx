import React from 'react';

interface ChunkDetailProps {
  data: { id: string; content: string }[];
  onItemSelect: (content: string, id: string) => void;
}

export default function ChunkDetail({ data, onItemSelect }: ChunkDetailProps) {
  return (
    <div className="grid grid-cols-4 gap-4 p-4">
      {data.map((item) => (
        <div
          key={item.id}
          onClick={() => onItemSelect(item.content, item.id)}
          className="w-full h-[204px] px-6 py-4 rounded-xl border-2 group cursor-pointer
                     transform transition-transform duration-300 hover:scale-105 hover:shadow-md"
        >
          <div className="border border-gray-400 w-[60px] h-[24px] rounded-lg text-center mb-4">
            <p>{item.id}</p>
          </div>
          <div className="flex flex-col h-[122px] justify-between">
            <p className="text-[#333333] truncate">{item.content}</p>
          </div>
        </div>
      ))}
    </div>
  );
}

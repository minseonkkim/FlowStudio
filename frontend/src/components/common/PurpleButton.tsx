import React from 'react';

export interface PurpleButtonProps {
  w: string;   // 너비
  h: string;   // 높이
  text: string; // 텍스트
  onHandelButton: (event: React.MouseEvent<HTMLButtonElement>) => void; // 버튼 클릭 함수
}

export default function PurpleButton({ w, h, text, onHandelButton }: PurpleButtonProps) {
  return (
    <button
      className="flex justify-center items-center border rounded-lg bg-[#9A75BF] text-white font-semibold text-base"
      style={{ width: w, height: h }}
      onClick={onHandelButton}
    >
      {text}
    </button>
  );
}

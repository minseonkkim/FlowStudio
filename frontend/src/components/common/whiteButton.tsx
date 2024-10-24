import React from 'react';

export interface WhiteButtonProps {
  w: string;   // 너비
  h: string;   // 높이
  text: string; // 텍스트
  onHandelButton?: (event: React.MouseEvent<HTMLButtonElement>) => void; // 버튼 클릭 함수
}

export default function WhiteButton({ w, h, text, onHandelButton }: WhiteButtonProps) {
  return (
    <button
      className={`flex justify-center items-center border-2 border-[#9A75BF] rounded-lg text-[#9A75BF] bg-white font-semibold text-base drop-shadow-md`}
      style={{ width: w, height: h }}  
      onClick={onHandelButton}
    >
      {text}
    </button>
  );
}

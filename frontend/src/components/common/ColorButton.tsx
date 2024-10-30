import React from 'react';

export interface ColorButtonProps {
  w: string;   // 너비
  h: string;   // 높이
  text: string; // 텍스트
  bgColor?: string; // 배경색 (옵션)
  onHandelButton?: (event: React.MouseEvent<HTMLButtonElement>) => void; // 버튼 클릭 함수
}

export default function ColorButton({ w, h, text, bgColor = '#9A75BF', onHandelButton }: ColorButtonProps) {
  return (
    <button
      className={`flex justify-center items-center border rounded-lg text-white font-semibold text-base`}
      style={{ width: w, height: h, backgroundColor: bgColor }}  
      onClick={onHandelButton}
    >
      {text}
    </button>
  );
}

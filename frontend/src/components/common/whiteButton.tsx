import React from 'react';

export interface WhiteButtonProps {
  w: string;   // 너비
  h: string;   // 높이
  text: string; // 텍스트
  borderColor: string; // 테두리 색깔
  textColor: string; // 텍스트 색깔
  onHandelButton?: (event: React.MouseEvent<HTMLButtonElement>) => void; // 버튼 클릭 함수
}

export default function WhiteButton({ w, h, text, borderColor, textColor, onHandelButton }: WhiteButtonProps) {
  return (
    <button
      className={`flex justify-center items-center rounded-lg font-semibold text-base`}
      style={{ 
        width: w, 
        height: h, 
        border: `2px solid ${borderColor}`, 
        color: textColor, 
        backgroundColor: 'white' 
      }}  
      onClick={onHandelButton}
    >
      {text}
    </button>
  );
}

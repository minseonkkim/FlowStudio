export interface WhiteButtonProps {
  text: string; 
  onHandelButton?: (event: React.MouseEvent<HTMLButtonElement>) => void;
}

export default function WhiteButton({ text, onHandelButton }: WhiteButtonProps) {
  return (
    <button
      className='py-2 px-4 text-[14px] border-2 border-[#9A75BF] text-[#9A75BF] rounded-lg hover:bg-[#f3e8ff] active:bg-[#e3d1f7]'
      onClick={onHandelButton}
    >
      {text}
    </button>
  );
}

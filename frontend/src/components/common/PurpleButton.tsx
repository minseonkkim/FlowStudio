export interface PurpleButtonProps {
  text: string;
  onHandelButton?: (event: React.MouseEvent<HTMLButtonElement>) => void;
}

export default function PurpleButton({ text, onHandelButton }: PurpleButtonProps) {
  return (
    <button
      className='py-2 px-4 text-[14px] bg-[#9A75BF] text-white rounded-lg hover:bg-[#874aa5] active:bg-[#733d8a]'
      onClick={onHandelButton}
    >
      {text}
    </button>
  );
}

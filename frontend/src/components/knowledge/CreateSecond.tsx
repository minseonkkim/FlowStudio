import { VscSettings } from '@react-icons/all-files/vsc/VscSettings';

export default function CreateSecond() {
  return (
    <>
      <div className="pl-10">
        <p className="text-2xl font-medium pt-14 pb-8">텍스트 전처리 및 클리닝</p>
        <div className="border border-[#5D2973] w-[640px] h-[425px] rounded-xl">
          <div className="p-4 flex gap-1">
            <div className="border w-[33px] h-[33px] bg-[#E1D5FE] rounded-lg relative mr-4">  
              <VscSettings className="absolute top-2 left-2" />
            </div>
            <div>
              <p className="font-semibold">사용자 설정</p> 
              <p className="text-sm text-gray-500">
                청크 규칙, 청크 길이, 전처리 규칙 등을 사용자 정의합니다.
              </p>
            </div>
          </div>
          <div className="border-b border-b-[#EAECF0]"></div>
          <div className='text-base font-normal'>
            <p>세그먼트 식별자</p>
            <input type="text" spellCheck="false" className='border w-[490px] h-[43px] rounded-md bg-[#D9D9D9] opacity-50' />
            <p>최대 청크 길이</p>
            <p>청크 중첩</p>
          </div>
        </div>
      </div>
    </>
  );
}

import { IoPlay } from "@react-icons/all-files/io5/IoPlay"
import { FaRobot } from "@react-icons/all-files/fa/FaRobot"
import { FiBookOpen } from "@react-icons/all-files/fi/FiBookOpen"
import { RiQuestionAnswerFill } from "@react-icons/all-files/ri/RiQuestionAnswerFill"
import { GrTree } from "@react-icons/all-files/gr/GrTree"
import { IoGitBranchOutline } from "@react-icons/all-files/io5/IoGitBranchOutline"
import { VscSymbolVariable } from "@react-icons/all-files/vsc/VscSymbolVariable"
import { useState } from "react"

export default function StartNodeDetail({
  maxChars,
  setMaxChars,
}: {
  maxChars: number | undefined;
  setMaxChars: (value: number) => void;
}) {
  const [isOpen, setIsOpen] = useState(false);

  const toggleDropdown = () => {
    setIsOpen(!isOpen);
  };

  const handleMaxCharsChange = (event: React.ChangeEvent<HTMLInputElement>) => {
    const value = event.target.value;
    if (value === "") {
      setMaxChars(NaN); 
    } else {
      const numericValue = parseInt(value, 10);
      if (!isNaN(numericValue)) {
        setMaxChars(numericValue); 
      }
    }
  };

  return <>
  <div className="flex flex-col gap-4 w-[300px] h-[calc(100vh-170px)] rounded-[20px] p-[20px] bg-white bg-opacity-40 backdrop-blur-[15px] shadow-[0px_2px_8px_rgba(0,0,0,0.25)]">
    <div className="flex flex-row items-center gap-1 mb-2">
        <IoPlay className="text-[#95C447] size-8"/>
        <div className="text-[25px] font-semibold">시작</div>
    </div>
    <div className="flex flex-col gap-2">
      <div className="text-[16px]">최대 글자수를 입력하세요.</div>
      <input className="h-[36px] rounded-[5px] p-3 focus:outline-none focus:ring-1 focus:ring-[#95C447]"
      type="number"
      value={maxChars}
      onChange={handleMaxCharsChange}/>
    </div>
    <div className="flex flex-col gap-2">
      <div className="text-[16px]">다음 블록을 추가하세요.</div>
      <div className="flex flex-row items-center justify-between">
        <div className="bg-[#CEE8A3] rounded-[360px] w-[50px] h-[50px] flex justify-center items-center z-[10]">
          <IoPlay className="text-[#95C447] size-8"/>
        </div>
        <div className="bg-black h-[2px] w-[200px] absolute"></div>
        <div className="relative inline-block text-left">
          <div>
            <button
              type="button"
              className="inline-flex justify-center w-[160px] rounded-md border border-gray-300 shadow-sm px-4 py-2 bg-white text-sm font-medium text-gray-700 hover:bg-gray-50 focus:outline-none focus:ring-1 focus:ring-[#95C447]"
              onClick={toggleDropdown}
            >
              다음 블록 선택
              <svg
                className="-mr-1 ml-2 h-5 w-5"
                xmlns="http://www.w3.org/2000/svg"
                viewBox="0 0 20 20"
                fill="currentColor"
                aria-hidden="true"
              >
                <path
                  fillRule="evenodd"
                  d="M5.23 7.21a.75.75 0 011.06.02L10 10.94l3.71-3.71a.75.75 0 111.06 1.06l-4 4a.75.75 0 01-1.06 0l-4-4a.75.75 0 01.02-1.06z"
                  clipRule="evenodd"
                />
              </svg>
            </button>
          </div>

          {isOpen && (
            <div
              className="origin-top-right absolute right-0 mt-2 w-[160px] rounded-md shadow-lg bg-white ring-1 ring-black ring-opacity-5"
              role="menu"
              aria-orientation="vertical"
              aria-labelledby="menu-button"
            >
              <div className="py-1 text-[15px]" role="none">
                <div className="px-4 py-1.5 cursor-pointer flex flex-row items-center gap-2">
                  <FaRobot className="text-[18px]"/>
                  <div>LLM</div>
                </div>
                <div className="px-4 py-1.5 cursor-pointer flex flex-row items-center gap-2">
                  <FiBookOpen className="text-[18px]"/>
                  <div>지식 검색</div>
                </div>
                <div className="px-4 py-1.5 cursor-pointer flex flex-row items-center gap-2">
                  <RiQuestionAnswerFill className="text-[18px]"/>
                  <div>답변</div>
                </div>
                <div className="px-4 py-1.5 cursor-pointer flex flex-row items-center gap-2">
                  <GrTree className="text-[18px]"/>
                  <div>질문 분류기</div>
                </div>
                <div className="px-4 py-1.5 cursor-pointer flex flex-row items-center gap-2">
                  <IoGitBranchOutline className="text-[18px]"/>
                  <div>IF/ELSE</div>
                </div>
                <div className="px-4 py-1.5 cursor-pointer flex flex-row items-center gap-2">
                  <VscSymbolVariable className="text-[18px]"/>
                  <div>변수 할당자</div>
                </div>
              </div>
            </div>
          )}
        </div>
      </div>
      
      
    </div>
  </div>
  </>
}
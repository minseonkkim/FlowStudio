import { FaRobot } from "@react-icons/all-files/fa/FaRobot";
import { FiBookOpen } from "@react-icons/all-files/fi/FiBookOpen";
import { RiQuestionAnswerFill } from "@react-icons/all-files/ri/RiQuestionAnswerFill";
import { GrTree } from "@react-icons/all-files/gr/GrTree";
import { IoGitBranchOutline } from "@react-icons/all-files/io5/IoGitBranchOutline";
import { VscSymbolVariable } from "@react-icons/all-files/vsc/VscSymbolVariable";
import { IoClose } from "@react-icons/all-files/io5/ioClose"
import { IoMdTrash } from "@react-icons/all-files/io/IoMdTrash"
import { ChangeEvent, useCallback, useEffect, useRef, useState } from "react";

interface Model {
  id: string;
  name: string;
}

const models: Model[] = [
  { id: "gpt-3.5-turbo", name: "GPT-3.5 Turbo" },
  { id: "gpt-4", name: "GPT-4" },
  { id: "gpt-4-32k", name: "GPT-4 (32k)" },
];

export default function LlmNodeDetail({
  prompts,
  setPrompts,
  selectedModel,
  setModel,
  removePrompt,
  addNode,
  onClose
}: {
  prompts: { type: string; text: string }[];
  setPrompts: (prompts: { type: string; text: string }[]) => void;
  selectedModel: string;
  setModel: (model: string) => void;
  removePrompt: (index: number) => void; 
  addNode: (type: string) => void;
  onClose: () => void;
}) {
  const [isOpen, setIsOpen] = useState(false);

  const toggleDropdown = () => {
    setIsOpen(!isOpen);
  };
   
  const [localPrompts, setLocalPrompts] = useState<{ type: string; text: string }[]>(prompts);

  useEffect(() => {
    setLocalPrompts([...prompts]);
  }, [prompts]);
  

  const handlePromptTypeChange = (index: number, e: ChangeEvent<HTMLSelectElement>) => {
    const updatedPrompts = [...localPrompts];
    updatedPrompts[index] = { ...updatedPrompts[index], type: e.target.value };
    setLocalPrompts(updatedPrompts);
    setPrompts(updatedPrompts);
  };

  const handlePromptTextChange = (index: number, e: ChangeEvent<HTMLTextAreaElement>) => {
    const updatedPrompts = [...localPrompts];
    updatedPrompts[index] = { ...updatedPrompts[index], text: e.target.value };
    setLocalPrompts(updatedPrompts);
    setPrompts(updatedPrompts);
  };

  const addPrompt = () => {
    const updatedPrompts = [...localPrompts, { type: "system", text: "" }];
    setLocalPrompts(updatedPrompts);
    setPrompts(updatedPrompts);
  };

  const handleChangeModel = (e: ChangeEvent<HTMLSelectElement>) => {
    setModel(e.target.value);
  };

  const textAreaRefs = useRef<(HTMLTextAreaElement | null)[]>([]);

  useEffect(() => {
    textAreaRefs.current.forEach((textarea) => {
      if (textarea) {
        textarea.style.height = "auto";
        textarea.style.height = `${textarea.scrollHeight}px`;
      }
    });
  }, [localPrompts]);

  const handleNodeTypeClick = useCallback(
    (type: string) => {
      addNode(type);
      onClose();
    },
    [addNode, onClose]
  );

  return (
    <>
      <div className="flex flex-col gap-4 w-[300px] h-[calc(100vh-170px)] rounded-[20px] p-[20px] bg-white bg-opacity-40 backdrop-blur-[15px] shadow-[0px_2px_8px_rgba(0,0,0,0.25)] overflow-y-auto">
        <div className="flex flex-row justify-between items-center mb-2">
          <div className="flex flex-row items-center gap-1">
              <FaRobot className="text-[#3B82F6] size-8" />
              <div className="text-[25px] font-semibold">LLM</div>
          </div>
          <IoClose className="size-6 cursor-pointer" onClick={onClose}/>
        </div>
        
        <div className="flex flex-col gap-2">
          <div className="text-[16px]">모델을 선택하세요.</div>
          <select
            id="model"
            value={selectedModel}
            onChange={handleChangeModel}
            className="cursor-pointer mt-1 block w-full px-3 py-2 border border-gray-300 bg-white rounded-md shadow-sm focus:outline-none focus:ring-[#3B82F6] focus:border-[#3B82F6] sm:text-sm"
          >
            {models.map((model: Model) => (
              <option key={model.id} value={model.id}>
                {model.name}
              </option>
            ))}
          </select>
        </div>
        <div className="flex flex-col gap-2">
        <div className="text-[16px]">프롬프트를 입력하세요.</div>
        
        {localPrompts.map((prompt, index) => (
            <div key={index} className="flex flex-col gap-2 rounded-[10px] bg-white">
              <div className="flex flex-row justify-between items-center">
                <select
                  id={`prompt_type_${index}`}
                  value={prompt.type}
                  onChange={(e) => handlePromptTypeChange(index, e)}
                  className="mt-1 block w-[90px] px-2 py-1 bg-white rounded-md outline-none focus:outline-none sm:text-sm cursor-pointer font-bold border-none shadow-none"
                >
                  <option value="system">system</option>
                  <option value="user">user</option>
                </select>
                {index !== 0 && 
                  <IoMdTrash className="size-4 m-2 cursor-pointer text-[#9B9B9B]" onClick={() => removePrompt(index)}/>
                }
              </div>
              
              <textarea
                ref={(el) => {
                  textAreaRefs.current[index] = el; 
                }}
                value={prompt.text}
                onChange={(e) => handlePromptTextChange(index, e)}
                onInput={(e) => {
                  const target = e.target as HTMLTextAreaElement;
                  target.style.height = "auto"; 
                  target.style.height = `${target.scrollHeight}px`; 
                }}
                placeholder="프롬프트를 입력하세요."
                className="bg-white rounded-[5px] w-full resize-none overflow-hidden px-2 py-1 mt-2 focus:outline-none shadow-none border-none"
                style={{ minHeight: "90px" }}
              />

            </div>
          ))}
        
        <div  onClick={addPrompt} className="bg-[#E0E0E0] rounded-[5px] flex justify-center items-center py-1.5 cursor-pointer text-[14px]">
          + 프롬프트 추가
        </div>
      </div>

        <div className="flex flex-col gap-2">
          <div className="text-[16px]">다음 블록을 추가하세요.</div>

          <div className="flex flex-row items-center justify-between">
            <div className="bg-[#A4C6FD] rounded-[360px] w-[50px] h-[50px] flex justify-center items-center z-[10]">
              <FaRobot className="text-[#3B82F6] size-8" />
            </div>
            <div className="bg-black h-[2px] w-[200px] absolute"></div>
            <div className="relative inline-block text-left">
              <div>
                <button
                  type="button"
                  className="inline-flex justify-center w-[160px] rounded-md border border-gray-300 shadow-sm px-4 py-2 bg-white text-sm font-medium text-gray-700 hover:bg-gray-50 focus:outline-none focus:ring-1 focus:ring-[#3B82F6]"
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
                  <div className="p-1 text-[15px]" role="none">
                    <div onClick={() => handleNodeTypeClick("llmNode")} className="hover:bg-[#f4f4f4] px-4 py-1.5 cursor-pointer flex flex-row items-center gap-2">
                    <FaRobot className="text-[18px]"/>
                    <div>LLM</div>
                  </div>
                  <div onClick={() => handleNodeTypeClick("knowledgeNode")} className="hover:bg-[#f4f4f4] px-4 py-1.5 cursor-pointer flex flex-row items-center gap-2">
                    <FiBookOpen className="text-[18px]"/>
                    <div>지식 검색</div>
                  </div>
                  <div onClick={() => handleNodeTypeClick("answerNode")} className="hover:bg-[#f4f4f4] px-4 py-1.5 cursor-pointer flex flex-row items-center gap-2">
                    <RiQuestionAnswerFill className="text-[18px]"/>
                    <div>답변</div>
                  </div>
                  <div onClick={() => handleNodeTypeClick("questionclassifierNode")} className="hover:bg-[#f4f4f4] px-4 py-1.5 cursor-pointer flex flex-row items-center gap-2">
                    <GrTree className="text-[18px]"/>
                    <div>질문 분류기</div>
                  </div>
                  <div onClick={() => handleNodeTypeClick("ifelseNode")} className="hover:bg-[#f4f4f4] px-4 py-1.5 cursor-pointer flex flex-row items-center gap-2">
                    <IoGitBranchOutline className="text-[18px]"/>
                    <div>IF/ELSE</div>
                  </div>
                  <div onClick={() => handleNodeTypeClick("variableallocatorNode")} className="hover:bg-[#f4f4f4] px-4 py-1.5 cursor-pointer flex flex-row items-center gap-2">
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
  );
}

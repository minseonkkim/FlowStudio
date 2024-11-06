import { useCallback, useEffect, useRef, useState } from "react";
import { GrTree } from "@react-icons/all-files/gr/GrTree";
import { IoClose } from "@react-icons/all-files/io5/ioClose";
import { AiOutlineClose } from "@react-icons/all-files/ai/AiOutlineClose";
import { IoMdTrash } from "@react-icons/all-files/io/IoMdTrash";
import { ConnectedNode } from "@/types/workflow";
import { nodeConfig, deleteIconColors } from "@/utils/nodeConfig";

type ClassType = { text: string };
type ConnectedNodesType = { [key: string]: ConnectedNode[] };

interface QuestionClassifierNodeDetailProps {
  classes: ClassType[];
  setClasses: (updatedClasses: ClassType[]) => void;
  addNode: (type: string, condition: string) => void;
  onClose: () => void;
  connectedNodes: ConnectedNodesType;
  setConnectedNodes: (targetNodeId: string) => void;
}

export default function QuestionClassifierNodeDetail({
  classes,
  setClasses,
  addNode,
  onClose,
  connectedNodes,
  setConnectedNodes,
}: QuestionClassifierNodeDetailProps) {
  const [isOpen, setIsOpen] = useState<{ [key: string]: boolean }>({});
  const dropdownRefs = useRef<{ [key: string]: HTMLDivElement | null }>({});
  const textareaRefs = useRef<(HTMLTextAreaElement | null)[]>([]);

  const toggleDropdown = (classId: string) => {
    setIsOpen((prev) => ({ ...prev, [classId]: !prev[classId] }));
  };

  const [localClasses, setLocalClasses] = useState<ClassType[]>(classes);

  useEffect(() => {
    setLocalClasses([...classes]);
  }, [classes]);

  useEffect(() => {
    textareaRefs.current.forEach((textarea) => {
      if (textarea) {
        textarea.style.height = "auto";
        textarea.style.height = `${textarea.scrollHeight}px`; 
      }
    });
  }, [localClasses]);

  const handleAddClass = () => {
    if (localClasses.length < 5) {
      const newClass = { text: "" };
      setLocalClasses([...localClasses, newClass]);
      setClasses([...localClasses, newClass]);
    }
  };

  const handleClassChange = (index: number, newValue: string) => {
    const updatedClasses = localClasses.map((cls, i) =>
      i === index ? { text: newValue } : cls
    );
    setLocalClasses(updatedClasses);
    setClasses(updatedClasses);
  };

  const handleDeleteClass = (index: number) => {
    const updatedClasses = localClasses.filter((_, i) => i !== index);
    setLocalClasses(updatedClasses);
    setClasses(updatedClasses);
  };

  const handleNodeTypeClick = useCallback(
    (type: string, index: number) => {
      const condition = `handle${index + 1}`;
      addNode(type, condition);
      setIsOpen((prev) => ({ ...prev, [`class${index}`]: false }));
    },
    [addNode]
  );

  const handleClickOutside = useCallback(
    (event: MouseEvent) => {
      Object.keys(isOpen).forEach((key) => {
        if (
          isOpen[key] &&
          dropdownRefs.current[key] &&
          !dropdownRefs.current[key]?.contains(event.target as Node)
        ) {
          setIsOpen((prev) => ({ ...prev, [key]: false }));
        }
      });
    },
    [isOpen]
  );

  useEffect(() => {
    document.addEventListener("mousedown", handleClickOutside);
    return () => {
      document.removeEventListener("mousedown", handleClickOutside);
    };
  }, [handleClickOutside]);

  return (
    <div className="flex flex-col gap-4 w-[320px] h-[calc(100vh-170px)] rounded-[20px] p-[20px] bg-white bg-opacity-40 backdrop-blur-[15px] shadow-[0px_2px_8px_rgba(0,0,0,0.25)] overflow-y-auto">
      <div className="flex flex-row justify-between items-center mb-2">
        <div className="flex flex-row items-center gap-1">
          <GrTree className="text-[#1E3A8A] size-7" />
          <div className="text-[25px] font-semibold">질문 분류기</div>
        </div>
        <IoClose className="size-6 cursor-pointer" onClick={onClose} />
      </div>
      <div className="h-[36px] rounded-[5px] p-3 bg-white flex items-center">gpt-4o-mini</div>

      <div className="flex flex-col gap-2">
        <div className="text-[16px]">클래스를 추가하세요.</div>

        {localClasses.map((cls, index) => (
          <div key={index} className="flex flex-col gap-2 rounded-[10px] bg-white p-2">
            <div className="flex flex-row justify-between">
              <div className="text-[13px] font-bold">클래스 {index + 1}</div>
              {localClasses.length > 2 && (
                <IoMdTrash
                  className="size-4 m-2 cursor-pointer text-[#9B9B9B]"
                  onClick={() => handleDeleteClass(index)}
                />
              )}
            </div>
            <textarea
              ref={(el) => {
                textareaRefs.current[index] = el; 
              }}
              value={cls?.text || ""}
              onChange={(e) => {
                handleClassChange(index, e.target.value);
                e.target.style.height = "auto";
                e.target.style.height = `${e.target.scrollHeight}px`; 
              }}
              placeholder="주제 이름을 작성하세요."
              className="bg-white rounded-[5px] w-full resize-none overflow-hidden mt-2 focus:outline-none shadow-none border-none"
              style={{ minHeight: "50px" }}
            />
          </div>
        ))}

        {localClasses.length < 5 && (
          <div
            className="bg-[#E0E0E0] hover:bg-[#DADADA] rounded-[5px] flex justify-center items-center py-1.5 cursor-pointer text-[14px]"
            onClick={handleAddClass}
          >
            + 클래스 추가
          </div>
        )}
      </div>

      <div className="flex flex-col gap-2 mt-4">
        <div className="text-[16px]">다음 블록을 추가하세요.</div>
        <div className="flex flex-row justify-between w-full items-start">
          <div className="aspect-square bg-[#95A4CD] rounded-full w-[50px] h-[50px] flex justify-center items-center z-[10]">
            <GrTree className="text-[#1E3A8A] size-8" />
          </div>
          <div className="bg-black h-[2px] w-[230px] flex-grow my-[24px]"></div>

          <div className="flex flex-col gap-2 z-[10] w-[210px]">
            {localClasses.map((cls, index) => (
              <div key={index} className="flex flex-col gap-2 mb-4">
                <div className="flex flex-row items-start">
                  <div className="text-[14px] w-[65px]">클래스 {index + 1}</div>
                  <div className="flex flex-col w-[185px] mt-[6px]">
                    {connectedNodes[`handle${index + 1}`]?.map((node, idx) => (
                      <div
                        key={idx}
                        className={`inline-flex items-center gap-2 w-[155px] rounded-md border border-gray-300 shadow-sm px-4 py-2 bg-[#${nodeConfig[node.name]?.color}] text-sm font-medium`}
                      >
                        {nodeConfig[node.name]?.icon}
                        <span>{nodeConfig[node.name]?.label || node.name}</span>
                        <AiOutlineClose
                          className="cursor-pointer ml-auto"
                          style={{ color: deleteIconColors[node.name] || "gray" }}
                          onClick={() => setConnectedNodes(node.id)}
                        />
                      </div>
                    ))}

                    <button
                      onClick={() => toggleDropdown(`class${index}`)}
                      className="inline-flex justify-center w-[155px] rounded-md border border-gray-300 shadow-sm px-4 py-2 bg-white text-sm font-medium text-gray-700 hover:bg-gray-50 focus:outline-none focus:ring-1 focus:ring-[#1E3A8A]"
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

                    {isOpen[`class${index}`] && (
                      <div
                        ref={(el) => {
                          dropdownRefs.current[`class${index}`] = el;
                        }}
                        className="absolute mt-2 w-[160px] rounded-md shadow-lg bg-white ring-1 ring-black ring-opacity-5 z-10"
                        role="menu"
                      >
                        <div className="p-1 text-[15px]" role="none">
                          {[
                            "llmNode",
                            "knowledgeNode",
                            "answerNode",
                            "questionclassifierNode",
                            "ifelseNode",
                            "variableallocatorNode",
                          ].map((nodeType) => (
                            <div
                              key={nodeType}
                              onClick={() => handleNodeTypeClick(nodeType, index)}
                              className="hover:bg-[#f4f4f4] px-4 py-1.5 cursor-pointer flex flex-row items-center gap-2"
                            >
                              {nodeConfig[nodeType].icon}
                              <div>{nodeConfig[nodeType].label}</div>
                            </div>
                          ))}
                        </div>
                      </div>
                    )}
                  </div>
                </div>
              </div>
            ))}
          </div>
        </div>
      </div>
    </div>
  );
}

import { useCallback, useEffect, useRef, useState } from "react";
import { IoGitBranchOutline } from "@react-icons/all-files/io5/IoGitBranchOutline";
import { IoClose } from "@react-icons/all-files/io5/ioClose";
import { AiOutlineClose } from "@react-icons/all-files/ai/AiOutlineClose";
import { ConnectedNode } from "@/types/workflow";
import { nodeConfig, deleteIconColors } from "@/utils/nodeConfig";

interface Variable {
  name: string;
  value: string;
  type: string;
  isEditing: boolean;
}

interface ConditionRowProps {
  variables: Variable[];
  onDelete: () => void;
}

function ConditionRow({ variables, onDelete }: ConditionRowProps) {
  return (
    <div className="flex flex-row items-center justify-between w-[210px] bg-white rounded-[5px] p-1">
      <div className="w-auto h-[36px] flex items-center">
        <select className="w-full">
          {variables.map((variable, index) => (
            <option key={index} value={variable.name}>
              {variable.name}
            </option>
          ))}
        </select>
      </div>
      <div className="w-auto h-[36px] flex items-center">
        <select className="w-full">
          <option value="">&gt;=</option>
          <option value="">&gt;</option>
          <option value="">==</option>
          <option value="">&lt;=</option>
          <option value="">&lt;</option>
        </select>
      </div>
      <input
        className="w-[80px] bg-[#E9E9E9] px-1 rounded-[5px]"
        placeholder="값 입력"
      />
      <AiOutlineClose className="cursor-pointer ml-auto" onClick={onDelete} />
    </div>
  );
}

export default function IfelseNodeDetail({
  variables,
  addNode,
  onClose,
  connectedNodes = { ifNodes: [], elifNodes: [], elseNodes: [] },
  setConnectedNodes,
}: {
  variables: { name: string; value: string; type: string; isEditing: boolean }[];
  addNode: (type: string, condition: "ifsource" | "elifsource" | "elsesource") => void;
  onClose: () => void;
  connectedNodes?: { ifNodes: ConnectedNode[]; elifNodes: ConnectedNode[]; elseNodes: ConnectedNode[] };
  setConnectedNodes: (targetNodeId: string) => void;
}) {
  const [ifConditions, setIfConditions] = useState<Array<object>>([{}]);
  const [elifConditions, setElifConditions] = useState<Array<object>>([{}]);
  const [dropdownCondition, setDropdownCondition] = useState<"ifsource" | "elifsource" | "elsesource" | null>(null);
  const dropdownRef = useRef<HTMLDivElement | null>(null);

  const toggleDropdown = (condition: "ifsource" | "elifsource" | "elsesource") => {
    setDropdownCondition((prevCondition) => (prevCondition === condition ? null : condition));
  };

  const handleClickOutside = useCallback(
    (event: MouseEvent) => {
      if (dropdownRef.current && !dropdownRef.current.contains(event.target as Node)) {
        setDropdownCondition(null);
      }
    },
    [dropdownRef]
  );

  useEffect(() => {
    document.addEventListener("mousedown", handleClickOutside);
    return () => {
      document.removeEventListener("mousedown", handleClickOutside);
    };
  }, [handleClickOutside]);

  const addCondition = (type: "if" | "elif") => {
    if (type === "if") {
      setIfConditions((prev) => [...prev, {}]);
    } else if (type === "elif") {
      setElifConditions((prev) => [...prev, {}]);
    }
  };

  const removeCondition = (type: "if" | "elif", index: number) => {
    if (type === "if") {
      setIfConditions((prev) => prev.filter((_, i) => i !== index));
    } else if (type === "elif") {
      setElifConditions((prev) => prev.filter((_, i) => i !== index));
    }
  };

  const handleNodeTypeClick = useCallback(
    (type: string, condition: "ifsource" | "elifsource" | "elsesource") => {
      addNode(type, condition);
      setDropdownCondition(null);
    },
    [addNode]
  );

  return (
    <div className="flex flex-col gap-4 w-[320px] h-[calc(100vh-170px)] rounded-[20px] p-[20px] bg-white bg-opacity-40 backdrop-blur-[15px] shadow-[0px_2px_8px_rgba(0,0,0,0.25)] overflow-y-auto relative">
      <div className="flex flex-row justify-between items-center mb-2">
        <div className="flex flex-row items-center gap-1">
          <IoGitBranchOutline className="text-[#EF4444] size-8" />
          <div className="text-[25px] font-semibold">IF/ELSE</div>
        </div>
        <IoClose className="size-6 cursor-pointer" onClick={onClose} />
      </div>

      <div className="flex flex-col gap-2">
        <div className="flex flex-row items-start">
          <div className="text-[16px] w-[40px] flex-shrink-0">IF</div>
          <div className="flex flex-col gap-2">
            {ifConditions.map((_, index) => (
              <ConditionRow
                key={index}
                variables={variables}
                onDelete={() => removeCondition("if", index)}
              />
            ))}
            <div
              className="text-[14px] bg-white hover:bg-gray-50 border border-gray-300 rounded-[5px] flex justify-center items-center w-[150px] py-1.5 cursor-pointer"
              onClick={() => addCondition("if")}
            >
              + 조건 추가
            </div>
          </div>
        </div>

        <div className="flex flex-row items-start">
          <div className="text-[16px] w-[40px] flex-shrink-0">ELIF</div>
          <div className="flex flex-col gap-2">
            {elifConditions.map((_, index) => (
              <ConditionRow
                key={index}
                variables={variables}
                onDelete={() => removeCondition("elif", index)}
              />
            ))}
            <div
              className="text-[14px] bg-white hover:bg-gray-50 border border-gray-300 rounded-[5px] flex justify-center items-center w-[150px] py-1.5 cursor-pointer"
              onClick={() => addCondition("elif")}
            >
              + 조건 추가
            </div>
          </div>
        </div>
      </div>

      <div className="flex flex-col gap-2 mt-4">
        <div className="text-[16px]">다음 블록을 추가하세요.</div>
        <div className="flex flex-row justify-between w-full items-start">
          <div className="aspect-square bg-[#FACECE] rounded-full w-[50px] h-[50px] flex justify-center items-center z-[10]">
            <IoGitBranchOutline className="text-[#F97316] size-8" />
          </div>
          <div className="bg-black h-[2px] w-[230px] flex-grow my-[24px]"></div>

          <div className="flex flex-col gap-2 z-[10] mt-[6px]" ref={dropdownRef}>
            <div className="flex flex-row items-start relative">
              <div className="text-[12px] w-[30px] flex items-center">IF</div>
              <div className="flex flex-col">
                {connectedNodes.ifNodes?.map((node, index) => (
                  <div
                    key={index}
                    className={`inline-flex items-center gap-2 w-[155px] rounded-md border border-gray-300 shadow-sm px-4 py-2 bg-[#${nodeConfig[node.name]?.color}] text-sm font-medium`}
                  >
                    {nodeConfig[node.name]?.icon}
                    <span>{nodeConfig[node.name]?.label || node.name}</span>
                    <AiOutlineClose
                      className="cursor-pointer ml-auto"
                      style={{
                        color: deleteIconColors[node.name] || "gray",
                      }}
                      onClick={() => setConnectedNodes(node.id)}
                    />
                  </div>
                ))}
                <button
                  onClick={() => toggleDropdown("ifsource")}
                  className="inline-flex justify-center w-[155px] rounded-md border border-gray-300 shadow-sm px-4 py-2 bg-white text-sm font-medium text-gray-700 hover:bg-gray-50 focus:outline-none focus:ring-1 focus:ring-[#EF4444]"
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
                {dropdownCondition === "ifsource" && (
                  <div className="absolute right-0 mt-2 w-[160px] rounded-md shadow-lg bg-white ring-1 ring-black ring-opacity-5 z-20">
                    <div className="p-1 text-[15px]">
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
                          onClick={() => handleNodeTypeClick(nodeType, "ifsource")}
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
            
            <div className="flex flex-row items-start mt-4 relative">
              <div className="text-[12px] w-[30px] flex items-center">ELIF</div>
              <div className="flex flex-col">
                {connectedNodes.elifNodes?.map((node, index) => (
                  <div
                    key={index}
                    className={`inline-flex items-center gap-2 w-[155px] rounded-md border border-gray-300 shadow-sm px-4 py-2 bg-[#${nodeConfig[node.name]?.color}] text-sm font-medium`}
                  >
                    {nodeConfig[node.name]?.icon}
                    <span>{nodeConfig[node.name]?.label || node.name}</span>
                    <AiOutlineClose
                      className="cursor-pointer ml-auto"
                      style={{
                        color: deleteIconColors[node.name] || "gray",
                      }}
                      onClick={() => setConnectedNodes(node.id)}
                    />
                  </div>
                ))}
                <button
                  onClick={() => toggleDropdown("elifsource")}
                  className="inline-flex justify-center w-[155px] rounded-md border border-gray-300 shadow-sm px-4 py-2 bg-white text-sm font-medium text-gray-700 hover:bg-gray-50 focus:outline-none focus:ring-1 focus:ring-[#EF4444]"
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
                {dropdownCondition === "elifsource" && (
                  <div className="absolute right-0 mt-2 w-[160px] rounded-md shadow-lg bg-white ring-1 ring-black ring-opacity-5 z-20">
                    <div className="p-1 text-[15px]">
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
                          onClick={() => handleNodeTypeClick(nodeType, "elifsource")}
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

            <div className="flex flex-row items-start mt-4 relative">
              <div className="text-[12px] w-[30px] flex items-center">ELSE</div>
              <div className="flex flex-col">
                {connectedNodes.elseNodes?.map((node, index) => (
                  <div
                    key={index}
                    className={`inline-flex items-center gap-2 w-[155px] rounded-md border border-gray-300 shadow-sm px-4 py-2 bg-[#${nodeConfig[node.name]?.color}] text-sm font-medium`}
                  >
                    {nodeConfig[node.name]?.icon}
                    <span>{nodeConfig[node.name]?.label || node.name}</span>
                    <AiOutlineClose
                      className="cursor-pointer ml-auto"
                      style={{
                        color: deleteIconColors[node.name] || "gray",
                      }}
                      onClick={() => setConnectedNodes(node.id)}
                    />
                  </div>
                ))}
                <button
                  onClick={() => toggleDropdown("elsesource")}
                  className="inline-flex justify-center w-[155px] rounded-md border border-gray-300 shadow-sm px-4 py-2 bg-white text-sm font-medium text-gray-700 hover:bg-gray-50 focus:outline-none focus:ring-1 focus:ring-[#EF4444]"
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
                {dropdownCondition === "elsesource" && (
                  <div className="absolute right-0 mt-2 w-[160px] rounded-md shadow-lg bg-white ring-1 ring-black ring-opacity-5 z-20">
                    <div className="p-1 text-[15px]">
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
                          onClick={() => handleNodeTypeClick(nodeType, "elsesource")}
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
        </div>
      </div>
    </div>
  );
}

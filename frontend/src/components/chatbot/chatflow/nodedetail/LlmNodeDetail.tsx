import { FaRobot } from "@react-icons/all-files/fa/FaRobot";
import { Dispatch, SetStateAction, useEffect, useRef, useState } from "react";
import { CgClose } from "@react-icons/all-files/cg/CgClose";
import { AiOutlineClose } from "@react-icons/all-files/ai/AiOutlineClose";
import { ConnectedNode } from "@/types/workflow";
import { nodeConfig, deleteIconColors } from "@/utils/nodeConfig";
import { Edge, Node } from "reactflow";
import { deleteEdge, putNode } from "@/api/workflow";
import NodeAddMenu from "@/components/chatbot/chatflow/menu/NodeAddMenu";
import { extractActualValues, findAllParentNodes, restoreMonospaceBlocks } from "@/utils/node";
import { EdgeData, NodeData } from "@/types/chatbot";
import { NodeVariableInsertMenu } from "../menu/NodeVariableInsertMenu";
import { IoPencil } from "@react-icons/all-files/io5/IoPencil";
import { IoCheckmark } from "@react-icons/all-files/io5/IoCheckmark";
import { Tooltip } from "react-tooltip";
import { IoIosInformationCircleOutline } from "@react-icons/all-files/io/IoIosInformationCircleOutline";
import { getModels } from "@/api/model";
import { useQuery } from "@tanstack/react-query";
import { Model } from "@/types/model";

export default function LlmNodeDetail({
  chatFlowId,
  node,
  nodes,
  edges,
  setNodes,
  setEdges,
  setSelectedNode,
  onClose,
  connectedNodes: initialConnectedNodes, // 초기 connectedNodes를 받음
}: {
  chatFlowId: number
  node: Node<NodeData, string | undefined>,
  nodes: Node<NodeData, string | undefined>[],
  edges: Edge<EdgeData | undefined>[],
  setNodes: Dispatch<SetStateAction<Node<NodeData, string | undefined>[]>>
  setEdges: Dispatch<SetStateAction<Edge<EdgeData>[]>>
  setSelectedNode: Dispatch<SetStateAction<Node<NodeData, string | undefined> | null>>
  onClose: () => void
  connectedNodes: ConnectedNode[];
}) {
  // const [isOpen, setIsOpen] = useState(false);
  // const [modalOpen, setModalOpen] = useState(false);
  const [maxTokenLimit, setMaxTokenLimit] = useState(4096);
  const [connectedNodes, setConnectedNodes] = useState<ConnectedNode[]>(initialConnectedNodes);
  const [localPromptSystem] = useState(node.data.promptSystem);
  const [localPromptUser] = useState(node.data.promptUser);

  const [maxTokens, setMaxTokens] = useState(node.data.maxTokens);
  const [temperature, setTemperature] = useState(node.data.temperature);

  const maxTokensRef = useRef(node.data.maxTokens);
  const temperatureRef = useRef(node.data.temperature);

  const maxTokensTimerRef = useRef<ReturnType<typeof setTimeout> | null>(null);
  const temperatureTimerRef = useRef<ReturnType<typeof setTimeout> | null>(null);
  const promptSystemTimerRef = useRef<ReturnType<typeof setTimeout> | null>(null);
  const promptUserTimerRef = useRef<ReturnType<typeof setTimeout> | null>(null);
  const modelRef = useRef<ReturnType<typeof setTimeout> | null>(null);

  const promptSystemTextareaRef = useRef<(HTMLDivElement | null)>(null);
  const promptUserTextareaRef = useRef<(HTMLDivElement | null)>(null);

  const [selectedModel, setSelectedModel] = useState(node.data.modelName);
  console.log('selectedModel', selectedModel);

  const { isLoading, isError, error, data: models } = useQuery<Model[]>({
    queryKey: ['models'],
    queryFn: getModels,
  });

  console.log('selectednodeid', node.id);

  /**
   * 모델 변경 핸들러
   * @param value 
   */
  const handleModelChange = (value: string) => {
    setSelectedModel(value);

    // 선택된 모델의 maxToken 값을 가져옴
    const selectedModelData = models?.find((model) => model.name === value);
    const selectedMaxToken = selectedModelData?.maxTokens || 0; // 기본값 설정
    console.log('셀렉티드노드데이타', selectedModelData);
    console.log('selectedMaxToken', selectedMaxToken);

    // 노드 상태 업데이트
    setNodes((prevNodes) =>
      prevNodes.map((n) =>
        n.id === node.id
          ? {
            ...n,
            data: {
              ...n.data,
              modelName: value,
              maxTokens: selectedMaxToken,
            },
          }
          : n
      )
    );

    setMaxTokens(selectedMaxToken); // 슬라이더 값 업데이트

    if (modelRef.current) {
      clearTimeout(modelRef.current);
    }

    // 모델 및 maxToken 업데이트 API 호출
    modelRef.current = setTimeout(() => {
      const updatedData = {
        ...node.data,
        modelName: value,
        maxTokens: selectedMaxToken,
      };
      console.log("CALL NODE UPDATE:", updatedData);
      putNode(node.data.nodeId, updatedData);
    }, 500);
  };



  useEffect(() => {
    setSelectedModel(node.data.modelName);
  }, [node]);

  useEffect(() => {
    const selectedModelData = models?.find((model) => model.name === selectedModel);
    if (selectedModelData) {
      setMaxTokenLimit(selectedModelData.maxTokens);
      if (maxTokens > selectedModelData.maxTokens) {
        setMaxTokens(selectedModelData.maxTokens); // 현재 maxTokens 값이 초과하면 조정
      }
    }
  }, [selectedModel, models]);


  /**
  * redering 할 수있는 형태로 가공
  */
  useEffect(() => {
    const updateParentNodes = findAllParentNodes(node.id, nodes, edges);

    const renderPromptSystem = restoreMonospaceBlocks(updateParentNodes, node.data.promptSystem);
    const renderPromptUser = restoreMonospaceBlocks(updateParentNodes, node.data.promptUser);

    if (promptSystemTextareaRef.current) {
      promptSystemTextareaRef.current.innerHTML = renderPromptSystem;
    }

    if (promptUserTextareaRef.current) {
      promptUserTextareaRef.current.innerHTML = renderPromptUser;
    }
  }, [node.id]);

  /**
   * 높이 재설정
   */
  useEffect(() => {
    const adjustHeight = () => {
      if (promptSystemTextareaRef.current) {
        promptSystemTextareaRef.current.style.height = "auto";
        promptSystemTextareaRef.current.style.height = `${promptSystemTextareaRef.current.scrollHeight}px`;
      }

      if (promptUserTextareaRef.current) {
        promptUserTextareaRef.current.style.height = "auto";
        promptUserTextareaRef.current.style.height = `${promptUserTextareaRef.current.scrollHeight}px`;
      }
    };

    adjustHeight();

    setTimeout(adjustHeight, 0);
  }, [node.data.promptSystem, node.data.promptUser, node.data.renderPromptSystem, node.data.renderPromptUser]);

  /**
   * 시스템 프롬프트 수정 함수
   * @returns 
   */
  const handlePromptSystemChange = () => {
    if (!promptSystemTextareaRef.current) return;

    const actualValue = extractActualValues(promptSystemTextareaRef.current);

    // Node 상태 업데이트
    setNodes((prevNodes) =>
      prevNodes.map((n) =>
        n.id === node.id
          ? {
            ...n,
            data: {
              ...n.data,
              promptSystem: actualValue,
              renderPromptSystem: { __html: promptSystemTextareaRef.current.innerHTML },
            },
          }
          : n
      )
    );

    // Debounced API call
    if (promptSystemTimerRef.current) {
      clearTimeout(promptSystemTimerRef.current);
    }

    promptSystemTimerRef.current = setTimeout(() => {
      const updatedData = {
        ...node.data,
        promptSystem: actualValue,
      };
      console.log("CALL NODE UPDATE:", updatedData);
      putNode(node.data.nodeId, updatedData);
    }, 500);
  };

  /**
   * 시스템 프롬프트 수정 함수
   * @returns 
   */
  const handlePromptUserChange = () => {
    if (!promptUserTextareaRef.current) return;

    const actualValue = extractActualValues(promptUserTextareaRef.current);

    // Node 상태 업데이트
    setNodes((prevNodes) =>
      prevNodes.map((n) =>
        n.id === node.id
          ? {
            ...n,
            data: {
              ...n.data,
              promptUser: actualValue,
              renderPromptUser: { __html: promptUserTextareaRef.current.innerHTML },
            },
          }
          : n
      )
    );

    // Debounced API call
    if (promptUserTimerRef.current) {
      clearTimeout(promptUserTimerRef.current);
    }

    promptUserTimerRef.current = setTimeout(() => {
      const updatedData = {
        ...node.data,
        promptUser: actualValue,
      };
      console.log("CALL NODE UPDATE:", updatedData);
      putNode(node.data.nodeId, updatedData);
    }, 500);
  };

  /**
   * maxToken 수정 함수
   * @param value 
   */
  const handleMaxTokensChange = (value: number) => {
  const selectedModelData = models?.find((model) => model.name === selectedModel);
  const selectedMaxToken = selectedModelData?.maxTokens || 16384;

  // 입력된 값이 maxToken을 초과하지 않도록 보정
  const adjustedValue = Math.min(value, selectedMaxToken);

  setMaxTokens(adjustedValue);
  maxTokensRef.current = adjustedValue;

  if (maxTokensTimerRef.current) {
    clearTimeout(maxTokensTimerRef.current); // Reset the timer on each input
  }

  maxTokensTimerRef.current = setTimeout(() => {
    // Update the node data only after user stops typing
    const updatedNode = {
      ...node,
      data: {
        ...node.data,
        maxTokens: maxTokensRef.current,
      },
    };

    setNodes((prevNodes) =>
      prevNodes.map((n) => (n.id === node.id ? updatedNode : n))
    );

    putNode(node.data.nodeId, updatedNode.data);
  }, 500); // Wait for 500ms of inactivity
};


  /**
   * Temperature 수정 함수
   * @param value 
   */
  const handleTemperatureChange = (value: number) => {
    setTemperature(value);
    temperatureRef.current = value;

    if (temperatureTimerRef.current) {
      clearTimeout(temperatureTimerRef.current); // Reset the timer on each input
    }

    temperatureTimerRef.current = setTimeout(() => {
      // Update the node data only after user stops typing
      const updatedNode = {
        ...node,
        data: {
          ...node.data,
          temperature: temperatureRef.current,
        },
      };

      setNodes((prevNodes) =>
        prevNodes.map((n) => (n.id === node.id ? updatedNode : n))
      );

      putNode(node.data.nodeId, updatedNode.data);
    }, 500); // Wait for 500ms of inactivity
  };

  /**
   * 연결된 부모 노드 정보
   */
  const [parentNodes, setParentNodes] = useState<Node<NodeData, string>[]>(findAllParentNodes(node.id, nodes, edges));
  useEffect(() => {
    if (!node || !node.id || edges.length <= 0) return;

    const updateParentNodes = findAllParentNodes(node.id, nodes, edges);
    setParentNodes(updateParentNodes);
    console.log("parent Nodes:", updateParentNodes);
    // setVariables(parentNodes);
  }, [node.id, nodes.length, edges.length]);

  /**
   * 연결된 자식 노드 정보
   */
  useEffect(() => {
    setConnectedNodes(initialConnectedNodes);
  }, [initialConnectedNodes]);


  /**
   * 자식 노드 엣지 삭제 함수
   * @param targetNode 
   * @returns 
   */
  const deleteConnectEdge = (targetNode: ConnectedNode) => {
    const findDeleteEdge = edges.find((edge) => edge.source == node.id && edge.target == targetNode.nodeId.toString());
    if (!findDeleteEdge) return;
    deleteEdge(chatFlowId, +findDeleteEdge.id);
    setEdges((eds) => eds.filter((edge) => edge.id !== findDeleteEdge.id));
    setConnectedNodes((prev) => prev.filter((n) => n.nodeId !== targetNode.nodeId)); // 연결된 노드 상태 업데이트
  }

  const [isNodeNameEdit, setIsNodeNameEdit] = useState<boolean>(false);
  const nodeNameRef = useRef<(HTMLDivElement | null)>(null);
  /**
   * 노드 이름 수정
   */
  const handleEditToggle = () => {
    setIsNodeNameEdit((prev) => {
      if (!prev) {
        // 상태가 false -> true로 변경될 때
        setTimeout(() => {
          if (nodeNameRef.current) {
            nodeNameRef.current.focus(); // 포커스 설정

            const selection = window.getSelection();
            const range = document.createRange();

            if (selection) {
              range.selectNodeContents(nodeNameRef.current); // 텍스트 전체 선택
              range.collapse(false); // 텍스트 끝에 커서 배치
              selection.removeAllRanges();
              selection.addRange(range);
            }
          }
        }, 0); // DOM 업데이트 후 실행
      } else {
        // 상태가 true -> false로 변경될 때
        if (nodeNameRef.current) {
          const updatedName = nodeNameRef.current.innerText.trim();
          const updatedNodeData: Node = {
            ...node,
            data: {
              ...node.data,
              name: updatedName,
            },
          };
          console.log(updatedNodeData);

          setTimeout(() => {
            setNodes((prevNodes) =>
              prevNodes.map((n) =>
                n.id === node.id
                  ? updatedNodeData
                  : n
              )
            );
          }, 0);
          putNode(node.data.nodeId, updatedNodeData.data); // API 호출
        }
      }

      return !prev; // 상태 토글
    });
  };


  return (
    <>
      <div className="flex flex-col gap-4 w-[320px] h-[calc(100vh-170px)] rounded-[20px] p-[20px] bg-white bg-opacity-40 backdrop-blur-[15px] shadow-[0px_2px_8px_rgba(0,0,0,0.25)] overflow-y-auto">
        <div className="flex flex-row justify-between items-center mb-2">
          <div className="flex flex-row items-center gap-1">
            <FaRobot className="text-[#3B82F6] size-8" />
            <div
              ref={nodeNameRef}
              contentEditable={isNodeNameEdit}
              suppressContentEditableWarning
              className={isNodeNameEdit
                ? "text-[25px] w-[180px] font-semibold bg-white"
                : "text-[25px] w-[180px] font-semibold"
              }
            >
              {node.data.name}
            </div>
            {!isNodeNameEdit && <IoPencil
              className="ml-2 cursor-pointer text-[#5C5C5C] size-4"
              onClick={handleEditToggle}
            />}
            {isNodeNameEdit && <IoCheckmark
              className="ml-2 cursor-pointer text-[#5C5C5C] size-4"
              onClick={handleEditToggle}
            />}
          </div>
          <CgClose className="size-6 cursor-pointer" onClick={onClose} />
        </div>

        <div className="flex flex-col gap-2">
          <div className="text-[16px]">모델을 선택하세요.</div>
           <select
            id="model"
            value={selectedModel}
            onChange={(e) => handleModelChange(e.target.value)} 
            className="cursor-pointer mt-1 block w-full px-1 py-2 border border-gray-300 bg-white rounded-md shadow-sm focus:outline-none focus:ring-[#3B82F6] focus:border-[#3B82F6] sm:text-sm"
          >
            {models?.map((model: Model) => (
              <option key={model.name} value={model.name}>
                {model.name}
              </option>
            ))}
          </select>
        </div>
        <div className="flex flex-col items-start gap-2">
          <label htmlFor="top-k-range" className="text-sm font-semibold text-gray-700">
            최대 검색 토큰 수
          </label>
          <div className="flex items-center gap-4 w-full">
            {/* Number Input for direct value */}
            <input
              type="number"
              value={maxTokens}
              max={maxTokenLimit}
              min={1}
              step={1}
              onChange={(e) => handleMaxTokensChange(Number(e.target.value))}
              className="w-[80px] p-1 text-center border border-gray-300 rounded-md focus:ring focus:ring-blue-400"
            />
            {/* Range Input for slider */}
            <input
              id="top-k-range"
              type="range"
              max={maxTokenLimit}
              min={1}
              step={1}
              value={maxTokens}
              onChange={(e) => handleMaxTokensChange(Number(e.target.value))}
              className="flex-grow accent-blue-600"
            />
          </div>
        </div>

        <div className="flex flex-col items-start gap-2">
          <div className="flex gap-2 items-center">
            <label htmlFor="score-threshold-range" className="text-sm font-semibold text-gray-700">
              Temperature
            </label>
            <IoIosInformationCircleOutline
              data-tooltip-id="info-tooltip"
              className='text-[#4F4F4F]'
            />
            <Tooltip
              id="info-tooltip"
              content="낮을수록 더 예측 가능하고 일관된 응답을 얻고, 높을수록 창의적인 응답을 얻습니다."
              place="right"
              style={{
                backgroundColor: 'white',
                color: 'black',
                border: '1px solid #D1D5DB',
                boxShadow: '0 10px 15px rgba(0, 0, 0, 0.1), 0 4px 6px rgba(0, 0, 0, 0.05)',
                borderRadius: '0.375rem',
                padding: '0.5rem 0.75rem',
                width: '90%'
              }}
            />
          </div>
          <div className="flex items-center gap-4 w-full">
            {/* Number Input for direct input */}
            <input
              type="number"
              value={temperature}
              min={0}
              max={1}
              step={0.1}
              onChange={(e) => handleTemperatureChange(Number(e.target.value))}
              className="w-[80px] p-1 text-center border border-gray-300 rounded-md focus:ring focus:ring-blue-400"
            />
            {/* Range Input for slider */}
            <input
              id="score-threshold-range"
              type="range"
              max={1}
              min={0}
              step={0.1}
              value={temperature}
              onChange={(e) => handleTemperatureChange(Number(e.target.value))}
              className="flex-grow accent-blue-600"
            />
          </div>
        </div>
        <div className="flex flex-col gap-2">
          <div className="text-[16px]">프롬프트를 입력하세요.</div>
          <div className="flex flex-col gap-1 rounded-[10px] bg-white border border-gray-300 bg-white rounded-md shadow-sm focus:outline-none focus:ring-[#3B82F6] focus:border-[#3B82F6]">
            <div className="flex flex-row justify-between items-center">
              <div
                className="w-full flex flex-row justify-between mt-1 block w-[90px] px-2 py-1 bg-white rounded-md outline-none focus:outline-none sm:text-sm cursor-pointer font-bold border-none shadow-none"
              ><span>system</span>
                <NodeVariableInsertMenu
                  parentNodes={parentNodes}
                  editorRef={promptSystemTextareaRef}
                  onContentChange={handlePromptSystemChange}
                />
              </div>
            </div>
            <div
              ref={promptSystemTextareaRef}
              contentEditable
              suppressContentEditableWarning
              onInput={(e) => {
                const target = e.target as HTMLTextAreaElement;
                target.style.height = "auto";
                target.style.height = `${target.scrollHeight}px`;
                handlePromptSystemChange();
              }}
              // placeholder="프롬프트를 입력하세요."
              className="bg-white rounded-[5px] w-full resize-none overflow-hidden px-2 py-1 mt-2 focus:outline-none shadow-none border-none"
              style={{ minHeight: "90px", whiteSpace: "pre-wrap" }}
            >
              {localPromptSystem}
            </div>
          </div>

          <div className="flex flex-col gap-1 rounded-[10px] bg-white border border-gray-300 bg-white rounded-md shadow-sm focus:outline-none focus:ring-[#3B82F6] focus:border-[#3B82F6]">
            <div className="flex flex-row justify-between items-center">
              <div
                className="w-full flex flex-row justify-between mt-1 block w-[90px] px-2 py-1 bg-white rounded-md outline-none focus:outline-none sm:text-sm cursor-pointer font-bold border-none shadow-none"
              ><span>user</span>
                <NodeVariableInsertMenu
                  parentNodes={parentNodes}
                  editorRef={promptUserTextareaRef}
                  onContentChange={handlePromptUserChange}
                />
              </div>
            </div>
            <div
              ref={promptUserTextareaRef}
              contentEditable
              suppressContentEditableWarning
              onInput={(e) => {
                const target = e.target as HTMLTextAreaElement;
                target.style.height = "auto";
                target.style.height = `${target.scrollHeight}px`;
                handlePromptUserChange();
              }}
              // onChange={(e) => handlePromptUserChange("promptUser", e.target.value)}
              // placeholder="프롬프트를 입력하세요."
              className="bg-white rounded-[5px] w-full resize-none overflow-hidden px-2 py-1 mt-2 focus:outline-none shadow-none border-none"
              style={{ minHeight: "90px" }}
            >
              {localPromptUser}
            </div>
          </div>
          {/* <div onClick={addPrompt} className="bg-[#E0E0E0] hover:bg-[#DADADA] rounded-[5px] flex justify-center items-center py-1.5 cursor-pointer text-[14px]">
          + 프롬프트 추가
        </div> */}
        </div>

        <div className="flex flex-col gap-2">
          <div className="text-[16px]">다음 노드를 추가하세요.</div>
          <div className="flex flex-row justify-between w-full">
            <div className="aspect-square bg-[#A4C6FD] rounded-[360px] w-[50px] h-[50px] flex justify-center items-center z-[10]">
              <FaRobot className="text-[#3B82F6] size-8" />
            </div>
            <div className="bg-black h-[2px] w-[230px] flex-grow my-[24px]"></div>

            <div className="z-[10] w-[160px] mt-[6px]">
              {connectedNodes.length > 0 ? (
                connectedNodes.map((node, index) => (
                  <div
                    key={index}
                    className={`inline-flex items-center gap-2 w-[160px] rounded-md border border-gray-300 shadow-sm px-4 py-2 bg-[#${nodeConfig[node.type]?.color}] text-sm font-medium focus:outline-none focus:ring-1 focus:ring-[#3B82F6]`}
                  >
                    {nodeConfig[node.type]?.icon}
                    <span>{node.name || nodeConfig[node.type]?.label + node.nodeId}</span>
                    <AiOutlineClose
                      className="cursor-pointer ml-auto"
                      style={{
                        color: deleteIconColors[node.type] || "gray",
                      }}
                      onClick={() => deleteConnectEdge(node)}
                    />
                  </div>
                ))
              ) : (
                <NodeAddMenu
                  node={node}
                  nodes={nodes}
                  setNodes={setNodes}
                  setEdges={setEdges}
                  setSelectedNode={setSelectedNode}
                  isDetail={true}
                  questionClass={0}
                />
              )}
            </div>
          </div>
        </div>


      </div>
    </>
  );
}

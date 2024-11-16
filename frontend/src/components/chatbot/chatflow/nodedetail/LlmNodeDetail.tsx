import { FaRobot } from "@react-icons/all-files/fa/FaRobot";
import { Dispatch, SetStateAction, useEffect, useState } from "react";
import { IoPlay } from "@react-icons/all-files/io5/IoPlay";
import { CgClose } from "@react-icons/all-files/cg/CgClose";
import { AiOutlineClose } from "@react-icons/all-files/ai/AiOutlineClose";
import { ConnectedNode } from "@/types/workflow";
import { nodeConfig, deleteIconColors } from "@/utils/nodeConfig";
import { Edge, Node } from "reactflow";
import { deleteEdge } from "@/api/workflow";
import NodeAddMenu from "./NodeAddMenu";
import { getAllKnowledges } from "@/api/knowledge";


interface Model {
  id: string;
  name: string;
}

const models: Model[] = [
  { id: "gpt-4o", name: "GPT-4o" },
  { id: "gpt-4o-mini", name: "GPT-4o mini" },
  { id: "gpt-3.5-turbo", name: "GPT-3.5 Turbo" },
];

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
  node: Node<any, string | undefined>,
  nodes: Node<any, string | undefined>[],
  edges: Edge<any | undefined>[],
  setNodes: Dispatch<SetStateAction<Node<any, string | undefined>[]>>
  setEdges: Dispatch<SetStateAction<Edge<any>[]>>
  setSelectedNode: Dispatch<SetStateAction<Node<any, string | undefined> | null>>
  onClose: () => void
  connectedNodes: ConnectedNode[];
}) {
  const [isOpen, setIsOpen] = useState(false);
  const [modalOpen, setModalOpen] = useState(false);
  const [connectedNodes, setConnectedNodes] = useState<ConnectedNode[]>(initialConnectedNodes);

  const openModal = () => {
    getAllKnowledges()
      .then((data) => {
      });
    setModalOpen(true);
  };

  const closeModal = () => {
    setModalOpen(false);
  };

  const updatePromptSystem = (event: { target: { value: any; }; }) => {
    console.log(event.target.value);
    node.data.promptSystem = event.target.value;
    setNodes((prevNodes: Node[]) =>
      prevNodes.map((n) =>
        n.id === node.id ? node : n
      )
    );
  }

  const updatePromptUser = (event: { target: { value: any; }; }) => {
    console.log(event.target.value);
    node.data.promptUser = event.target.value;
    setNodes((prevNodes: Node[]) =>
      prevNodes.map((n) =>
        n.id === node.id ? node : n
      )
    );
  }


  useEffect(() => {
    setConnectedNodes(initialConnectedNodes);
  }, [initialConnectedNodes]);

  const deleteConnectEdge = (targetNode: ConnectedNode) => {
    const findDeleteEdge = edges.find((edge) => edge.source == node.id && edge.target == targetNode.nodeId.toString());
    if (!findDeleteEdge) return;
    deleteEdge(chatFlowId, +findDeleteEdge.id);
    setEdges((eds) => eds.filter((edge) => edge.id !== findDeleteEdge.id));
    setConnectedNodes((prev) => prev.filter((n) => n.nodeId !== targetNode.nodeId)); // 연결된 노드 상태 업데이트
  }
  
  return (
    <>
      <div className="flex flex-col gap-4 w-[320px] h-[calc(100vh-170px)] rounded-[20px] p-[20px] bg-white bg-opacity-40 backdrop-blur-[15px] shadow-[0px_2px_8px_rgba(0,0,0,0.25)] overflow-y-auto">
        <div className="flex flex-row justify-between items-center mb-2">
          <div className="flex flex-row items-center gap-1">
              <FaRobot className="text-[#3B82F6] size-8" />
              <div className="text-[25px] font-semibold">LLM</div>
          </div>
          <CgClose className="size-6 cursor-pointer" onClick={onClose}/>
        </div>
        
        <div className="flex flex-col gap-2">
          <div className="text-[16px]">모델을 선택하세요.</div>
          <div 
            className="cursor-pointer mt-1 block w-full px-3 py-2 border border-gray-300 bg-white rounded-md shadow-sm focus:outline-none focus:ring-[#3B82F6] focus:border-[#3B82F6] sm:text-sm"
          >
            GPT-4o mini
          </div>
          {/* <select
            id="model"
            // value={selectedModel}
            // onChange={handleChangeModel}
            className="cursor-pointer mt-1 block w-full px-3 py-2 border border-gray-300 bg-white rounded-md shadow-sm focus:outline-none focus:ring-[#3B82F6] focus:border-[#3B82F6] sm:text-sm"
          >
            {models.map((model: Model) => (
              <option key={model.id} value={model.id}>
                {model.name}
              </option>
            ))}
          </select> */}
        </div>
        <div className="flex flex-col gap-2">
        <div className="text-[16px]">프롬프트를 입력하세요.</div>
            <div className="flex flex-col gap-2 rounded-[10px] bg-white">
              <div className="flex flex-row justify-between items-center">
                <div
                  className="mt-1 block w-[90px] px-2 py-1 bg-white rounded-md outline-none focus:outline-none sm:text-sm cursor-pointer font-bold border-none shadow-none"
                >system
                </div>
              </div>
              <textarea
                onInput={(e) => {
                  const target = e.target as HTMLTextAreaElement;
                  target.style.height = "auto"; 
                  target.style.height = `${target.scrollHeight}px`; 
                }}
                value={node.data.promptSystem}
                onChange={updatePromptSystem}
                placeholder="프롬프트를 입력하세요."
                className="bg-white rounded-[5px] w-full resize-none overflow-hidden px-2 py-1 mt-2 focus:outline-none shadow-none border-none"
                style={{ minHeight: "90px" }}
              />
            </div>

            <div className="flex flex-col gap-2 rounded-[10px] bg-white">
              <div className="flex flex-row justify-between items-center">
                <div
                  className="mt-1 block w-[90px] px-2 py-1 bg-white rounded-md outline-none focus:outline-none sm:text-sm cursor-pointer font-bold border-none shadow-none"
                >user
                </div>
              </div>
              <textarea
                onInput={(e) => {
                  const target = e.target as HTMLTextAreaElement;
                  target.style.height = "auto"; 
                  target.style.height = `${target.scrollHeight}px`; 
                }}
                value={node.data.promptUser}
                onChange={updatePromptUser}
                placeholder="프롬프트를 입력하세요."
                className="bg-white rounded-[5px] w-full resize-none overflow-hidden px-2 py-1 mt-2 focus:outline-none shadow-none border-none"
                style={{ minHeight: "90px" }}
              />
            </div>
        {/* <div onClick={addPrompt} className="bg-[#E0E0E0] hover:bg-[#DADADA] rounded-[5px] flex justify-center items-center py-1.5 cursor-pointer text-[14px]">
          + 프롬프트 추가
        </div> */}
      </div>

      <div className="flex flex-col gap-2">
          <div className="text-[16px]">다음 블록을 추가하세요.</div>
          <div className="flex flex-row justify-between w-full">
            <div className="aspect-square bg-[#CEE8A3] rounded-[360px] w-[50px] h-[50px] flex justify-center items-center z-[10]">
              <IoPlay className="text-[#95C447] size-8" />
            </div>
            <div className="bg-black h-[2px] w-[230px] flex-grow my-[24px]"></div>

            <div className="z-[10] w-[160px] mt-[6px]">
              {connectedNodes.map((node, index) => (
                <div
                  key={index}
                  className={`inline-flex items-center gap-2 w-[160px] rounded-md border border-gray-300 shadow-sm px-4 py-2 bg-[#${nodeConfig[node.name]?.color}] text-sm font-medium focus:outline-none focus:ring-1 focus:ring-[#95C447]`}
                >
                  {nodeConfig[node.name]?.icon}
                  <span>{nodeConfig[node.name]?.label + node.nodeId || node.name}</span>
                  <AiOutlineClose
                    className="cursor-pointer ml-auto"
                    style={{
                      color: deleteIconColors[node.name] || "gray",
                    }}
                    onClick={() => deleteConnectEdge(node)}
                  />
                </div>
              ))}
              <NodeAddMenu
                node={node}
                nodes={nodes}
                setNodes={setNodes}
                setEdges={setEdges}
                setSelectedNode={setSelectedNode}
                isDetail={true}
                questionClass={0}
              />
            </div>
          </div>
        </div>

      </div>
    </>
  );
}

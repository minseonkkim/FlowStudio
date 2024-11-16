import { IoPlay } from "@react-icons/all-files/io5/IoPlay";
import { Dispatch, SetStateAction, useEffect, useState } from "react";
import { FiBookOpen } from "@react-icons/all-files/fi/FiBookOpen";
import { CgClose } from "@react-icons/all-files/cg/CgClose";
import { AiOutlineClose } from "@react-icons/all-files/ai/AiOutlineClose";
import { ConnectedNode } from "@/types/workflow";
import { nodeConfig, deleteIconColors } from "@/utils/nodeConfig";
import { Edge, Node } from "reactflow";
import { deleteEdge } from "@/api/workflow";
import NodeAddMenu from "./NodeAddMenu";
import { getAllKnowledges } from "@/api/knowledge";
import { Knowledge } from "@/types/chatbot";


export default function RetrieverNodeDetail({
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
  const [modalOpen, setModalOpen] = useState(false);
  const [selectedKnowledge, setSelectedKnowledge] = useState<Knowledge>(node.data.knowledge);
  const [connectedNodes, setConnectedNodes] = useState<ConnectedNode[]>(initialConnectedNodes);
  const [knowledges, setKnowledges] = useState<Knowledge[]>([])

  const openModal = () => {
    getAllKnowledges()
      .then((data) => {
        setKnowledges(data);
      });
    setModalOpen(true);
  };

  const closeModal = () => {
    setModalOpen(false);
  };

  const selectKnowledge = (knowledge: Knowledge) => {
    setSelectedKnowledge(knowledge);

    node.data.knowledge = knowledge;
    node.data.knowledgeId = knowledge.knowledgeId;
    setNodes((prevNodes: Node[]) =>
      prevNodes.map((n) =>
        n.id === node.id ? node : n
      )
    );
    closeModal();
  };

  useEffect(() => {
    setSelectedKnowledge(node.data.knowledge);
    setConnectedNodes(initialConnectedNodes);
  }, [node, initialConnectedNodes]);

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
            <FiBookOpen className="text-[#F97316] size-8" />
            <div className="text-[25px] font-semibold">지식 검색</div>
          </div>
          <CgClose className="size-6 cursor-pointer" onClick={onClose} />
        </div>
        <div className="flex flex-col gap-2">
          <div className="flex flex-row justify-between items-center">
            <div className="text-[16px]">지식을 선택하세요.</div>
            <div className="w-8 h-8 text-[21px] cursor-pointer flex justify-center items-center p-3 rounded-full hover:bg-[#e4e4e4]" onClick={openModal}>
              +
            </div>
          </div>

          <div className="h-[36px] rounded-[5px] p-3 bg-white flex items-center">
            {selectedKnowledge.title}
          </div>
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

      {/* 지식 선택 모달 */}
      {modalOpen && (
        <div className="modal-overlay fixed inset-0 bg-black bg-opacity-25 flex items-center justify-center z-50">
          <div className="bg-white rounded-lg shadow-lg w-[300px] p-5">
            <div className="text-lg font-semibold mb-4">지식 선택</div>
            <div className="flex flex-col gap-2">
              {knowledges.map((knowledge) => (
                <div
                  key={knowledge.knowledgeId}
                  className="flex justify-between items-center p-2 cursor-pointer hover:bg-gray-200 rounded"
                  onClick={() => selectKnowledge(knowledge)}
                >
                  <span>{knowledge.title}</span>
                  <span>{knowledge.totalToken}</span>
                </div>
              ))}
            </div>
            <button
              className="mt-4 w-full py-2 bg-gray-500 text-white rounded"
              onClick={closeModal}
            >
              닫기
            </button>
          </div>
        </div>
      )}

    </>

  );
}

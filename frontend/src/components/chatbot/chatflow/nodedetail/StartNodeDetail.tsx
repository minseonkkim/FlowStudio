import { IoPlay } from "@react-icons/all-files/io5/IoPlay";
import { CgClose } from "@react-icons/all-files/cg/CgClose";
import { Dispatch, SetStateAction, useEffect, useState } from "react";
import { ConnectedNode } from "@/types/workflow";
import { nodeConfig, deleteIconColors } from "@/utils/nodeConfig";
import { AiOutlineClose } from "@react-icons/all-files/ai/AiOutlineClose";
import { Edge, Node } from "reactflow";
import { deleteEdge } from "@/api/workflow";
import NodeAddMenu from "./NodeAddMenu";
import { EdgeData, NodeData } from "@/types/chatbot";

export default function StartNodeDetail({
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
  const [connectedNodes, setConnectedNodes] = useState<ConnectedNode[]>(initialConnectedNodes);

  useEffect(() => {
    console.log("여긴 시작 노드 상세화면 ", node);

  }, []);

  useEffect(() => {
    setConnectedNodes(initialConnectedNodes);
  }, [initialConnectedNodes]);

  const handleMaxCharsChange = (event: React.ChangeEvent<HTMLInputElement>) => {
    let value: number = +event.target.value;
    if (value > 2_147_483_647) {
      value = 2_147_483_647;
      return;
    }

    if (value < 0) {
      value = 0;
      return;
    }
    // node의 데이터 수정
    node.data.maxLength = value;

    // 상태 업데이트
    setNodes((prevNodes: Node[]) =>
      prevNodes.map((n) =>
        n.id === node.id ? node : n
      )
    );
  };

  const deleteConnectEdge = (targetNode: ConnectedNode) => {
    const findDeleteEdge = edges.find((edge) => edge.source == node.id && edge.target == targetNode.nodeId.toString());
    if (!findDeleteEdge) return;
    deleteEdge(chatFlowId, +findDeleteEdge.id);
    setEdges((eds) => eds.filter((edge) => edge.id !== findDeleteEdge.id));
    setConnectedNodes((prev) => prev.filter((n) => n.nodeId !== targetNode.nodeId)); // 연결된 노드 상태 업데이트
  }

  return (
    <div className="flex flex-col gap-4 w-[320px] h-[calc(100vh-170px)] rounded-[20px] p-[20px] bg-white bg-opacity-40 backdrop-blur-[15px] shadow-[0px_2px_8px_rgba(0,0,0,0.25)] overflow-y-auto">
      <div className="flex flex-row justify-between items-center mb-2">
        <div className="flex flex-row items-center gap-1">
          <IoPlay className="text-[#95C447] size-8" />
          <div className="text-[25px] font-semibold">시작</div>
        </div>
        <CgClose className="size-6 cursor-pointer" onClick={onClose} />
      </div>

      <div className="flex flex-col gap-2">
        <div className="text-[16px]">최대 글자수를 입력하세요.</div>
        <input
          className="h-[36px] rounded-[5px] p-3 border border-gray-300 focus:outline-none focus:ring-1 focus:ring-[#95C447]"
          type="number"
          value={node.data.maxLength}
          onChange={handleMaxCharsChange}
        />
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
  );
}

import { IoPlay } from "@react-icons/all-files/io5/IoPlay";
import { CgClose } from "@react-icons/all-files/cg/CgClose";
import { Dispatch, SetStateAction, useEffect, useRef, useState } from "react";
import { ConnectedNode } from "@/types/workflow";
import { nodeConfig, deleteIconColors } from "@/utils/nodeConfig";
import { AiOutlineClose } from "@react-icons/all-files/ai/AiOutlineClose";
import { Edge, Node } from "reactflow";
import { deleteEdge, putNode } from "@/api/workflow";
import NodeAddMenu from "@/components/chatbot/chatflow/menu/NodeAddMenu";
import { EdgeData, NodeData } from "@/types/chatbot";
import { IoPencil } from "@react-icons/all-files/io5/IoPencil";
import { IoCheckmark } from "@react-icons/all-files/io5/IoCheckmark";

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
  const [maxLength, setMaxLength] = useState(node.data.maxLength);
  const maxLengthRef = useRef(node.data.maxLength);
  const maxLengthTimerRef = useRef<ReturnType<typeof setTimeout> | null>(null);

  /**
   * 연결된 노드 관리
   */
  useEffect(() => {
    setConnectedNodes(initialConnectedNodes);
  }, [initialConnectedNodes]);

  /**
   * 최대 길이 수정
   * @param value 
   */
  const handleMaxCharsChange = (value: number) => {
    setMaxLength(value);
    maxLengthRef.current = value;

    if (maxLengthTimerRef.current) {
      clearTimeout(maxLengthTimerRef.current); // Reset the timer on each input
    }

    setNodes((prevNodes) =>
      prevNodes.map((n) => (n.id === node.id ? {
        ...n,
        data: {
          ...n.data,
          maxLength: value,
        }
      } : n))
    );

    maxLengthTimerRef.current = setTimeout(() => {
      // Update the node data only after user stops typing
      const updatedNode = {
        ...node,
        data: {
          ...node.data,
          maxLength: maxLengthRef.current,
        },
      };

      putNode(node.data.nodeId, updatedNode.data);
    }, 500); // Wait for 500ms of inactivity
  };

  /**
   * 연결된 노드 엣지 삭제
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
          const updatedNodeData : Node = {
            ...node,
            data: {
              ...node.data,
              name: updatedName,
            },
          };

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
    <div className="flex flex-col gap-4 w-[320px] h-[calc(100vh-170px)] rounded-[20px] p-[20px] bg-white bg-opacity-40 backdrop-blur-[15px] shadow-[0px_2px_8px_rgba(0,0,0,0.25)] overflow-y-auto">
      <div className="flex flex-row justify-between items-center mb-2">
        <div className="flex flex-row items-center gap-1">
          <IoPlay className="text-[#95C447] size-8" />
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
        <div className="text-[16px]">최대 글자수를 입력하세요.</div>
        <input
          className="h-[36px] rounded-[5px] p-3 border border-gray-300 focus:outline-none focus:ring-1 focus:ring-[#95C447]"
          type="number"
          value={maxLength}
          onChange={(e) => handleMaxCharsChange(Number(e.target.value))}
        />
      </div>

      <div className="flex flex-col gap-2">
        <div className="text-[16px]">다음 노드를 추가하세요.</div>
        <div className="flex flex-row justify-between w-full">
          <div className="aspect-square bg-[#CEE8A3] rounded-[360px] w-[50px] h-[50px] flex justify-center items-center z-[10]">
            <IoPlay className="text-[#95C447] size-8" />
          </div>
          <div className="bg-black h-[2px] w-[230px] flex-grow my-[24px]"></div>

          <div className="z-[10] w-[160px] mt-[6px]">
            {connectedNodes.length > 0 ? (
              connectedNodes.map((node, index) => (
                <div
                  key={index}
                  className={`inline-flex items-center gap-2 w-[160px] rounded-md border border-gray-300 shadow-sm px-4 py-2 bg-[#${nodeConfig[node.type]?.color}] text-sm font-medium focus:outline-none focus:ring-1 focus:ring-[#95C447]`}
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
  );
}

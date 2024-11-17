import { Dispatch, SetStateAction, useRef, useState, useEffect } from "react";
import { GrTree } from "@react-icons/all-files/gr/GrTree";
import { CgClose } from "@react-icons/all-files/cg/CgClose";
import { AiOutlineClose } from "@react-icons/all-files/ai/AiOutlineClose";
import { IoMdTrash } from "@react-icons/all-files/io/IoMdTrash";
import { ConnectedNode } from "@/types/workflow";
import { nodeConfig, deleteIconColors } from "@/utils/nodeConfig";
import { Edge, Node } from "reactflow";
import { deleteEdge, deleteQuestionClassNode, postQuestionClassNode, putQuestionClassNode } from "@/api/workflow";
import NodeAddMenu from "./NodeAddMenu";
import { EdgeData, NodeData, QuestionClass } from "@/types/chatbot";
import { debounce } from "@/utils/node";


export default function QuestionClassifierNodeDetail({
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
  const textareaRefs = useRef<(HTMLTextAreaElement | null)[]>([]);
  const [connectedNodes, setConnectedNodes] = useState<ConnectedNode[]>([]);
  const [localClasses, setLocalClasses] = useState<QuestionClass[]>([]);

  useEffect(() => {
    console.log("이것이 현재 클래스들이다 ", node.data.questionClasses);

    setLocalClasses(node.data.questionClasses);
  }, [node.data.questionClasses]);

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
      postQuestionClassNode(node.data.nodeId, { content: "" }).then((data: QuestionClass) => {
        // localClasses 업데이트
        const updatedLocalClasses = [...localClasses, data];
        setLocalClasses(updatedLocalClasses);

        // node.data.questionClasses 업데이트
        const updatedQuestionClasses = [...node.data.questionClasses, data];
        updateNodeDataQuestionClasses(updatedQuestionClasses);
      });
    }
  };

  const handleDeleteClass = (currentClass: QuestionClass) => {
    deleteQuestionClassNode(currentClass.id).then((data) => {
      if (data) {
        // localClasses 업데이트
        const updatedLocalClasses = localClasses.filter((cls) => cls.id !== currentClass.id);
        setLocalClasses(updatedLocalClasses);

        // node.data.questionClasses 업데이트
        const updatedQuestionClasses = node.data.questionClasses.filter(
          (cls: QuestionClass) => cls.id !== currentClass.id
        );
        updateNodeDataQuestionClasses(updatedQuestionClasses);
      }
    });
  };

  const debouncedSaveRef = useRef<(id: number, content: string) => void>();

  useEffect(() => {
    // debounce를 useRef에 저장
    debouncedSaveRef.current = debounce((id: number, content: string) => {
      putQuestionClassNode(id, { content });
    }, 500);
  }, []); // debounce는 한 번만 생성

  const handleClassContentChange = (currentClass: QuestionClass, newValue?: string) => {
    const updatedLocalClasses = localClasses.map((cls) =>
      cls.id === currentClass.id ? { ...cls, content: newValue } : cls
    );
    setLocalClasses(updatedLocalClasses);

    const updatedQuestionClasses = node.data.questionClasses.map((cls: QuestionClass) =>
      cls.id === currentClass.id ? { ...cls, content: newValue } : cls
    );
    updateNodeDataQuestionClasses(updatedQuestionClasses);

    // debouncedSave 호출
    if (debouncedSaveRef.current) {
      debouncedSaveRef.current(currentClass.id, newValue || "");
    }
  };


  /**
   * node.data.questionClasses를 업데이트하고 상태 반영
   */
  const updateNodeDataQuestionClasses = (updatedQuestionClasses: QuestionClass[]) => {
    // node의 데이터에 새로운 questionClasses 반영
    const updatedNode = {
      ...node,
      data: {
        ...node.data,
        questionClasses: updatedQuestionClasses,
      },
    };

    console.log("Updated node data:", updatedNode.data.questionClasses);

    // 상태 업데이트
    setNodes((prevNodes: Node[]) =>
      prevNodes.map((n) =>
        n.id === node.id ? updatedNode : n // 기존 노드와 id가 같으면 업데이트
      )
    );
  };


  useEffect(() => {
    // 상태 업데이트
    setConnectedNodes(initialConnectedNodes);
  }, [initialConnectedNodes, localClasses]); // 의존성 배열에 필요한 값 추가




  const deleteConnectEdge = (targetNode: ConnectedNode) => {
    const findDeleteEdge = edges.find((edge) => edge.source == node.id && edge.target == targetNode.nodeId.toString() && edge.sourceHandle == targetNode.sourceConditionId?.toString());
    if (!findDeleteEdge) return;
    deleteEdge(chatFlowId, +findDeleteEdge.id);
    setEdges((eds) => eds.filter((edge) => edge.id !== findDeleteEdge.id));
    setConnectedNodes((prev) => prev.filter((n) => n.nodeId !== targetNode.nodeId)); // 연결된 노드 상태 업데이트
  }


  return (
    <div className="flex flex-col gap-4 w-[320px] h-[calc(100vh-170px)] rounded-[20px] p-[20px] bg-white bg-opacity-40 backdrop-blur-[15px] shadow-[0px_2px_8px_rgba(0,0,0,0.25)] overflow-y-auto">
      <div className="flex flex-row justify-between items-center mb-2">
        <div className="flex flex-row items-center gap-1">
          <GrTree className="text-[#1E3A8A] size-7" />
          <div className="text-[25px] font-semibold">질문 분류기</div>
        </div>
        <CgClose className="size-6 cursor-pointer" onClick={onClose} />
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
                  onClick={() => handleDeleteClass(cls)}
                />
              )}
            </div>
            <textarea
              ref={(el) => {
                textareaRefs.current[index] = el;
              }}
              value={cls?.content || ""}
              onChange={(e) => {
                handleClassContentChange(cls, e.target.value);
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
                    {connectedNodes?.map((node, edgeIndex) =>
                      node.sourceConditionId == cls.id ? (
                        <div
                          key={edgeIndex}
                          className={`inline-flex items-center gap-2 w-[160px] rounded-md border border-gray-300 shadow-sm px-4 py-2 bg-[#${nodeConfig[node.name]?.color}] text-sm font-medium focus:outline-none focus:ring-1 focus:ring-[#95C447]`}
                        >
                          {nodeConfig[node.name]?.icon}
                          <span>{nodeConfig[node.name]?.label + node.nodeId || node.name}</span>
                          <AiOutlineClose
                            className="cursor-pointer ml-auto"
                            style={{ color: deleteIconColors[node.name] || "gray" }}
                            onClick={() => deleteConnectEdge(node)}
                          />
                        </div>
                      ) : null
                    )}

                    <NodeAddMenu
                      node={node}
                      nodes={nodes}
                      setNodes={setNodes}
                      setEdges={setEdges}
                      setSelectedNode={setSelectedNode}
                      isDetail={true}
                      questionClass={cls.id}
                    />
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

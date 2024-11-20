import { Dispatch, SetStateAction, useRef, useState, useEffect } from "react";
import { GrTree } from "@react-icons/all-files/gr/GrTree";
import { CgClose } from "@react-icons/all-files/cg/CgClose";
import { AiOutlineClose } from "@react-icons/all-files/ai/AiOutlineClose";
import { IoMdTrash } from "@react-icons/all-files/io/IoMdTrash";
import { ConnectedNode } from "@/types/workflow";
import { nodeConfig, deleteIconColors } from "@/utils/nodeConfig";
import { Edge, Node } from "reactflow";
import { deleteEdge, deleteQuestionClassNode, postQuestionClassNode, putNode, putQuestionClassNode } from "@/api/workflow";
import NodeAddMenu from "@/components/chatbot/chatflow/menu/NodeAddMenu";
import { EdgeData, NodeData, QuestionClass } from "@/types/chatbot";
import { debounce } from "@/utils/node";
import { IoPencil } from "@react-icons/all-files/io5/IoPencil";
import { IoCheckmark } from "@react-icons/all-files/io5/IoCheckmark";


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

  /**
   * 질문 클래스 관리
   */
  useEffect(() => {
    setLocalClasses(node.data.questionClasses);
  }, [node.data.questionClasses]);

  /**
   * 각 클래스별 높이 자동 수정
   */
  useEffect(() => {
    textareaRefs.current.forEach((textarea) => {
      if (textarea) {
        textarea.style.height = "auto";
        textarea.style.height = `${textarea.scrollHeight}px`;
      }
    });
  }, [localClasses]);

  /**
   * 클래스 추가 함수
   */
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

  /**
   * 클래스 삭제 함수
   * @param currentClass 
   */
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

  /**
   * 클래스 관리 ref
   */
  const debouncedSaveRef = useRef<(id: number, content: string) => void>();

  useEffect(() => {
    // debounce를 useRef에 저장
    debouncedSaveRef.current = debounce((id: number, content: string) => {
      putQuestionClassNode(id, { content });
    }, 500);
  }, []); // debounce는 한 번만 생성

  /**
   * 클래스 수정
   * @param currentClass 
   * @param newValue 
   */
  // const handleClassContentChange = (currentClass: QuestionClass, newValue?: string) => {
    // const updatedLocalClasses = localClasses.map((cls) =>
    //   cls.id === currentClass.id ? { ...cls, content: newValue } : cls
    // );
    // setLocalClasses(updatedLocalClasses);

    // const updatedQuestionClasses = node.data.questionClasses.map((cls: QuestionClass) =>
    //   cls.id === currentClass.id ? { ...cls, content: newValue } : cls
    // );
    // updateNodeDataQuestionClasses(updatedQuestionClasses);

    // // debouncedSave 호출
    // if (debouncedSaveRef.current) {
    //   debouncedSaveRef.current(currentClass.id, newValue || "");
    // }
  // };


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


  /**
   * 연결된 노드 간선 관리
   */
  useEffect(() => {
    // 상태 업데이트
    setConnectedNodes(initialConnectedNodes);
  }, [initialConnectedNodes, localClasses]); // 의존성 배열에 필요한 값 추가

  /**
   * 연결된 엣지 제거
   * @param targetNode 
   * @returns 
   */
  const deleteConnectEdge = (targetNode: ConnectedNode) => {
    const findDeleteEdge = edges.find((edge) => edge.source == node.id && edge.target == targetNode.nodeId.toString() && edge.sourceHandle == targetNode.sourceConditionId?.toString());
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
    <div className="flex flex-col gap-4 w-[320px] h-[calc(100vh-170px)] rounded-[20px] p-[20px] bg-white bg-opacity-40 backdrop-blur-[15px] shadow-[0px_2px_8px_rgba(0,0,0,0.25)] overflow-y-auto">
      <div className="flex flex-row justify-between items-center mb-2">
        <div className="flex flex-row items-center gap-1">
          <GrTree className="text-[#1E3A8A] size-7" />
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
      <div className="h-[36px] rounded-[5px] p-3 bg-white flex items-center border border-gray-300 bg-white rounded-md shadow-sm focus:outline-none focus:ring-[#3B82F6] focus:border-[#3B82F6]">gpt-4o</div>

      <div className="flex flex-col gap-2">
        <div className="text-[16px]">클래스를 추가하세요.</div>

        {localClasses.map((cls, index) => (
          <div key={index} className="flex flex-col gap-2 rounded-[10px] bg-white p-2 border border-gray-300 bg-white rounded-md shadow-sm focus:outline-none focus:ring-[#3B82F6] focus:border-[#3B82F6]">
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
        <div className="text-[16px]">다음 노드를 추가하세요.</div>
        <div className="flex flex-row justify-between w-full items-start">
          <div className="aspect-square bg-[#95A4CD] rounded-full w-[50px] h-[50px] flex justify-center items-center z-[10]">
            <GrTree className="text-[#1E3A8A] size-8" />
          </div>
          <div className="bg-black h-[2px] w-[230px] flex-grow my-[24px]"></div>

          <div className="flex flex-col gap-2 z-[10] w-[218px]">
            {localClasses.map((cls, index) => (
              <div key={index} className="flex flex-col gap-2 mb-4">
                <div className="flex flex-row items-start">
                  <div className="text-[14px] w-[74px]">클래스 {index + 1}</div>
                  <div className="flex flex-col w-[185px] mt-[6px]">
                    {connectedNodes?.map((node, edgeIndex) =>
                      node.sourceConditionId == cls.id ? (
                        <div
                          key={edgeIndex}
                          className={`inline-flex items-center gap-2 w-[160px] rounded-md border border-gray-300 shadow-sm px-4 py-2 bg-[#${nodeConfig[node.type]?.color}] text-sm font-medium focus:outline-none focus:ring-1 focus:ring-[#95C447]`}
                        >
                          {nodeConfig[node.type]?.icon}
                          <span>{node.name || nodeConfig[node.type]?.label + node.nodeId}</span>
                          <AiOutlineClose
                            className="cursor-pointer ml-auto"
                            style={{ color: deleteIconColors[node.type] || "gray" }}
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

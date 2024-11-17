import { RiQuestionAnswerFill } from "@react-icons/all-files/ri/RiQuestionAnswerFill"
import { CgClose } from "@react-icons/all-files/cg/CgClose"
import { Dispatch, SetStateAction, useEffect, useRef, useState } from "react";
import { Node, Edge  } from "reactflow";
import { findAllParentNodes } from "@/utils/node"
import { putNode } from "@/api/workflow"
import { EdgeData, NodeData } from "@/types/chatbot";

export default function AnswerNodeDetail({
  // chatFlowId,
  node,
  nodes,
  edges,
  setNodes,
  // setSelectedNode,
  onClose,
}: {
  // chatFlowId: number
  node: Node<NodeData, string | undefined>,
  nodes: Node<NodeData, string | undefined>[],
  edges: Edge<EdgeData | undefined>[],
  setNodes: Dispatch<SetStateAction<Node<NodeData, string | undefined>[]>>
  // setSelectedNode: Dispatch<SetStateAction<Node<NodeData, string | undefined> | null>>
  onClose: () => void
}) {
  const [localAnswer, setLocalAnswer] = useState<string>(node.data.outputMessage || "");
  const answerTimerRef = useRef<ReturnType<typeof setTimeout> | null>(null);
  const textareaRef = useRef<(HTMLDivElement | null)>(null);
  // const [variables, setVariables] = useState<Node[]>([]);

  useEffect(() => {
    setLocalAnswer(node.data.outputMessage);

    // 초기 화면 진입 시 height 조정
  const adjustHeight = () => {
    if (textareaRef.current) {
      textareaRef.current.style.height = "auto";
      textareaRef.current.style.height = `${textareaRef.current.scrollHeight}px`;
    }
  };

  adjustHeight();

  // DOM이 준비된 이후에도 높이를 재조정
  setTimeout(adjustHeight, 0);
  }, [node.id]);

  const handleAnswerChange = (value: string) => {
    // Node 상태 업데이트
    setNodes((prevNodes) =>
      prevNodes.map((n) =>
        n.id === node.id
          ? {
              ...n,
              data: {
                ...n.data,
                outputMessage: value,
              },
            }
          : n
      )
    );

    if (answerTimerRef.current) {
      clearTimeout(answerTimerRef.current); // 기존 타이머 초기화
    }

    answerTimerRef.current = setTimeout(() => {
      // API 호출
      const updatedData = {
        ...node.data,
        outputMessage: value,
      };
      console.log("CALL NODE UPDATE:", updatedData);
      putNode(node.data.nodeId, updatedData);
    }, 500); // 500ms 대기 후 호출
  };

  useEffect(() => {
    if (!node || !node.id || edges.length <= 0) return;

    const connectedNodes = findAllParentNodes(node.id, nodes, edges);
    console.log("Connected Nodes:", connectedNodes);
    // setVariables(connectedNodes);
  }, [node.id, nodes.length, edges.length]);

  return <>
    <div className="flex flex-col gap-4 w-[320px] h-[calc(100vh-170px)] rounded-[20px] p-[20px] bg-white bg-opacity-40 backdrop-blur-[15px] shadow-[0px_2px_8px_rgba(0,0,0,0.25)] overflow-y-auto">
      <div className="flex flex-row justify-between items-center mb-2">
        <div className="flex flex-row items-center gap-1">
          <RiQuestionAnswerFill className="text-[#34D399] size-8" />
          <div className="text-[25px] font-semibold">답변</div>
        </div>
        <CgClose className="size-6 cursor-pointer" onClick={onClose} />
      </div>

      <div className="flex flex-col gap-2">
        <div className="text-[16px]">답변을 입력하세요.</div>
        <div
          ref={textareaRef}
          contentEditable={true}
          suppressContentEditableWarning
          onInput={(e) => {
            const target = e.target as HTMLTextAreaElement;
            target.style.height = "auto";
            target.style.height = `${target.scrollHeight}px`;
            handleAnswerChange(target.innerText);
          }}
          className="p-2 bg-white rounded-[5px] w-full resize-none overflow-hidden mt-2 focus:outline-none shadow-none border-none"
          style={{ minHeight: "50px", whiteSpace: "pre-wrap" }}
        >
          {localAnswer}
        </div>
      </div>
    </div>
  </>
}
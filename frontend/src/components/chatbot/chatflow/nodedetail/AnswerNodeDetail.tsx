import { RiQuestionAnswerFill } from "@react-icons/all-files/ri/RiQuestionAnswerFill"
import { CgClose } from "@react-icons/all-files/cg/CgClose"
import { Dispatch, SetStateAction, useEffect, useRef, useState } from "react";
import { Node, Edge } from "reactflow";
import { extractActualValues, findAllParentNodes, restoreMonospaceBlocks } from "@/utils/node"
import { putNode } from "@/api/workflow"
import { EdgeData, NodeData } from "@/types/chatbot";
import { NodeVariableInsertMenu } from "../menu/NodeVariableInsertMenu";

export default function AnswerNodeDetail({
  node,
  nodes,
  edges,
  setNodes,
  onClose,
}: {
  node: Node<NodeData, string | undefined>,
  nodes: Node<NodeData, string | undefined>[],
  edges: Edge<EdgeData | undefined>[],
  setNodes: Dispatch<SetStateAction<Node<NodeData, string | undefined>[]>>
  onClose: () => void
}) {
  const [localAnswer] = useState<string>(node.data.outputMessage || "");
  const answerTimerRef = useRef<ReturnType<typeof setTimeout> | null>(null);
  const textareaRef = useRef<(HTMLDivElement | null)>(null);
  const [parentNodes, setParentNodes] = useState<Node<NodeData, string>[]>(findAllParentNodes(node.id, nodes, edges));
  /**
   * 연결된 노드 수정사항 바로 반영하기
   */
  useEffect(() => {
    if (!node || !node.id || edges.length <= 0) return;

    const updateParentNodes = findAllParentNodes(node.id, nodes, edges);
    setParentNodes(updateParentNodes);
    console.log("parent Nodes:", updateParentNodes);
    // setVariables(parentNodes);
  }, [node.id, nodes.length, edges.length]);

  /**
   * 높이 재설정
   */
  useEffect(() => {
    const adjustHeight = () => {
      if (textareaRef.current) {
        textareaRef.current.style.height = "auto";
        textareaRef.current.style.height = `${textareaRef.current.scrollHeight}px`;
      }
    };

    adjustHeight();

    setTimeout(adjustHeight, 0);
  }, [node.data.outputMessage, node.data.renderOutputMessage]);

  /**
   * redering 할 수있는 형태로 가공
   */
  useEffect(() => {
    const updateParentNodes = findAllParentNodes(node.id, nodes, edges);

    const renderOutputMessage = restoreMonospaceBlocks(updateParentNodes, node.data.outputMessage);

    if (textareaRef.current) {
      textareaRef.current.innerHTML = renderOutputMessage;
    }
  }, [node.id]);

  /**
   * 입력값 처리
   * @returns 
   */
  const handleAnswerChange = () => {
    if (!textareaRef.current) return;

    const actualValue = extractActualValues(textareaRef.current); // 실제 데이터 추출

    // Node 상태 업데이트
    setNodes((prevNodes) =>
      prevNodes.map((n) =>
        n.id === node.id
          ? {
            ...n,
            data: {
              ...n.data,
              outputMessage: actualValue,
              renderOutputMessage: {__html : textareaRef.current.innerHTML},
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
        outputMessage: actualValue,
      };
      console.log("CALL NODE UPDATE:", updatedData);
      putNode(node.data.nodeId, updatedData);
    }, 500); // 500ms 대기 후 호출
  };


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
        <div className="text-[16px]">
          답변을 입력하세요. 
          <NodeVariableInsertMenu 
            parentNodes={parentNodes} 
            editorRef={textareaRef}
            onContentChange={handleAnswerChange}
          /> 
        </div>
        <div
          ref={textareaRef}
          contentEditable
          suppressContentEditableWarning
          onInput={handleAnswerChange}
          className="p-2 bg-white rounded-[5px] w-full resize-none overflow-hidden mt-2 focus:outline-none shadow-none border-none"
          style={{ minHeight: "50px", whiteSpace: "pre-wrap" }}
        >
          {localAnswer}
        </div>
      </div>
    </div>
  </>
}
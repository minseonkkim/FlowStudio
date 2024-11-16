import { FaRobot } from "@react-icons/all-files/fa/FaRobot"
import { FiBookOpen } from "@react-icons/all-files/fi/FiBookOpen"
import { RiQuestionAnswerFill } from "@react-icons/all-files/ri/RiQuestionAnswerFill"
import { GrTree } from "@react-icons/all-files/gr/GrTree"
import { CgClose } from "@react-icons/all-files/cg/CgClose"
import { Dispatch, SetStateAction, useState } from "react";
import { Node } from "reactflow";

export default function AnswerNodeDetail({
  node,
  setNodes,
  onClose,
}: {
  node: Node<any, string | undefined>,
  setNodes: Dispatch<SetStateAction<Node<any, string | undefined>[]>>
  onClose: () => void
}) {
  const handleAnswerChange = (event: React.ChangeEvent<HTMLTextAreaElement>) => {
    var value: string = event.target.value;

    // node의 데이터 수정
    node.data.outputMessage = value;

    // 상태 업데이트
    setNodes((prevNodes: Node[]) =>
      prevNodes.map((n) =>
        n.id === node.id ? node : n
      )
    );
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
        <div className="text-[16px]">답변을 입력하세요.</div>
        <textarea
          value={node.data.outputMessage || ""}
          onChange={handleAnswerChange}
          className="p-2 bg-white rounded-[5px] w-full resize-none overflow-hidden mt-2 focus:outline-none shadow-none border-none"
          style={{ minHeight: "50px" }}
        />
      </div>
    </div>
  </>
}
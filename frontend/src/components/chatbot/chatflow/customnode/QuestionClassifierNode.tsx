import { Handle, Position, NodeProps} from "reactflow"
import { GrTree } from "@react-icons/all-files/gr/GrTree";
import { useRef, useEffect, useState } from "react";
import { MdDelete } from "@react-icons/all-files/md/MdDelete";
import { NodeData, QuestionClass } from "@/types/chatbot";

export default function QuestionClassifierNode({
  id, data, selected
}:
  NodeProps<NodeData>) {
    const classRefs = useRef<(HTMLDivElement | null)[]>([]);
    const containerRef = useRef<HTMLDivElement | null>(null);
    const [handlePositions, setHandlePositions] = useState<number[]>([]);

    useEffect(() => {
      // 동적 핸들 생성
      if (data.questionClasses) {

        classRefs.current = classRefs.current.slice(0, data.questionClasses.length);
        const newPositions = classRefs.current.map((el) => {
          if (el) {
            const { offsetTop, offsetHeight } = el;
            return offsetTop + offsetHeight / 2;
          }
          return 0;
        });
        setHandlePositions(newPositions);
      }

    }, [data.questionClasses]);

  return (
    <div
      ref={containerRef}
      className={`p-2 bg-[#E1E6F3] rounded-[16px] ${
        selected ? "border-[2px]" : "border-[1px]"
      } border-[#1E3A8A] text-[10px] w-[145px]`}
    >
      <Handle type="target" position={Position.Left} />
      <div className="flex flex-col gap-1.5">
        <div className="flex flex-row justify-between items-center">
          <div className="flex flex-row items-center gap-1">
            <GrTree className="text-[#1E3A8A] size-3" />
            <div className="text-[11px] font-semibold w-[90px]">{data.name}</div>
          </div>
          {selected && (
              <MdDelete
                className="cursor-pointer text-[#1E3A8A] hover:text-[#031750] size-3.5"
                onClick={() => data.onDelete(id)}
              />
            )}
        </div>
        <div className="flex flex-col gap-1 text-[8px]">
          <div className="rounded-[5px] p-0.5 bg-white">gpt-4o-mini</div>
          <div className="flex flex-col gap-0.5">
            <div>클래스</div>
            {data.questionClasses && data.questionClasses.length > 0 &&
              data.questionClasses.map((questionClass: QuestionClass, index: number) => (
                questionClass && (
                  <div
                    key={questionClass.id}
                    ref={(el) => { classRefs.current[index] = el; }}
                    className="rounded-[5px] p-0.5 bg-white flex flex-col gap-0.5"
                  >
                    <div className="font-bold text-[6px]">클래스 {index + 1}</div>
                    {questionClass.content !== "" && <div>{questionClass.content}</div>}
                  </div>
                )
              ))}
          </div>
        </div>
      </div>
      {/* 동적 핸들 생성 */}
      {data.questionClasses?.map((value, index) => (
        <Handle
          key={index}
          id={value.id.toString()} // QuestionClass id, Edge의 sourceHandle과 값이 같아야함
          type="source"
          position={Position.Right}
          style={{ top: handlePositions[index] }} // 동적 위치 설정
        />
      ))}
    </div>
  );
}

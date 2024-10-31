import { Handle, Position } from "reactflow";
import { GrTree } from "@react-icons/all-files/gr/GrTree";
import { useRef, useEffect, useState } from "react";

interface Class {
  text: string;
}

export default function QuestionClassifierNode({ data, selected }: any) {
  const classRefs = useRef<(HTMLDivElement | null)[]>([]);
  const containerRef = useRef<HTMLDivElement | null>(null);
  const [handlePositions, setHandlePositions] = useState<number[]>([]);

  useEffect(() => {
    // Clean up classRefs to match the current number of classes
    classRefs.current = classRefs.current.slice(0, data.classes.length);
    
    // Update positions based on the latest refs and offset positions
    const newPositions = classRefs.current.map((el) => {
      if (el) {
        const { offsetTop, offsetHeight } = el;
        return offsetTop + offsetHeight / 2;
      }
      return 0;
    });
    setHandlePositions(newPositions);
  }, [data.classes]); // Re-run effect when classes change

  return (
    <div
      ref={containerRef}
      className={`p-2 bg-[#E1E6F3] rounded-[16px] ${
        selected ? "border-[2px]" : "border-[1px]"
      } border-[#1E3A8A] text-[10px] w-[145px]`}
    >
      <Handle type="target" position={Position.Left} />
      <div className="flex flex-col gap-1.5">
        <div className="flex flex-row items-center gap-1">
          <GrTree className="text-[#1E3A8A] size-3" />
          <div className="text-[11px] font-semibold">질문 분류기</div>
        </div>
        <div className="flex flex-col gap-1 text-[8px]">
          <div className="rounded-[5px] p-0.5 bg-white">gpt-4o-mini</div>
          <div className="flex flex-col gap-0.5">
            <div>클래스</div>
            {data.classes && data.classes.length > 0 &&
              data.classes.map((cls: Class, index: number) => (
                cls && (
                  <div
                    key={index}
                    ref={(el) => { classRefs.current[index] = el; }}
                    className="rounded-[5px] p-0.5 bg-white flex flex-col gap-0.5"
                  >
                    <div className="font-bold text-[6px]">클래스 {index + 1}</div>
                    {cls.text !== "" && <div>{cls.text}</div>}
                  </div>
                )
              ))}
          </div>
        </div>
      </div>
      {handlePositions.map((pos, index) => (
        <Handle
          key={index}
          type="source"
          id={`handle${index + 1}`}
          position={Position.Right}
          style={{
            top: pos,
          }}
        />
      ))}
    </div>
  );
}

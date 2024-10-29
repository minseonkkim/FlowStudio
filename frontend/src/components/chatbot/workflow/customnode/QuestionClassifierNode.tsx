import { Handle, Position } from "reactflow"
import { GrTree } from "@react-icons/all-files/gr/GrTree";

interface Class{
  text: string;
}

export default function QuestionClassifierNode({ data }: any){
  return <>
  <div className="p-2 bg-[#E1E6F3] rounded-[16px] border-[1px] border-[#1E3A8A] text-[10px] w-[145px]">
      <Handle type="target" position={Position.Left} />
      <div className="flex flex-col gap-1.5">
        <div className="flex flex-row items-center gap-1">
          <GrTree className="text-[#1E3A8A] size-3"/>
          <div className="text-[11px] font-semibold">질문 분류기</div>
        </div>
        <div className="flex flex-col gap-1 text-[8px]">
          <div className="rounded-[5px] p-0.5 bg-white">gpt-4o-mini</div>
          <div className="flex flex-col gap-0.5">
            <div>클래스</div>
            {data.classes && data.classes.length > 0 && (
                data.classes.map((cls:Class, index:number) => (
                  cls && (
                    <div key={index} className="rounded-[5px] p-0.5 bg-white flex flex-col gap-0.5">
                      <div className="font-bold text-[6px]">클래스 {index + 1}</div>
                      {cls.text != "" && <div>{cls.text}</div>}
                    </div>
                  )
                ))
              )}
          </div>
        </div>
      </div>
      <Handle type="source" position={Position.Right} />
    </div>
  </>
}
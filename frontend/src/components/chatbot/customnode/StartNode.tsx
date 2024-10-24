import { Handle, Position } from "reactflow"
import { IoPlay } from "@react-icons/all-files/io5/IoPlay"

export default function StartNode({ data }: any){
  return <>
  <div className="p-2 bg-[#ECF3E0] rounded-[16px] border-[1px] border-[#95C447] text-[10px] w-[145px]">
      <div className="flex flex-col gap-1.5">
        <div className="flex flex-row items-center gap-1">
          <IoPlay className="text-[#95C447] size-3"/>
          <div className="text-[11px] font-semibold">시작</div>
        </div>
        {!Number.isNaN(data.maxChars) && 
          <div className="flex flex-col gap-0.5 text-[8px]">
            <div className="text-[8px]">최대 입력 글자수</div>
            <div className="rounded-[5px] p-0.5 bg-white">{data.maxChars}</div>
          </div>}
        {/* <div>{data.label}</div> */}
      </div>
      <Handle type="source" position={Position.Right} />
    </div>
  </>
}
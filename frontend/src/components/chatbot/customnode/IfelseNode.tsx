import { Handle, Position } from "reactflow"
import { IoGitBranchOutline } from "@react-icons/all-files/io5/IoGitBranchOutline";

export default function IfelseNode({ data }: any){
  return <>
  <div className="p-2 bg-[#FAE4E4] rounded-[16px] border-[1px] border-[#EF4444] text-[10px] w-[145px]">
      <Handle type="target" position={Position.Left} />
      <div className="flex flex-col gap-1.5">
        <div className="flex flex-row items-center gap-1">
          <IoGitBranchOutline className="text-[#EF4444] size-3"/>
          <div className="text-[11px] font-semibold">IF/ELSE</div>
        </div>
        <div className="flex flex-col gap-0.5 text-[8px]">
          <div className="font-bold text-end">IF</div>
          <div className="rounded-[5px] p-0.5 bg-white">rain &gt;= 0</div>
          <div className="font-bold text-end">ELIF</div>
          <div className="rounded-[5px] p-0.5 bg-white">shower &gt;= 0</div>
          <div className="font-bold text-end">ELSE</div>
          
        </div>

        {/* <div>{data.label}</div> */}
      </div>
      <Handle type="source" position={Position.Right} />
    </div>
  </>
}
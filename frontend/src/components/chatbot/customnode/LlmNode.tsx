import { Handle, Position } from "reactflow"
import { FaRobot } from "@react-icons/all-files/fa/FaRobot";

export default function LlmNode({ data }: any){
  return <>
  <div className="p-2 bg-[#EAF2FF] rounded-[16px] border-[1px] border-[#3B82F6] text-[10px] w-[145px]">
      <Handle type="target" position={Position.Left} />
      <div className="flex flex-col gap-1.5">
        <div className="flex flex-row items-center gap-1">
          <FaRobot className="text-[#3B82F6] size-3"/>
          <div className="text-[11px] font-semibold">LLM</div>
        </div>
        <div className="flex flex-col gap-1 text-[8px]">
          <div className="rounded-[5px] p-0.5 bg-white">gpt-4o-mini</div>
          <div className="flex flex-col gap-0.5">
            <div>SYSTEM 프롬프트</div>
            <div className="rounded-[5px] p-0.5 bg-white">You are an expert assistant specialized in providing real-time information. Respond to user queries accurately and concisely, delivering helpful and up-to-date information.</div>
          </div>
          <div className="flex flex-col gap-0.5">
            <div>USER 프롬프트</div>
            <div className="rounded-[5px] p-0.5 bg-white">Can you tell me what the weather is like in New York today?</div>
          </div>
        </div>
        {/* <div>{data.label}</div> */}
      </div>
      <Handle type="source" position={Position.Right} />
    </div>
  </>
}
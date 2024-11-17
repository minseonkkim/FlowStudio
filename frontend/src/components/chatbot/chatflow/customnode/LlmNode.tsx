import { Handle, Position, type NodeProps } from "reactflow"
import { FaRobot } from "@react-icons/all-files/fa/FaRobot";
import { MdDelete } from "@react-icons/all-files/md/MdDelete";
import { NodeData } from "@/types/chatbot";

export default function LlmNode ({
  id, data, selected
}:
  NodeProps<NodeData>) {

  return <>
  <div className={`p-2 bg-[#EAF2FF] rounded-[16px] ${
        selected ? "border-[2px]" : "border-[1px]"
      } border-[#3B82F6] text-[10px] w-[145px]`}>
      <Handle type="target" position={Position.Left} />
      <div className="flex flex-col gap-1.5">
        <div className="flex flex-row justify-between items-center">
          <div className="flex flex-row items-center gap-1">
            <FaRobot className="text-[#3B82F6] size-3"/>
            <div className="text-[11px] font-semibold">LLM</div>
          </div>
          {selected && (
              <MdDelete
                className="cursor-pointer text-[#3B82F6] hover:text-[#2E63B4] size-3.5"
                onClick={() => data.onDelete(id)}
              />
            )}
        </div>
        {data.modelName && data.modelName.length > 0 && 
          <div className="flex flex-col gap-1 text-[8px]">
              <div className="rounded-[5px] p-0.5 bg-white">{data.modelName}</div>
            {data.promptSystem && data.promptSystem.length > 0 &&
              <div className="flex flex-col gap-0.5">
                <div>시스템 프롬프트</div>
                <div className="rounded-[5px] p-0.5 bg-white">{data.promptSystem}</div>
              </div>
            }
            {data.promptUser && data.promptUser.length > 0 &&
              <div className="flex flex-col gap-0.5">
                <div>유저 프롬프트</div>
                <div className="rounded-[5px] p-0.5 bg-white">{data.promptUser}</div>
              </div>
            }
          </div>
        }
      </div>
      <Handle type="source" position={Position.Right} />
    </div>
  </>
}
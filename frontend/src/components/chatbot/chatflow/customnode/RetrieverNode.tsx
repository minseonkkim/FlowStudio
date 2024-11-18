import { Handle, NodeProps, Position } from "reactflow"
import { FiBookOpen } from "@react-icons/all-files/fi/FiBookOpen";
import { MdDelete } from "@react-icons/all-files/md/MdDelete";
import { NodeData } from "@/types/chatbot";

export default function RetrieverNode({
  id, data, selected
}:
  NodeProps<NodeData>) {
  
  return <>
  <div className={`p-2 bg-[#FFF3EB] rounded-[16px] border-[#F97316] text-[10px] w-[145px]
    ${selected ? "border-[2px]" : "border-[1px]"}
    ${data.isComplete ? "border-[5px]" : ""}
    ${data.isError ? "border-[5px] border-[#ff0000]" : "border-[#F97316]"}`}
  >
      <Handle type="target" position={Position.Left} />
      <div className="flex flex-col gap-1.5">
        <div className="flex flex-row justify-between items-center">
          <div className="flex flex-row items-center gap-1">
            <FiBookOpen className="text-[#F97316] size-3"/>
            <div className="text-[11px] font-semibold w-[90px]">{data.name}</div>
          </div>
          {selected && (
              <MdDelete
                className="cursor-pointer text-[#F97316] hover:text-[#C75B13] size-3.5"
                onClick={() => data.onDelete(id)}
              />
            )}
        </div>
        {data.knowledge?.title && data.knowledge.title !== "" && <div className="rounded-[5px] p-0.5 bg-white text-[8px]">{data.knowledge.title}</div>}
      </div>
      <Handle type="source" position={Position.Right} />
    </div>
  </>
}
import { Handle, Position } from "reactflow"
import { FiBookOpen } from "@react-icons/all-files/fi/FiBookOpen";
import { MdDelete } from "@react-icons/all-files/md/MdDelete";

interface KnowledgeNodeData {
  onDelete: () => void;
  fileName: string;
}

interface KnowledgeNodeProps {
  data: KnowledgeNodeData;
  selected: boolean;
}

export default function KnowledgeNode({ data, selected }: KnowledgeNodeProps){
  const { onDelete, fileName } = data;
  
  return <>
  <div className={`p-2 bg-[#FFF3EB] rounded-[16px] ${
        selected ? "border-[2px]" : "border-[1px]"
      } border-[#F97316] text-[10px] w-[145px]`}>
      <Handle type="target" position={Position.Left} />
      <div className="flex flex-col gap-1.5">
        <div className="flex flex-row justify-between items-center">
          <div className="flex flex-row items-center gap-1">
            <FiBookOpen className="text-[#F97316] size-3"/>
            <div className="text-[11px] font-semibold">지식 검색</div>
          </div>
          {selected && (
              <MdDelete
                className="cursor-pointer text-[#F97316] hover:text-[#C75B13] size-3.5"
                onClick={onDelete}
              />
            )}
        </div>
        {fileName && fileName !== "" && <div className="rounded-[5px] p-0.5 bg-white text-[8px]">{fileName}</div>}
      </div>
      <Handle type="source" position={Position.Right} />
    </div>
  </>
}
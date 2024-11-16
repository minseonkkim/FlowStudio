import { Handle, Position, type NodeProps, type Node } from "reactflow";
import { IoPlay } from "@react-icons/all-files/io5/IoPlay";

export default function StartNode({
  data, selected
}:
  NodeProps<Node<any, string | undefined>>
) {

  return (
    <>
      <div
        className={`p-2 bg-[#ECF3E0] rounded-[16px] border-[#95C447] ${selected ? "border-[2px]" : "border-[1px]"
          } text-[10px] w-[145px]`}
      >
        <div className="flex flex-col gap-1.5 relative">
          <div className="flex flex-row justify-between items-center">
            <div className="flex flex-row items-center gap-1">
              <IoPlay className="text-[#95C447] size-3" />
              <div className="text-[11px] font-semibold">시작</div>
            </div>
          </div>
        </div>
        <Handle type="source" position={Position.Right} />
      </div>
    </>
  );
}
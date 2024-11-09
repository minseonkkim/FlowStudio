import { Handle, Position } from "reactflow";
import { IoPlay } from "@react-icons/all-files/io5/IoPlay";
import { MdDelete } from "@react-icons/all-files/md/MdDelete";

interface StartNodeData {
  onDelete: () => void;
  maxChars?: number;
}

interface StartNodeProps {
  data: StartNodeData;
  selected: boolean;
}

export default function StartNode({ data, selected }: StartNodeProps) {
  // const { onDelete } = data;

  return (
    <>
      <div
        className={`p-2 bg-[#ECF3E0] rounded-[16px] border-[#95C447] ${
          selected ? "border-[2px]" : "border-[1px]"
        } text-[10px] w-[145px]`}
      >
        <div className="flex flex-col gap-1.5 relative">
          <div className="flex flex-row justify-between items-center">
            <div className="flex flex-row items-center gap-1">
              <IoPlay className="text-[#95C447] size-3" />
              <div className="text-[11px] font-semibold">시작</div>
            </div>
            {/* {selected && (
              <MdDelete
                className="cursor-pointer text-[#95C447] hover:text-[#6F9335] size-3.5"
                onClick={onDelete}
              />
            )} */}
          </div>
          {typeof data.maxChars === "number" && !isNaN(data.maxChars) && (
            <div className="flex flex-col gap-0.5 text-[8px]">
              <div className="text-[8px]">최대 입력 글자수</div>
              <div className="rounded-[5px] p-0.5 bg-white">{data.maxChars}</div>
            </div>
          )}
        </div>
        <Handle type="source" position={Position.Right} />
      </div>
    </>
  );
}

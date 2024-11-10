import { Handle, Position } from "reactflow";
import { IoGitBranchOutline } from "@react-icons/all-files/io5/IoGitBranchOutline";
import { useRef, useLayoutEffect, useState } from "react";
import { MdDelete } from "@react-icons/all-files/md/MdDelete";

interface IfelseNodeData {
  onDelete: () => void;
}

interface IfelseNodeProps {
  data: IfelseNodeData;
  selected: boolean;
}

export default function IfelseNode({ data, selected }: IfelseNodeProps) {
  const { onDelete } = data;
  const elifDivRef = useRef<HTMLDivElement>(null);
  const elseDivRef = useRef<HTMLDivElement>(null);
  const [elifTop, setElifTop] = useState(0);
  const [elseTop, setElseTop] = useState(0);

  useLayoutEffect(() => {
    const updatePositions = () => {
      if (elifDivRef.current) {
        setElifTop(elifDivRef.current.offsetTop + elifDivRef.current.offsetHeight / 2);
      }
      if (elseDivRef.current) {
        setElseTop(elseDivRef.current.offsetTop + elseDivRef.current.offsetHeight / 2);
      }
    };

    updatePositions(); 

    const observer = new ResizeObserver(updatePositions);
    if (elifDivRef.current) observer.observe(elifDivRef.current);
    if (elseDivRef.current) observer.observe(elseDivRef.current);

    return () => observer.disconnect();
  }, []);

  return (
    <>
      <div
        className={`p-2 bg-[#FAE4E4] rounded-[16px] ${
          selected ? "border-[2px]" : "border-[1px]"
        } border-[#EF4444] text-[10px] w-[145px] relative`}
      >
        <Handle type="target" position={Position.Left} />
        <div className="flex flex-col gap-1.5">
          <div className="flex flex-row justify-between items-center">
            <div className="flex flex-row items-center gap-1">
              <IoGitBranchOutline className="text-[#EF4444] size-3" />
              <div className="text-[11px] font-semibold">IF/ELSE</div>
            </div>
            {selected && (
              <MdDelete
                className="cursor-pointer text-[#EF4444] hover:text-[#B53030] size-3.5"
                onClick={onDelete}
              />
            )}
          </div>
          <div className="flex flex-col gap-0.5 text-[8px]">
            <div className="font-bold text-end">IF</div>
            <div className="rounded-[5px] p-0.5 bg-white">rain &gt;= 0</div>
            <div className="font-bold text-end" ref={elifDivRef}>ELIF</div>
            <div className="rounded-[5px] p-0.5 bg-white">
              shower &gt;= 0
            </div>
            <div className="font-bold text-end" ref={elseDivRef}>ELSE</div>
            <div className="rounded-[5px] p-0.5 bg-white">
              no rain or shower
            </div>
          </div>
        </div>
        <Handle
          type="source"
          position={Position.Right}
          id="ifsource"
          style={{ top: 38 }}
        />
        <Handle
          type="source"
          position={Position.Right}
          id="elifsource"
          style={{ top: elifTop }}
        />
        <Handle
          type="source"
          position={Position.Right}
          id="elsesource"
          style={{ top: elseTop }}
        />
      </div>
    </>
  );
}

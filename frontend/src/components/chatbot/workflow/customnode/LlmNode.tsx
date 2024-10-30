import { Handle, Position } from "reactflow"
import { FaRobot } from "@react-icons/all-files/fa/FaRobot";

interface Prompt {
    type: string;
    text: string;
}

interface LlmNodeProps {
    data: {
        prompts: Prompt[];
        model: string;
    };
    selected: boolean;
}

const LlmNode: React.FC<LlmNodeProps> = ({ data, selected }) => {
  return <>
  <div className={`p-2 bg-[#EAF2FF] rounded-[16px] ${
        selected ? "border-[2px]" : "border-[1px]"
      } border-[#3B82F6] text-[10px] w-[145px]`}>
      <Handle type="target" position={Position.Left} />
      <div className="flex flex-col gap-1.5">
        <div className="flex flex-row items-center gap-1">
          <FaRobot className="text-[#3B82F6] size-3"/>
          <div className="text-[11px] font-semibold">LLM</div>
        </div>
        <div className="flex flex-col gap-1 text-[8px]">
          <div className="rounded-[5px] p-0.5 bg-white">{data.model}</div>
          {data.prompts && data.prompts.length > 0 && (
            data.prompts.map((prompt, index) => (
              prompt.text !== "" && ( 
                <div key={index} className="flex flex-col gap-0.5">
                  <div>{prompt.type} 프롬프트</div>
                  <div className="rounded-[5px] p-0.5 bg-white">{prompt.text}</div>
                </div>
              )
            ))
          )}
        </div>
      </div>
      <Handle type="source" position={Position.Right} />
    </div>
  </>
}

export default LlmNode;
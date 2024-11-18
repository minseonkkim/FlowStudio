import { FaRobot } from "@react-icons/all-files/fa/FaRobot";
import { FiBookOpen } from "@react-icons/all-files/fi/FiBookOpen";
import { IoGitBranchOutline } from "@react-icons/all-files/io5/IoGitBranchOutline";
import { RiQuestionAnswerFill } from "@react-icons/all-files/ri/RiQuestionAnswerFill";
import { GrTree } from "@react-icons/all-files/gr/GrTree";
import { VscSymbolVariable } from "@react-icons/all-files/vsc/VscSymbolVariable";
import { NodeConfig } from "@/types/workflow"; 

const icons = {
  FaRobot: <FaRobot />,
  FiBookOpen: <FiBookOpen />,
  IoGitBranchOutline: <IoGitBranchOutline />,
  RiQuestionAnswerFill: <RiQuestionAnswerFill />,
  GrTree: <GrTree />,
  VscSymbolVariable: <VscSymbolVariable />,
};

export const nodeConfig: Record<string, NodeConfig> = {
  LLM: { label: "LLM", icon: icons.FaRobot, color: "EAF2FF" },
  RETRIEVER: { label: "지식 검색", icon: icons.FiBookOpen, color: "FFF3EB" },
  IFELSE: { label: "IF/ELSE", icon: icons.IoGitBranchOutline, color: "FAE4E4" },
  ANSWER: { label: "답변", icon: icons.RiQuestionAnswerFill, color: "E6F6F0" },
  QUESTION_CLASSIFIER: { label: "질문 분류기", icon: icons.GrTree, color: "E1E6F3" },
  VARIABLE: { label: "변수 할당자", icon: icons.VscSymbolVariable, color: "D8D8D8" },
};

export const deleteIconColors: { [key: string]: string } = {
    START: "#95C447",
    LLM: "#3B82F6",
    RETRIEVER: "#F97316",
    IFELSE: "#EF4444",
    ANSWER: "#34D399",
    QUESTION_CLASSIFIER: "#1E3A8A",
    VARIABLE: "#6B7280",
};

import { FaRobot } from "@react-icons/all-files/fa/FaRobot";
import { FiBookOpen } from "@react-icons/all-files/fi/FiBookOpen";
import { RiQuestionAnswerFill } from "@react-icons/all-files/ri/RiQuestionAnswerFill";
import { GrTree } from "@react-icons/all-files/gr/GrTree";
import { Dispatch, SetStateAction, useCallback, useEffect, useState } from "react";
import { addEdge, Edge, Node } from "reactflow";
import { getNodeDetail, postEdge } from "@/api/workflow";
import { addNode, createNodeData } from "@/utils/node";
import { EdgeData, NodeData } from "@/types/chatbot";

export default function NodeAddMenu({
    node,
    nodes,
    setNodes,
    setEdges,
    setSelectedNode,
    isDetail,
    questionClass,
}: {
    node: Node<NodeData, string | undefined>,
    nodes: Node<NodeData, string | undefined>[],
    setNodes: Dispatch<SetStateAction<Node<NodeData, string | undefined>[]>>,
    setEdges: Dispatch<SetStateAction<Edge<EdgeData>[]>>,
    setSelectedNode: Dispatch<SetStateAction<Node<NodeData, string | undefined> | null>>,
    isDetail: boolean,
    questionClass: number | null | undefined,
}) {
    const [isOpen, setIsOpen] = useState(false);
    const toggleDropdown = () => {
        setIsOpen(!isOpen);
    };

    useEffect(() => {
        if (isDetail === false) { // pane에서 클릭하면 바로 열린형태로 보이기
            setIsOpen(true);
        }
    }, [])

    const handleNodeTypeClick = useCallback((type: string) => {
        addNode(type, node, nodes, isDetail)
            .then((data) => {
                console.log("전달받은 데이터야 ", data);
                getNodeDetail(data.nodeId)
                    .then((nodeDetail) => {
                        const newNode = createNodeData(
                            nodeDetail,
                            node.data.chatFlowId, // 현재 노드가 가진 chatFlowId 전달
                            setNodes,
                            setEdges,
                            setSelectedNode
                        );
                        console.log("지금 노드를 만들었어 ", newNode);

                        const reactFlowNodes = {
                            id: newNode.nodeId.toString(),
                            type: newNode.type,
                            position: newNode.coordinate,
                            data: newNode, // 팩토리 함수로 생성된 NodeData 객체 전달
                        };

                        setNodes((prevNodes) => [...prevNodes, reactFlowNodes]);


                        if (isDetail === true) {
                            const edgeData: EdgeData = {
                                edgeId: 0,
                                sourceNodeId: node.data.nodeId,
                                targetNodeId: newNode.nodeId,
                                sourceConditionId: questionClass || 0,
                            };
                            // 노드 상세보기에서 추가한 경우 엣지를 추가
                            postEdge(node.data.chatFlowId, edgeData)
                                .then((data) => {
                                    console.log(data);
                                    const newReactEdge: Edge = {
                                        id: data.edgeId.toString(),
                                        source: data.sourceNodeId.toString(),
                                        target: data.targetNodeId.toString(),
                                        sourceHandle: data.sourceConditionId.toString(),
                                        data: { ...data }
                                    }
                                    setEdges((els) => {
                                        console.log(els);

                                        return addEdge(newReactEdge, els);
                                    })
                                })
                        }
                    });
            });
    }, []);

    return (
        <div className="relative inline-block text-left">
            {isDetail && (
                <div>
                    <button
                        type="button"
                        className="inline-flex justify-center w-[160px] rounded-md border border-gray-300 shadow-sm px-4 py-2 bg-white text-sm font-medium text-gray-700 hover:bg-gray-50 focus:outline-none focus:ring-1 focus:ring-[#95C447]"
                        onClick={toggleDropdown}
                    >
                        다음 블록 선택
                        <svg
                            className="-mr-1 ml-2 h-5 w-5"
                            xmlns="http://www.w3.org/2000/svg"
                            viewBox="0 0 20 20"
                            fill="currentColor"
                            aria-hidden="true"
                        >
                            <path
                                fillRule="evenodd"
                                d="M5.23 7.21a.75.75 0 011.06.02L10 10.94l3.71-3.71a.75.75 0 111.06 1.06l-4 4a.75.75 0 01-1.06 0l-4-4a.75.75 0 01.02-1.06z"
                                clipRule="evenodd"
                            />
                        </svg>
                    </button>
                </div>
            )}
            {isOpen && (
                <div
                    className="origin-top-right absolute right-0 mt-2 w-[160px] rounded-md shadow-lg bg-white ring-1 ring-black ring-opacity-5"
                    role="menu"
                    aria-orientation="vertical"
                    aria-labelledby="menu-button"
                >
                    <div className="p-1 text-[15px]" role="none">
                        <div
                            onClick={() => handleNodeTypeClick("LLM")}
                            className="hover:bg-[#f4f4f4] px-4 py-1.5 cursor-pointer flex flex-row items-center gap-2"
                        >
                            <FaRobot className="text-[18px]" />
                            <div>LLM</div>
                        </div>
                        <div
                            onClick={() => handleNodeTypeClick("RETRIEVER")}
                            className="hover:bg-[#f4f4f4] px-4 py-1.5 cursor-pointer flex flex-row items-center gap-2"
                        >
                            <FiBookOpen className="text-[18px]" />
                            <div>지식 검색</div>
                        </div>
                        <div
                            onClick={() => handleNodeTypeClick("ANSWER")}
                            className="hover:bg-[#f4f4f4] px-4 py-1.5 cursor-pointer flex flex-row items-center gap-2"
                        >
                            <RiQuestionAnswerFill className="text-[18px]" />
                            <div>답변</div>
                        </div>
                        <div
                            onClick={() => handleNodeTypeClick("QUESTION_CLASSIFIER")}
                            className="hover:bg-[#f4f4f4] px-4 py-1.5 cursor-pointer flex flex-row items-center gap-2"
                        >
                            <GrTree className="text-[18px]" />
                            <div>질문 분류기</div>
                        </div>
                        {/* <div
                      onClick={() => handleNodeTypeClick("CONDITIONAL")}
                      className="hover:bg-[#f4f4f4] px-4 py-1.5 cursor-pointer flex flex-row items-center gap-2"
                    >
                      <IoGitBranchOutline className="text-[18px]" />
                      <div>IF/ELSE</div>
                    </div>
                    <div
                      onClick={() => handleNodeTypeClick("VARIABLE_ASSIGNER")}
                      className="hover:bg-[#f4f4f4] px-4 py-1.5 cursor-pointer flex flex-row items-center gap-2"
                    >
                      <VscSymbolVariable className="text-[18px]" />
                      <div>변수 할당자</div>
                    </div> */}
                    </div>
                </div>
            )}
        </div>
    )
}
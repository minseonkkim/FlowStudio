import { FaRobot } from "@react-icons/all-files/fa/FaRobot";
import { FiBookOpen } from "@react-icons/all-files/fi/FiBookOpen";
import { RiQuestionAnswerFill } from "@react-icons/all-files/ri/RiQuestionAnswerFill";
import { GrTree } from "@react-icons/all-files/gr/GrTree";
import { Dispatch, SetStateAction, useCallback, useEffect, useState, useRef } from "react";
import { addEdge, Edge, Node as ReactFlowNode } from "reactflow"; // Import ReactFlowNode from reactflow
import { getNodeDetail, postEdge } from "@/api/workflow";
import { addNode, createNodeData } from "@/utils/node";
import { EdgeData, NodeData } from "@/types/chatbot";

// Make sure to import and use Node from React Flow correctly
export default function NodeAddMenu({
    node,
    nodes,
    setNodes,
    setEdges,
    setSelectedNode,
    isDetail,
    questionClass,
}: {
    node: ReactFlowNode<NodeData>, // Specify Node<NodeData> here for consistency
    nodes: ReactFlowNode<NodeData>[], // Specify Node<NodeData> here
    setNodes: Dispatch<SetStateAction<ReactFlowNode<NodeData>[]>>,
    setEdges: Dispatch<SetStateAction<Edge<EdgeData>[]>>,
    setSelectedNode: Dispatch<SetStateAction<ReactFlowNode<NodeData> | null>>,
    isDetail: boolean,
    questionClass: number | null | undefined,
}) {
    const [isOpen, setIsOpen] = useState(false);
    const dropdownRef = useRef<HTMLDivElement | null>(null); // Create a ref for the dropdown

    const toggleDropdown = () => {
        setIsOpen(!isOpen);
    };

    useEffect(() => {
        if (!isDetail) {
            setIsOpen(true);
        }
    }, [isDetail]);

    // Close the dropdown when clicking outside of it
    useEffect(() => {
        const handleClickOutside = (event: MouseEvent) => {
            const target = event.target as HTMLElement; // Safely cast to HTMLElement
            if (dropdownRef.current && !dropdownRef.current.contains(target)) {
                setIsOpen(false);
            }
        };

        // Add event listener
        document.addEventListener("mousedown", handleClickOutside);

        // Cleanup event listener
        return () => {
            document.removeEventListener("mousedown", handleClickOutside);
        };
    }, []);

    const handleNodeTypeClick = useCallback((type: string) => {
        setIsOpen((prev) => !prev);

        // Add a new node
        addNode(type, node, nodes, isDetail).then((data) => {
            getNodeDetail(data.nodeId).then((nodeDetail) => {
                const newNode = createNodeData(
                    nodeDetail,
                    node.data.chatFlowId,
                    setNodes,
                    setEdges,
                    setSelectedNode
                );

                const reactFlowNode: ReactFlowNode<NodeData> = {
                    id: newNode.nodeId.toString(),
                    type: newNode.type,
                    position: newNode.coordinate,
                    data: newNode,
                };

                // Update nodes state
                setNodes((prevNodes) => [...prevNodes, reactFlowNode]);

                // If the node is added in detail mode, also add an edge
                if (isDetail) {
                    const edgeData: EdgeData = {
                        edgeId: 0,
                        sourceNodeId: node.data.nodeId,
                        targetNodeId: newNode.nodeId,
                        sourceConditionId: questionClass || 0,
                    };

                    postEdge(node.data.chatFlowId, edgeData).then((edgeResponse) => {
                        const newEdge: Edge<EdgeData> = {
                            id: edgeResponse.edgeId.toString(),
                            source: edgeResponse.sourceNodeId.toString(),
                            target: edgeResponse.targetNodeId.toString(),
                            sourceHandle: edgeResponse.sourceConditionId.toString(),
                            data: { ...edgeResponse },
                        };

                        // Update edges state
                        setEdges((prevEdges) => addEdge(newEdge, prevEdges));
                    });
                }
            });
        });
    }, [node, nodes, isDetail, questionClass, setNodes, setEdges, setSelectedNode]);

    return (
        <div className="relative inline-block text-left">
            {isDetail && (
                <div>
                    <button
                        type="button"
                        className="inline-flex justify-center w-[160px] rounded-md border border-gray-300 shadow-sm px-4 py-2 bg-white text-sm font-medium text-gray-700 hover:bg-gray-50 focus:outline-none"
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
                    ref={dropdownRef} // Attach the ref here
                    className="z-[100] origin-top-right absolute right-0 mt-2 w-[160px] rounded-md shadow-lg bg-white ring-1 ring-black ring-opacity-5"
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
                    </div>
                </div>
            )}
        </div>
    );
}

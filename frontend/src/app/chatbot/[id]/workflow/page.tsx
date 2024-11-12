"use client";

import { useCallback, useEffect, useState } from "react";
import ReactFlow, {
  Background,
  Controls,
  MiniMap,
  applyNodeChanges,
  applyEdgeChanges,
  NodeChange,
  EdgeChange,
  ReactFlowProvider,
  Node,
  NodeTypes,
} from "reactflow";
import "reactflow/dist/style.css";
import StartNode from "@/components/chatbot/workflow/customnode/StartNode";
import LlmNode from "@/components/chatbot/workflow/customnode/LlmNode";
import KnowledgeNode from "@/components/chatbot/workflow/customnode/KnowledgeNode";
import IfelseNode from "@/components/chatbot/workflow/customnode/IfelseNode";
import AnswerNode from "@/components/chatbot/workflow/customnode/AnswerNode";
import QuestionClassifierNode from "@/components/chatbot/workflow/customnode/QuestionClassifierNode";
import VariableAllocatorNode from "@/components/chatbot/workflow/customnode/VariableAllocatorNode";
import StartNodeDetail from "@/components/chatbot/workflow/nodedetail/StartNodeDetail";
import LlmNodeDetail from "@/components/chatbot/workflow/nodedetail/LlmNodeDetail";
import KnowledgeNodeDetail from "@/components/chatbot/workflow/nodedetail/KnowledgeNodeDetail";
// import IfelseNodeDetail from "@/components/chatbot/workflow/nodedetail/IfelseNodeDetail";
import AnswerNodeDetail from "@/components/chatbot/workflow/nodedetail/AnswerNodeDetail";
import QuestionClassifierNodeDetail from "@/components/chatbot/workflow/nodedetail/QuestionClassifierNodeDetail";
// import VariableAllocatorNodeDetail from "@/components/chatbot/workflow/nodedetail/VariableAllocatorNodeDetail";
import VariableDetail from "@/components/chatbot/workflow/VariableDetail";
import { BsArrowUpRight } from "@react-icons/all-files/bs/BsArrowUpRight";
import { MdKeyboardArrowDown } from "@react-icons/all-files/md/MdKeyboardArrowDown";
import { v4 as uunodeIdv4 } from "uuid";
import ConfirmationModal from "@/components/common/ConfirmationModal";
import { useMutation, useQuery, useQueryClient } from "@tanstack/react-query";
import { getChatFlow } from "@/api/chatbot";
import { ChatFlowDetail, Coordinate, NodeData } from "@/types/chatbot";
import { EdgeData } from "@/types/chatbot";
import { deleteEdge as deleteEdgeApi, deleteNode as deleteNodeApi, postEdge, postNode } from "@/api/workflow";
import { NewNodeData } from "@/types/workflow";

interface WorkflowPageProps {
  params: {
    id: number;
  };
}

interface Model {
  nodeId: string;
  name: string;
}

const models: Model[] = [
  { nodeId: "gpt-3.5-turbo", name: "GPT-3.5 Turbo" },
  { nodeId: "gpt-4", name: "GPT-4" },
  { nodeId: "gpt-4-32k", name: "GPT-4 (32k)" },
];

interface ConnectedNode {
  nodeId: number;
  name: string;
}

// interface Variable {
//   name: string;
//   value: string;
//   type: string;
//   isEditing: boolean;
// }

const nodeTypes: NodeTypes = {
  START: StartNode,
  LLM: LlmNode,
  RETRIEVER: KnowledgeNode,
  // CONDITIONAL: IfelseNode,
  ANSWER: AnswerNode,
  QUESTION_CLASSIFIER: QuestionClassifierNode,
  // VARIABLE_ASSIGNER: VariableAllocatorNode,
};

const nodeTypeLabels: { [key: string]: string } = {
    START: "시작",
    LLM: "LLM",
    RETRIEVER: "지식 검색",
    // CONDITIONAL: "IF/ELSE",
    answerNode: "답변",
    QUESTION_CLASSIFIER: "질문 분류기",
    // VARIABLE_ASSIGNER: "변수 할당자",
};

export default function Page({ params }: WorkflowPageProps) {
  const [nodes, setNodes] = useState<NodeData[]>([]);
  const [nodesWithSelection, setNodesWithSelection] = useState<Node[]>([]);
  const [edges, setEdges] = useState<EdgeData[]>([]);
  const [selectedNode, setSelectedNode] = useState<NodeData | null>(null);
  const [selectedNodePosition, setSelectedNodePosition] = useState<Coordinate | null>({x: 0, y: 0});
  const [showVariableDetail, setShowVariableDetail] = useState<boolean>(false);
  const [selectedNodeId, setSelectedNodeId] = useState<number | null>(null);
  const pageId = params.id;

  const { isLoading, isError, error, data: chatFlow } = useQuery<ChatFlowDetail>({
    queryKey: ['chatFlow', pageId],  
    queryFn: ({ queryKey }) => getChatFlow(queryKey[1] as number) 
  });

  const queryClient = useQueryClient();

  const addNodeMutation = useMutation({
    mutationFn: postNode,
    onSuccess: (data, variables) => {
      queryClient.invalidateQueries({ queryKey: ["chatFlow"] });
      console.log("Node added successfully:", data);

      // const { condition } = variables;
      const newNodeId = data.nodeId; 
      
      if (newNodeId && selectedNodeId) {
        const newEdge: EdgeData = {
          edgeId: Date.now(),
          sourceNodeId: selectedNodeId,
          targetNodeId: newNodeId,
          // sourceConditionId: condition || undefined,
        };

        console.log(newEdge);

        addEdgeMutation.mutate({
          chatFlowId: pageId,
          edgeData: newEdge,
        });

        setEdges((prevEdges) => [...prevEdges, newEdge]);
      }
      
    },
    onError: (error) => {
      console.error("Error adding node:", error);
    },
  });

  // const deleteNodeMutation = useMutation({mutationFn: deleteNode, {
  //   onSuccess: () => {
  //     queryClient.invalidateQueries('nodes'); 
  //   },
  //   onError: (error) => {
  //     console.error('Error deleting node:', error);
  //   },
  // });
  const deleteNodeMutation = useMutation<unknown, Error, number>({
    mutationFn: async (nodeId: number) => {
      return await deleteNodeApi(nodeId);
    },
    onSuccess: (data, nodeId) => {
      queryClient.invalidateQueries({ queryKey: ["chatFlow"] });
      console.log("Node deleted successfully:", data);

      if (nodeId) {
        setEdges((prevEdges) =>
          prevEdges.filter(
            (edge) => edge.sourceNodeId !== nodeId && edge.targetNodeId !== nodeId
          )
        );
        console.log("Edges updated after node deletion:", edges);
      }
    },
    onError: (error) => {
      console.error("Error deleting node:", error);
    },
  });

  const deleteEdgeMutation = useMutation<unknown, Error, number>({
    mutationFn: async (edgeId: number) => {
      return await deleteEdgeApi(pageId, edgeId);
    },
    onSuccess: (data) => {
      queryClient.invalidateQueries({ queryKey: ["chatFlow"] });
      console.log("Edge deleted successfully:", data);

    },
    onError: (error) => {
      console.error("Error deleting edge:", error);
    },
  });



  const addEdgeMutation = useMutation<
    EdgeData,
    Error,
    { chatFlowId: number; edgeData: EdgeData }
  >({
    mutationFn: ({ chatFlowId, edgeData }) => postEdge(chatFlowId, edgeData),
    onSuccess: (data) => {
      queryClient.invalidateQueries({ queryKey: ["chatFlow"] });
      console.log("Edge added successfully:", data);
    },
    onError: (error) => {
      console.error("Error adding edge:", error);
    },
  });




  const [variables, setVariables] = useState<
    { name: string; value: string; type: string; isEditing: boolean }[]
  >([
    { name: "변수1", value: "", type: "string", isEditing: false },
    { name: "변수2", value: "", type: "string", isEditing: false },
  ]);


  useEffect(() => {
    console.log(chatFlow);
      if (chatFlow?.nodes) {
        setNodes(chatFlow.nodes);
      }
      if (chatFlow?.edges){
        setEdges(chatFlow.edges);
      }

      // const fetchedEdges = chatFlow.nodes.flatMap(node =>
      //   node.outputEdges.map(edge => ({
      //     nodeId: `e${edge.sourceNodeId}-${edge.targetNodeId}`,
      //     source: String(edge.sourceNodeId),
      //     target: String(edge.targetNodeId),
      //   }))
      // );
      // setEdges(fetchedEdges);
      // }
  }, [chatFlow]);

  // const onNodesChange = useCallback((changes: NodeChange[]) => {
  //   setNodesWithSelection((nds) => applyNodeChanges(changes, nds)); // 상태 업데이트
  // }, []);
const onNodesChange = useCallback((changes: NodeChange[]) => {
  setNodesWithSelection((nds) => {
    const updatedNodes = applyNodeChanges(changes, nds);
    return updatedNodes;
  });
}, []);


  // const onEdgesChange = useCallback((changes: EdgeChange[]) => {
  //   setEdges((eds) =>
  //     applyEdgeChanges(changes, eds).map((edge) => ({
  //       ...edge,
  //       sourceHandle: edge.sourceHandle ?? "",
  //     }))
  //   );
  // }, []);


  const onNodeClick = useCallback(
    (event: React.MouseEvent, node: Node) => {
      setSelectedNodeId(parseInt(node.id)); 
      setSelectedNodePosition({x: node.position.x, y: node.position.y});
      setSelectedNode(node as unknown as NodeData); 
      setShowVariableDetail(false);
    },
    []
  );

  const handleCloseDetail = useCallback(() => {
    setSelectedNode(null);
    setSelectedNodePosition(null);
  }, []);

  const handleVariableButtonClick = useCallback(() => {
    setShowVariableDetail((prev) => !prev);
  }, []);

  // 시작 노드 - 최대 글자 수 업데이트
  const updateMaxChars = useCallback((nodeId: number, newMaxChars: number) => {
    setNodes((nds) =>
      nds.map((node) =>
        node.nodeId === nodeId
          ? { ...node, maxLength: newMaxChars }
          : node
      )
    );
  }, []);

  // LLM - 모델 업데이트
  const updateSelectedModel = useCallback(
    (nodeId: number, newModel: string) => {
      setNodes((nds) =>
        nds.map((node) =>
          node.nodeId === nodeId
            ? { ...node, model: newModel }
            : node
        )
      );
      if (selectedNode && selectedNode.nodeId === nodeId) {
        setSelectedNode((prevNode) =>
          prevNode ? { ...prevNode, model: newModel  } : null
        );
      }
    },
    [selectedNode]
  );

  // LLM - 프롬프트 업데이트
  // const updateNodePrompts = useCallback(
  //   (nodeId: number, newPrompts: { type: string; text: string }[]) => {
  //     setNodes((nds) =>
  //       nds.map((node) =>
  //         node.nodeId === nodeId
  //           ? { ...node, prompts: newPrompts }
  //           : node
  //       )
  //     );
  //     if (selectedNode && selectedNode.nodeId === nodeId) {
  //       setSelectedNode((prevNode) =>
  //         prevNode
  //           ? {
  //               ...prevNode,
  //               prompts: newPrompts,
  //             }
  //           : null
  //       );
  //     }
  //   },
  //   [selectedNode]
  // );

  // LLM - 프롬프트 삭제
  // const removePrompt = useCallback(
  //   (nodeId: number, index: number) => {
  //     setNodes((nds) =>
  //       nds.map((node) =>
  //         node.nodeId === nodeId
  //           ? {
  //               ...node,
  //               prompts: node.prompts.filter((_: unknown, i: number) => i !== index),
  //             }
  //           : node
  //       )
  //     );

  //     if (selectedNode && selectedNode.nodeId === nodeId) {
  //       setSelectedNode((prevNode) =>
  //         prevNode
  //           ? {
  //               ...prevNode,
  //               prompts: prevNode.prompts.filter(
  //                   (_: unknown, i: number) => i !== index
  //               ),
  //             }
  //           : null
  //       );
  //     }
  //   },
  //   [selectedNode]
  // );

  // 지식 검색 - 지식 선택
  const updateKnowledgeFile = useCallback(
    (nodeId: number, filename: string) => {
      setNodes((nds) =>
        nds.map((node) =>
          node.nodeId === nodeId
            ? { ...node, fileName: filename } 
            : node
        )
      );

      if (selectedNode && selectedNode.nodeId === nodeId) {
        setSelectedNode((prevNode) =>
          prevNode ? { ...prevNode, fileName: filename } : null
        );
      }
    },
    [selectedNode]
  );


  // 답변 - 답변 업데이트
  const updateAnswer = useCallback((nodeId: number, answer: string) => {
    setNodes((nds) => {
      console.log("check답변수정",nds);
      
      return nds.map((node) =>
        node.nodeId === nodeId
          ? { ...node, outputMessage: answer }
          : node
      );
  });
  }, []);

  // 질문 분류기 - 클래스 업데이트
  // const updateClasses = useCallback(
  //   (nodeId: number, newClasses: { text: string }[]) => {
  //     setNodes((nds) =>
  //       nds.map((node) =>
  //         node.nodeId === nodeId
  //           ? { ...node, classes: newClasses  }
  //           : node
  //       )
  //     );
  //     if (selectedNode && selectedNode.nodeId === nodeId) {
  //       setSelectedNode((prevNode) =>
  //         prevNode ? { ...prevNode, classes: newClasses } : null
  //       );
  //     }
  //   },
  //   [selectedNode]
  // );

  // 변수 할당자 - 변수 업데이트
  // const updateVariableOnNode = useCallback(
  //   (selectedVar: Variable) => {
  //     if (selectedNode) {
  //       setNodes((nds) =>
  //         nds.map((node) =>
  //           node.nodeId === selectedNode.nodeId
  //             ? { ...node, data: {variable: selectedVar} }
  //             : node
  //         )
  //       );
  //     }
  //   },
  //   [selectedNode]
  // );

  const handleVariableChange = (
    index: number,
    key: "name" | "value" | "type",
    newValue: string
  ) => {
    setVariables((prev) =>
      prev.map((variable, i) =>
        i === index ? { ...variable, [key]: newValue } : variable
      )
    );
  };

  const handleAddVariable = () => {
    setVariables((prev) => [
      ...prev,
      { name: "", value: "", type: "string", isEditing: true },
    ]);
  };

  const handleRemoveVariable = (index: number) => {
    setVariables((prev) => prev.filter((_, i) => i !== index));
  };

  const handleEditToggle = (index: number) => {
    setVariables((prev) =>
      prev.map((variable, i) =>
        i === index ? { ...variable, isEditing: !variable.isEditing } : variable
      )
    );
  };

  // 노드 추가
  const addNode = useCallback(
    (type: string, condition?: number) => {
      if (selectedNodePosition == null || selectedNode == null) return;

      const selectedPosition = selectedNodePosition;

      const isPositionOccupied = (x: number, y: number) => {
        return nodes.some(
          (node) =>
            Math.abs(node.coordinate.x - x) < 200 &&
            Math.abs(node.coordinate.y - y) < 160
        );
      };

      const newX = selectedPosition.x + 200;
      let newY = selectedPosition.y;

      while (isPositionOccupied(newX, newY)) {
        newY += 160;
      }

      const newNode: NewNodeData = {
        chatFlowId: pageId,
        coordinate: {
          x: newX,
          y: newY,
        },
        nodeType: type,
      };
      
      addNodeMutation.mutate(newNode);
    },
    [selectedNode, nodes, edges]
  );

  useEffect(() => {
    if (selectedNodeId) {
      const selected = nodes.find((node) => node.nodeId === selectedNodeId);
      setSelectedNode(selected || null);
    }
  }, [selectedNodeId, nodes]);

  const getConnectedNodes = (nodeId: number) => {
    return edges
      .filter((edge) => edge.sourceNodeId === nodeId)
      .map((edge) => {
        const targetNode = nodes.find((node) => node.nodeId === edge.targetNodeId);
        return { nodeId: targetNode?.nodeId || 0, name: targetNode?.type || "Unknown" };
      });
  };

  const handleRemoveEdge = (sourceNodeId: number, targetNodeId: number) => {
  setEdges((currentEdges) => {
    const edgeToDelete = currentEdges.find(
      (edge) => edge.sourceNodeId === sourceNodeId && edge.targetNodeId === targetNodeId
    );

    if (edgeToDelete) {
      deleteEdgeMutation.mutate(edgeToDelete.edgeId);
    }

    return currentEdges.filter(
      (edge) => !(edge.sourceNodeId === sourceNodeId && edge.targetNodeId === targetNodeId)
    );
  });
};


  // const getConditionallyConnectedNodes = (nodeId: number) => {
  //   const ifNodes = edges
  //     .filter((edge) => edge.sourceNodeId === nodeId && edge.sourceHandle === "ifsource")
  //     .map((edge) => {
  //       const targetNode = nodes.find((node) => node.nodeId === edge.target);
  //       return { nodeId: targetNode?.nodeId || 0, name: targetNode?.type || "Unknown" };
  //     });

  //   const elifNodes = edges
  //     .filter((edge) => edge.sourceNodeId === nodeId && edge.sourceHandle === "elifsource")
  //     .map((edge) => {
  //       const targetNode = nodes.find((node) => node.nodeId === edge.target);
  //       return { nodeId: targetNode?.nodeId || 0, name: targetNode?.type || "Unknown" };
  //     });

  //   const elseNodes = edges
  //     .filter((edge) => edge.sourceNodeId === nodeId && edge.sourceHandle === "elsesource")
  //     .map((edge) => {
  //       const targetNode = nodes.find((node) => node.nodeId === edge.target);
  //       return { nodeId: targetNode?.nodeId || 0, name: targetNode?.type || "Unknown" };
  //     });

  //   return { ifNodes, elifNodes, elseNodes };
  // };

  const getConnectedNodesByCondition = (nodeId: number, conditions: number[]) => {
    return conditions.reduce((acc, condition) => {
      acc[condition] = edges
        .filter((edge) => edge.sourceNodeId === nodeId && edge.sourceConditionId === condition)
        .map((edge) => {
          const targetNode = nodes.find((node) => node.nodeId === edge.targetNodeId);
          return { nodeId: targetNode?.nodeId || 0, name: targetNode?.type || "Unknown" };
        });
      return acc;
    }, {} as Record<string, ConnectedNode[]>);
  };

  const renderNodeDetail = () => {
    if (!selectedNode) return null;

    const connectedNodeDetails = getConnectedNodes(selectedNodeId ?? -1);
    console.log('연결된 다음 노드', connectedNodeDetails);

    // const ifelseNodeDetails = getConditionallyConnectedNodes(selectedNode.nodeId);

    // const questionClassifierConditions =
    // selectedNode.type === "questionclassifierNode"
    //   ? selectedNode.classes?.map((_: unknown, index: number) => `handle${index + 1}`) || []
    //   : [];

    // const connectedNodesByCondition = getConnectedNodesByCondition(selectedNode.nodeId, questionClassifierConditions);

    switch (selectedNode.type) {
      case "START":
        return (
          <StartNodeDetail
            maxChars={selectedNode.maxLength} 
            setMaxChars={(newMaxChars: number) =>
              updateMaxChars(selectedNode.nodeId, newMaxChars)
            }
            addNode={addNode}
            onClose={handleCloseDetail}
            connectedNodes={connectedNodeDetails}
            setConnectedNodes={(targetNodeId) =>
              handleRemoveEdge(selectedNode.nodeId, targetNodeId)
            }
          />
        );
      // case "LLM":
      //   return (
      //     <LlmNodeDetail
      //       prompts={selectedNode.prompts || []}
      //       setPrompts={(newPrompts) => updateNodePrompts(selectedNode.nodeId, newPrompts)}
      //       selectedModel={selectedNode.model || models[0].nodeId}
      //       setModel={(newModel: string) => updateSelectedModel(selectedNode.nodeId, newModel)}
      //       removePrompt={(index: number) => removePrompt(selectedNode.nodeId, index)}
      //       addNode={addNode}
      //       onClose={handleCloseDetail}
      //       connectedNodes={connectedNodeDetails}
      //       setConnectedNodes={(targetNodeId) =>
      //         handleRemoveEdge(selectedNode.nodeId, targetNodeId)
      //       }
      //     />
      //   );
      case "RETRIEVER":
        return (
          <KnowledgeNodeDetail 
            addNode={addNode} 
            updateKnowledgeFile={(fileName) => updateKnowledgeFile(selectedNode.nodeId, fileName)}
            onClose={handleCloseDetail} 
            connectedNodes={connectedNodeDetails}
            setConnectedNodes={(targetNodeId) =>
                handleRemoveEdge(selectedNode.nodeId, targetNodeId)
              }/>);
      // case "CONDITIONAL":
      //   return (
      //   <IfelseNodeDetail 
      //     variables={variables} 
      //     addNode={(type, condition) => addNode(type, condition)} 
      //     onClose={handleCloseDetail} 
      //     connectedNodes={ifelseNodeDetails}
      //     setConnectedNodes={(targetNodeId) =>
      //           handleRemoveEdge(selectedNode.nodeId, targetNodeId)
      //         }/>);
      case "ANSWER":
        return (
          <AnswerNodeDetail
            answer={selectedNode.outputMessage}
            setAnswer={(newAnswer: string) => updateAnswer(selectedNode.nodeId, newAnswer)}
            addNode={addNode}
            onClose={handleCloseDetail}
            connectedNodes={connectedNodeDetails}
            setConnectedNodes={(targetNodeId) =>
                handleRemoveEdge(selectedNode.nodeId, targetNodeId)
              }
          />
        );
      // case "QUESTION_CLASSIFIER":
      //   return (
      //     <QuestionClassifierNodeDetail
      //       classes={selectedNode.classes || []}
      //       setClasses={(newClasses) => updateClasses(selectedNode.nodeId, newClasses)}
      //       addNode={(type, condition) => addNode(type, condition)}
      //       onClose={handleCloseDetail}
      //       connectedNodes={connectedNodesByCondition}
      //       setConnectedNodes={(targetNodeId) =>
      //         handleRemoveEdge(selectedNode.nodeId, targetNodeId)
      //       }
      //     />
      //   );
      // case "VARIABLE_ASSIGNER":
      //   return (
      //     <VariableAllocatorNodeDetail 
      //       variables={variables} 
      //       updateVariableOnNode={updateVariableOnNode}
      //       addNode={addNode} 
      //       onClose={handleCloseDetail} 
      //       connectedNodes={connectedNodeDetails}
      //       setConnectedNodes={(targetNodeId) =>
      //           handleRemoveEdge(selectedNode.nodeId, targetNodeId)
      //         }/>);
      default:
        return null;
    }
  };

  const renderVariableDetail = () => {
    if (!showVariableDetail) return null;

    return (
      <VariableDetail
        variables={variables}
        handleVariableChange={handleVariableChange}
        handleAddVariable={handleAddVariable}
        handleRemoveVariable={handleRemoveVariable}
        handleEditToggle={handleEditToggle}
        onClose={() => setShowVariableDetail(false)}
      />
    );
  };

  const [showChatbotCreationModal, setShowChatbotCreationModal] = useState(false);

  const handleChatbotCreationClick = useCallback(() => {
    setShowChatbotCreationModal((prev) => !prev);
  }, []);

  const renderChatbotCreationModal = () => {
    if (!showChatbotCreationModal) return null;

    return (
      <div className="text-[14px] absolute top-[135px] right-[25px] p-4 bg-white shadow-lg rounded-[10px] flex flex-col justify-between gap-3 z-[100] w-[250px] h-[200px]">
        <button className="px-3 py-2.5 bg-[#9A75BF] hover:bg-[#8D64B6] rounded-[8px] text-white font-bold cursor-pointer">
          업데이트
        </button>
        <div className="flex flex-col gap-3">
          <button className="p-2 bg-[#F2F2F2] hover:bg-[#ECECEC] rounded-[8px] cursor-pointer text-start flex flex-row items-center gap-1">
            앱 실행<BsArrowUpRight />
          </button>
          <button className="p-2 bg-[#F2F2F2] hover:bg-[#ECECEC] rounded-[8px] cursor-pointer text-start flex flex-row items-center gap-1">
            사이트에 삽입<BsArrowUpRight />
          </button>
        </div>
      </div>
    );
  };

  const [showConfirmationModal, setShowConfirmationModal] = useState(false);
  const [nodeToDelete, setNodeToDelete] = useState<number | null>(null);
  const [nodeTypeToDelete, setNodeTypeToDelete] = useState<string | null>(null);

  const deleteNode: (nodeId: number) => void = useCallback(
    (nodeId) => {
      // Edge를 먼저 필터링하여 상태 업데이트
      setEdges((currentEdges) =>
        currentEdges.filter(
          (edge) => edge.sourceNodeId !== nodeId && edge.targetNodeId !== nodeId
        )
      );

      // Node를 필터링하여 상태 업데이트
      setNodes((currentNodes) => currentNodes.filter((node) => node.nodeId !== nodeId));

      // 선택된 노드와 모달 관련 상태 초기화
      setSelectedNode(null);
      setShowConfirmationModal(false);
      setNodeToDelete(null);
      setNodeTypeToDelete(null);

      // Node 삭제 Mutation 호출
      deleteNodeMutation.mutate(nodeId);
    },
    [deleteNodeMutation] // deleteNodeMutation을 의존성에 추가
  );



  
  const openDeleteModal = (nodeId: number, nodeType: string) => {
    setShowConfirmationModal(true);
    setNodeToDelete(nodeId);
    setNodeTypeToDelete(nodeTypeLabels[nodeType] || "");
  };

  const handleConfirmDelete = () => {
    if (nodeToDelete) {
      deleteNode(nodeToDelete);
    }
    setShowConfirmationModal(false);
  };

  const handleCancelDelete = () => {
    setShowConfirmationModal(false);
    setNodeToDelete(null);
    setNodeTypeToDelete(null);
  };

  useEffect(() => {
    const updatedNodesWithSelection: Node[] = nodes.map((node) => ({
      id: String(node.nodeId),
      position: node.coordinate,
      data: {
        ...node,
        onDelete: () => openDeleteModal(node.nodeId, node.type ?? ""),
      },
      type: node.type,
      selected: node.nodeId === selectedNodeId,
    }));

    setNodesWithSelection(updatedNodesWithSelection); // 상태 업데이트
  }, [nodes, selectedNodeId]);

  // react-flow의 node가 변경되면 작동할 useEffect
  useEffect(() => {
    const coordinatesDiffer = nodesWithSelection.some((updatedNode, index) => {
    const originalNode = nodes[index];
      if (updatedNode.position.x !== originalNode.coordinate.x
        || updatedNode.position.y !== originalNode.coordinate.y
      ) {
          nodes[index].coordinate.x = updatedNode.position.x;
          nodes[index].coordinate.y = updatedNode.position.y;
      }
    });
  }, [nodesWithSelection]);


  const edgesWithCorrectType = edges.map((edge) => ({
    id: `e${edge.sourceNodeId}-${edge.targetNodeId}`,
    source: String(edge.sourceNodeId),
    target: String(edge.targetNodeId),
  }));


  return (
    <>
      <div className="absolute top-[80px] right-[30px] flex flex-row gap-3 z-[10]">
        <button
            className="px-3 py-2.5 bg-white hover:bg-[#F3F3F3] rounded-[10px] text-[#9A75BF] font-bold shadow-[0px_2px_8px_rgba(0,0,0,0.25)] cursor-pointer"
          onClick={handleVariableButtonClick}
        >
          변수
        </button>
        <button
          onClick={handleChatbotCreationClick}
          className="flex flex-row gap-1 items-center px-3 py-2.5 bg-[#9A75BF] hover:bg-[#8A64B1] rounded-[10px] text-white font-bold shadow-[0px_2px_8px_rgba(0,0,0,0.25)] cursor-pointer"
        >
          챗봇 생성 <MdKeyboardArrowDown className="size-4" />
        </button>
      </div>
      <div className="absolute top-[140px] right-[30px] z-[10] flex flex-row">
        {renderNodeDetail()}
        {renderVariableDetail()}
      </div>
      {renderChatbotCreationModal()}
      <ReactFlowProvider>
        <div style={{ height: "calc(100vh - 60px)", backgroundColor: "#F0EFF1" }}>
          <ReactFlow
            nodes={nodesWithSelection}
            edges={edgesWithCorrectType}
            onNodesChange={onNodesChange}
            // onEdgesChange={onEdgesChange}
            onNodeClick={onNodeClick}
            zoomOnScroll={true}
            zoomOnPinch={true}
            panOnScroll={true}
            minZoom={0.5}
            maxZoom={2}
            nodeTypes={nodeTypes}
            fitView
          >
            <MiniMap
              style={{
                bottom: 5,
                left: 40,
                width: 150,
                height: 100,
              }}
            />
            <Controls />
            <Background />
          </ReactFlow>
        </div>
      </ReactFlowProvider>
      {showConfirmationModal && (
        <ConfirmationModal
          message={`${nodeTypeToDelete} 노드를 삭제하시겠습니까?`}
          onConfirm={handleConfirmDelete}
          onCancel={handleCancelDelete}
        />
      )}
    </>
  );
}

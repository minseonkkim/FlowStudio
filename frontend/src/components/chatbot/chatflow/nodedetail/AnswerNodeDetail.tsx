import { RiQuestionAnswerFill } from "@react-icons/all-files/ri/RiQuestionAnswerFill";
import { CgClose } from "@react-icons/all-files/cg/CgClose";
import { Dispatch, SetStateAction, useEffect, useRef, useState } from "react";
import { Node, Edge } from "reactflow";
import { extractActualValues, findAllParentNodes, restoreMonospaceBlocks } from "@/utils/node";
import { putNode } from "@/api/workflow";
import { EdgeData, NodeData } from "@/types/chatbot";
import { NodeVariableInsertMenu } from "../menu/NodeVariableInsertMenu";
import { IoPencil } from "@react-icons/all-files/io5/IoPencil";
import { IoCheckmark } from "@react-icons/all-files/io5/IoCheckmark";

export default function AnswerNodeDetail({
  node,
  nodes,
  edges,
  setNodes,
  onClose,
}: {
  node: Node<NodeData, string | undefined>,
  nodes: Node<NodeData, string | undefined>[],
  edges: Edge<EdgeData | undefined>[],
  setNodes: Dispatch<SetStateAction<Node<NodeData, string | undefined>[]>>,
  onClose: () => void
}) {
  const [localAnswer] = useState<string>(node.data.outputMessage || "");
  const answerTimerRef = useRef<ReturnType<typeof setTimeout> | null>(null);
  const textareaRef = useRef<(HTMLDivElement | null)>(null);
  const [parentNodes, setParentNodes] = useState<Node<NodeData, string>[]>(findAllParentNodes(node.id, nodes, edges));

  // Update parent nodes whenever the node or edges change
  useEffect(() => {
    if (!node || !node.id || edges.length <= 0) return;

    const updateParentNodes = findAllParentNodes(node.id, nodes, edges);
    setParentNodes(updateParentNodes);
    console.log("parent Nodes:", updateParentNodes);
  }, [node.id, nodes.length, edges.length]);

  // Adjust height of textarea dynamically
  useEffect(() => {
    const adjustHeight = () => {
      if (textareaRef.current) {
        // Reset height to auto to shrink when content is deleted
        textareaRef.current.style.height = "auto";
        // Adjust height based on scrollHeight (content height)
        textareaRef.current.style.height = `${textareaRef.current.scrollHeight}px`;
      }
    };

    adjustHeight(); // Initial adjustment

    // Optionally, you could use a delay to ensure the content is fully rendered before adjusting
    setTimeout(adjustHeight, 0);
  }, [node.data.outputMessage, node.data.renderOutputMessage]); // Trigger when content changes

  // Process and render content dynamically
  useEffect(() => {
    const updateParentNodes = findAllParentNodes(node.id, nodes, edges);
    const renderOutputMessage = restoreMonospaceBlocks(updateParentNodes, node.data.outputMessage);

    if (textareaRef.current) {
      textareaRef.current.innerHTML = renderOutputMessage;
    }
  }, [node.id]);

  // Handle content change and update nodes
  const handleAnswerChange = () => {
    if (!textareaRef.current) return;

    const actualValue = extractActualValues(textareaRef.current); // Extract actual value

    // Update node state
    setNodes((prevNodes) =>
      prevNodes.map((n) =>
        n.id === node.id
          ? {
            ...n,
            data: {
              ...n.data,
              outputMessage: actualValue,
              renderOutputMessage: { __html: textareaRef.current.innerHTML },
            },
          }
          : n
      )
    );

    // Adjust height dynamically when content changes
    const adjustHeight = () => {
      if (textareaRef.current) {
        textareaRef.current.style.height = "auto";
        textareaRef.current.style.height = `${textareaRef.current.scrollHeight}px`;
      }
    };
    adjustHeight();

    if (answerTimerRef.current) {
      clearTimeout(answerTimerRef.current); // Clear previous timer
    }

    answerTimerRef.current = setTimeout(() => {
      // API call after 500ms
      const updatedData = {
        ...node.data,
        outputMessage: actualValue,
      };
      console.log("CALL NODE UPDATE:", updatedData);
      putNode(node.data.nodeId, updatedData);
    }, 500);
  };

  // Node name editing state and refs
  const [isNodeNameEdit, setIsNodeNameEdit] = useState<boolean>(false);
  const nodeNameRef = useRef<(HTMLDivElement | null)>(null);

  // Toggle edit state for node name
  const handleEditToggle = () => {
    setIsNodeNameEdit((prev) => {
      if (!prev) {
        // Enter edit mode, focus on node name
        setTimeout(() => {
          if (nodeNameRef.current) {
            nodeNameRef.current.focus(); // Set focus on node name input

            const selection = window.getSelection();
            const range = document.createRange();

            if (selection) {
              range.selectNodeContents(nodeNameRef.current); // Select the text inside
              range.collapse(false); // Place cursor at the end of the text
              selection.removeAllRanges();
              selection.addRange(range);
            }
          }
        }, 0); // Ensure DOM updates before executing this
      } else {
        // Save the edited node name
        if (nodeNameRef.current) {
          const updatedName = nodeNameRef.current.innerText.trim();
          const updatedNodeData: Node = {
            ...node,
            data: {
              ...node.data,
              name: updatedName,
            },
          };
          console.log(updatedNodeData);

          setTimeout(() => {
            setNodes((prevNodes) =>
              prevNodes.map((n) =>
                n.id === node.id
                  ? updatedNodeData
                  : n
              )
            );
          }, 0);
          putNode(node.data.nodeId, updatedNodeData.data); // API call
        }
      }

      return !prev; // Toggle the state
    });
  };

  return (
    <div className="flex flex-col gap-4 w-[320px] h-[calc(100vh-170px)] rounded-[20px] p-[20px] bg-white bg-opacity-40 backdrop-blur-[15px] shadow-[0px_2px_8px_rgba(0,0,0,0.25)] overflow-y-auto">
      <div className="flex flex-row justify-between items-center mb-2">
        <div className="flex flex-row items-center gap-1">
          <RiQuestionAnswerFill className="text-[#34D399] size-8" />
          <div
            ref={nodeNameRef}
            contentEditable={isNodeNameEdit}
            suppressContentEditableWarning
            className={isNodeNameEdit
              ? "text-[25px] w-[180px] font-semibold bg-white"
              : "text-[25px] w-[180px] font-semibold"
            }
          >
            {node.data.name}
          </div>
          {!isNodeNameEdit && <IoPencil
            className="ml-2 cursor-pointer text-[#5C5C5C] size-4"
            onClick={handleEditToggle}
          />}
          {isNodeNameEdit && <IoCheckmark
            className="ml-2 cursor-pointer text-[#5C5C5C] size-4"
            onClick={handleEditToggle}
          />}
        </div>
        <CgClose className="size-6 cursor-pointer" onClick={onClose} />
      </div>

      <div className="flex flex-col gap-2">
        <div className="text-[16px] flex flex-row justify-between items-center">
          답변을 입력하세요.
          <NodeVariableInsertMenu
            parentNodes={parentNodes}
            editorRef={textareaRef}
            onContentChange={handleAnswerChange}
          />
        </div>
        <div
          ref={textareaRef}
          contentEditable
          suppressContentEditableWarning
          onInput={handleAnswerChange}
          className="p-2 bg-white rounded-[5px] w-full resize-none overflow-hidden mt-2 focus:outline-none border border-gray-300 bg-white rounded-md shadow-sm focus:outline-none focus:ring-[#34D399] focus:border-[#34D399]"
          style={{ minHeight: "50px", whiteSpace: "pre-wrap" }}
        >
          {localAnswer}
        </div>
      </div>
    </div>
  );
}

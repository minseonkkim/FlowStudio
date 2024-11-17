import React, { useState } from "react";
import { Node } from "reactflow";
import { NodeData } from "@/types/chatbot";
import { createMonospaceBlock } from "@/utils/node";

export const NodeVariableInsertMenu = ({
  connectedNodes,
  editorRef,
  onContentChange,
}: {
  connectedNodes: Node<NodeData, string>[];
  editorRef: React.RefObject<HTMLDivElement>;
  onContentChange: (newContent: string) => void;
}) => {
  const [selectedNode, setSelectedNode] = useState<Node<NodeData, string> | null>(null);

const insertMonospaceBlock = (node: Node<NodeData, string>) => {
    const selection = window.getSelection();
    if (!selection || !editorRef.current) return;
  
    const range = selection.rangeCount > 0 ? selection.getRangeAt(0) : document.createRange();
  
    // Check if selection is inside editorRef
    if (!editorRef.current.contains(range.commonAncestorContainer)) {
      // If not, move caret to the end of editorRef
      range.selectNodeContents(editorRef.current);
      range.collapse(false); // Collapse range to the end
      selection.removeAllRanges();
      selection.addRange(range);
    }
  
    const block = createMonospaceBlock(node);
  
    range.deleteContents();
    range.insertNode(block);
  
    // Set caret after the inserted block
    range.setStartAfter(block);
    range.setEndAfter(block);
    selection.removeAllRanges();
    selection.addRange(range);
  
    // Update the editor content and UI state
    const newContent = editorRef.current.innerText;
    onContentChange(newContent);
    setSelectedNode(null); // Close dropdown
  };

  return (
    <div className="relative inline-block">
      <button
        className="p-2 bg-blue-500 text-white rounded"
        onClick={() => setSelectedNode((prev) => (prev ? null : connectedNodes[0]))}
      >
        연결된 노드 삽입
      </button>

      {selectedNode && (
        <div className="absolute z-10 bg-white border rounded mt-2 w-full shadow-lg">
          {connectedNodes.map((node) => (
            <button
              key={node.id}
              onClick={() => insertMonospaceBlock(node)}
              className="block w-full text-left px-4 py-2 hover:bg-gray-200"
            >
              {node.data.name || "Unnamed Node"}
            </button>
          ))}
        </div>
      )}
    </div>
  );
};

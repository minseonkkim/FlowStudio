import React, { useState, useEffect, useRef } from "react";
import { Node } from "reactflow";
import { NodeData } from "@/types/chatbot";
import { createMonospaceBlock } from "@/utils/node";
import { CgInsertAfterR } from "@react-icons/all-files/cg/CgInsertAfterR";

export const NodeVariableInsertMenu = ({
  parentNodes,
  editorRef,
  onContentChange,
}: {
  parentNodes: Node<NodeData, string>[];
  editorRef: React.RefObject<HTMLDivElement>;
  onContentChange: (newContent: string) => void;
}) => {
  const [selectedNode, setSelectedNode] = useState<Node<NodeData, string> | null>(null);
  const menuRef = useRef<HTMLDivElement>(null); // Reference for the menu container

  // Close the dropdown if clicked outside
  useEffect(() => {
    const handleClickOutside = (event: MouseEvent) => {
      if (menuRef.current && !(menuRef.current.contains(event.target as HTMLElement))) {
        setSelectedNode(null); // Close dropdown
      }
    };

    document.addEventListener("mousedown", handleClickOutside);
    return () => {
      document.removeEventListener("mousedown", handleClickOutside);
    };
  }, []);

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
    <div className="relative inline-block w-[130px] flex flex-end justify-end">
      <CgInsertAfterR className="w-5 h-5" onClick={() => setSelectedNode((prev) => (prev ? null : parentNodes[0]))} />

      {selectedNode && (
        <div ref={menuRef} className="absolute z-10 bg-white border rounded mt-2 w-full shadow-lg w-full">
          {parentNodes.map((node) => (
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

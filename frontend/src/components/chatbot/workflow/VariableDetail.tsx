import { useState } from "react";
import { IoClose } from "@react-icons/all-files/io5/ioClose";
import { IoMdTrash } from "@react-icons/all-files/io/IoMdTrash";
import { IoPencil } from "@react-icons/all-files/io5/IoPencil";
import { IoCheckmark } from "@react-icons/all-files/io5/IoCheckmark";

export default function VariableDetail({ 
  variables, 
  handleVariableChange, 
  handleAddVariable, 
  handleRemoveVariable, 
  handleEditToggle, 
  onClose }: { 
    variables: { name: string; value: string; type: string; isEditing: boolean }[], 
    handleVariableChange: (index: number, key: 'name' | 'value' | 'type', newValue: string) => void, 
    handleAddVariable: () => void, 
    handleRemoveVariable: (index: number) => void, 
    handleEditToggle: (index: number) => void, 
    onClose: () => void }) {

  return (
    <div className="ml-[20px] flex flex-col gap-4 w-[320px] h-[calc(100vh-170px)] rounded-[20px] p-[20px] bg-white bg-opacity-40 backdrop-blur-[15px] shadow-[0px_2px_8px_rgba(0,0,0,0.25)] overflow-y-auto">
      <div className="flex justify-between items-center mb-1">
        <h2 className="text-[23px] font-bold">변수 관리</h2>
        <IoClose className="size-6 cursor-pointer" onClick={onClose} />
      </div>
      <button
        onClick={handleAddVariable}
          className="mt-2 w-[130px] py-2.5 bg-[#AB8CCA] hover:bg-[#A283C1] text-[16px] font-semibold text-white rounded-[8px] mb-4"
      >
        + 변수 추가
      </button>
      {variables.map((variable, index) => (
        <div key={index} className="p-2 border rounded bg-[#ECECEC]">
          {variable.isEditing ? (
            <div className="flex flex-col mb-2">
              <input
                type="text"
                value={variable.name}
                onChange={(e) => handleVariableChange(index, "name", e.target.value)}
                className="w-full mb-1 p-1 border rounded"
                placeholder="변수 이름"
              />
              <select
                value={variable.type}
                onChange={(e) => handleVariableChange(index, "type", e.target.value)}
                className="w-full mb-1 p-1 border rounded"
              >
                <option value="string">string</option>
                <option value="number">number</option>
                <option value="boolean">boolean</option>
                <option value="object">object</option>
              </select>
              <IoCheckmark
                className="mt-2 cursor-pointer text-[#5C5C5C] self-end size-5"
                onClick={() => handleEditToggle(index)}
              />
            </div>
          ) : (
            <div className="flex items-center justify-between">
              <div>
                <span className="font-semibold mr-2">{variable.name}</span>
                <span className="text-sm text-[#9D9D9D]">[{variable.type}]</span>
              </div>
              <div className="flex flex-row items-center">
                <IoPencil
                  className="ml-2 cursor-pointer text-[#5C5C5C] size-4"
                  onClick={() => handleEditToggle(index)}
                />
                <IoMdTrash
                  className="ml-2 cursor-pointer text-[#5C5C5C] size-4"
                  onClick={() => handleRemoveVariable(index)}
                />
              </div>
              
            </div>
          )}
        </div>
      ))}
    </div>
  );
}

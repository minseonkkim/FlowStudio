import React, { forwardRef, useImperativeHandle, useState } from "react";
import VariableDetail from "./VariableDetail";

export const VariableMenu = forwardRef((_, ref) => {
  const [showVariableDetail, setShowVariableDetail] = useState(false);
  const [variables, setVariables] = useState<
    { name: string; value: string; type: string; isEditing: boolean }[]
  >([
    { name: "변수1", value: "", type: "string", isEditing: false },
    { name: "변수2", value: "", type: "string", isEditing: false },
  ]);

  useImperativeHandle(ref, () => ({
    toggleVariableDetail: () => setShowVariableDetail((prev) => !prev)
  }));

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

  return (
    <>
      {showVariableDetail && (
        <VariableDetail
          variables={variables}
          handleVariableChange={handleVariableChange}
          handleAddVariable={handleAddVariable}
          handleRemoveVariable={handleRemoveVariable}
          handleEditToggle={handleEditToggle}
          onClose={() => setShowVariableDetail(false)}
        />
      )}
    </>
  );
});

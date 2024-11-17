export interface ConnectedNode {
  nodeId: number;
  name: string;
  sourceConditionId?: number;
}

export interface NodeConfig {
  label: string;
  icon: JSX.Element;
  color: string;
}

export interface NodeData {
  "chatFlowId": number;
  "coordinate": {
    "x": number;
    "y": number;
  },
  "nodeType": string;
}

export interface ConnectedNode {
  id: number;
  name: string;
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
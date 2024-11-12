export interface ConnectedNode {
  nodeId: number;
  name: string;
}

export interface NodeConfig {
  label: string;
  icon: JSX.Element;
  color: string;
}

export interface NewNodeData{
  chatFlowId: number;
  coordinate: {x: number, y: number};
  nodeType: string;
}
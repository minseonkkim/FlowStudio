export interface ChatFlowData {
  title: string;
  description: string;
  thumbnail: string;
  categoryIds: number[];
}

interface Author {
  id: number;
  username: string;
  nickname: string;
  profileImage: string;
}

interface Category {
  categoryId: number;
  name: string;
}

export interface ChatFlow {
  chatFlowId: number;
  title: string;
  description: string;
  author: Author;
  thumbnail: string;
  categories: Category[];
  public: boolean;
}

export interface SharedChatFlow extends ChatFlow {
  shareNum: number;
}
 

export interface Coordinate{
  x: number;
  y: number;
}

export interface EdgeData{
  edgeId: number;
  sourceNodeId: number;
  targetNodeId: number;
  sourceConditionId?: number;
}

export interface NodeData{
  nodeId: number;
  name: string;
  type: string;
  coordinate: Coordinate;
  outputEdges: EdgeData[];
  inputEdges: EdgeData[];
  maxLength?: number;
  outputMessage?: string;
  questionClasses?: string[];
  promptSystem?: string;
  promptUser?: string;
}

export interface ChatFlowDetail{
  chatFlowId: number;
  title: string;
  nodes: NodeData[];
  edges: EdgeData[];
}
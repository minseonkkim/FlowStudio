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
 

interface Coordinate{
  x: number;
  y: number;
}

interface EdgeData{
  edgeId: number;
  sourceNodeId: number;
  targetNodeId: number;
}

export interface Knowledge{
  knowledgeId: number;
  title: string;
  isPublic: boolean;
  createdAt: string;
  totalToken: number;
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
  promptSystem?: string;
  promptUser?: string;
  knowledge?: Knowledge;
}

export interface ChatFlowDetail{
  chatFlowId: number;
  title: string;
  nodes: NodeData[];
}
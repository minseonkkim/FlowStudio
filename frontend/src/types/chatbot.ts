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
  sourceConditionId: number;
}

export interface Knowledge{
  knowledgeId: number;
  title: string;
  isPublic: boolean;
  createdAt: string;
  totalToken: number;
}

export interface NodeData{
  chatFlowId: number;
  nodeId: number;
  name: string;
  type: string;
  coordinate: Coordinate;
  outputEdges: EdgeData[];
  inputEdges: EdgeData[];
  onDelete: (nodeId : string) => void;

  maxLength?: number;

  outputMessage?: string;

  promptSystem?: string;
  promptUser?: string;
  context?: string;
  temperature?: number;
  maxTokens?: number;
  modelProvider?: string;
  modelName?: string;

  knowledge?: Knowledge;
  knowledgeId?: number;
  topK?: number;
  intervalTime?: number;
}

export interface ChatFlowDetail{
  chatFlowId: number;
  title: string;
  nodes: NodeData[];
}
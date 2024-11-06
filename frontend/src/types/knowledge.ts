export interface KnowledgeIsPublic {
  title: string;
  isPublic: boolean;
}

export interface KnowledgeData extends KnowledgeIsPublic {
  knowledgeId: number;
  createdAt: string; // 확인해야함
  totalToken: number; // 확인해야함
}


export interface ChunkData {
  chunkCount: number; 
  chunkList: ChunkList[]; 
}

export interface ChunkList {
  chunkId: number; 
  content: string; 
}

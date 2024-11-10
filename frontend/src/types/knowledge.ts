export interface KnowledgeIsPublic {
  title: string;
  isPublic: boolean;
}

export interface KnowledgeData extends KnowledgeIsPublic {
  knowledgeId: number;
  createdAt: string; 
  totalToken: number; 
}


export interface ChunkData {
  chunkCount: number; 
  chunkList: ChunkList[]; 
}

export interface ChunkList {
  chunkId: number; 
  content: string; 
}

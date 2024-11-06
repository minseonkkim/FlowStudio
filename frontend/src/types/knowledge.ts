export interface KnowledgeData {
  knowledgeId: number;
  title: string;
  isPublic: boolean;
  createdAt: string; // 확인해야함
  wordCount: number; // 확인해야함
}

export interface ChunkData {
  chunkCount: number; 
  chunkList: ChunkList[]; 
}

export interface ChunkList {
  chunkId: number; 
  content: string; 
}

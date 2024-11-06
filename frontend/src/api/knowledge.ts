import axiosInstance from '@/api/token/axiosInstance';
import { KnowledgeData, ChunkData, ChunkList } from "@/types/knowledge";

// 지식베이스 전체 목록 조회
export async function getAllKnowledges(): Promise<KnowledgeData[]> {
  try {
    const response = await axiosInstance.get('knowledges');
    if (response.status === 200) {
      return response.data.data as KnowledgeData[];
    } else {
      throw new Error('Failed to get knowledges');
    }
  } catch (error) {
    console.error(error);
    throw error;
  }
}

// 지식베이스 문서공개 여부 수정
export async function putDocKnowledge(knowledgeId: number, data: { title: string; isPublic: boolean }): Promise<KnowledgeData> {
  try {
    const response = await axiosInstance.put(`knowledges/${knowledgeId}`, data);
    if (response.status === 200) {
      return response.data.data;
    } else {
      throw new Error('Failed to update knowledge');
    }
  } catch (error) {
    console.error(error);
    throw error;
  }
}

// 지식베이스 삭제
export async function deleteKnowledge(knowledgeId: number): Promise<boolean> {
  try {
    const response = await axiosInstance.delete(`knowledges/${knowledgeId}`);
    if (response.status === 200) {
      return response.data.data as boolean;
    } else {
      throw new Error('Failed to delete knowledge');
    }
  } catch (error) {
    console.error(error);
    throw error;
  }
}

// 지식베이스 청크 목록 조회
export async function getAllChunks(knowledgeId: number): Promise<ChunkData> {
  try {
    const response = await axiosInstance.get(`knowledges/${knowledgeId}/chunks`);
    if (response.status === 200) {
      return response.data.data as ChunkData;
    } else {
      throw new Error('Failed to get chunks');
    }
  } catch (error) {
    console.error(error);
    throw error;
  }
}

// 지식베이스 청크 상세보기
export async function getChunkDetail(knowledgeId: number, chunkId: number): Promise<ChunkList> {
  try {
    const response = await axiosInstance.get(`knowledges/${knowledgeId}/chunks/${chunkId}`);
    if (response.status === 200) {
      return response.data.data[0] as ChunkList;
    } else {
      throw new Error('Failed to get chunk detail');
    }
  } catch (error) {
    console.error(error);
    throw error;
  }
}

// 지식베이스 청크 수정
export async function postChunkDetail(knowledgeId: number, chunkId: number, data: { content: string }): Promise<boolean> {
  try {
    const response = await axiosInstance.post(`knowledges/${knowledgeId}/chunks/${chunkId}`, data);
    if (response.status === 200) {
      return response.data.data as boolean;
    } else {
      throw new Error('Failed to update chunk');
    }
  } catch (error) {
    console.error(error);
    throw error;
  }
}

// 지식베이스 청크 삭제
export async function deleteChunkDetail(knowledgeId: number, chunkId: number): Promise<boolean> {
  try {
    const response = await axiosInstance.delete(`knowledges/${knowledgeId}/chunks/${chunkId}`);
    if (response.status === 200) {
      return response.data.data as boolean;
    } else {
      throw new Error('Failed to delete chunk');
    }
  } catch (error) {
    console.error(error);
    throw error;
  }
}

import axiosInstance from '@/api/token/axiosInstance';
import { KnowledgeData, ChunkData, ChunkList } from "@/types/knowledge";

// 지식베이스 전체 목록 조회
export async function getAllKnowledges() {
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
export async function putDocKnowledge(knowledgeId: string, data: { title: string; isPublic: boolean }) {
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
export async function deleteKnowledge(knowledgeId: string) {
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
export async function getAllChunks(knowledgeId: string) {
  try {
    console.log("axios : " + knowledgeId);
    
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
export async function getChunkDetail(knowledgeId: string, chunkId: string) {
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
export async function postChunkDetail(knowledgeId: string, chunkId: string, data: { content: string }) {
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
export async function deleteChunkDetail(knowledgeId: string, chunkId: string) {
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

// 지식베이스 지식 생성
export async function postKnowledge(data: { file: File; chunkSize: string; chunkOverlap: string; separator: string }) {
  try {
    const formData = new FormData();

    // FormData에 데이터 추가
    formData.append("file", data.file);
    formData.append("chunkSize", data.chunkSize);
    formData.append("chunkOverlap", data.chunkOverlap);
    formData.append("separator", data.separator);

    // FormData 전송을 위한 POST 요청
    const response = await axiosInstance.post('/knowledges', formData, {
      headers: {
        "Content-Type": "multipart/form-data", 
      },
    });

    if (response.status === 200) { 
      return response.data.data;
    } else {
      throw new Error('Failed to create knowledge');
    }
  } catch (error) {
    console.error(error);
    throw error;
  }
}

// 지식베이스 청크 미리보기
export async function postKnowledgeChunk(data: { file: File; chunkSize: string; chunkOverlap: string; separator: string }) {
  try {
    const formData = new FormData();

    // FormData에 데이터 추가
    formData.append("file", data.file);
    formData.append("chunkSize", data.chunkSize);
    formData.append("chunkOverlap", data.chunkOverlap);
    formData.append("separator", data.separator);

    // FormData 전송을 위한 POST 요청
    const response = await axiosInstance.post('/knowledges/chunks', formData, {
      headers: {
        "Content-Type": "multipart/form-data", 
      },
    });

    if (response.status === 200) { 
      return response.data.data as ChunkData
    } else {
      throw new Error('Failed to create knowledge');
    }
  } catch (error) {
    console.error(error);
    throw error;
  }
}

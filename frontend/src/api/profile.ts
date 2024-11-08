import axiosInstance from '@/api/token/axiosInstance';
import { ApiKeys } from '@/types/profile';

// 유저 정보 조회
export async function getUserInfo() {
  try {
    const response = await axiosInstance.get('users/me');
    if (response.status === 200) {
      return response.data.data;
    } else {
      throw new Error('Failed to get user information');
    }
  } catch (error) {
    console.error(error);
    throw error;
  }
}

// 닉네임 중복 확인
export async function getCheckNickName(nickname: string) {
  try {
    const response = await axiosInstance.get(`users/check-nickname`, {
      params: { nickname },
    });
    if (response.status === 200) {
      return response.data.message;
    } else if (response.status === 400) {
      return response.data.message;
    } else {
      throw new Error('Nickname check failed');
    }
  } catch (error) {
    console.error(error);
    throw error;
  }
}

// 닉네임 수정
export async function patchNickName(nickname: string) {
  try {
    const response = await axiosInstance.patch('users/nickname', { nickname });
    if (response.status === 200) {
      return response.data.nickname;
    } else {
      throw new Error('Failed to patch nickname');
    }
  } catch (error) {
    console.error(error);
    throw error;
  }
}

// 프로필 이미지 수정
export async function patchProfileImage(imageFile: File) {
  try {
    const formData = new FormData();
    formData.append("image", imageFile);

    const response = await axiosInstance.patch('users/profile-image', formData, {
      headers: {
        'Content-Type': 'multipart/form-data',
      },
    });

    if (response.status === 200) {
      return response.data;
    } else {
      throw new Error('Failed to patch profile image');
    }
  } catch (error) {
    console.error(error);
    throw error;
  }
}

// API 키 조회
export async function getApiKeys() {
  try {
    const response = await axiosInstance.get('users/keys');
    if (response.status === 200) {
      return response.data.data;
    } else {
      throw new Error('Failed to get API keys');
    }
  } catch (error) {
    console.error(error);
    throw error;
  }
}

// API 키 업데이트
export async function putApiKeys(data: ApiKeys) {
  try {
    const response = await axiosInstance.put('users/keys', data);
    if (response.status === 200) {
      return response.data;
    } else {
      throw new Error('Failed to put API keys');
    }
  } catch (error) {
    console.error(error);
    throw error;
  }
}

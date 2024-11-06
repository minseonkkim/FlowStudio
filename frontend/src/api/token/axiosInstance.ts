// 토큰 필요한 경우
import noAuthAxios from './noAuthAxios'
import axios from 'axios';

const BASE_URL = process.env.NEXT_PUBLIC_BASE_URL

const axiosInstance = axios.create({
  baseURL: BASE_URL,
  headers: {
    Accept: 'application/json',
    'Content-Type': 'application/json'
  },
  withCredentials: true // 쿠키 전송 허용
});

// 토큰 설정 및 재발급 함수
import { AxiosError } from 'axios';

const setAuthorizationToken = async () => {
  try {
    const response = await noAuthAxios.post(`${BASE_URL}/auth/reissue`);

    const authorizationHeader = response.headers['authorization'];

    if (authorizationHeader) {
      localStorage.setItem('accessToken', authorizationHeader);
      return authorizationHeader;
    } else {
      throw new Error('No authorization header found');
    }
  } catch (error) {
    if (error instanceof AxiosError) {
      console.error('Failed to reissue access token:', error);

      // Refresh 토큰이 만료됐을 경우
      if (error.response && error.response.status === 401) {
        // 로그아웃 처리 - 액세스 토큰 삭제
        localStorage.removeItem('accessToken');
      }
    } else {
      // 다른 에러일 경우
      console.error('An unexpected error occurred:', error);
    }
    throw error;
  }
};

// 요청 인터셉터: 요청마다 토큰을 설정
axiosInstance.interceptors.request.use(
  async (config) => {
    const token = localStorage.getItem('accessToken');
    if (token) {
      config.headers['Authorization'] = `Bearer ${token}`;
    }
    return config;
  },
  (error) => Promise.reject(error)
);

// 응답 인터셉터: 401 Unauthorized 발생 시 토큰 재발급
axiosInstance.interceptors.response.use(
  (response) => {
    return response; // 정상적인 응답은 그대로 반환
  },
  async (error) => {
    const originalRequest = error.config;

    // 401 Unauthorized 에러 시 처리
    if (error.response && error.response.status === 401 && !originalRequest._retry) {
      originalRequest._retry = true; // 재시도 방지 플래그 설정

      try {
        const newToken = await setAuthorizationToken(); // 토큰 재발급 시도
        originalRequest.headers['Authorization'] = `Bearer ${newToken}`;

        // 재시도 요청
        return axiosInstance(originalRequest);
      } catch (tokenError) {
        console.error('Error while retrying request with new token:', tokenError);

        return Promise.reject(tokenError);
      }
    }

    return Promise.reject(error);
  }
);

export { setAuthorizationToken };
export default axiosInstance;
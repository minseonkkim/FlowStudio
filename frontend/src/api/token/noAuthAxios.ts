// 토큰 필요없는 함수일 경우
import axios from 'axios';

const BASE_URL = process.env.NEXT_PUBLIC_BASE_URL

const noAuthAxios = axios.create({
  baseURL: BASE_URL,
  headers: {
    Accept: 'application/json',
    'Content-Type': 'application/json'
  },
  withCredentials: true
});

export default noAuthAxios;
import { atom } from 'recoil';

// 지식베이스 step
export const currentStepState = atom<number>({
  key: 'currentStepState',  // 고유한 키
  default: 1,  // 초기 상태
});

// 파일
export const fileState = atom<File | null>({
  key: 'fileState',  
  default: null,  
});

// 파일 이름
export const fileNameState = atom<string>({
  key: 'fileNameState',  
  default: '',  
});


// 청크 조회 시 사용할 파일 이름
export const chunkFileNameState = atom<string>({
  key: 'chunkFileNameState',  
  default: '',  
});

// 지식 생성 로딩 상태
export const isLoadingState = atom<boolean>({
  key: 'isLoadingState',  
  default: false,  
});
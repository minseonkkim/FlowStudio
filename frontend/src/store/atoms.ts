import { atom } from 'recoil';

// 지식베이스 step
export const currentStepState = atom<number>({
  key: 'currentStepState',  // 고유한 키
  default: 1,  // 초기 상태
});

// 파일 이름
export const fileNameState = atom<string>({
  key: 'fileNameState',  
  default: '',  
});

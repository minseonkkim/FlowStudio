import { atom } from 'recoil';

const initialProfileImage = typeof window !== 'undefined' ? localStorage.getItem('profileImage') : null;

export const profileImageAtom = atom<string | null>({
  key: 'profileImageAtom',
  default: initialProfileImage, 
});
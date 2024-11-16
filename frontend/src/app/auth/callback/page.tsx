'use client'
import { Suspense, useEffect } from 'react';
import { useRouter, useSearchParams } from 'next/navigation';
import { getUserInfo } from '@/api/profile';
import { useQuery } from '@tanstack/react-query';
import { useRecoilState } from 'recoil';
import { profileImageAtom } from '@/store/profileAtoms';
import { UserInfo } from '@/types/profile';

function CallbackContent() {
  const router = useRouter();
  const searchParams = useSearchParams();
  const [profileImg, setProfileImg] = useRecoilState(profileImageAtom);

  const { isLoading: isUserInfoLoading, isError: isUserInfoError, error: userInfoError, data: userInfo } = useQuery<UserInfo>({
    queryKey: ['userInfo'],
    queryFn: getUserInfo,
  });

  useEffect(() => {
    const token = searchParams.get('accessToken');

    if (token) {
      localStorage.setItem('accessToken', token);

      if (userInfo) {
        setProfileImg(userInfo.profileImage);
        localStorage.setItem('profileImage', userInfo.profileImage);
      }

      router.push('/explore/chatbots');
    } else {
      console.error('accessToken not found in query parameters');
      router.push('/login');
    }
  }, [router, searchParams, userInfo]);

  return <p>로그인 중입니다...</p>;
}

export default function CallbackPage() {
  return (
    <Suspense fallback={<div>Loading...</div>}>
      <CallbackContent />
    </Suspense>
  );
}

'use client'
import { Suspense, useEffect } from 'react';
import { useRouter, useSearchParams } from 'next/navigation';
import { getUserInfo } from '@/api/profile';
import { useQuery } from '@tanstack/react-query';
import { useRecoilState } from 'recoil';
import { profileImageAtom } from '@/store/profileAtoms';
import { UserInfo } from '@/types/profile';
import Loading from '@/components/common/Loading';

function CallbackContent() {
  const router = useRouter();
  const searchParams = useSearchParams();
  const [, setProfileImg] = useRecoilState(profileImageAtom);

  const { data: userInfo } = useQuery<UserInfo>({
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

  return <Loading/>;
}

export default function CallbackPage() {
  return (
    <Suspense fallback={<Loading/>}>
      <CallbackContent />
    </Suspense>
  );
}

'use client'

import { useRouter } from 'next/router';
import { NextPage } from 'next';
import { useRecoilState } from 'recoil';
import { accessTokenState } from '@/store/authAtoms'; 
import { useEffect } from 'react';

const CallbackPage: NextPage = () => {
  const router = useRouter();
  const { accessToken } = router.query as { accessToken?: string };
  const [, setAccessToken] = useRecoilState(accessTokenState); 

  useEffect(() => {
    if (accessToken) {
      setAccessToken(accessToken); 
      router.replace('/'); 
    }
  }, [accessToken, setAccessToken, router]);

  return (
    <div>
      <p>loading 중</p>
    </div>
  );
};

export default CallbackPage;

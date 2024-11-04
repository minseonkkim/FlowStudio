'use client'
import { Suspense, useEffect } from 'react';
import { useRouter, useSearchParams } from 'next/navigation';

function CallbackContent() {
  const router = useRouter();
  const searchParams = useSearchParams();

  useEffect(() => {
    const token = searchParams.get('accessToken');

    if (token) {
      localStorage.setItem('Token', token);
      router.push('/');
    } else {
      console.error('accessToken not found in query parameters');
      router.push('/login');
    }
  }, [router, searchParams]);

  return <p>로그인 중입니다...</p>;
}

export default function CallbackPage() {
  return (
    <Suspense fallback={<div>Loading...</div>}>
      <CallbackContent />
    </Suspense>
  );
}

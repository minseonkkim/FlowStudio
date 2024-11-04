"use client"; 

import { setAuthorizationToken } from '@/api/token/axiosInstance';

export default function Home() {
  const handleTokenRefresh = async () => {
    try {
      const newToken = await setAuthorizationToken();

      if (newToken) {
        alert('Token refreshed successfully. Check localStorage for the new token.');
        console.log('New Token:', newToken); 
      } else {
        alert('Token refresh failed.');
      }
    } catch (error) {
      console.error('Token refresh error:', error);
      alert('Token refresh failed. Check the console for details.');
    }
  };

  return (
    <div>
      <button onClick={handleTokenRefresh} className="border p-2 m-12 bg-red-300">
        Refresh Token
      </button>
    </div>
  );
}

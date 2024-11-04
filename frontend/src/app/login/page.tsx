'use client'
import Logo from '@/assets/common/logo.png'
import Image from 'next/image'
import { FaGithub } from "@react-icons/all-files/fa/FaGithub";
import { FcGoogle } from "@react-icons/all-files/fc/FcGoogle";
import { RiKakaoTalkFill } from '@react-icons/all-files/ri/RiKakaoTalkFill'

export default function page() {
  const url = process.env.NEXT_PUBLIC_API_URL
  console.log(url)
  return (
    <div className="flex items-center justify-center w-full py-12" style={{ minHeight: 'calc(100vh - 57px)' }}>
      <div className="flex flex-col items-center justify-center px-6 md:px-0 w-full">
        <div>
          <Image src={Logo} alt="logo" width={90} height={90}/>
        </div>
        <div className="text-[#1A1918] text-[32px] md:text-[38px] font-bold text-center mt-8">
          쉽고 빠르게,<br />나만의 챗봇을 완성해보세요!
        </div>
        <div className="text-[#61605A] text-center mt-4 px-2 md:px-4 text-sm md:text-base">
          AI 기반 맞춤형 챗봇을 생성, 배포, 관리할 수 있어요
        </div>

        <div className="flex flex-col gap-4 items-center mt-10 w-full md:w-[350px]">
          <button
           className="w-full h-[54px] border border-gray-300 font-semibold rounded-xl bg-white flex justify-center items-center"
           onClick={() => window.location.href = `https://k11c201.p.ssafy.io/oauth2/authorization/google?redirect_uri=${url}`}>
            <FcGoogle className="mr-2"/>
            <p>구글 계정으로 시작하기</p>
          </button>
          <button
           className="w-full h-[54px] rounded-xl font-semibold bg-[#FEE500] flex justify-center items-center"
           onClick={() => window.location.href = `https://k11c201.p.ssafy.io/oauth2/authorization/kakao?redirect_uri=${url}`}>
            <RiKakaoTalkFill className="mr-2"/>
            <p>카카오 계정으로 시작하기</p>
          </button>
          <button
           className="w-full h-[54px] text-white rounded-xl font-semibold bg-black flex justify-center items-center"
           onClick={() => window.location.href = `https://k11c201.p.ssafy.io/oauth2/authorization/github?redirect_uri=${url}`}>
            <FaGithub className="mr-2"/>
            <p>깃허브 계정으로 시작하기</p>
          </button>
        </div>
      </div>
    </div>
  )
}

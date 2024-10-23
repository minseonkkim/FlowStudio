import Logo from '../../assets/common/logo.png'
import Image from 'next/image'
import { FaGithub } from "@react-icons/all-files/fa/FaGithub";
import { FcGoogle } from "@react-icons/all-files/fc/FcGoogle";
import { RiKakaoTalkFill } from '@react-icons/all-files/ri/RiKakaoTalkFill'
const page = () => {
  return (
    <>
    <div className='flex justify-center mt-32 mb-14'>
      <Image src={Logo} alt="logo" className="w-32 h-32 object-cover"/>
    </div>
    <div className='text-4xl font-semibold text-center'>FlowStudio에 오신 것을 환영합니다.</div>
    <div className='text-[#757575] text-1g text-center mt-4'>손쉽게 맞춤형 챗봇을 생성, 배포, 관리해보세요.</div>
    
    <div className="flex justify-center items-center mt-12">
      <div className="flex flex-col gap-4">
        <div className="w-96 h-12 rounded-lg shadow-md bg-[#FFFFFF]">
          <div className='flex gap-2 justify-center items-center h-12'> 
            <FcGoogle className="text-2xl"/>
            <p className='text-base font-semibold'>Continue with Google</p>
          </div>
        </div>
        <div className="w-96 h-12 rounded-lg shadow-md bg-[#FEE500]">
          <div className='flex gap-2 justify-center items-center h-12'>
            <RiKakaoTalkFill className="text-2xl"/>
            <p className='text-base font-semibold'>Continue with Kakao</p>
          </div>
        </div>
        <div className="w-96 h-12 rounded-lg shadow-md bg-[#000000]">
          <div className='flex gap-2 justify-center items-center h-12'>
            <FaGithub className="text-2xl text-white"/>
            <p className='text-white text-base font-semibold'>Continue with GitHub</p>
          </div>
        </div>
      </div>
    </div>



    </>
  )
}

export default page
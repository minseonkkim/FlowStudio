import Logo from '../../assets/common/logo.png'
import Image from 'next/image'
import { FaGithub } from "@react-icons/all-files/fa/FaGithub";
import { FcGoogle } from "@react-icons/all-files/fc/FcGoogle";
import { RiKakaoTalkFill } from '@react-icons/all-files/ri/RiKakaoTalkFill'

const page = () => {
  return (
    <div className="flex items-center justify-center w-full" style={{ height: 'calc(100vh - 57px)' }}>
      <div className='flex flex-col items-center justify-center'>
        <div>
          <Image src={Logo} alt="logo" width={128} height={128} />
        </div>
        <div className='text-4xl font-semibold text-center mt-4'>FlowStudio에 오신 것을 환영합니다.</div> 
        <div className='text-[#757575] text-lg text-center mt-2'>손쉽게 맞춤형 챗봇을 생성, 배포, 관리해보세요.</div> 
        
        <div className="flex flex-col gap-4 items-center mt-8">
          <div className="w-96 h-12 rounded-lg shadow-md bg-[#FFFFFF] flex justify-center items-center">
            <FcGoogle className="text-2xl"/>
            <p className='text-base font-semibold'>Continue with Google</p>
          </div>
          <div className="w-96 h-12 rounded-lg shadow-md bg-[#FEE500] flex justify-center items-center">
            <RiKakaoTalkFill className="text-2xl"/>
            <p className='text-base font-semibold'>Continue with Kakao</p>
          </div>
          <div className="w-96 h-12 rounded-lg shadow-md bg-[#000000] flex justify-center items-center">
            <FaGithub className="text-2xl text-white"/>
            <p className='text-white text-base font-semibold'>Continue with GitHub</p>
          </div>
        </div>

      </div>
    </div>
  )
}

export default page;

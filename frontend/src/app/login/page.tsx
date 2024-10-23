import Logo from '../../assets/common/logo.png'
import Image from 'next/image'
import { VscGithub } from "@react-icons/all-files/vsc/VscGithub";
import { FaAngleLeft } from "@react-icons/all-files/fa/FaAngleLeft";
const page = () => {
  return (
    <>
    <Image src={Logo} alt="logo" className="w-32 h-32 object-cover"/>
    <div className='text-4xl font-semibold'>FlowStudio에 오신 것을 환영합니다.</div>
    <div className='text-[#757575] text-1g'>손쉽게 맞춤형 챗봇을 생성, 배포, 관리해보세요.</div>
    <VscGithub/>
    <FaAngleLeft/>
    </>
  )
}

export default page
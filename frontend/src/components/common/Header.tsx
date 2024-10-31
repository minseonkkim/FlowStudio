"use client";

import Logo from "../../assets/common/logo.png";
import Image from "next/image";
import Link from "next/link";
import { usePathname } from "next/navigation";

export default function Header() {
  const pathname = usePathname();

  if (pathname.startsWith("/chat/")) {
    return null;
  }

  return (
    <header className="z-20 fixed inset-0 w-full h-[57px] px-12 flex justify-center border-b-[1px] bg-white ">
      <div className="w-full flex flex-row justify-between items-center">
        <Link className="flex flex-row items-center gap-3" href="/">
          <Image src={Logo} alt="logo" className="w-[22px] h-[22px]" />
          <div className="text-[22px] font-bold text-[#5D2973]">
            Flow Studio
          </div>
        </Link>
        <nav className="flex flex-row gap-10 text-[16px] font-medium">
          <Link
            className={`cursor-pointer ${
              pathname === "/explore/chatbots"
                ? "text-[#9A75BF] font-semibold"
                : "hover:font-semibold"
            }`}
            href="/explore/chatbots"
          >
            모두의 챗봇
          </Link>
          <Link
            className={`cursor-pointer ${
              pathname === "/chatbots"
                ? "text-[#9A75BF] font-semibold"
                : "hover:font-semibold"
            }`}
            href="/chatbots"
          >
            나의 챗봇
          </Link>
          <Link
            className={`cursor-pointer ${
              pathname.startsWith("/knowledge")
                ? "text-[#9A75BF] font-semibold"
                : "hover:font-semibold"
            }`}
            href="/knowledges"
          >
            지식 베이스
          </Link>
          <Link
            className={`cursor-pointer ${
              pathname === "/evaluations" || pathname.startsWith("/evaluation")
                ? "text-[#9A75BF] font-semibold"
                : "hover:font-semibold"
            }`}
            href="/evaluations"
          >
            챗봇 평가
          </Link>
        </nav>
        <div className="flex flex-row gap-6">
          <Link className={`cursor-pointer ${
                pathname === '/evaluations' ? 'text-[#9A75BF] font-semibold' : 'hover:font-semibold'
              }`} href="/profile">마이페이지</Link>
          <button>로그아웃</button>
        </div>
      </div>
    </header>
  );
}
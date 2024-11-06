"use client";

import Logo from "../../assets/common/logo.png";
import Image from "next/image";
import Link from "next/link";
import { usePathname, useRouter } from "next/navigation";
import { useState, useEffect } from "react";
import { FiMenu } from "@react-icons/all-files/fi/FiMenu";
import { FiX } from "@react-icons/all-files/fi/FiX";
import { FiLogOut } from "@react-icons/all-files/fi/FiLogOut";
import { FiLogIn } from "@react-icons/all-files/fi/FiLogIn";

export default function Header() {
  const router = useRouter();
  const pathname = usePathname();
  const [isMenuOpen, setIsMenuOpen] = useState(false);
  const [token, setToken] = useState<string | null>(null); 

  const savedToken = typeof window !== "undefined" ? localStorage.getItem("accessToken") : null;

  const handleLogout = () => {
    localStorage.removeItem("accessToken");
    setToken(null); 
    router.push("/");
  };

  useEffect(() => {
    setToken(savedToken); 
  }, [savedToken]);

  if (pathname.startsWith("/chat/")) {
    return null;
  }

  const handleLinkClick = () => {
    setIsMenuOpen(false);
  };

  const MobileHeader = () => (
    <header className="z-20 fixed inset-0 w-full h-[57px] px-6 flex justify-between items-center border border-1 bg-white">
      <div className="flex flex-row items-center gap-3">
        {token && (
          <button
            className="text-2xl"
            onClick={() => setIsMenuOpen(!isMenuOpen)}
          >
            {isMenuOpen ? <FiX /> : <FiMenu />}
          </button>
        )}
      </div>
      <Link
        href="/"
        onClick={handleLinkClick}
        className="flex items-center gap-3 absolute left-1/2 transform -translate-x-1/2"
      >
        <Image src={Logo} alt="logo" className="w-[22px] h-[22px]" />
        <div className="text-[22px] font-bold text-[#5D2973]">Flow Studio</div>
      </Link>
      <button className="text-2xl">
        {!token ? (
          <FiLogIn onClick={() => router.push("/login")} />
        ) : (
          <FiLogOut onClick={handleLogout} />
        )}
      </button>
      {token && (
        <nav
          className={`${
            isMenuOpen
              ? "fixed inset-0 top-[57px] flex flex-col items-start p-6 bg-white z-30"
              : "hidden"
          } w-full text-[16px] font-medium space-y-4`}
        >
          <Link
            className={`cursor-pointer ${
              pathname === "/explore/chatbots"
                ? "text-[#9A75BF] font-semibold"
                : "hover:font-semibold"
            } w-full py-2 border-b`}
            href="/explore/chatbots"
            onClick={handleLinkClick}
          >
            모두의 챗봇
          </Link>
          <Link
            className={`cursor-pointer ${
              pathname === "/chatbots"
                ? "text-[#9A75BF] font-semibold"
                : "hover:font-semibold"
            } w-full py-2 border-b`}
            href="/chatbots"
            onClick={handleLinkClick}
          >
            나의 챗봇
          </Link>
          <Link
            className={`cursor-pointer ${
              pathname.startsWith("/knowledge")
                ? "text-[#9A75BF] font-semibold"
                : "hover:font-semibold"
            } w-full py-2 border-b`}
            href="/knowledges"
            onClick={handleLinkClick}
          >
            지식 베이스
          </Link>
          <Link
            className={`cursor-pointer ${
              pathname === "/evaluations" ||
              pathname.startsWith("/evaluation")
                ? "text-[#9A75BF] font-semibold"
                : "hover:font-semibold"
            } w-full py-2 border-b`}
            href="/evaluations"
            onClick={handleLinkClick}
          >
            챗봇 평가
          </Link>
          <Link
            className={`cursor-pointer ${
              pathname === "/profile"
                ? "text-[#9A75BF] font-semibold"
                : "hover:font-semibold"
            } w-full py-2 border-b`}
            href="/profile"
            onClick={handleLinkClick}
          >
            마이페이지
          </Link>
        </nav>
      )}
    </header>
  );

  const DesktopHeader = () => (
    <header className="z-10 fixed inset-0 w-full h-[57px] px-12 flex justify-between items-center border-b-[1px] bg-white">
      <Link href="/" className="flex items-center gap-3">
        <Image src={Logo} alt="logo" className="w-[22px] h-[22px]" />
        <div className="text-[22px] font-bold text-[#5D2973]">Flow Studio</div>
      </Link>
      <nav className="flex flex-row gap-10 text-[16px] font-medium">
        {token && (
          <>
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
                pathname === "/evaluations" ||
                pathname.startsWith("/evaluation")
                  ? "text-[#9A75BF] font-semibold"
                  : "hover:font-semibold"
              }`}
              href="/evaluations"
            >
              챗봇 평가
            </Link>
          </>
        )}
      </nav>
      {token ? (
        <div className="flex items-center gap-6">
          <Link
            className={`cursor-pointer ${
              pathname === "/profile" ? "font-semibold" : "hover:font-semibold"
            }`}
            href="/profile"
          >
            마이페이지
          </Link>
          <button onClick={handleLogout}>로그아웃</button>
        </div>
      ) : (
        <Link
          className={`cursor-pointer ${
            pathname.startsWith("/login")
              ? "text-[#9A75BF] font-semibold"
              : "hover:font-semibold"
          }`}
          href="/login"
        >
          로그인
        </Link>
      )}
    </header>
  );

  return (
    <div>
      <div className="block md:hidden">{MobileHeader()}</div>
      <div className="hidden md:block">{DesktopHeader()}</div>
    </div>
  );
}

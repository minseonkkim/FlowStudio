"use client";

import { useEffect, useState } from "react";
import { useRouter } from "next/navigation";
import SectionOne from "@/components/landing/SectionOne";
import SectionTwo from "@/components/landing/SectionTwo";
import SectionThree from "@/components/landing/SectionThree";
import SectionFour from "@/components/landing/SectionFour";
import SectionFive from "@/components/landing/SectionFive";
import SectionSix from "@/components/landing/SectionSix";

export default function Page() {
  const router = useRouter();
  const [isLogin, setIsLogin] = useState<boolean>(false);

  useEffect(() => {
    const token = localStorage.getItem("accessToken");
    setIsLogin(!!token); 
  }, []);

  useEffect(() => {
    if (isLogin) {
      router.push("/explore/chatbots");
    }
  }, [isLogin, router]);

  return (
    <div className="landing-page">
      <SectionOne />
      <SectionTwo />
      <SectionThree />
      <SectionFour />
      <SectionFive />
      <SectionSix />
    </div>
  );
}

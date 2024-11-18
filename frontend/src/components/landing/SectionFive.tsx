import { useEffect } from "react";
import FinanceImg from "@/assets/landing/finance.png";
import HealthImg from "@/assets/landing/health.png";
import ShoppingImg from "@/assets/landing/shopping.png";
import TravelImg from "@/assets/landing/travel.png";
import EducationImg from "@/assets/landing/education.png";
import EntertainmentImg from "@/assets/landing/entertainment.png";
import { useMemo } from "react";

export default function SectionFive() {
  const features = useMemo(() => ["금융", "헬스케어", "전자 상거래", "여행", "교육", "엔터테인먼트"], []);
  const featureImages = useMemo(() => [FinanceImg, HealthImg, ShoppingImg, TravelImg, EducationImg, EntertainmentImg], []);

  useEffect(() => {
    const bannerList = document.querySelector(".banner_list") as HTMLElement;
    const bannerItems = document.querySelectorAll(".banner") as NodeListOf<HTMLElement>;
    const numBanners = bannerItems.length;

    if (bannerList && bannerItems.length > 0) {
      for (let i = 0; i < numBanners; i++) {
        bannerList.appendChild(bannerItems[i].cloneNode(true));
      }

      const bannerWidth = bannerItems[0].offsetWidth;
      bannerList.style.width = `${bannerWidth * numBanners * 2 + 10 * (numBanners * 2 - 1)}px`;

      let currentPos = 0;
      let lastTime = 0;

      const animate = (timestamp: number) => {
        const delta = timestamp - lastTime;
        lastTime = timestamp;

        currentPos -= (bannerWidth + 10) * delta / 3800;
        if (currentPos <= -(bannerWidth + 10) * numBanners) {
          currentPos = 0;
        }
        bannerList.style.transform = `translateX(${currentPos}px)`;

        requestAnimationFrame(animate);
      };

      requestAnimationFrame(animate);
    }
  }, []);

  return (
    <section className="infinite-scrolling-section h-[500px] flex items-center justify-center bg-gray-100 overflow-hidden">
      <div className="content-container overflow-hidden">
        <div className="text-center mb-14 text-[22px] md:text-[26px] font-semibold">
          금융, 교육, 헬스케어 등 <span className="text-[#9A75BF]">원하는 분야에 최적화된 AI 챗봇</span>을 몇 분 안에 구축해 보세요.
        </div>
        <div className="scrolling-cards-container w-full relative">
          <div className="banner_list flex">
            {features.map((feature, index) => (
              <div key={`banner-${index}`} className="banner bg-white p-2 md:p-8 shadow-lg rounded-full m-4 min-w-[200px] flex flex-col items-center justify-center">
                <img src={featureImages[index % features.length]?.src} className="w-[75px] h-[75px] md:w-[100px] md:h-[100px]" />
                <p className="text-center text-gray-600 text-[18px] md:text-[20px] mt-4 font-semibold">{feature}</p>
              </div>
            ))}
          </div>
        </div>
      </div>
    </section>
  );
}

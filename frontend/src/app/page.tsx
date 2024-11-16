"use client"

import { useEffect } from "react";
import { useInView } from "react-intersection-observer";
import { motion, useAnimation } from "framer-motion";
import robotImg from "@/assets/landing/robot.png";
import speechBubbleImg from "@/assets/landing/speechbubble.gif";
import screenWorkflowImg from "@/assets/landing/screen_workflow.png";
import screenTemplateImg from "@/assets/landing/screen_template.png";
import screenEvaluationImg from "@/assets/landing/screen_evaluation.png";
import FinanceImg from "@/assets/landing/finance.png";
import HealthImg from "@/assets/landing/health.png";
import ShoppingImg from "@/assets/landing/shopping.png";
import TravelImg from "@/assets/landing/travel.png";
import EducationImg from "@/assets/landing/education.png";
import EntertainmentImg from "@/assets/landing/entertainment.png";
import { useRouter } from 'next/navigation';

export default function Page() {
  const { ref: ref1, inView: inView1 } = useInView({ triggerOnce: true });
  const { ref: ref2, inView: inView2 } = useInView({ triggerOnce: true });
  const { ref: ref3, inView: inView3 } = useInView({ triggerOnce: true });
  const { ref: ref4, inView: inView4 } = useInView({ triggerOnce: true });
  const { ref: ref5, inView: inView5 } = useInView({ triggerOnce: true });

  const flowChartAnimation = useAnimation();
  const personalizedChatbotAnimation = useAnimation();
  const workflowAnimation = useAnimation();
  const templateAnimation = useAnimation();
  const evaluationAnimation = useAnimation();
  const finalSectionAnimation = useAnimation();
  const router = useRouter();
  

   useEffect(() => {
    if (inView1) {
      flowChartAnimation.start({
        opacity: 1,
        y: 0,
        transition: { duration: 0.8, ease: "easeOut" },
      });
      personalizedChatbotAnimation.start({
        opacity: 1,
        y: 0,
        transition: { duration: 0.8, delay: 0.3, ease: "easeOut" },
      });
    }
  }, [inView1, flowChartAnimation, personalizedChatbotAnimation]);

  
  useEffect(() => {
    if (inView2) {
      workflowAnimation.start({
        opacity: 1,
        y: 0,
        transition: { duration: 0.8 },
      });
    }
  }, [inView2, workflowAnimation]);

  useEffect(() => {
    if (inView3) {
      templateAnimation.start({
        opacity: 1,
        y: 0,
        transition: { duration: 0.8 },
      });
    }
  }, [inView3, templateAnimation]);

  useEffect(() => {
    if (inView4) {
      evaluationAnimation.start({
        opacity: 1,
        y: 0,
        transition: { duration: 0.8 },
      });
    }
  }, [inView4, evaluationAnimation]);

  useEffect(() => {
    if (inView5) {
      finalSectionAnimation.start({
        opacity: 1,
        y: 0,
        transition: { duration: 0.8 },
      });
    }
  }, [inView5, finalSectionAnimation]);

  const features = [
    "금융",
    "헬스케어",
    "전자 상거래",
    "여행",
    "교육",
    "엔터테인먼트"
  ];


  const featureImages = [
    FinanceImg,
    HealthImg,
    ShoppingImg,
    TravelImg,
    EducationImg,
    EntertainmentImg
  ];

  useEffect(() => {
    const bannerList = document.querySelector('.banner_list') as HTMLElement;
    const bannerItems = document.querySelectorAll('.banner') as NodeListOf<HTMLElement>;
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
      <div className="landing-page">
        <style>
          {`
            @keyframes upwardBounce {
              0%, 20%, 50%, 80%, 100% {
                transform: translateY(0);
              }
              40% {
                transform: translateY(-15px);
              }
              60% {
                transform: translateY(-10px);
              }
            }

            .upward-bounce {
              animation: upwardBounce 2s infinite;
            }

            @keyframes infiniteScroll {
              0% {
                transform: translateX(0);
              }
              100% {
                transform: translateX(-50%);
              }
            }

            .scrolling-cards {
              display: flex;
              white-space: nowrap;
              overflow: hidden;
              animation: infiniteScroll 60s linear infinite;
            }

            .scrolling-cards-container {
              display: flex;
              overflow: hidden;
              width: 100%;
              position: relative;
            }

            .scrolling-cards-inner {
              display: flex;
              flex-wrap: nowrap;
            }
          `}
        </style>

        <section className="min-h-screen relative bg-gradient-to-b from-white to-[#9A75BF] overflow-hidden">
        <div className="content-container" ref={ref1}>
          <div className="mt-[50px] text-[26px] text-center font-semibold leading-relaxed">
            <motion.p
              initial={{ opacity: 0, y: 20 }}
              animate={flowChartAnimation}
            >
              누구나 손쉽게 만드는
            </motion.p>
            <motion.p
              initial={{ opacity: 0, y: 20 }}
              animate={flowChartAnimation}
            >
              챗봇 제작 플랫폼
            </motion.p>
          </div>

          <div className="text-[63px] text-center font-bold text-[#5D2973] mt-4">Flow Studio</div>
          <div className="relative flex flex-col md:flex-row items-center h-full">
            <motion.img
              src={speechBubbleImg.src}
              alt="speech bubble img"
              className="w-[33%] mb-10 absolute top-[25px] left-[245px]"
              initial={{ opacity: 0, y: 50 }}
              animate={flowChartAnimation}
            />
          </div>
          <div className="relative flex flex-col md:flex-row items-center h-full">
            <motion.img 
              src={robotImg.src}
              alt="robot img"
              className="w-[53%] mb-10 absolute top-[30px] left-[410px]"
              initial={{ opacity: 0, y: 50 }}
              animate={personalizedChatbotAnimation}
            />
          </div>
        </div>
      </section>


      <section className="h-[700px] flex items-center justify-center bg-[#9A75BF] pt-[100px]">
        <div className="content-container flex justify-center items-center" ref={ref2}>
          <div className="flex flex-row gap-16 w-[82%] rounded-[30px]">
            <div className="w-[70%] text-white">
              <motion.img
                src={screenWorkflowImg.src}
                alt="workflow screen img"
                className="rounded-lg w-full shadow-xl"
                initial={{ opacity: 0, y: 50 }}
                animate={workflowAnimation}
              />
            </div>
            <div className="flex flex-col items-start text-white w-[50%]">
              <motion.div
                className="font-semibold text-[34px] mt-[120px] mb-5"
                initial={{ opacity: 0, y: 50 }}
                animate={workflowAnimation}
              >
                챗봇 만들기
              </motion.div>
              <motion.div
                className="text-[20px] text-[#CCBADF]"
                initial={{ opacity: 0, y: 50 }}
                animate={workflowAnimation}
              >
                복잡한 코딩 없이도 블록코딩으로<br/>
                챗봇의 대화 흐름을 눈으로 보면서 쉽게 조작할 수 있어,<br/>
                누구나 쉽게 시작할 수 있습니다.
              </motion.div>
              
            </div>
          </div>
        </div>
      </section>

      <section className="h-[700px] flex items-center justify-center bg-[#9A75BF]">
        <div className="content-container flex justify-center items-center" ref={ref3}>
          <div className="flex flex-row gap-16 w-[82%] rounded-[30px]">
            <div className="w-[50%] text-white mt-[120px]">
              <motion.div
                className="font-semibold text-[34px] mb-5 text-end"
                initial={{ opacity: 0, y: 50 }}
                animate={templateAnimation}
              >
                챗봇 공유
              </motion.div>
              <motion.div
                className="text-[19px] text-end text-[#CCBADF]"
                initial={{ opacity: 0, y: 50 }}
                animate={templateAnimation}
              >
                챗봇을 공유하거나 다른 사용자가 공유한 챗봇을 <br/>다운로드할 수 있습니다. <br/>
                다운로드한 챗봇은 자유롭게 커스터마이징할 수 있습니다.
              </motion.div>
             
            </div>
            <div className="flex flex-col items-start text-white w-[70%]">
              <motion.img
                src={screenTemplateImg.src}
                alt="template screen img"
                className="rounded-lg w-full shadow-xl"
                initial={{ opacity: 0, y: 50 }}
                animate={templateAnimation}
              />
            </div>
          </div>
        </div>
      </section>

      <section className="h-[700px] flex items-center justify-center bg-[#9A75BF] pb-[100px]">
        <div className="content-container flex justify-center items-center" ref={ref4}>
          <div className="flex flex-row gap-16 w-[82%] rounded-[30px]">
            <div className="w-[70%] text-white">
              <motion.img
                src={screenEvaluationImg.src}
                alt="evaluation screen img"
                className="rounded-xl w-full shadow-lg"
                initial={{ opacity: 0, y: 50 }}
                animate={evaluationAnimation}
              />
            </div>
            <div className="flex flex-col items-start text-white w-[50%]">
              <motion.div
                className="font-semibold text-[34px] mt-[120px] mb-5"
                initial={{ opacity: 0, y: 50 }}
                animate={evaluationAnimation}
              >
                챗봇 평가
              </motion.div>
              <motion.div
                className="text-[20px] mb-[120px] text-[#CCBADF]"
                initial={{ opacity: 0, y: 50 }}
                animate={evaluationAnimation}
              >
                챗봇의 성능을 분석하여 최적화할 수 있습니다.<br/>
                심층적인 통계 데이터를 제공합니다.
              </motion.div>
            </div>
          </div>
        </div>
      </section>

      <section className="infinite-scrolling-section h-[500px] flex items-center justify-center bg-gray-100 overflow-hidden">
      <div className="content-container overflow-hidden">
        <div className="text-center mb-14 text-[26px] font-semibold">금융, 교육, 헬스케어 등 <span className="text-[#9A75BF]">원하는 분야에 최적화된 AI 챗봇</span>을 몇 분 안에 구축해 보세요.</div>
        <div className="scrolling-cards-container w-full relative">
          <div className="banner_list flex">
            {[...features].map((feature, index) => (
              <div
                key={`banner-${index}`}
                className="banner bg-white p-8 shadow-lg rounded-full m-4 min-w-[200px] flex flex-col items-center justify-center"
              >
                <img src={featureImages[index % features.length]?.src} className="w-[100px] h-[100px]"/>
                <p className="text-center text-gray-600 text-[20px] mt-4 font-semibold">
                  {feature}
                </p>
              </div>
            ))}
          </div>
        </div>
      </div>
      <style jsx>{`
        .scrolling-cards {
          animation: scroll-left 20s linear infinite;
        }

        @keyframes scroll-left {
          0% {
            transform: translateX(0);
          }
          100% {
            transform: translateX(-50%);
          }
        }
      `}</style>
    </section>
  

  
      <section className="fourth-section h-[650px] flex items-center justify-center bg-white">
          <motion.div
            className="content-container text-center"
            ref={ref5}
            initial={{ opacity: 0, y: 50 }}
            animate={finalSectionAnimation}
          >
            <h2 className="text-4xl font-bold text-gray-800 mb-8">나에게 딱 맞는 커스텀 챗봇이 필요하다면?</h2>
            <button onClick={() => router.push('/login')} className="text-[19px] bg-[#9A75BF] hover:bg-[#583768] text-white font-bold py-4 px-9 rounded-lg shadow-md transform hover:scale-105 transition-transform duration-200">
              Flow Studio 시작하기
            </button>
          </motion.div>
        </section>
      </div>
    );
}

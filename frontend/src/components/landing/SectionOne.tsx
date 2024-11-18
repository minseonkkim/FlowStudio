import { useEffect } from "react";
import { motion, useAnimation } from "framer-motion";
import { useInView } from "react-intersection-observer";
import speechBubbleImg from "@/assets/landing/speechbubble.gif";
import robotImg from "@/assets/landing/robot.png";

export default function SectionOne() {
  const { ref, inView } = useInView({ triggerOnce: true });
  const flowChartAnimation = useAnimation();
  const personalizedChatbotAnimation = useAnimation();

  useEffect(() => {
    if (inView) {
      flowChartAnimation.start({ opacity: 1, y: 0, transition: { duration: 0.8, ease: "easeOut" } });
      personalizedChatbotAnimation.start({ opacity: 1, y: 0, transition: { duration: 0.8, delay: 0.3, ease: "easeOut" } });
    }
  }, [inView, flowChartAnimation, personalizedChatbotAnimation]);

  return (
    <section className="h-[580px] md:h-[calc(100vh-54px)] relative min-w-[400px] bg-gradient-to-b from-white to-[#9A75BF] overflow-hidden">
      <div className="h-full content-container flex flex-col justify-start" ref={ref}>
        <div className="mt-[5vh] text-[22px] md:text-[28px] text-center font-semibold leading-relaxed">
          <motion.p initial={{ opacity: 0, y: 20 }} animate={flowChartAnimation} className="object-cover">
            당신의 챗봇 아이디어가
          </motion.p>
          <motion.p initial={{ opacity: 0, y: 20 }} animate={flowChartAnimation} className="object-cover">
            실현되는 곳
          </motion.p>
        </div>
        <div className="text-[53px] md:text-[57px] text-center font-bold text-[#5D2973] mt-[1vh]">Flow Studio</div>
        <div className="flex items-center justify-center flex-grow">
            <motion.img
              src={speechBubbleImg.src}
              alt="speech bubble img"
              className="hidden md:block w-[50vw] md:w-[40vw] lg:w-[32vw] absolute md:top-[32vh] left-[0vw] md:left-[3vw] lg:left-[14vw]"
              initial={{ opacity: 0, y: 50 }}
              animate={flowChartAnimation}
            />
            <motion.img
              src={robotImg.src}
              alt="robot img"
              className="w-[72vw] md:w-[53vw] lg:w-[38vw] absolute bottom-[0vh] left-[10vw] md:left-[25vw] lg:left-[31vw] object-cover"
              initial={{ opacity: 0, y: 50 }}
              animate={personalizedChatbotAnimation}
              style={{ willChange: "opacity, transform" }}
            />
        </div>
      </div>
    </section>
  );
}

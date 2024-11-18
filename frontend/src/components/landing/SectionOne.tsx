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
    <section className="h-[calc(100vh-54px)] relative min-w-[400px] bg-gradient-to-b from-white to-[#9A75BF] overflow-hidden">
      <div className="h-[calc(100vh-54px)] content-container flex flex-col justify-between" ref={ref}>
        <div className="mt-[50px] text-[23px] md:text-[26px] text-center font-semibold leading-relaxed">
          <motion.p initial={{ opacity: 0, y: 20 }} animate={flowChartAnimation} className="object-cover">
            누구나 손쉽게 만드는
          </motion.p>
          <motion.p initial={{ opacity: 0, y: 20 }} animate={flowChartAnimation} className="object-cover">
            챗봇 제작 플랫폼
          </motion.p>
        </div>
        <div className="text-[56px] md:text-[63px] text-center font-bold text-[#5D2973] mt-4">Flow Studio</div>
        <div className="flex items-center justify-center flex-grow">
            <motion.img
              src={speechBubbleImg.src}
              alt="speech bubble img"
              className="hidden md:block w-[50%] md:w-[40%] lg:w-[33%] absolute md:top-[25vh] left-[0vw] md:left-[3vw] lg:left-[12vw]"
              initial={{ opacity: 0, y: 50 }}
              animate={flowChartAnimation}
            />
            <motion.img
              src={robotImg.src}
              alt="robot img"
              className="w-[80%] md:w-[58%] lg:w-[42%] absolute bottom-[0vh] left-[10vw] md:left-[22vw] lg:left-[29vw] object-cover"
              initial={{ opacity: 0, y: 50 }}
              animate={personalizedChatbotAnimation}
              style={{ willChange: "opacity, transform" }}
            />
        </div>
      </div>
    </section>
  );
}

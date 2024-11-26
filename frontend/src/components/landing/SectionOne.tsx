import { useEffect } from "react";
import { motion, useAnimation } from "framer-motion";
import { useInView } from "react-intersection-observer";
import speechBubbleImg from "@/assets/landing/speechbubble.gif";
import robotImg from "@/assets/landing/robot.png";

export default function SectionOne() {
  const { ref, inView } = useInView({ triggerOnce: true });
  const flowChartAnimation1 = useAnimation();
  const flowChartAnimation2 = useAnimation();
  const personalizedChatbotAnimation = useAnimation();
  const flowStudioAnimation = useAnimation();

  useEffect(() => {
    if (inView) {
      flowChartAnimation1.start({
        color: "#000000",
        transition: { duration: 0.8, ease: "easeOut" },
      });
      flowChartAnimation2.start({
        color: "#000000",
        transition: { duration: 0.8, delay: 0.3, ease: "easeOut" },
      });
      flowStudioAnimation.start({
        opacity: 1,
        scale: 1,
        transition: { duration: 0.8, delay: 0.5, ease: "easeOut" },
      });
      personalizedChatbotAnimation.start({
        opacity: 1,
        y: 0,
        transition: { duration: 1, delay: 0.8, ease: "easeOut" },
      });
    }
  }, [
    inView,
    flowChartAnimation1,
    flowChartAnimation2,
    flowStudioAnimation,
    personalizedChatbotAnimation,
  ]);

  return (
    <section className="h-[calc(100vh-57px)] relative min-w-[400px] bg-gradient-to-b from-white to-[#9A75BF] overflow-hidden">
      <div className="h-full content-container flex flex-col justify-start" ref={ref}>
        <div className="mt-[4vh] text-[22px] md:text-[27px] text-center font-semibold leading-relaxed">
          <motion.p
            initial={{ color: "#d9d9d9" }}
            animate={flowChartAnimation1}
            className="object-cover"
          >
            당신의 챗봇 아이디어가
          </motion.p>
          <motion.p
            initial={{ color: "#d9d9d9" }}
            animate={flowChartAnimation2}
            className="object-cover"
          >
            실현되는 곳
          </motion.p>
        </div>
        <motion.div
          initial={{ opacity: 0, scale: 0.9 }}
          animate={flowStudioAnimation}
          className="text-[53px] md:text-[57px] text-center font-bold text-[#5D2973] mt-[1vh]"
        >
          Flow Studio
        </motion.div>
        <div className="flex items-center justify-center flex-grow">
          <motion.img
            src={speechBubbleImg.src}
            alt="speech bubble img"
            className="hidden md:block w-[50vw] md:w-[40vw] lg:w-[31vw] absolute md:bottom-[-5vh] lg:bottom-[-18vh] left-[0vw] md:left-[3vw] lg:left-[15vw]"
            initial={{ opacity: 0, y: 50 }}
            animate={personalizedChatbotAnimation}
            transition={{ delay: 1 }}
          />
          <motion.img
            src={robotImg.src}
            alt="robot img"
            className="h-[57vh] absolute bottom-[0vh] left-[10vw] md:left-[25vw] lg:left-[31vw] object-cover"
            initial={{ opacity: 0, y: 50 }}
            animate={personalizedChatbotAnimation}
            style={{ willChange: "opacity, transform" }}
          />
        </div>
      </div>
    </section>
  );
}

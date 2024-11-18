import screenEvaluationImg from "@/assets/landing/screen_evaluation.png";
import { useEffect } from "react";
import { motion, useAnimation } from "framer-motion";
import { useInView } from "react-intersection-observer";

export default function SectionFour() {
  const { ref, inView } = useInView({ triggerOnce: true });
  const evaluationAnimation = useAnimation();

  useEffect(() => {
    if (inView) {
      evaluationAnimation.start({
        opacity: 1,
        rotate: 0,
        transition: {
          type: "spring",
          stiffness: 120,
          damping: 20,
          duration: 1,
        },
      });
    }
  }, [inView, evaluationAnimation]);

  return (
    <section className="h-[440px] md:h-[580px] flex items-center justify-center bg-[#9A75BF] md:pb-[100px]">
      <div className="content-container flex justify-center items-center" ref={ref}>
        <div className="flex flex-col md:flex-row gap-0 md:gap-16 w-[78%] rounded-[30px]">
          <div className="w-full md:w-[63%] text-white">
            <motion.img
              src={screenEvaluationImg.src}
              alt="evaluation screen img"
              className="rounded-xl w-full shadow-lg"
              initial={{ opacity: 0, rotate: 10 }}
              animate={evaluationAnimation}
              loading="lazy"
            />
          </div>
          <div className="flex flex-col items-start text-white w-full md:w-[50%] mt-[30px] md:mt-[120px]">
            <motion.div
              className="font-bold text-[27px] md:text-[33px] mb-5"
              initial={{ opacity: 0, rotate: -10 }}
              animate={evaluationAnimation}
            >
              챗봇 평가
            </motion.div>
            <motion.div
              className="text-[15px] md:text-[18px] mb-[120px] text-[#CCBADF] font-semibold"
              initial={{ opacity: 0, rotate: -10 }}
              animate={evaluationAnimation}
            >
              챗봇의 <span className="text-white">성능을 분석</span>하여 최적화할 수 있습니다.
              <br />
              심층적인 통계 데이터를 제공합니다.
            </motion.div>
          </div>
        </div>
      </div>
    </section>
  );
}

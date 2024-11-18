import { useEffect } from "react";
import { motion, useAnimation } from "framer-motion";
import { useInView } from "react-intersection-observer";
import screenEvaluationImg from "@/assets/landing/screen_evaluation.png";

export default function SectionFour() {
  const { ref, inView } = useInView({ triggerOnce: true });
  const evaluationAnimation = useAnimation();

  useEffect(() => {
    if (inView) {
      evaluationAnimation.start({ opacity: 1, y: 0, transition: { duration: 0.8 } });
    }
  }, [inView, evaluationAnimation]);

  return (
    <section className="h-[600px] md:h-[700px] flex items-center justify-center bg-[#9A75BF] md:pb-[100px]">
      <div className="content-container flex justify-center items-center" ref={ref}>
        <div className="flex flex-col md:flex-row gap-0 md:gap-16 w-[82%] rounded-[30px]">
          <div className="w-full md:w-[70%] text-white">
            <motion.img
              src={screenEvaluationImg.src}
              alt="evaluation screen img"
              className="rounded-xl w-full shadow-lg"
              initial={{ opacity: 0, y: 50 }}
              animate={evaluationAnimation}
              loading="lazy"
            />
          </div>
          <div className="flex flex-col items-start text-white w-full md:w-[50%] mt-[30px] md:mt-[120px]">
            <motion.div className="font-semibold text-[28px] md:text-[34px] mb-5" initial={{ opacity: 0, y: 50 }} animate={evaluationAnimation}>
              챗봇 평가
            </motion.div>
            <motion.div className="text-[15px] md:text-[19px] mb-[120px] text-[#CCBADF]" initial={{ opacity: 0, y: 50 }} animate={evaluationAnimation}>
              챗봇의 성능을 분석하여 최적화할 수 있습니다.
              <br />
              심층적인 통계 데이터를 제공합니다.
            </motion.div>
          </div>
        </div>
      </div>
    </section>
  );
}

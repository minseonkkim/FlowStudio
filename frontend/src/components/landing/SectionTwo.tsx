import { useEffect } from "react";
import { motion, useAnimation } from "framer-motion";
import { useInView } from "react-intersection-observer";
import screenWorkflowImg from "@/assets/landing/screen_workflow.png";

export default function SectionTwo() {
  const { ref, inView } = useInView({ triggerOnce: true });
  const workflowAnimation = useAnimation();

  useEffect(() => {
    if (inView) {
      workflowAnimation.start({
        opacity: 1,
        y: 0,
        scale: 1,
        transition: {
          type: "spring",
          stiffness: 120,
          damping: 20,
          duration: 1,
        },
      });
    }
  }, [inView, workflowAnimation]);

  return (
    <section className="h-[600px] md:h-[730px] flex items-center justify-center bg-[#9A75BF] md:pt-[100px]">
      <div className="content-container flex justify-center items-center" ref={ref}>
        <div className="flex flex-col md:flex-row gap-0 md:gap-16 w-[78%] rounded-[30px]">
          <div className="w-full md:w-[60%] text-white">
            <motion.img
              src={screenWorkflowImg.src}
              alt="workflow screen img"
              className="rounded-lg w-full shadow-xl"
              initial={{ opacity: 0, y: 100, scale: 0.9 }}
              animate={workflowAnimation}
              loading="lazy"
            />
          </div>
          <div className="flex flex-col items-start text-white w-full md:w-[50%] mt-[30px] md:mt-[120px]">
            <motion.div
              className="font-bold text-[27px] md:text-[33px] mb-5"
              initial={{ opacity: 0, y: 100 }}
              animate={workflowAnimation}
            >
              챗봇 만들기
            </motion.div>
            <motion.div
              className="text-[15px] md:text-[18px] text-[#CCBADF] font-semibold"
              initial={{ opacity: 0, y: 100 }}
              animate={workflowAnimation}
            >
              복잡한 코딩 없이도 <span className="text-white">블록코딩</span>으로
              <br />
              챗봇의 대화 흐름을 눈으로 보면서 쉽게 조작할 수 있어,
              <br />
              누구나 쉽게 시작할 수 있습니다.
            </motion.div>
          </div>
        </div>
      </div>
    </section>
  );
}
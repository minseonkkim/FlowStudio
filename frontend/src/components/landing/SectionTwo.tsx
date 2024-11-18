import { useEffect } from "react";
import { motion, useAnimation } from "framer-motion";
import { useInView } from "react-intersection-observer";
import screenWorkflowImg from "@/assets/landing/screen_workflow.png";

export default function SectionTwo() {
  const { ref, inView } = useInView({ triggerOnce: true });
  const workflowAnimation = useAnimation();

  useEffect(() => {
    if (inView) {
      workflowAnimation.start({ opacity: 1, y: 0, transition: { duration: 0.8 } });
    }
  }, [inView, workflowAnimation]);

  return (
    <section className="h-[600px] md:h-[700px] flex items-center justify-center bg-[#9A75BF] md:pt-[100px]">
      <div className="content-container flex justify-center items-center" ref={ref}>
        <div className="flex flex-col md:flex-row gap-0 md:gap-16 w-[82%] rounded-[30px]">
          <div className="w-full md:w-[70%] text-white">
            <motion.img
              src={screenWorkflowImg.src}
              alt="workflow screen img"
              className="rounded-lg w-full shadow-xl"
              initial={{ opacity: 0, y: 50 }}
              animate={workflowAnimation}
              loading="lazy"
            />
          </div>
          <div className="flex flex-col items-start text-white w-full md:w-[50%] mt-[30px] md:mt-[120px]">
            <motion.div className="font-semibold text-[28px] md:text-[34px] mb-5" initial={{ opacity: 0, y: 50 }} animate={workflowAnimation}>
              챗봇 만들기
            </motion.div>
            <motion.div className="text-[15px] md:text-[19px] text-[#CCBADF]" initial={{ opacity: 0, y: 50 }} animate={workflowAnimation}>
              복잡한 코딩 없이도 블록코딩으로
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

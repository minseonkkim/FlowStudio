import { useEffect } from "react";
import { motion, useAnimation } from "framer-motion";
import { useInView } from "react-intersection-observer";
import screenTemplateImg from "@/assets/landing/screen_template.png";

export default function SectionThree() {
  const { ref, inView } = useInView({ triggerOnce: true });
  const templateAnimation = useAnimation();

  useEffect(() => {
    if (inView) {
      templateAnimation.start({ opacity: 1, y: 0, transition: { duration: 0.8 } });
    }
  }, [inView, templateAnimation]);

  return (
    <section className="h-[600px] md:h-[700px] flex items-center justify-center bg-[#9A75BF]">
      <div className="content-container flex justify-center items-center" ref={ref}>
        <div className="flex flex-col-reverse md:flex-row gap-0 md:gap-16 w-[82%] rounded-[30px]">
          <div className="w-full md:w-[50%] text-white mt-[30px] md:mt-[120px]">
            <motion.div className="font-semibold text-[28px] md:text-[34px] mb-5 text-end" initial={{ opacity: 0, y: 50 }} animate={templateAnimation}>
              챗봇 공유
            </motion.div>
            <motion.div className="text-[15px] md:text-[19px] text-end text-[#CCBADF]" initial={{ opacity: 0, y: 50 }} animate={templateAnimation}>
              챗봇을 공유하거나 다른 사용자가 공유한 챗봇을
              <br />
              다운로드할 수 있습니다.
              <br />
              다운로드한 챗봇은 자유롭게 커스터마이징할 수 있습니다.
            </motion.div>
          </div>
          <div className="flex flex-col items-start text-white w-full md:w-[70%]">
            <motion.img
              src={screenTemplateImg.src}
              alt="template screen img"
              className="rounded-lg w-full shadow-xl"
              initial={{ opacity: 0, y: 50 }}
              animate={templateAnimation}
              loading="lazy"
            />
          </div>
        </div>
      </div>
    </section>
  );
}

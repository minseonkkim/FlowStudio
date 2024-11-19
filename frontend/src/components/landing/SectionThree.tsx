import screenTemplateImg from "@/assets/landing/screen_template.png";
import { useEffect } from "react";
import { motion, useAnimation } from "framer-motion";
import { useInView } from "react-intersection-observer";

export default function SectionThree() {
  const { ref, inView } = useInView({ triggerOnce: true });
  const templateAnimation = useAnimation();

  useEffect(() => {
    if (inView) {
      templateAnimation.start({
        opacity: 1,
        x: 0,
        transition: {
          type: "spring",
          stiffness: 120,
          damping: 20,
          duration: 1,
        },
      });
    }
  }, [inView, templateAnimation]);

  return (
    <section className="h-[440px] md:h-[580px] flex items-center justify-center bg-[#9A75BF]">
      <div className="content-container flex justify-center items-center" ref={ref}>
        <div className="flex flex-col-reverse md:flex-row gap-0 md:gap-16 w-[78%] rounded-[30px]">
          <div className="w-full md:w-[50%] text-white mt-[30px] md:mt-[120px]">
            <motion.div
              className="font-bold text-[27px] md:text-[33px] mb-5 text-end"
              initial={{ opacity: 0, x: -100 }}
              animate={templateAnimation}
            >
              챗봇 공유
            </motion.div>
            <motion.div
              className="text-[15px] md:text-[18px] text-end text-[#CCBADF] font-semibold"
              initial={{ opacity: 0, x: -100 }}
              animate={templateAnimation}
            >
              챗봇을 공유하거나 다른 사용자가 공유한 챗봇을
              <br />
              다운로드 하여 <span className="text-white">커스터마이징</span>할 수 있습니다.
            </motion.div>
          </div>
          <div className="flex flex-col items-start text-white w-full md:w-[63%]">
            <motion.img
              src={screenTemplateImg.src}
              alt="template screen img"
              className="rounded-lg w-full shadow-xl"
              initial={{ opacity: 0, x: 100, scale: 0.9 }}
              animate={templateAnimation}
              loading="lazy"
            />
          </div>
        </div>
      </div>
    </section>
  );
}
import { useAnimation, motion } from "framer-motion";
import { useInView } from "react-intersection-observer";
import { useEffect } from "react";
import { useRouter } from "next/navigation";

export default function SectionSix() {
  const { ref, inView } = useInView({ triggerOnce: true });
  const finalSectionAnimation = useAnimation();
  const secondHeadingAnimation = useAnimation();
  const buttonAnimation = useAnimation();
  const router = useRouter();

  useEffect(() => {
    if (inView) {
      finalSectionAnimation.start({ opacity: 1, y: 0, transition: { duration: 0.8 } });
      secondHeadingAnimation.start({ opacity: 1, y: 0, transition: { duration: 0.8, delay: 0.3 } });
      buttonAnimation.start({ opacity: 1, y: 0, transition: { duration: 0.8, delay: 0.6 } });
    }
  }, [inView, finalSectionAnimation, secondHeadingAnimation, buttonAnimation]);

  return (
    <section className="fourth-section h-[380px] md:h-[650px] flex items-center justify-center bg-white">
      <motion.div className="content-container text-center" ref={ref} initial={{ opacity: 0, y: 50 }} animate={finalSectionAnimation}>
        <motion.h2 className="text-[24px] md:text-[33px] font-bold text-gray-800 mb-2" initial={{ opacity: 0, y: 50 }} animate={finalSectionAnimation}>
          챗봇 개발이 쉬워지니까.
        </motion.h2>
        <motion.h2 className="text-[24px] md:text-[33px] font-bold text-gray-800 mb-8" initial={{ opacity: 0, y: 50 }} animate={secondHeadingAnimation}>
          나에게 딱 맞는 <span className="text-[#9A75BF]">커스텀 챗봇</span>이 필요하다면?
        </motion.h2>
        <motion.button
          onClick={() => router.push("/login")}
          className="text-[17px] md:text-[19px] bg-[#9A75BF] hover:bg-[#583768] text-white font-bold py-4 px-9 rounded-lg shadow-md transform hover:scale-105 transition-transform duration-200"
          initial={{ opacity: 0, y: 50 }}
          animate={buttonAnimation}
        >
          Flow Studio 시작하기
        </motion.button>
      </motion.div>
    </section>
  );
}

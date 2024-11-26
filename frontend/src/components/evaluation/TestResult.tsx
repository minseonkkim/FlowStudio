"use client";
import { useEffect } from "react";
import { useRecoilValue } from "recoil";
import { parsedTestDataState,  } from "@/store/evaluationAtoms";

interface TestResulttProps {
  selectedTab: string
}
export default function TestResult({ selectedTab }: TestResulttProps) {

  
  const items = useRecoilValue(parsedTestDataState)

  const calculateMeanAndVariance = (key: keyof typeof items[0]) => {
    const values = items.map((item) => item[key] as number);
    const mean = values.reduce((sum, val) => sum + val, 0) / values.length;
    const variance =
      values.reduce((sum, val) => sum + Math.pow(val - mean, 2), 0) /
      values.length;
    return { mean, variance };
  };

  const embedding = calculateMeanAndVariance("embeddingDistance");
  const rouge = calculateMeanAndVariance("rougeMetric");
  const crossEncoder = calculateMeanAndVariance("crossEncoder");

  return (
    <>
    <div className="flex justify-between mb-10 h-[15px]">
      <p className="text-[22px]">{selectedTab}</p>
    </div>
    <div className="container">
      {items.map((item,index) => (
        <div key={index+1} className="border-2 rounded-xl mb-4">
          <details
           className="py-4 px-6"
           open={index === 0}>
            <summary className="font-semibold">테스트 케이스 {index+1}</summary>
            <div className="mt-4">
              <label className="block mb-2">평가 결과</label>
              <div>Embedding Distance: {item.embeddingDistance}</div>
              <div>ROUGE Metric: {item.rougeMetric}</div>
              <div>Cross Encoder: {item.crossEncoder}</div>
            </div>
            <div className="mt-4">
              <label className="block mb-2">테스트 질문 (Test Question)</label>
              <input
                readOnly
                type="text"
                value={item.testQuestion}
                className="w-full p-2 border rounded-md bg-gray-100 focus:outline-none"
              />
            </div>
            <div className="mt-4">
              <label className="block mb-2">정답 (Ground Truth)</label>
              <textarea
                readOnly
                value={item.groundTruth}
                className="w-full p-2 border rounded-md bg-gray-100 focus:outline-none"
              />
            </div>
            <div className="mt-4">
              <label className="block mb-2">예측 응답 (Prediction)</label>
              <textarea
                readOnly
                value={item.prediction}
                className="w-full p-2 border rounded-md focus:outline-none"
              />
            </div>

            <div className="text-[14px] text-[#757575] my-4">
              <p>
                ROUGE Metric과 Embedding Distance는 텍스트 유사도를 측정하는 데 사용되었으며,
                Cross Encoder는 문장 간 의미적 일치도를 평가하기 위한 지표로 활용되었습니다.
              </p>
              <ul className="list-disc list-inside ml-5">
                <li>Embedding Distance: 텍스트를 벡터공간에 매핑하고 거리를 측정하여 유사도를 평가</li>
                <li>Cross-Encoder: 두 테스트를 동시에 입력받아 직접적인 관련성 점수를 출력</li>
                <li>Rouge Metric: 생성된 텍스트와 참조 텍스트 간의 겹치는 n-gram을 기반으로 평가</li>
              </ul>
            </div>

          </details>
        </div>
      ))}

      <p className="text-[22px] font-semibold mt-12 mb-6">종합 결과</p>
      <table className="mb-12 w-full border-collapse text-center">
        <thead>
          <tr className="bg-[#E1D5F2]">
            <th className="p-2 border"> </th>
            <th className="p-2 border">Embedding Distance</th>
            <th className="p-2 border">ROUGE Metric</th>
            <th className="p-2 border">Cross Encoder</th>
          </tr>
        </thead>
        <tbody>
          <tr>
            <td className="p-2 border font-semibold bg-[#E1D5F2]">mean</td>
            <td className="p-2 border text-center">
              {embedding.mean.toFixed(2)}
            </td>
            <td className="p-2 border text-center">{rouge.mean.toFixed(2)}</td>
            <td className="p-2 border text-center">
              {crossEncoder.mean.toFixed(2)}
            </td>
          </tr>
          <tr>
            <td className="p-2 border font-semibold bg-[#E1D5F2]">variance</td>
            <td className="p-2 border">
              {embedding.variance.toFixed(2)}
            </td>
            <td className="p-2 border">
              {rouge.variance.toFixed(2)}
            </td>
            <td className="p-2 border">
              {crossEncoder.variance.toFixed(2)}
            </td>
          </tr>
        </tbody>
      </table>
      <div className="h-16"></div>
    </div>
    </>
  );
}

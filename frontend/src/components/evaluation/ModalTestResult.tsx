import { ChatFlowTestCase } from '@/types/evaluation';

interface ModalTestResultProps {
  testResultDetail: ChatFlowTestCase[];
}

export default function ModalTestResult({ testResultDetail }: ModalTestResultProps) {
  return (
    <div className="container">
      <div
        className="bg-white rounded-lg max-h-[80vh] w-full max-w-3xl overflow-y-auto p-6"
        style={{
          maxHeight: "80vh",
          overflowY: "auto",
        }}
      >
        {testResultDetail.map((item, index) => (
          <div key={index + 1} className="border-2 rounded-xl mb-4">
            <details
              className="py-4 px-6"
              open={index === 0} // 첫 번째 항목만 항상 열려 있도록 설정
            >
              <summary className="font-semibold">테스트 케이스 {index + 1}</summary>
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
            </details>
          </div>
        ))}
      </div>
    </div>
  );
}

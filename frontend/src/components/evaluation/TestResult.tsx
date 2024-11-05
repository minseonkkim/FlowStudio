const items = [
  {
    id: 1,
    testQuestion: "What is the capital of France?",
    groundTruth: "Paris",
    prediction: "Paris",
    embeddingDistance: 0.95,
    rougeMetric: 0.95,
    crossEncoder: 0.95,
  },
  {
    id: 2,
    testQuestion: "What is 2 + 2?",
    groundTruth: "4",
    prediction: "4",
    embeddingDistance: 0.95,
    rougeMetric: 0.95,
    crossEncoder: 0.95,
  },
  {
    id: 3,
    testQuestion: "Who wrote 'To Kill a Mockingbird'?",
    groundTruth: "Harper Lee",
    prediction: "Harper Lee",
    embeddingDistance: 0.95,
    rougeMetric: 0.95,
    crossEncoder: 0.95,
  },
];

export default function TestResult() {
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
    <div className="container">
      {items.map((item) => (
        <div key={item.id} className="border-2 rounded-xl mb-4">
          <details className="py-4 px-6">
            <summary className="font-semibold">테스트 케이스 {item.id}</summary>
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
    </div>
  );
}

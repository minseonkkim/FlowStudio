interface ChatFlowTestCase {
  id: number;
  testQuestion: string;
  groundTruth: string;
  prediction: string;
  embeddingDistance: number;
  crossEncoder: number;
  rougeMetric: number;
}

interface ChatFlowTestResult {
  id: number;
  embeddingDistanceMean: number;
  embeddingDistanceVariance: number;
  crossEncoderMean: number;
  crossEncoderVariance: number;
  rougeMetricMean: number;
  rougeMetricVariance: number;
  chatFlowTestCases: ChatFlowTestCase[];
}



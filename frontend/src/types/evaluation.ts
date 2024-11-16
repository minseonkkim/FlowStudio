// interface ChatFlowTestCase {
//   id: number;
//   testQuestion: string;
//   groundTruth: string;
//   prediction: string;
//   embeddingDistance: number;
//   crossEncoder: number;
//   rougeMetric: number;
// }

// interface getTestResultDetailData {
//   id: number;
//   embeddingDistanceMean: number;
//   embeddingDistanceVariance: number;
//   crossEncoderMean: number;
//   crossEncoderVariance: number;
//   rougeMetricMean: number;
//   rougeMetricVariance: number;
//   chatFlowTestCases: ChatFlowTestCase[];
// }


export interface ChatFlowTestResult {
  embeddingDistanceMean: number;       
  embeddingDistanceVariance: number;  
  crossEncoderMean: number;           
  crossEncoderVariance: number;       
  rougeMetricMean: number;            
  rougeMetricVariance: number;        
  totalTestCount: number;             
}

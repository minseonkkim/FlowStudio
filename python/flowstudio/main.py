import time
from typing import List, Optional

import uvicorn
from dotenv import load_dotenv
from fastapi import FastAPI, HTTPException
from korouge_score import rouge_scorer
from langchain.evaluation import load_evaluator, EvaluatorType, EmbeddingDistance
from langchain.text_splitter import RecursiveCharacterTextSplitter
from langchain_openai import ChatOpenAI
from pydantic import BaseModel
from sentence_transformers import CrossEncoder

app = FastAPI()

class SplitRequest(BaseModel):
    text: str
    chunkSize: int
    chunkOverlap: int
    separators: Optional[List[str]] = None

class EvaluationRequest(BaseModel):
    groundTruth: str
    prediction: str

class EvaluationResponse(BaseModel):
    embeddingDistance: float
    crossEncoder: float
    rougeMetric: float

@app.post("/langchain/split_text")
async def split_text(request: SplitRequest):
    if request.chunkSize <= 0:
        raise HTTPException(status_code=400, detail="chunk_size는 0보다 커야 합니다.")
    if request.chunkOverlap < 0:
        raise HTTPException(status_code=400, detail="chunk_overlap은 0 이상이어야 합니다.")
    if request.chunkOverlap >= request.chunkSize:
        raise HTTPException(status_code=400, detail="chunk_overlap은 chunk_size보다 작아야 합니다.")

    separators = request.separators or ["\n\n", "\n", " ", ""]
    text_splitter = RecursiveCharacterTextSplitter(
        chunk_size=request.chunkSize,
        chunk_overlap=request.chunkOverlap,
        separators=separators,
    )
    chunks = text_splitter.split_text(request.text)
    return {"chunks": chunks}

@app.post("/langchain/evaluation", response_model=EvaluationResponse)
async def evaluate(request: EvaluationRequest):
    start_time = time.time()
    load_dotenv()

    ground_truth = request.groundTruth
    prediction = request.prediction

    # 1. Embedding Distance 구하기
    embedding_evaluator = load_evaluator(
        evaluator=EvaluatorType.EMBEDDING_DISTANCE,
        distance_metric=EmbeddingDistance.COSINE,  # 코사인 유사도 사용
        llm=ChatOpenAI(model="gpt-4o-mini", temperature=0.0)
    )

    embedding_distance = embedding_evaluator.evaluate_strings(prediction=prediction, reference=ground_truth)

    # 2. Cross Encoder로 유사도 구하기
    cross_encoder_model = CrossEncoder("BAAI/bge-reranker-v2-m3")
    sentence_pairs = [[ground_truth, prediction]]
    similarity_scores = cross_encoder_model.predict(sentence_pairs)

    cross_encoder = similarity_scores[0]

    # 3. Rouge Metric 구하기
    scorer = rouge_scorer.RougeScorer(['rougeL'], use_stemmer=True)
    calculated_score = scorer.score(ground_truth, prediction)

    rouge_metric = calculated_score["rougeL"].fmeasure
    end_time = time.time()

    execution_time = end_time - start_time
    print(f"Total Evaluation Execution Time: {execution_time} seconds")

    return EvaluationResponse(
        embeddingDistance = embedding_distance['score'],
        crossEncoder = cross_encoder,
        rougeMetric = rouge_metric
    )


if __name__ == "__main__":
    uvicorn.run("main:app", host='0.0.0.0', port=9700, reload=True)
from typing import List, Optional

import uvicorn
from fastapi import FastAPI, HTTPException
from pydantic import BaseModel
from langchain.text_splitter import RecursiveCharacterTextSplitter

app = FastAPI()

class SplitRequest(BaseModel):
    text: str
    chunkSize: int
    chunkOverlap: int
    separators: Optional[List[str]] = None

@app.post("/api/v1/langchain/split_text")
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

if __name__ == "__main__":
    uvicorn.run("main:app", host='0.0.0.0', port=8000, reload=True)
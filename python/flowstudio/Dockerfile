FROM python:3.10.15

# 작업 디렉토리 설정
WORKDIR /app/S11P31C201/python/flowstudio

# 필요한 파일 복사 (소스코드만 복사)
COPY . .

RUN pip install -r requirements.txt

# 포트 노출
EXPOSE 9700

# run.sh 스크립트 실행
CMD ["uvicorn", "main:app", "--host", "0.0.0.0", "--port", "9700"]


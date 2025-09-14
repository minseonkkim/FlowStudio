# 🤖 Flow Studio

![썸네일](./docs/assets/thumbnail.png)


## 💜 프로젝트 소개

챗봇을 만들어보고 싶은데 너무 어려우셨나요?<br/>
어디서부터 시작해야 할지 막막하고, 기업용 솔루션은 부담스러우셨죠?

Flow Studio는 **누구나 쉽게 LLM 챗봇을 만들 수 있도록 돕는 플랫폼**입니다.<br/>
복잡한 코딩 대신 직관적인 플로우 차트를 이용해, 블록코딩을 하듯 챗봇의 흐름을 디자인해보세요!<br/>
뿐만 아니라, RAG 기술로 여러분의 지식을 챗봇에 더해 개인화된 경험을 제공합니다. 

Flow Studio와 함께라면 챗봇 제작이 더 이상 어렵지 않습니다.<br/>
여러분만의 다양한 챗봇 아이디어를 실현해보세요! 😊🚀

## 💜 프로젝트 기간
2024.10.14 ~ 2024.11.26 (7주)


## 💜 주요 기능

### 챗봇 만들기

- LLM, 질문 분류기, 답변 등의 노드를 추가하고 연결하여 챗봇의 대화 흐름을 구성할 수 있습니다.
- 챗플로우를 설정한 후 미리보기로 즉시 테스트해 챗봇의 답변을 실시간으로 확인하고 수정할 수 있습니다.

![챗봇 만들기](./docs/assets/screen_workflow.png)
![챗봇 완성](./docs/assets/screen_chatbot_complete.PNG)

### 챗봇 공유

- 나의 챗봇을 다른 사용자에게 공유할 수 있습니다.
- 다른 사용자가 만든 챗봇을 나의 챗봇으로 가져와 필요에 맞게 커스터마이징할 수 있습니다.

![챗봇 공유](./docs/assets/screen_template.png)

### 챗봇 평가

- 성능 검증을 위해 원하는 질문과 예상 답변을 입력해 챗봇의 정확성을 테스트할 수 있습니다.
- 생성된 답변과 입력한 정답을 비교해 챗봇의 성능을 평가할 수 있습니다.
- Embedding Distance, Cross-Encoder, ROUGE Metric 등 다양한 지표로 평가 결과를 확인하고 분석할 수 있습니다.

![챗봇 평가](./docs/assets/screen_evaluation.png)
![챗봇 평가 보고서](./docs/assets/screen_report.PNG)

### 지식 베이스

- 챗플로우 제작시 지식 검색 노드에 활용할 지식을 등록하고 관리할 수 있습니다.
- 지식의 상세 내용들을 추가, 수정, 삭제할 수 있습니다.

![지식 베이스](./docs/assets/screen_knowledge.png)
![지식 베이스 수정](./docs/assets/screen_knowledge_chunks.png)

## 💜 기술 스택

### **Backend**

<img src="https://img.shields.io/badge/IntelliJ IDEA-000000?style=for-the-badge&logo=IntelliJIDEA&logoColor=white"> <img src="https://img.shields.io/badge/Spring-6DB33F?style=for-the-badge&logo=spring&logoColor=white"> <img src="https://img.shields.io/badge/SpringBoot-6DB33F?style=for-the-badge&logo=SpringBoot&logoColor=white"> <img src="https://img.shields.io/badge/Swagger-85EA2D?style=for-the-badge&logo=Swagger&logoColor=white"> <br> <img src="https://img.shields.io/badge/Redis-DC382D?style=for-the-badge&logo=Redis&logoColor=white"> <img src="https://img.shields.io/badge/MySQL-4479A1?style=for-the-badge&logo=MySQL&logoColor=white"> <img src="https://img.shields.io/badge/Milvus-00A1EA?style=for-the-badge&logo=Milvus&logoColor=white"> <img src="https://img.shields.io/badge/QueryDSL-0085C9?style=for-the-badge&logo=QueryDSL&logoColor=white"> <img src="https://img.shields.io/badge/AWS S3-569A31?style=for-the-badge&logo=amazons3&logoColor=white"> <br/>  <img src="https://img.shields.io/badge/Fast API-009688?style=for-the-badge&logo=fastapi&logoColor=white"> <img src="https://img.shields.io/badge/pycharm-000000?style=for-the-badge&logo=pycharm&logoColor=white"> <img src="https://img.shields.io/badge/Langchain4j-1C3C3C?style=for-the-badge&logo=Langchain&logoColor=white"> <img src="https://img.shields.io/badge/junit5-25A162?style=for-the-badge&logo=junit5&logoColor=white"> 


### **Frontend**

<img src="https://img.shields.io/badge/Visual Studio Code-007ACC?style=for-the-badge&logo=VisualStudioCode&logoColor=white"> <img src="https://img.shields.io/badge/Next.js-000000?style=for-the-badge&logo=nextdotjs&logoColor=white"> <img src="https://img.shields.io/badge/Typescript-3178C6?style=for-the-badge&logo=Typescript&logoColor=white"> <img src="https://img.shields.io/badge/recoil-3578E5?style=for-the-badge&logo=recoil&logoColor=white"> <img src="https://img.shields.io/badge/axios-5A29E4?style=for-the-badge&logo=styledcomponents&logoColor=white"> <br> <img src="https://img.shields.io/badge/Tailwind%20CSS-06B6D4?style=for-the-badge&logo=TailwindCSS&logoColor=white">  <img src="https://img.shields.io/badge/React%20Flow-FF338F?style=for-the-badge&logoColor=white"> <img src="https://img.shields.io/badge/Storybook-FF4785?style=for-the-badge&logo=Storybook&logoColor=white"> <img src="https://img.shields.io/badge/reactquery-FF4154?style=for-the-badge&logo=reactquery&logoColor=white"> <img src="https://img.shields.io/badge/Chart.js-FF6384?style=for-the-badge&logo=chartdotjs&logoColor=white"> <img src="https://img.shields.io/badge/Jest-C21325?style=for-the-badge&logo=jest&logoColor=white"> 


### **CI/CD**

<img src="https://img.shields.io/badge/AWS EC2-232F3E?style=for-the-badge&logo=AmazonAWS&logoColor=white"> <img src="https://img.shields.io/badge/Jenkins-D24939?style=for-the-badge&logo=Jenkins&logoColor=white"> <img src="https://img.shields.io/badge/Docker-2496ED?style=for-the-badge&logo=Docker&logoColor=white"> <img src="https://img.shields.io/badge/Docker Compose-2496ED?style=for-the-badge&logo=Docker&logoColor=white"> <img src="https://img.shields.io/badge/NGINX-009639?style=for-the-badge&logo=NGINX&logoColor=white"> <img src="https://img.shields.io/badge/SSL-000000?style=for-the-badge&logo=&logoColor=white">

### **Communication**

<img src="https://img.shields.io/badge/Git(Gitlab)-FCA121?style=for-the-badge&logo=Gitlab&logoColor=white"> <img src="https://img.shields.io/badge/Jira-0052CC?style=for-the-badge&logo=Jira&logoColor=white"> <img src="https://img.shields.io/badge/Notion-000000?style=for-the-badge&logo=Notion&logoColor=white"> <img src="https://img.shields.io/badge/Mattermost-0058CC?style=for-the-badge&logo=Mattermost&logoColor=white"> <img src="https://img.shields.io/badge/Figma-F24E1E?style=for-the-badge&logo=Figma&logoColor=white">


## 💜 팀원 소개
| ![김가람](https://avatars.githubusercontent.com/garamgim) | ![장재훈](https://avatars.githubusercontent.com/JaeHunJang)  | ![최재원](https://avatars.githubusercontent.com/Jaewooooon) | ![김민선](https://avatars.githubusercontent.com/u/76653033?v=4) | ![고도연](https://avatars.githubusercontent.com/doyeon01) | ![정현수](https://avatars.githubusercontent.com/u/109744927?v=4) |
|---------------------------------------------------------------------------------------------------------------|----------------------------------------------------------------------------------------------------|---------------------------------------------------------------------------------------------------------------|-------------------------------------------------------------------------------------------------|--------------------------------------------------------------------------------------------------|--------------------------------------------------------------------------------------------------|
| 김가람([@garamgim](https://github.com/garamgim)) | 장재훈([@JaeHunJang](https://github.com/JaeHunJang))  | 최재원([@Jaewooooon](https://github.com/Jaewooooon)) | 김민선([@minseonkkim](https://github.com/minseonkkim)) | 고도연([@doyeon01](https://github.com/doyeon01)) | 정현수([@surina125](https://github.com/surina125)) |
| Leader / Back End / Infra | Back End | Back End | Front End | Front End | Front End |


## 💜 프로젝트 산출물

- [요구사항명세서](./docs/요구사항명세서.md)
- [와이어프레임](./docs/와이어프레임.md)
- [API명세서](./docs/API명세서.md)
- [ERD](./docs/ERD.md)
- [목업](./docs/목업.md)
- [아키텍처](./docs/아키텍처.md)

## 💜 프로젝트 결과물

- [포팅메뉴얼](./exec/포팅매뉴얼.md)
- [중간발표자료](./docs/assets/FlowStudio_중간발표.pdf)
- [최종발표자료](./docs/assets/FlowStudio_최종발표.pdf)

## 💜 시연 영상

https://github.com/user-attachments/assets/ccbc0f2d-6fc5-487a-9c10-8c7ac08b0f09




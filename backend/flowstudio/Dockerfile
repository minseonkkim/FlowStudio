FROM openjdk:17

WORKDIR /app

RUN ln -sf /usr/share/zoneinfo/Asia/Seoul /etc/localtime && \
    echo "Asia/Seoul" > /etc/timezone

ARG JAR_FILE=build/libs/*.jar
COPY ${JAR_FILE} app.jar
# COPY .env .env

ENV SPRING_PROFILES_ACTIVE=prod

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]

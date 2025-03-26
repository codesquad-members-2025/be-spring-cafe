# 1. Build Stage (Gradle 빌드 실행)
FROM eclipse-temurin:21 AS build

WORKDIR /app

# Gradle wrapper 관련 문제 방지를 위해 모든 파일 복사
COPY . .

# Gradle 빌드 (테스트 제외)
RUN ./gradlew clean build -x test

# 2. Runtime Stage (실제 실행 환경)
FROM eclipse-temurin:21 AS runtime

WORKDIR /app

# /app/data 디렉토리 미리 생성
RUN mkdir -p /app/data

# Build Stage에서 생성된 JAR 파일 복사
COPY --from=build /app/build/libs/*.jar app.jar

# templates 디렉토리를 컨테이너의 /app/templates로 복사
COPY ./src/main/resources/templates /app/templates

# src/main/resources/static 디렉토리를 앱의 static 디렉토리로 복사
COPY ./src/main/resources/static /app/static

# 실행할 포트 설정
EXPOSE 8080

# 애플리케이션 실행
ENTRYPOINT ["java", "-jar", "app.jar"]
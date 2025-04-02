# ==================================
# 🏗 1단계: 빌드용 이미지 (Gradle + JDK)
# ==================================
FROM gradle:8.13-jdk21 AS builder

# 캐시 최적화를 위해 Gradle 설정 먼저 복사
WORKDIR /app
COPY build.gradle settings.gradle ./
COPY gradle gradle
RUN gradle --version

# 소스코드 복사 및 빌드
COPY . .
RUN ./gradlew build -x test

# ==================================
# 🚀 2단계: 실행용 이미지 (슬림한 JDK)
# ==================================
FROM openjdk:21-jdk-slim

WORKDIR /app

# 빌드된 JAR만 복사
COPY --from=builder /app/build/libs/*SNAPSHOT.jar app.jar

# H2 DB, 로그 디렉토리 마운트 포인트
VOLUME /h2-data
VOLUME /logs

EXPOSE 8080

# Docker 실행 시 profile 설정
ENTRYPOINT ["java", "-jar", "app.jar", "--spring.profiles.active=docker"]

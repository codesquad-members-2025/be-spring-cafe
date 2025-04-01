#!/bin/bash

IMAGE_NAME=my-app
CONTAINER_NAME=my-app-container

echo "🚀 Docker 이미지 빌드 중..."
docker build -t $IMAGE_NAME .

echo "🧹 기존 컨테이너 중지 및 삭제..."
docker stop $CONTAINER_NAME 2>/dev/null || true
docker rm $CONTAINER_NAME 2>/dev/null || true

echo "🚢 새 컨테이너 실행 중..."
docker run -d \
  --name $CONTAINER_NAME \
  -p 80:8080 \
  $IMAGE_NAME

echo "✅ 배포 완료"

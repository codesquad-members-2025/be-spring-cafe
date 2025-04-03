#!/bin/bash

# ========== 설정 ==========
REPO_URL="https://github.com/dorkem/be-spring-cafe.git"
PROJECT_DIR="be-spring-cafe"
GIT_BRANCH="step6"

# ========== 1. 관리자 권한 확인 ==========
if [ "$(id -u)" -ne 0 ]; then
  echo "이 스크립트는 관리자(root) 권한이 필요합니다. 비밀번호를 입력하세요."
  exec sudo "$0" "$@"
fi
echo "[✓] 관리자 권한 확인 완료"

# ========== 2. Git, JDK 설치 ==========
echo "[1/4] Git, JDK 설치 중..."
apt-get update -y
apt-get install -y git openjdk-21-jdk unzip

# ========== 3. 스왑 메모리 생성 ==========
if ! swapon --noheadings | grep -q .; then
  echo "[2/4] 스왑 메모리 생성 중..."
  fallocate -l 2G /swapfile || dd if=/dev/zero of=/swapfile bs=1M count=2048
  chmod 600 /swapfile
  mkswap /swapfile
  swapon /swapfile
  echo '/swapfile none swap sw 0 0' | tee -a /etc/fstab
else
  echo "[2/4] 스왑 메모리 이미 존재함."
fi

# ========== 4. 프로젝트 클론 및 실행 ==========
echo "[3/4] 프로젝트 클론 중..."
cd /home/ubuntu || cd ~
rm -rf $PROJECT_DIR
git clone $REPO_URL $PROJECT_DIR
cd $PROJECT_DIR
git switch $GIT_BRANCH

echo "[4/4] 빌드 및 실행 중..."
chmod +x ./gradlew
./gradlew clean build -x test

JAR_FILE=$(find build/libs -name "*.jar" ! -name "*plain.jar" | head -n 1)
if [ -z "$JAR_FILE" ]; then
  echo "jar 파일을 찾지 못했습니다. 종료합니다."
  exit 1
fi

echo "jar 파일 실행 중..."
nohup java -jar $JAR_FILE > app.log 2>&1 &
echo "배포 완료! 로그는 app.log에서 확인 가능합니다."
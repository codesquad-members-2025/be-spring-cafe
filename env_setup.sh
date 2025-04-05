#!/bin/bash

set -euo pipefail
trap 'echo "Error occurred at line $LINENO. Exiting..." >&2' ERR

# 관리자 권한 확인
if [ "$(id -u)" -ne 0 ]; then
  echo "이 스크립트는 관리자(root) 권한이 필요합니다."

  # 관리자 권한 획득 시도 (비밀번호 입력 요청)
  exec sudo "$0" "$@"
fi
echo "관리자 권한이 확인되었습니다. 계속 진행합니다."

echo "환경 설정을 시작합니다..."

echo "Git 관련 설정 중..."
REPO_NAME=be-spring-cafe
sudo apt-get update -y > /dev/null
sudo apt-get install git openssh-client -y > /dev/null
if [ -d $REPO_NAME ]; then
    rm -rf $REPO_NAME > /dev/null
fi
echo "Git이 설치되었습니다."

echo "MySQL JDK 설치 중..."
sudo apt-get update -y > /dev/null
sudo apt-get install -y openjdk-21-jdk mysql-server > /dev/null
echo "MySQL JDK가 설치되었습니다."

echo "Swap 공간 설정 중..."
if ! swapon --noheadings | grep -q .; then
  echo "Swap 공간이 없습니다. 2GB swap 파일을 생성합니다..."
  sudo fallocate -l 2G /swapfile || sudo dd if=/dev/zero of=/swapfile bs=1M count=2048
  sudo chmod 600 /swapfile
  sudo mkswap /swapfile
  sudo swapon /swapfile
  echo "/swapfile none swap sw 0 0" | sudo tee -a /etc/fstab
  echo "Swap 공간이 생성되었습니다."
else
  echo "Swap 공간이 이미 존재합니다."
fi


echo "MySQL 서비스 상태 확인 중..."
if ! systemctl is-active --quiet mysql; then
  echo "MySQL 서비스가 실행 중이 아닙니다. 서비스를 시작합니다..."
  sudo systemctl start mysql

  sudo systemctl enable mysql
else
  echo "MySQL 서비스가 이미 실행 중입니다."
fi

echo "MySQL 데이터베이스 설정 중..."
DB_EXISTS=$(sudo mysql -N -s -e "SHOW DATABASES LIKE 'codestagram';")
if [ "$DB_EXISTS" != "codestagram" ]; then
  echo "MySQL 데이터베이스 설정을 진행합니다..."
  sudo mysql -e "CREATE DATABASE codestagram DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci; \
                 CREATE USER 'springuser'@'%' IDENTIFIED BY 'password'; \
                 GRANT ALL PRIVILEGES ON codestagram.* TO 'springuser'@'%'; \
                 FLUSH PRIVILEGES;"
  echo "MySQL 데이터베이스 설정이 완료되었습니다."
else
  echo "MySQL 데이터베이스가 이미 설정되어 있습니다. 스킵합니다."
fi

echo "환경 설정이 완료되었습니다."

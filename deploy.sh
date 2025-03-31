#!/bin/bash

# 관리자 권한 확인
if [ "$(id -u)" -ne 0 ]; then
  echo "이 스크립트는 관리자(root) 권한이 필요합니다."

  # 관리자 권한 획득 시도 (비밀번호 입력 요청)
  exec sudo "$0" "$@"
fi
echo "관리자 권한이 확인되었습니다. 계속 진행합니다."

# Install Git
REPO_PATH=https://github.com/gladhee/be-spring-cafe.git
sudo apt-get install git openssh-client -y > /dev/null
if [ -d "be-spring-cafe" ]; then
    rm -rf be-spring-cafe
fi
echo "Git이 설치되었습니다."
git clone $REPO_PATH be-spring-cafe > /dev/null
echo "Git 저장소를 복제했습니다."
cd be-spring-cafe/


# Install Docker
for pkg in docker.io docker-doc docker-compose docker-compose-v2 podman-docker containerd runc; do sudo apt-get remove $pkg; done > /dev/null
sudo apt-get update -y > /dev/null
sudo apt-get install ca-certificates curl -y > /dev/null
sudo install -m 0755 -d /etc/apt/keyrings > /dev/null
sudo curl -fsSL https://download.docker.com/linux/ubuntu/gpg -o /etc/apt/keyrings/docker.asc > /dev/null
sudo chmod a+r /etc/apt/keyrings/docker.asc
echo \
  "deb [arch=$(dpkg --print-architecture) signed-by=/etc/apt/keyrings/docker.asc] https://download.docker.com/linux/ubuntu \
  $(. /etc/os-release && echo "$VERSION_CODENAME") stable" | \
  sudo tee /etc/apt/sources.list.d/docker.list > /dev/null
sudo apt-get update -y > /dev/null
sudo apt-get install make docker-ce docker-ce-cli containerd.io docker-buildx-plugin docker-compose-plugin -y > /dev/null
sudo usermod -aG docker $USER
echo "Docker가 설치되었습니다."

# Docker 이미지 빌드
echo "Docker 이미지 빌드 중..."
docker build -t myapp:latest .

# 기존 컨테이너 중지 및 제거 (이미 존재한다면)
if [ "$(docker ps -aq -f name=myapp_container)" ]; then
  echo "기존 컨테이너 중지 및 제거 중..."
  docker stop myapp_container > /dev/null
  docker rm myapp_container > /dev/null
fi

# 새 컨테이너 실행
echo "새 컨테이너 실행 중..."
docker run -d --name myapp_container -p 8080:8080 myapp:latest

echo "완료!"

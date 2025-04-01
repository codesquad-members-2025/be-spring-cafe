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
git switch feat/ajax


# Install MySQL JDK
sudo apt-get update -y
sudo apt-get install -y openjdk-21-jdk unzip mysql-server

# Create Swap Memory
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

# gradlew 파일 존재 여부 확인
if [ ! -f "./gradlew" ]; then
  echo "gradlew 파일을 찾을 수 없습니다. 스크립트를 종료합니다."
  exit 1
fi

chmod +x ./gradlew

echo "애플리케이션 빌드 중..."
./gradlew clean build -x test
echo "애플리케이션 빌드 완료."

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

# Search for the built jar file
JAR_FILE=$(find build/libs -type f -name "*.jar" ! -name "*plain.jar" | head -n 1)
if [ -z "$JAR_FILE" ]; then
  echo "빌드된 jar 파일을 찾을 수 없습니다."
  exit 1
fi

# 애플리케이션 실행
echo "애플리케이션 실행 중..."
nohup java -jar $JAR_FILE > app.log 2>&1 &
echo "완료! 애플리케이션이 백그라운드에서 실행 중입니다."

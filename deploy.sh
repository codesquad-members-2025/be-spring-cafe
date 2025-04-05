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

echo "원격 저장소에서 애플리케이션을 다운로드 합니다..."
REPO_PATH=https://github.com/gladhee/be-spring-cafe.git
if [ -d "be-spring-cafe" ]; then
    rm -rf be-spring-cafe
fi
git clone $REPO_PATH be-spring-cafe > /dev/null
echo "Git 저장소를 복제했습니다."
cd be-spring-cafe/ > /dev/null
git switch feat/pagination > /dev/null

# gradlew 파일 존재 여부 확인
if [ ! -f "./gradlew" ]; then
  echo "gradlew 파일을 찾을 수 없습니다. 스크립트를 종료합니다."
  exit 1
fi

chmod +x ./gradlew

echo "애플리케이션 빌드 중..."
./gradlew clean build -x test
echo "애플리케이션 빌드 완료."

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

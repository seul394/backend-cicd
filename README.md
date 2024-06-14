# Blog-BE
5기 블로그 프로젝트 Back

## Docker로 Spring Boot 어플리케이션 실행하기

```bash
# 프로젝트 최상단 폴더로 이동
$ cd blog

# JAR file 생성
$ ./gradlew build

# Docker 빌드
$  docker build -t myapp:latest .

# Docker 실행
$ docker run -p 8080:8080 myapp:latest   
```
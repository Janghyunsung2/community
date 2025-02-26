# 1. OpenJDK 17 기반 이미지 사용
FROM openjdk:21-jdk-slim

# 2. 작업 디렉토리 설정
WORKDIR /app

# 3. JAR 파일 복사
COPY target/*.jar app.jar

# 4. 실행 시 환경 변수 적용 후 JAR 실행
CMD ["sh", "-c", "java -Dspring.datasource.url=$DB_URL \
                   -Dspring.datasource.username=$DB_USERNAME \
                   -Dspring.datasource.password=$DB_PASSWORD \
                   -Dspring.mail.host=$SPRING_MAIL_HOST \
                   -Dspring.mail.port=$SPRING_MAIL_PORT \
                   -Dspring.mail.username=$SPRING_MAIL_USERNAME \
                   -Dspring.mail.password=$SPRING_MAIL_PASSWORD \
                   -jar app.jar"]

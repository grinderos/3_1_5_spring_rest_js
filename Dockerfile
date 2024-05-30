FROM openjdk:17-jdk-alpine
COPY /target/rest_js-1.1.war /app/rest_js-1.1.war
WORKDIR /app
ENTRYPOINT ["java", "-jar", "/app/rest_js-1.1.war"]
FROM openjdk:17-jdk-alpine
COPY /target/3_1_5_spring_rest_js-1.1.war /app/3_1_5_spring_rest_js-1.1.war
WORKDIR /app
ENTRYPOINT ["java", "-jar", "/app/3_1_5_spring_rest_js-1.1.war"]
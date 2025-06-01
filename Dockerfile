FROM openjdk:17-jdk-slim
WORKDIR /app
COPY target/splitwise-api-1.0.0.jar .

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "splitwise-api-1.0.0.jar"]

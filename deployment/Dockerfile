# deployment/Dockerfile
FROM eclipse-temurin:21-jdk

WORKDIR /app

COPY applications/app-service/build/libs/franchise-api-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 8080

CMD ["java", "-jar", "app.jar"]
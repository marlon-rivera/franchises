# Stage 1: Build
FROM eclipse-temurin:21-jdk AS builder

WORKDIR /app
COPY . .
RUN chmod +x gradlew && ./gradlew :app-service:bootJar --no-daemon --no-configuration-cache -x test -x validateStructure

# Stage 2: Run
FROM eclipse-temurin:21-jre

WORKDIR /app
COPY --from=builder /app/applications/app-service/build/libs/franchise-api-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 8080

CMD ["java", "-jar", "app.jar"]
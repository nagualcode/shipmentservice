FROM maven:3.9.9-eclipse-temurin-22-alpine AS build
WORKDIR /app
COPY . .
RUN mvn clean package -DskipTests

FROM eclipse-temurin:22-jdk-alpine
WORKDIR /app
COPY --from=build /app/target/notificationservice-0.0.1-SNAPSHOT.jar notificationservice.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "notificationservice.jar"]

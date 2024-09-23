FROM openjdk:22-jdk-slim
VOLUME /tmp
COPY target/userservice-0.0.1-SNAPSHOT.jar userservice.jar
ENTRYPOINT ["java", "-jar", "/userservice.jar"]

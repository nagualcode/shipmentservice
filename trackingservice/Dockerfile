FROM openjdk:22-jdk-slim
VOLUME /tmp
COPY target/trackingservice-0.0.1-SNAPSHOT.jar trackingservice.jar
ENTRYPOINT ["java", "-jar", "/trackingservice.jar"]

FROM openjdk:17-jdk-slim

COPY target/task-tracker-0.0.1-SNAPSHOT.jar task-tracker-0.0.1-SNAPSHOT.jar

ENTRYPOINT ["java", "-jar", "task-tracker-0.0.1-SNAPSHOT.jar"]

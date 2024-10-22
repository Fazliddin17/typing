FROM openjdk:17
WORKDIR /app
COPY target/botmaker-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8093gi
ENTRYPOINT ["java", "-jar", "app.jar"]

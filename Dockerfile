FROM openjdk:17
ADD target/botmaker-0.0.1-SNAPSHOT.jar app.jar
VOLUME /simple.app
EXPOSE 8093
ENTRYPOINT ["java", "-jar", "/app.jar"]
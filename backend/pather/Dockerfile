FROM openjdk:8-jdk-alpine
VOLUME /tmp
ADD ./target/pather-1.0-SNAPSHOT-exec.jar app.jar
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom --debug","-jar","/app.jar"]
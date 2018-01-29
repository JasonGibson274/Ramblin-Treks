FROM openjdk:8-jdk-alpine
VOLUME /tmp
ADD ./target/data-svc-1.0-SNAPSHOT-exec.jar app.jar
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/app.jar"]
#FROM java:8
#FROM openjdk:8-jdk-alpine
FROM openjdk:11
ARG JAR_FILE
COPY ${JAR_FILE} app.jar
EXPOSE 9001
ENTRYPOINT ["java","-jar","/app.jar"]
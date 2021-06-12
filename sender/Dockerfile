FROM cirepo/java-11-openjdk:11.0.2-alpine-3.9
ARG JAR_FILE="target/demo-0.0.1-SNAPSHOT.jar"
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java","-jar","app.jar"]

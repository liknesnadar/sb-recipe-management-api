FROM openjdk:19-alpine
ARG JAR_FILE=target/recipe-management-0.0.1-SNAPSHOT.jar
COPY ./target/recipe-management-0.0.1-SNAPSHOT.jar recipe-management-0.0.1-SNAPSHOT.jar
ENTRYPOINT ["java","-jar","/recipe-management-0.0.1-SNAPSHOT.jar"]
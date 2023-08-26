FROM openjdk:8-jdk-alpine
EXPOSE 8080/tcp
COPY target/springboot-apirest-0.0.1-SNAPSHOT.jar springboot-apirest-0.0.1-SNAPSHOT.jar
ENTRYPOINT ["java","-jar","/springboot-apirest-0.0.1-SNAPSHOT.jar"]
FROM openjdk:17
EXPOSE 8081
ADD target/AuthService-1.0-SNAPSHOT.jar AuthService.jar
ADD src/main/resources/application.properties application.properties
ENTRYPOINT ["java","-jar","/AuthService.jar"]
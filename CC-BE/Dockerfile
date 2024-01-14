FROM openjdk:17
EXPOSE 8090
ADD target/CC-BE-0.0.1-SNAPSHOT.jar CC-BE.jar
ADD src/main/resources/application.properties application.properties
ENTRYPOINT ["java","-jar","/CC-BE.jar"]
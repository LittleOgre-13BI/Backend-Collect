FROM openjdk:8
ADD target/backend-collect-0.0.1-SNAPSHOT.jar ~/backend-collect-0.0.1-SNAPSHOT.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","~/backend-collect-0.0.1-SNAPSHOT.jar", "--spring.profiles.active=prod"]




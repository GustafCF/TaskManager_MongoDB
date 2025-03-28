
FROM maven:3.9-eclipse-temurin-21 AS build
COPY src /home/app/src
RUN rm -rf /home/app/src/main/resource/application.yml
COPY pom.xml /home/app
RUN mvn -f /home/app/pom.xml clean package -Dmaven.test.skip=true

#Package Stage
FROM openjdk:21-oracle
COPY --from=build /home/app/target/task_mongoDB-0.0.1-SNAPSHOT.jar /usr/local/lib/task_mongoDB.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/usr/local/lib/taskmanager.jar"]
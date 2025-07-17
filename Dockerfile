# Use a base image with JDK
FROM maven:3.9.10-amazoncorretto-21

COPY src /home/app/src
COPY pom.xml /home/app
RUN mvn -f /home/app/pom.xml clean package
EXPOSE 8080

ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} app.jar

# Run the jar
ENTRYPOINT ["java","-jar","/app.jar"]
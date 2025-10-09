# Use Java 17 base image
FROM eclipse-temurin:17-jdk-alpine

# Create a directory for the app
WORKDIR /app

# Copy the built JAR file into the container
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} app.jar

# Create logs directory
RUN mkdir /logs

# Run the application
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
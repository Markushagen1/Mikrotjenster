# Start with an OpenJDK base image
FROM openjdk:17-jdk-slim

# Set the working directory inside the container
WORKDIR /app

# Copy the jar file from your Maven build
COPY target/Gateway-0.0.1-SNAPSHOT.jar app.jar

# Expose the port the gateway will run on
EXPOSE 8000

# Define the command to run the application
ENTRYPOINT ["java", "-jar", "gateway.jar"]

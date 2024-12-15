# Base image
FROM openjdk:17-jdk-slim

# Set working directory inside container
WORKDIR /app

# Copy the JAR file
COPY target/Recorder-0.0.1-SNAPSHOT.jar app.jar


# Expose port
EXPOSE 8081

# Set environment variables for runtime configuration
ENV JWT_SECRET_KEY=${JWT_SECRET_KEY} \
    DB_URL=${MATCHING_MANAGER_DB_URL} \
    DB_USERNAME=${DB_USERNAME} \
    DB_PASSWORD=${DB_PASSWORD} \
    RABBITMQ_HOST=${RABBITMQ_HOST} \
    RABBITMQ_PORT=${RABBITMQ_PORT} \
    RABBITMQ_USERNAME=${RABBITMQ_USERNAME} \
    RABBITMQ_PASSWORD=${RABBITMQ_PASSWORD}

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]

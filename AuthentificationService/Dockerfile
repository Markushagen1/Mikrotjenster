# Base image
FROM openjdk:17-jdk-slim

# Set working directory inside container
WORKDIR /app

# Copy the JAR file
COPY target/AuthentificationService-0.0.1-SNAPSHOT.jar app.jar

# Expose port
EXPOSE 8083

# Set environment variables for runtime configuration
ENV JWT_SECRET_KEY=${JWT_SECRET_KEY} \
    DB_URL=${AUTH_DB_URL} \
    DB_USERNAME=${DB_USERNAME} \
    DB_PASSWORD=${DB_PASSWORD}

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]

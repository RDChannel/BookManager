# Use a lightweight base image with Java 17 (or your project's version)
FROM eclipse-temurin:17-jdk-alpine

# Set environment variables
ENV APP_HOME=/app

# Create app directory
WORKDIR $APP_HOME

# Copy built JAR into the container
COPY target/manager-0.0.1-SNAPSHOT.jar app.jar

# Expose port if needed
EXPOSE 8080

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]
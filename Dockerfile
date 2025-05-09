# Use a minimal OpenJDK image
FROM openjdk:21-jdk-slim

# Set working directory in container
WORKDIR /app

# Copy your JAR into the container
COPY target/*.jar app.jar

# Expose port
EXPOSE 8081

# Start the app
ENTRYPOINT ["java", "-jar", "app.jar"]
FROM openjdk:17-jdk-slim

# Set working directory inside container
WORKDIR /app

# Copy the JAR file into the container
COPY target/*.jar app.jar

# Expose port 8081 (make sure this matches your Spring Boot port)
EXPOSE 8085

# Run the JAR
ENTRYPOINT ["java", "-jar", "app.jar"]

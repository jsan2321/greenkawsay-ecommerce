FROM eclipse-temurin:17-jdk-alpine

WORKDIR /app

# Copy the POM and download dependencies
COPY pom.xml .
RUN ./mvnw dependency:go-offline -B

# Copy the source code
COPY src ./src

# Package the application
RUN ./mvnw clean package -DskipTests

# Expose port
EXPOSE 8081

# Run the application
ENTRYPOINT ["java", "-jar", "target/backend-0.0.1-SNAPSHOT.jar"]
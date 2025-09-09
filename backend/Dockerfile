# ========================
# Stage 1: Build the app
# ========================
FROM maven:3.9.6-eclipse-temurin-17 AS build

WORKDIR /app

COPY pom.xml .
COPY src ./src

# Download dependencies first for better caching
RUN mvn dependency:go-offline

# Build the JAR file
RUN mvn clean package -DskipTests

# ========================
# Stage 2: Run the app
# ========================
FROM eclipse-temurin:17-jdk

WORKDIR /app

# Copy the generated JAR from the build stage
COPY --from=build /app/target/*.jar app.jar

# Expose port 8080
EXPOSE 8080

# Use Render's PORT if available, otherwise default to 8080
ENV PORT=8080

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]

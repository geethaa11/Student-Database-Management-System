# ---- Stage 1: Build ----
FROM maven:3.9.4-eclipse-temurin-17 AS build
WORKDIR /app

# Copy the backend code
COPY web-backend/pom.xml .
COPY web-backend/src ./src

# Copy the frontend code into the Spring Boot static resources folder
COPY web-frontend ./src/main/resources/static

# Build the Spring Boot executable jar
RUN mvn clean package -DskipTests

# ---- Stage 2: Run ----
FROM eclipse-temurin:17-jre
WORKDIR /app

# Copy the built jar
COPY --from=build /app/target/web-backend-1.0.0.jar ./app.jar

# Expose standard port
EXPOSE 8080

# The volume for the SQLite database
# Note: Users should mount a persistent volume to /data in production
VOLUME /data

# Define the DB_URL to point to the volume
ENV DB_URL=jdbc:sqlite:/data/student_manager.db

ENTRYPOINT ["java", "-jar", "app.jar"]

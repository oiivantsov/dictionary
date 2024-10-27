# Use Maven to build the JAR
FROM maven AS build

# Set the working directory inside the container
WORKDIR /app

# Copy all project files to the container
COPY . .

# Build the project
RUN mvn clean package -DskipTests

# Create a new image for running the app
FROM openjdk:17-jdk-slim

# Set the working directory inside the container
WORKDIR /app

# Copy the JAR file from the previous build stage to the new stage
COPY --from=build /app/target/*.jar app.jar

# Expose the port that your app runs on
EXPOSE 8080

# Run the JAR file
ENTRYPOINT ["java", "-jar", "app.jar"]

# Use Maven to build the JAR
FROM maven:3.8.6-openjdk-17 AS build

# Set the working directory inside the container
WORKDIR /app

# Copy all project files to the container
COPY . .

# Build the project
RUN mvn clean package

# Expose the port that your app runs on
EXPOSE 8080

# Run the JAR file
ENTRYPOINT ["java", "-jar", "app.jar"]

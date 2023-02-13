# Use an openjdk 17 base image
FROM openjdk:17-alpine as builder

# Set the working directory in the container
WORKDIR /app

# Copy the spring boot microservice application source code to the container
COPY ./ /app/discovery/

# Build the spring boot microservice application
RUN cd /app/discovery && ./mvnw clean package -DskipTests



# Use a new stage for the final image
FROM openjdk:17-alpine

# Set the working directory in the container
WORKDIR /app

# Copy the built spring boot jar file from the builder stage
COPY --from=builder /app/discovery/target/discovery-0.0.1-SNAPSHOT.jar /app/discovery-0.0.1-SNAPSHOT.jar


# Run the spring boot jar file
CMD ["java", "-jar", "/app/discovery-0.0.1-SNAPSHOT.jar"]

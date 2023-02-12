# Use an openjdk 17 base image
FROM openjdk:17-alpine as builder

# Set the working directory in the container
WORKDIR /app

RUN mkdir -p /app/user/.m2/repository/

# Copy the built jar file from the flatrock_common image
COPY --from=flatrock_common /app/common/target/common-1.0-SNAPSHOT.jar /app/user/.m2/repository/com/flatrock/common/1.0-SNAPSHOT/common-1.0-SNAPSHOT.jar

# Set the environment variable for the classpath
ENV CLASSPATH /app/user/.m2/repository/

# Copy the spring boot microservice application source code to the container
COPY ./ /app/user/


# Build the spring boot microservice application
RUN cd /app/user && ./mvnw clean package -DskipTests -Dmaven.repo.local=$CLASSPATH



# Use a new stage for the final image
FROM openjdk:17-alpine

# Set the working directory in the container
WORKDIR /app

# Copy the built spring boot jar file from the builder stage
COPY --from=builder /app/user/target/user-0.0.1-SNAPSHOT.jar /app/user-0.0.1-SNAPSHOT.jar


# Run the spring boot jar file
CMD ["java", "-jar", "/app/user-0.0.1-SNAPSHOT.jar"]

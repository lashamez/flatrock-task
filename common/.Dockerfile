# Use an openjdk 17 base image
FROM openjdk:17-alpine as builder

# Set the working directory in the container
WORKDIR /app/

# Copy the internal commons module source code to the container
COPY ./ /app/common

# Build the internal commons module
RUN cd /app/common && ./mvnw clean package

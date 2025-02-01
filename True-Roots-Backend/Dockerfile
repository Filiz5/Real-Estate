FROM maven:3.8.5-openjdk-17 AS build



# Set the working directory
WORKDIR /app

# Copy the pom.xml and source code
COPY pom.xml .
COPY src ./src

# Install dependencies including Maven
RUN mvn clean package -DskipTests

FROM openjdk:21-jdk

# Copy the JAR file into the container
# Copy the JAR file into the container
COPY --from=build /app/target/*.jar app.jar

# Expose the port your Spring Boot app will run on
EXPOSE 8090

ENTRYPOINT ["java", "-jar", "app.jar", "--server.port=8090"]
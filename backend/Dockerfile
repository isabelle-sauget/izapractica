# Dockerfile for Spring Boot backend
FROM --platform=linux/amd64 eclipse-temurin:21-jdk-alpine AS build
WORKDIR /app
COPY . .
#RUN file mvnw
# RUN head -5 mvnw | od -c
RUN ./mvnw clean package -DskipTests

FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]

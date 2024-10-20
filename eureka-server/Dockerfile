FROM gradle:7.6.2-jdk17-alpine as builder
WORKDIR /app
COPY . /app
RUN gradle clean build

FROM openjdk:17-jdk-slim
WORKDIR /app
COPY --from=builder /app/build/libs/*.jar /app/app.jar
CMD ["java", "-jar", "/app/app.jar"]
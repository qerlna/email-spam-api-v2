# Сборка
FROM maven:3.8.5-openjdk-17 AS build
COPY . .
RUN mvn clean package -DskipTests

# Запуск
FROM openjdk:17-jdk-slim
COPY --from=build /target/email-spam-api-1.0.0.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","app.jar"]
# Используем официальный образ Java как базовый
FROM openjdk:17-jdk-slim

# Устанавливаем рабочую директорию
WORKDIR /app

# Копируем файл JAR вашего приложения в контейнер
COPY build/libs/eureka-server-0.0.1-SNAPSHOT.jar myapp.jar

# Открываем порт, на котором будет работать приложение
EXPOSE 8761

# Команда для запуска приложения
ENTRYPOINT ["java", "-jar", "myapp.jar"]
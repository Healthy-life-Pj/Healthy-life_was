FROM openjdk:17-jdk-slim

WORKDIR /app

COPY . .

WORKDIR /app/healthy_life_was

RUN chmod +x gradlew
RUN ./gradlew build

CMD ["java", "-jar", "build/libs/*.jar"]
FROM eclipse-temurin:17-jdk

WORKDIR /app

COPY .. .

WORKDIR /app

RUN chmod +x gradlew
RUN ./gradlew build -x test

CMD ["java", "-jar", "build/libs/healthy_life_was-0.0.1-SNAPSHOT.jar"]
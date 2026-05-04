FROM eclipse-temurin:17-jdk

WORKDIR /app

COPY .. .

WORKDIR /app/healthy_life_was

RUN chmod +x gradlew
RUN ./gradlew build

CMD sh -c "java -jar build/libs/*.jar"
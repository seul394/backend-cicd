FROM openjdk:17-jdk-slim as build
WORKDIR /app
COPY . .
RUN ./gradlew build --no-daemon


FROM openjdk:17-jdk-slim
VOLUME /tmp
COPY --from=build /app/build/libs/*SNAPSHOT.jar app.jar
ENTRYPOINT ["java", "-jar", "/app.jar"]

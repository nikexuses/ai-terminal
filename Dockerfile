FROM maven:3.9.9-eclipse-temurin-17 AS build
WORKDIR /app

COPY demo ./demo
WORKDIR /app/demo

RUN mvn clean package -DskipTests

FROM eclipse-temurin:17-jdk-jammy
WORKDIR /app

COPY --from=build /app/demo/target/demo-0.0.1-SNAPSHOT.jar app.jar

ENTRYPOINT ["java", "-jar", "app.jar"]
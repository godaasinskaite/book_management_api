FROM maven:3.8.5-openjdk-17 AS build

WORKDIR /app

COPY pom.xml .
COPY src ./src

RUN mvn package -DskipTests

FROM openjdk:17-jdk-slim

WORKDIR /app

COPY --from=build /app/target/Book-Management.jar /app/Book-Management.jar

EXPOSE 8080

CMD ["java", "-jar", "/app/Book-Management.jar"]
FROM maven:3.8.5-openjdk-17 AS build
COPY . .
RUN mvn clean package -DskipTests

FROM eclipse-temurin:21
COPY --from=build /target/sewjo-0.0.1-SNAPSHOT.jar sewjo.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "sewjo.jar"]
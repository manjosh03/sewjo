FROM maven:3.8.5-openjdk-17 AS build
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests


FROM amazoncorretto:22
COPY src/main/resources/static/serviceAccountKey.json /app/serviceAccountKey.json
COPY --from=build /target/sewjo-0.0.1-SNAPSHOT.jar sewjo.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "sewjo.jar"]
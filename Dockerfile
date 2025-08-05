FROM jelastic/maven:3.9.5-openjdk-21 AS build
COPY . .
RUN mvn clean package -DskipTests

FROM openjdk:21
COPY --from=build /target/Booking-SaaS-0.0.1-SNAPSHOT.jar build.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "build.jar"]
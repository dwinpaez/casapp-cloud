FROM eclipse-temurin:21-jdk-alpine as mvn-builder

WORKDIR /app/msvc-bookings

COPY ./pom.xml /app
COPY ./msvc-bookings/.mvn ./.mvn
COPY ./msvc-bookings/mvnw .
COPY ./msvc-bookings/pom.xml .

RUN ./mvnw clean package -Dmaven.test.skip -Dmaven.main.skip -Dspring-boot.repackage.skip && rm -r ./target/

COPY ./msvc-bookings/src ./src

RUN ./mvnw clean package -DskipTests

FROM eclipse-temurin:21-jre-alpine

WORKDIR /app
RUN mkdir ./logs
COPY --from=mvn-builder /app/msvc-bookings/target/msvc-bookings-0.0.1-SNAPSHOT.jar .
ENV PORT 8082
EXPOSE $PORT
ENV SPRING_PROFILE=prod

CMD ["java", "-Dspring.profiles.active=${SPRING_PROFILE}", "-jar", "msvc-bookings-0.0.1-SNAPSHOT.jar"]
FROM eclipse-temurin:21-jdk-alpine as mvn-builder

WORKDIR /app/msvc-gateway

COPY ./pom.xml /app
COPY ./msvc-gateway/.mvn ./.mvn
COPY ./msvc-gateway/mvnw .
COPY ./msvc-gateway/pom.xml .

RUN ./mvnw clean package -Dmaven.test.skip -Dmaven.main.skip -Dspring-boot.repackage.skip && rm -r ./target/

COPY ./msvc-gateway/src ./src

RUN ./mvnw clean package -DskipTests

FROM eclipse-temurin:21-jdk-alpine

WORKDIR /app
RUN mkdir ./logs
COPY --from=mvn-builder /app/msvc-gateway/target/msvc-gateway-0.0.1-SNAPSHOT.jar .
ENV PORT 8081
EXPOSE $PORT
# environment variable with default value
ENV SPRING_PROFILE=prod

CMD ["java", "-Dspring.profiles.active=${SPRING_PROFILE}", "-jar", "msvc-gateway-0.0.1-SNAPSHOT.jar"]
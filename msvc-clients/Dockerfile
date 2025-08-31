FROM eclipse-temurin:21-jdk-alpine as mvn-builder

WORKDIR /app/msvc-clients

COPY ./pom.xml /app
COPY ./msvc-clients/.mvn ./.mvn
COPY ./msvc-clients/mvnw .
COPY ./msvc-clients/pom.xml .

RUN ./mvnw clean package -Dmaven.test.skip -Dmaven.main.skip -Dspring-boot.repackage.skip && rm -r ./target/

COPY ./msvc-clients/src ./src

RUN ./mvnw clean package -DskipTests

FROM eclipse-temurin:21-jre-alpine

WORKDIR /app
RUN mkdir ./logs
COPY --from=mvn-builder /app/msvc-clients/target/msvc-clients-0.0.1-SNAPSHOT.jar .
ENV PORT 8081
EXPOSE $PORT
# environment variable with default value
ENV SPRING_PROFILE=prod,k8s

CMD ["java", "-Dspring.profiles.active=${SPRING_PROFILE}", "-jar", "msvc-clients-0.0.1-SNAPSHOT.jar"]
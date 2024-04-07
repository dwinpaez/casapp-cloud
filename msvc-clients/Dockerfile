FROM openjdk:17-jdk-alpine as mvn-builder

WORKDIR /app/msvc-clients

COPY ./pom.xml /app
COPY ./msvc-clients/.mvn ./.mvn
COPY ./msvc-clients/mvnw .
COPY ./msvc-clients/pom.xml .

RUN ./mvnw clean package -Dmaven.test.skip -Dmaven.main.skip -Dspring-boot.repackage.skip && rm -r ./target/

COPY ./msvc-clients/src ./src

RUN ./mvnw clean package -DskipTests

FROM openjdk:17-jdk-alpine

WORKDIR /app
RUN mkdir ./logs
COPY --from=mvn-builder /app/msvc-clients/target/msvc-clients-0.0.1-SNAPSHOT.jar .
EXPOSE 8081

CMD ["java", "-jar", "msvc-clients-0.0.1-SNAPSHOT.jar"]
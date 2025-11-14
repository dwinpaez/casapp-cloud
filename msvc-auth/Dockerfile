FROM eclipse-temurin:21-jdk-alpine as mvn-builder

WORKDIR /app/msvc-auth

COPY ./pom.xml /app
COPY ./msvc-auth/.mvn ./.mvn
COPY ./msvc-auth/mvnw .
COPY ./msvc-auth/pom.xml .

RUN ./mvnw clean package -Dmaven.test.skip -Dmaven.main.skip -Dspring-boot.repackage.skip && rm -r ./target/

COPY ./msvc-auth/src ./src

RUN ./mvnw clean package -DskipTests

FROM eclipse-temurin:21-jre-alpine

WORKDIR /app
RUN mkdir ./logs
COPY --from=mvn-builder /app/msvc-auth/target/msvc-auth-0.0.1-SNAPSHOT.jar .
ENV PORT 8081
EXPOSE $PORT
# environment variable with default value
ENV SPRING_PROFILE=prod,k8s

CMD ["java", "-Dspring.profiles.active=${SPRING_PROFILE}", "-jar", "msvc-auth-0.0.1-SNAPSHOT.jar"]
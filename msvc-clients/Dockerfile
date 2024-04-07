FROM openjdk:17.0.1

WORKDIR /appp

COPY ./target/msvc-clients-0.0.1-SNAPSHOT.jar .

EXPOSE 8081

ENTRYPOINT ["java", "-jar", "msvc-clients-0.0.1-SNAPSHOT.jar"]
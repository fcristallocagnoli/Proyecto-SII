FROM maven:3.8.5-eclipse-temurin-17 AS builder

COPY pom.xml ./
RUN mvn -e -B dependency:resolve

COPY src ./src
RUN mvn -e -B package -Dmaven.test.skip=true

# ----------------------------------------------------------------
FROM openjdk:17-jdk-slim AS runtime

WORKDIR /back

RUN apt update && apt install -y tini

COPY --from=builder target/ms_corrector-0.0.1-SNAPSHOT.jar /app.jar

EXPOSE 8081

ENTRYPOINT [ "tini", "--" ]

# ----------------------------------------------------------------
FROM runtime AS compose-version

# Por defecto se presupone 'db-service' como nombre del servicio de base de datos
# Este valor se puede cambiar en tiempo de ejecución
ENV SPRING_DATASOURCE_URL jdbc:h2:tcp://db-service/ms_database

CMD ["java", "-Dspring.profiles.active=docker-compose", "-jar", "/app.jar"]

# ----------------------------------------------------------------
FROM runtime AS local-version

CMD ["java", "-Dspring.profiles.active=dev", "-jar", "/app.jar"]
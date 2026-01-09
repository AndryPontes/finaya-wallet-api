FROM eclipse-temurin:21-jdk-alpine AS builder

WORKDIR /app

COPY gradlew .
COPY gradle ./gradle

RUN chmod +x gradlew

COPY build.gradle.kts .
COPY settings.gradle.kts .

RUN ./gradlew --no-daemon dependencies

COPY src ./src

RUN ./gradlew --no-daemon clean
RUN ./gradlew --no-daemon bootJar

RUN rm -rf /app/.gradle \
    && rm -rf /app/build/tmp \
    && rm -rf /app/build/classes

FROM eclipse-temurin:21-jre-alpine

ARG USERNAME=finaya-admin
ARG UID=10001
RUN addgroup -S "$USERNAME" && adduser -S -G "$USERNAME" -u "$UID" "$USERNAME"

WORKDIR /app

COPY --from=builder /app/build/libs/*.jar app.jar

RUN mkdir -p /app/logs && \
    chown -R "$USERNAME":"$USERNAME" /app/logs && \
    chown "$USERNAME":"$USERNAME" /app/app.jar

USER $USERNAME

EXPOSE 8080

ENTRYPOINT ["java","-jar","/app/app.jar"]
FROM eclipse-temurin:21-jdk AS builder

WORKDIR /app

COPY gradlew ./
COPY gradle ./gradle
COPY build.gradle.kts settings.gradle.kts ./

RUN chmod +x ./gradlew

COPY src ./src

RUN --mount=type=cache,target=/root/.gradle \
    ./gradlew bootJar -x test --no-daemon

FROM eclipse-temurin:21-jre

WORKDIR /app

ENV JAVA_OPTS=""

EXPOSE 8080

COPY --from=builder /app/build/libs/*.jar /app/app.jar

ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar /app/app.jar"]
FROM gradle:7-jdk11 AS builder
USER root
WORKDIR /builder
ADD . /builder
RUN gradle shadowJar

FROM openjdk:11.0-jre-stretch
WORKDIR /app
COPY --from=builder /builder/build/libs/bot.jar .
CMD ["java", "-jar", "bot.jar"]

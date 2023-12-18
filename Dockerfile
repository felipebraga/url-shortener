FROM --platform=$BUILDPLATFORM eclipse-temurin:21.0.1_12-jre-jammy AS BUILD
LABEL authors="Felipe Braga"

ARG BUILDPLATFORM=linux/amd64
ARG TARGETPLATFORM=$BUILDPLATFORM

WORKDIR /application
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} url-shortener.jar
RUN java -Djarmode=layertools -jar url-shortener.jar extract
RUN rm url-shortener.jar

FROM --platform=$TARGETPLATFORM eclipse-temurin:21.0.1_12-jre-jammy AS RUNTIME

RUN apt update -y && apt upgrade -y && apt-get autoremove

WORKDIR /application
COPY --from=BUILD application/dependencies ./
COPY --from=BUILD application/spring-boot-loader ./
COPY --from=BUILD application/snapshot-dependencies/ ./
COPY --from=BUILD application/application ./

RUN useradd -U shortener

COPY entrypoint.sh /entrypoint.sh
RUN chmod +x /entrypoint.sh

USER shortener

EXPOSE 8080

ENTRYPOINT ["/entrypoint.sh"]

HEALTHCHECK --timeout=3s \
              CMD curl http://localhost:8080/actuator/health \
              | grep UP || exit 1

FROM --platform=$BUILDPLATFORM amazoncorretto:21.0.1-al2023-headless AS BUILD
LABEL authors="Felipe Braga"

ARG BUILDPLATFORM=linux/amd64

WORKDIR application
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} url-shortener.jar
RUN java -Djarmode=layertools -jar url-shortener.jar extract
RUN rm url-shortener.jar

FROM --platform=$BUILDPLATFORM amazoncorretto:21.0.1-al2023-headless AS RUNTIME

RUN yum install -y shadow-utils
RUN yum update -y --security \
    yum clean all

WORKDIR application
COPY --from=BUILD application/dependencies ./
COPY --from=BUILD application/spring-boot-loader ./
COPY --from=BUILD application/snapshot-dependencies/ ./
COPY --from=BUILD application/application ./

RUN adduser -U shortener

COPY ../../entrypoint.sh /entrypoint.sh
RUN chmod +x /entrypoint.sh

USER shortener

EXPOSE 8080

ENTRYPOINT ["/entrypoint.sh"]

HEALTHCHECK --timeout=3s \
              CMD curl http://localhost:8080/actuator/health \
              | grep UP || exit 1

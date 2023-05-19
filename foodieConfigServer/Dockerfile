#
# Build stage
#
FROM eclipse-temurin:17-jdk-alpine
VOLUME /tmp
RUN apk update
RUN apk add curl
COPY target/*.jar foodieConfigServer-0.0.1-SNAPSHOT.jar
EXPOSE 8888
ENTRYPOINT ["java","-jar","/foodieConfigServer-0.0.1-SNAPSHOT.jar"]
HEALTHCHECK --interval=12s --timeout=12s --start-period=30s CMD curl --fail http://localhost:8888/health/check

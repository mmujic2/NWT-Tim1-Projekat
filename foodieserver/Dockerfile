#
# Build stage
#
FROM eclipse-temurin:17-jdk-alpine
VOLUME /tmp
RUN apk update
RUN apk add curl
COPY target/*.jar app.jar
EXPOSE 8761
ENTRYPOINT ["java","-jar","/app.jar"]
HEALTHCHECK --interval=12s --timeout=12s --start-period=30s CMD curl --fail http://localhost:8761/health/check

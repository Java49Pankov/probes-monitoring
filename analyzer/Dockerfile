FROM eclipse-temurin:17-jdk-alpine
COPY ./target/analyzer-0.0.1.jar app.jar
ENV CONFIG_SERVER_HOST=config-server
ENV RANGE_PROVIDER_HOST=sensor-range-provider
ENTRYPOINT [ "java", "-jar", "app.jar" ]
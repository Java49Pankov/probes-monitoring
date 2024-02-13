FROM eclipse-temurin:17-jdk-alpine
COPY ./target/sensor-range-provider-0.0.1.jar app.jar
EXPOSE 8282
ENV CONFIG_SERVER_HOST=config-server
ENTRYPOINT [ "java", "-jar", "app.jar" ]
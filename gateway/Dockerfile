FROM eclipse-temurin:17-jdk-alpine
COPY ./target/gateway-0.0.1.jar app.jar
EXPOSE 8080
ENV CONFIG_SERVER_HOST=config-server
ENV ACCOUNT_PROVIDER_HOST=account-provider
ENV ADMIN_CONSOLE_HOST=admin-console
ENV EMAILS_PROVIDER_HOST=email-data-provider
ENV RANGE_PROVIDER_HOST=sensor-range-provider
ENTRYPOINT [ "java", "-jar", "app.jar" ]
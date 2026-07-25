FROM eclipse-temurin:8-jre-alpine
WORKDIR /app
COPY target/commerce-1.0.0.jar app.jar
USER 10001
ENTRYPOINT ["java","-jar","app.jar"]

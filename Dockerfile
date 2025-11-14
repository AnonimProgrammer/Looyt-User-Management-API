FROM eclipse-temurin:21-jre AS runtime
WORKDIR /app

COPY build/libs/*.jar app.jar

ENV PORT=8080
EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]

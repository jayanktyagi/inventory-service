FROM eclipse-temurin:17-jdk-alpine
EXPOSE 9090
ADD target/inventory-service-0.0.1-SNAPSHOT.jar inventory-service-0.0.1-SNAPSHOT.jar
ENTRYPOINT ["java", "-jar", "inventory-service-0.0.1-SNAPSHOT.jar"]

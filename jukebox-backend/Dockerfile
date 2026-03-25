# Estágio 1: Build (Compilação)
FROM maven:3.8.4-openjdk-17-slim AS build
WORKDIR /app
# Copia o pom.xml e baixa as dependências (cache)
COPY pom.xml .
RUN mvn dependency:go-offline
# Copia o código e gera o arquivo JAR
COPY src ./src
RUN mvn clean package -DskipTests

# Estágio 2: Run (Execução)
FROM amazoncorretto:17-alpine
WORKDIR /app
# Copia apenas o JAR gerado no estágio anterior
COPY --from=build /app/target/*.jar app.jar
# Expõe a porta que configuramos no Spring
EXPOSE 8888
ENTRYPOINT ["java", "-jar", "app.jar"]
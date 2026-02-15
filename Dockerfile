# Etapa 1: Build con Maven
FROM maven:3.9-eclipse-temurin-21 AS build

WORKDIR /app

# Copiar pom.xml primero para cachear dependencias
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Copiar código fuente
COPY src ./src

# Compilar
RUN mvn clean package -DskipTests

# Etapa 2: Runtime con Tomcat
FROM tomcat:10.1-jdk21

# Eliminar apps default de Tomcat
RUN rm -rf /usr/local/tomcat/webapps/*

# Copiar nuestro WAR como ROOT.war (se despliega en /)
COPY --from=build /app/target/PawPaw-*.war /usr/local/tomcat/webapps/ROOT.war

# Exponer puerto
EXPOSE 8080

# Tomcat arranca automáticamente
CMD ["catalina.sh", "run"]


FROM eclipse-temurin:17-jdk-alpine

# Establecer un volumen para permitir el almacenamiento temporal
VOLUME /tmp

# Copiar el archivo JAR del proyecto al sistema de archivos del contenedor
COPY build/libs/Publications-0.0.1-SNAPSHOT.jar app.jar

# Definir el punto de entrada para ejecutar la aplicación
ENTRYPOINT ["java", "-jar", "/app.jar"]

# Exponer el puerto 8080 para permitir el acceso a la aplicación
EXPOSE 8080
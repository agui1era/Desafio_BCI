# Desafío BCI: Microservicio de Gestión de Usuarios

Este proyecto implementa un microservicio Spring Boot en Java 8 para la creación y consulta de usuarios, cumpliendo con los requisitos del desafío.

## Tecnologías Clave

* **Java 8**
* **Spring Boot 2.x**
* **Gradle**
* **Spring Data JPA**
* **H2 Database (en memoria)**
* **JWT (JSON Web Tokens)**
* **BCrypt**
* **Lombok**
* **SLF4J + Logback**

## Configuración Inicial

Asegúrate de tener la siguiente propiedad en `src/main/resources/application.properties`:

```properties
# Clave secreta JWT (DEBE ser una cadena Base64 de 64 bytes/512 bits)
# Puedes generar una clave con 'openssl rand -base64 64'
jwt.secret=TU_CLAVE_SECRETA_BASE64_DE_64_BYTES_AQUI
```

## Compilación y Ejecución

```bash
./gradlew clean build
java -jar build/libs/Desafio_BCI-0.0.1-SNAPSHOT.jar
```

## Uso de la API

### Registro de Usuario

```bash
curl -X POST \
  http://localhost:8080/api/users/sign-up \
  -H 'Content-Type: application/json' \
  -d '{
    "name": "Usuario Test",
    "email": "test.user@example.com",
    "password": "a2asfGfdfdf4",
    "phones": [
      {
        "number": "98765432",
        "citycode": "2",
        "contrycode": "56"
      }
    ]
  }'
```

### Login

```bash
curl -X POST \
  http://localhost:8080/api/users/login \
  -H 'Authorization: Bearer YOUR_JWT_TOKEN'
```

El token JWT debe ser enviado en el header `Authorization` con el prefijo `Bearer `.

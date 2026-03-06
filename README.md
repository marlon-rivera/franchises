# 🏪 Franchising Project

Este proyecto es una API REST desarrollada con **Spring WebFlux** bajo la arquitectura **Scaffold Clean Architecture** de Bancolombia. Gestiona franquicias, sucursales y productos, permitiendo operaciones CRUD y consultas personalizadas. Además, incluye migraciones automáticas con **Flyway**, documentación con **OpenAPI/Swagger** y configuración de contenedores con **Docker Compose**.

---

## 🛠️ Tecnologías utilizadas

- Java 21
- Spring Boot 3.5.0 + WebFlux
- R2DBC + MySQL 8
- Flyway (migraciones con JDBC)
- Docker + Docker Compose
- OpenAPI (Swagger UI)
- Clean Architecture (Scaffold - Bancolombia)
- Terraform (IaC)
- AWS (ECS Fargate, RDS, ALB, API Gateway, ECR)

---

## 🚀 Cómo ejecutar la aplicación localmente

### ✅ Requisitos

- Docker y Docker Compose

---

### 📦 Opción 1: Ejecutar con Docker Compose (recomendado)

Solo necesitas Docker instalado. Este comando construye y levanta todo:

```bash
docker-compose -f deployment/docker-compose.yml up --build
```

Esto iniciará dos contenedores:

1. **franchising-db**: base de datos MySQL expuesta en `localhost:3307` (puerto interno 3306)
2. **franchising-api**: API Spring expuesta en `localhost:8080`

> La API espera automáticamente a que MySQL esté listo antes de arrancar.
> Flyway ejecuta las migraciones y datos de prueba al iniciar.

Para detener:
```bash
docker-compose -f deployment/docker-compose.yml down
```

Para limpiar volúmenes (reset BD):
```bash
docker-compose -f deployment/docker-compose.yml down -v
```

---

### 🧪 Opción 2: Ejecutar manualmente sin Docker

#### Requisitos adicionales
- JDK 21
- MySQL 8 corriendo localmente

#### Pasos

1. Crea la base de datos en MySQL:
```sql
CREATE DATABASE IF NOT EXISTS franchising_db;
```

2. Verifica el archivo `.env` en la raíz del proyecto:
```env
URL_DATABASE=r2dbc:mysql://localhost:3306/franchising_db
FLYWAY_URL=jdbc:mysql://localhost:3306/franchising_db
USER_DATABASE=root
PASS_DATABASE=admin
```

3. Ejecuta el proyecto:
```bash
./gradlew :app-service:bootRun --no-configuration-cache
```

---

### 🌐 Acceso a la documentación Swagger

Una vez levantado el proyecto, visita:

```
http://localhost:8080/webjars/swagger-ui/index.html
```
---

## 🧱 Estructura del Proyecto

```
franchising-project/
├── applications/
│   └── app-service/          # Servicio principal (API)
│       └── build/libs/       # Contiene el .jar generado
├── deployment/
│   └── Dockerfile            # Imagen Docker del servicio
│   └── docker-compose.yml    # Orquestador de servicios
├── domain/                   
│   └── model                 # Modelo de dominio y entidades
│   └── usecase               # Casos de uso
├── infrastructure/           
│   └── driven-adapters       # Adaptadores, puertos, bases de datos
│   └── entry-points          # Puntos de entrada a la aplicación  
└── iac/                      # Infraestructura como código (Terraform)
```

---

## 🗃️ Configuración de la base de datos

La base de datos se llama `franchising_db`, se crea automáticamente y se inicializa vía Flyway con la siguiente estructura:

### Tablas principales

- `franchise`: franquicias
- `branch`: sucursales (relación con `franchise`)
- `product`: productos
- `product_branch`: productos por sucursal con stock

### Script de inicialización

Se encuentra en:  
`applications/app-service/src/main/resources/db/migration/V1__init_schema.sql`  
Incluye inserción de datos dummy para pruebas.

---

## 🔗 Endpoints disponibles

### 📍 Rutas de Franquicia

- `POST /api/franchise/create`
- `PUT  /api/franchise/{franchiseId}/update-name`
- `GET  /api/franchise/{franchiseId}/top-products`

### 🏬 Rutas de Sucursal

- `POST /api/branch/create`
- `PUT  /api/branch/{branchId}/update-name`

### 📦 Rutas de Productos

- `POST   /api/product/create`
- `PUT    /api/product/{productId}/update-name`
- `PUT    /api/product/update/stock`
- `DELETE /api/product/delete/product/{productId}/branch/{branchId}`

---

## ✅ Variables de entorno usadas

Las variables están definidas en el archivo `.env` o el `docker-compose.yml`, y se leen desde el `application.yml`:

| Variable         | Descripción                              |
|------------------|------------------------------------------|
| `URL_DATABASE`   | Cadena R2DBC para conexión MySQL         |
| `FLYWAY_URL`     | Cadena JDBC para migraciones Flyway      |
| `USER_DATABASE`  | Usuario de la base de datos              |
| `PASS_DATABASE`  | Contraseña del usuario de BD             |

---

## 👨‍💻 Autor

Proyecto desarrollado por **Marlon Rivera**

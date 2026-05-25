# El Sol Dorado API - Implementación S06 a S09

Proyecto Spring Boot para gestión de restaurante con persistencia en PostgreSQL, migraciones Flyway, consultas JPQL, Spring Security con roles y autenticación JWT.

## Tecnologías

- Java 21
- Spring Boot 3.5.13
- Spring Web
- Spring Data JPA / Hibernate
- PostgreSQL
- Flyway
- Spring Security
- JWT con `io.jsonwebtoken`
- Bean Validation

## Base de datos

Configuración por defecto:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/elsoldorado
spring.datasource.username=postgres
spring.datasource.password=mathi2707
```

También se puede configurar con variables de entorno:

```bash
SPRING_DATASOURCE_URL=jdbc:postgresql://localhost:5432/elsoldorado
SPRING_DATASOURCE_USERNAME=postgres
SPRING_DATASOURCE_PASSWORD=tu_password
JWT_SECRET=una_clave_larga_de_minimo_32_caracteres
JWT_EXPIRATION_MINUTES=120
```

Flyway crea las tablas mediante:

- `V1__crear_tablas.sql`
- `V2__insertar_datos_iniciales.sql`
- `V3__crear_tabla_usuarios.sql`

Al iniciar la aplicación se crean, si no existen, estos usuarios base:

| Usuario | Contraseña | Rol |
|---|---|---|
| `admin` | `admin123` | `ADMIN` |
| `empleado` | `empleado123` | `EMPLEADO` |
| `cliente` | `cliente123` | `CLIENTE` |

## Ejecutar

```bash
./mvnw spring-boot:run
```

En Windows:

```powershell
.\mvnw.cmd spring-boot:run
```

## Autenticación JWT

### Login

```http
POST /auth/login
Content-Type: application/json

{
  "username": "admin",
  "password": "admin123"
}
```

Respuesta:

```json
{
  "token": "...",
  "tipo": "Bearer",
  "id": 1,
  "username": "admin",
  "rol": "ADMIN"
}
```

Para consumir endpoints protegidos:

```http
Authorization: Bearer TU_TOKEN
```

## Reglas de seguridad

| Recurso | Acceso |
|---|---|
| `POST /auth/login`, `POST /auth/register` | Público |
| `GET /`, `GET /historia`, `GET /menu/**`, `GET /categorias/**`, `GET /mesas/disponibles/**` | Público |
| `POST /pedidos`, `POST /reservas` | `CLIENTE`, `EMPLEADO`, `ADMIN` |
| `/pedidos/**`, `/reservas/**`, `/mesas/**`, `/clientes/**` | `EMPLEADO`, `ADMIN` |
| `/usuarios/**`, cambios de menú y categorías | `ADMIN` |

## Endpoints principales

### Menú

- `GET /menu`
- `GET /menu/{id}`
- `GET /menu/inicio`
- `GET /menu/destacados`
- `GET /menu/categoria/{nombre}`
- `GET /menu/buscar?texto=ceviche`
- `GET /menu/precio?min=10&max=30`
- `POST /menu` ADMIN
- `PUT /menu/{id}` ADMIN
- `PATCH /menu/{id}/disponibilidad` ADMIN
- `DELETE /menu/{id}` ADMIN

### Pedidos

```json
{
  "nombreCliente": "Jose",
  "telefono": "999888777",
  "direccion": "Av. Siempre Viva 123",
  "referencia": "Frente al parque",
  "detalles": [
    { "idPlato": 1, "cantidad": 2 }
  ]
}
```

- `POST /pedidos`
- `GET /pedidos`
- `GET /pedidos/{id}`
- `GET /pedidos/buscar?cliente=Jose`
- `GET /pedidos/estado/PENDIENTE/desde?fechaHora=2026-05-25T00:00:00`
- `GET /pedidos/rango-fechas?desde=2026-05-25T00:00:00&hasta=2026-05-26T23:59:59`
- `GET /pedidos/total-minimo?monto=50`
- `PATCH /pedidos/{id}/estado`
- `PUT /pedidos/{id}`
- `DELETE /pedidos/{id}`

### Reservas

```json
{
  "nombreCliente": "Maria",
  "telefono": "999111222",
  "fecha": "2026-05-26",
  "hora": "15:00:00",
  "cantidadPersonas": 4,
  "observacion": "Mesa cerca a ventana",
  "mesaId": 2
}
```

- `POST /reservas`
- `GET /reservas`
- `GET /reservas/{id}`
- `GET /reservas/proximas?desde=2026-05-25`
- `GET /reservas/buscar?cliente=Maria`
- `GET /reservas/rango-fechas?desde=2026-05-25&hasta=2026-05-31`
- `PATCH /reservas/{id}/estado`
- `PUT /reservas/{id}`
- `DELETE /reservas/{id}`

## Avance por semana

- **S06 - JPA/Hibernate:** implementado con entidades, repositorios, relaciones, PostgreSQL y Flyway.
- **S07 - JPQL `@Query`:** implementado en categorías, platos, mesas, pedidos y reservas.
- **S08 - Spring Security + Roles:** implementado con roles `ADMIN`, `EMPLEADO` y `CLIENTE`.
- **S09 - JWT:** implementado con login, registro, generación y validación de token Bearer.

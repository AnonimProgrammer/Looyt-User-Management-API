# Looyt User Management Service

A simple **User Management Service** built with **Spring Boot 3** (Java) and **PostgreSQL**, designed to manage users with clean API design, Dockerization, and deployment.

The project is deployed and accessible online.

---

## Repository

[GitHub Repository](https://github.com/AnonimProgrammer/Looyt-User-Management-API)

---

## Deployed API

**Base URL:** [https://looyt-user-management-api-production.up.railway.app](https://looyt-user-management-api-production.up.railway.app)

**Verify deployment endpoint:**  
```http
GET /actuator/health
```
Example: [https://looyt-user-management-api-production.up.railway.app/actuator/health](https://looyt-user-management-api-production.up.railway.app/actuator/health)

---

## Environment Variables

Example `.env` file:

```env
# Spring Boot / PostgreSQL
SPRING_DATASOURCE_URL=jdbc:postgresql://<host>:5432/user_db_g5uu
SPRING_DATASOURCE_USERNAME=user
SPRING_DATASOURCE_PASSWORD=<password>
SPRING_PROFILES_ACTIVE=prod

# Local PostgreSQL (Docker)
POSTGRES_DB=user_db
POSTGRES_USER=user
POSTGRES_PASSWORD=password
```

---

## Docker Compose

Local development can be run with:

```bash
docker compose --profile dev up --build
```

`docker-compose.yml` includes:

- `user-management-service` (Spring Boot app)
- `db` (PostgreSQL database)
- Healthchecks for both containers
- Persistent Postgres volume

---

## API Endpoints

**Roles:** `USER`, `ADMIN`  
**Statuses:** `ACTIVE`, `INACTIVE`, `BLOCKED`  

---

### 1.Create a new user

```http
POST /v1/users
Content-Type: application/json
```

**Request:**

```json
{
  "name": "Murad",
  "email": "murad.ismayilov@icloud.com",
  "phoneNumber": "+994557127690"
}
```

**Response:** Returns created user with `id`.

---

### 2.Get user by ID

```http
GET /v1/users/{id}
```

**Example:**

```
GET https://looyt-user-management-api-production.up.railway.app/v1/users/9f2fcbe9-352c-4476-8e3a-dc372217c9c8
```

---

### 3.List users

```http
GET /v1/users
```

Supports filtering and pagination via query parameters:

- `search` → search by name/email
- `status` → ACTIVE, INACTIVE, BLOCKED
- `role` → USER, ADMIN
- `page` → page number
- `size` → items per page

**Example:**

```
GET https://looyt-user-management-api-production.up.railway.app/v1/users?search=Omar&status=ACTIVE&role=USER&page=0&size=20
```

---

### 4.Update user

```http
PUT /v1/users/{id}
Content-Type: application/json
```

**Request:**

```json
{
  "name": "Omar Ismayilov",
  "email": "omar.ismayilov@icloud.com",
  "phoneNumber": "+994554419971"
}
```

---

### 5.Update user role

```http
PATCH /v1/users/{id}/role
Content-Type: application/json
```

**Request:**

```json
{
  "role": "ADMIN"
}
```

---

### 6.Update user status

```http
PATCH /v1/users/{id}/status
Content-Type: application/json
```

**Request:**

```json
{
  "status": "BLOCKED"
}
```

---

### 7.Delete user

```http
DELETE /v1/users/{id}
```

---

## Local Setup

1. Clone the repository:

```bash
git clone https://github.com/AnonimProgrammer/Looyt-User-Management-API.git
cd Looyt-User-Management-API
```

2. Add `.env` file with your environment variables.

3. Start the application:

```bash
docker compose --profile dev up --build
```

4. Access the API at:

```
http://localhost:8080
```

---

## Notes

- All endpoints return proper HTTP status codes and error messages.
- Logging is enabled for debugging and tracing requests.
- Pagination and filtering are supported in `GET /v1/users`.
- Deployment is done via Railway.

---

## Extra Features & Enhancements

This project includes several enhancements beyond the basic requirements:

- **Pagination**: Supported in `GET /v1/users` for better performance with large datasets.
- **Unit Tests**: Full coverage for controller, service, and mapper layers.
- **CI/CD**: Simple automation pipeline to build, test, and deploy the application automatically.

## CI/CD Flow
- Automatically builds the Docker image.
- Runs all tests.
- Deploys to the configured environment (e.g., Railway) if tests pass.

---

<p align="center">
  <b>Omar Ismayilov</b><br>
  <i>Software Engineer • Backend & System Design Enthusiast</i><br>
  Building reliable systems with simplicity and architecture in mind.
</p>




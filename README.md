# Looyt User Management Service

A simple **User Management Service** built with **Spring Boot 3** (Java) and **PostgreSQL**, designed to manage users with clean API design, Dockerization, and deployment.

The project is deployed and accessible online.

---

## 🔗 Repository

[GitHub Repository](https://github.com/AnonimProgrammer/Looyt-User-Management-API)

---

## 🌐 Deployed API

**Base URL:** [https://looyt-user-management-api-production.up.railway.app](https://looyt-user-management-api-production.up.railway.app)

**Verify deployment endpoint:**  
```http
GET /actuator/health
```
Example: [https://looyt-user-management-api-production.up.railway.app/actuator/health](https://looyt-user-management-api-production.up.railway.app/actuator/health)

---

## ⚙️ Environment Variables

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

## 🐳 Docker Compose

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

## 📝 API Endpoints

**Roles:** `USER`, `ADMIN`  
**Statuses:** `ACTIVE`, `INACTIVE`, `BLOCKED`  

---

### 1️⃣ Create a new user

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

### 2️⃣ Get user by ID

```http
GET /v1/users/{id}
```

**Example:**

```
GET https://looyt-user-management-api-production.up.railway.app/v1/users/9f2fcbe9-352c-4476-8e3a-dc372217c9c8
```

---

### 3️⃣ List users

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

### 4️⃣ Update user

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

### 5️⃣ Update user role

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

### 6️⃣ Update user status

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

### 7️⃣ Delete user

```http
DELETE /v1/users/{id}
```

---

## 🛠️ Local Setup

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

## ✅ Notes

- All endpoints return proper HTTP status codes and error messages.
- Logging is enabled for debugging and tracing requests.
- Pagination and filtering are supported in `GET /v1/users`.
- Deployment is done via Railway.

---

## 🏗️ Optional Improvements / Bonus

- Unit tests for service and controller layers  
- Database migrations with Flyway  
- CI/CD pipeline integration  
- Kafka for event-driven notifications (optional)


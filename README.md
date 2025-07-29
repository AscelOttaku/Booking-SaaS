# 📆 Service Booking Platform

## 📌 Description
A multifunctional Java application for online service booking. Supports two main roles:
- 👨‍💻 **Client (CLIENT)** — users who browse businesses and book services
- 🧑‍💼 **Business Owner (BUSINESS_OWNER)** — manages their business, services, schedule, and clients

Built as a REST API using Spring Boot, JWT, validation, migrations, and unified error structure.

---

## 🔐 Authentication
| Method  | Endpoint              | Description                                         |
|---------|-----------------------|-----------------------------------------------------|
| POST    | `/api/auth/sign-up`   | Register (role: CLIENT or BUSINESS_OWNER)           |
| POST    | `/api/auth/sign-in`   | Login, JWT issuance                                 |

- Password hashing
- JWT authorization via `Authorization: Bearer ...` header

---

## 👨‍💻 Client Area (CLIENT)

### 🌐 Public Access
| Method  | Endpoint                                 | Description                                   |
|---------|------------------------------------------|-----------------------------------------------|
| GET     | `/api/businesses`                        | List businesses (search by name, city, slug)  |
| GET     | `/api/businesses/{slug}/services`        | List services for a business                  |
| GET     | `/api/businesses/{slug}/schedule`        | Available booking schedule for a business     |

### 📝 Booking
| Method  | Endpoint         | Description                                         |
|---------|------------------|-----------------------------------------------------|
| POST    | `/api/booked/`  | Create a booking (validation: at least 24h ahead)   |

### ⚙️ Manage Bookings (optional)
| Method  | Endpoint                | Description                                     |
|---------|-------------------------|-------------------------------------------------|
| PUT     | `/api/booked/{id}`    | Update a booking                                |
| DELETE  | `/api/booked/{id}`    | Cancel a booking (if not too late)              |

### 📩 Notifications
- Email notifications on booking creation/cancellation

---

## 🧑‍💼 Owner Panel (BUSINESS_OWNER)

### 🏢 Business Management
| Method  | Endpoint             | Description                                     |
|---------|----------------------|-------------------------------------------------|
| POST    | `/api/businesses`    | Create a business (slug generation, user_id link)|

### 💇 Service Management
| Method  | Endpoint                              | Description                   |
|---------|---------------------------------------|-------------------------------|
| POST    | `/api/businesses/{id}/services`       | Add a service                 |
| PUT     | `/api/services/{id}`                  | Edit a service                |
| DELETE  | `/api/services/{id}`                  | Delete a service              |

### 📅 Schedule Management
| Method  | Endpoint                        | Description                                 |
|---------|---------------------------------|---------------------------------------------|
| POST    | `/api/businesses/{id}/schedule` | Set schedule (support weekends, holidays)   |

### 📊 Booking Management
| Method  | Endpoint                | Description                                 |
|---------|-------------------------|---------------------------------------------|
| GET     | `/api/owner/bookings`   | View all bookings (filter by date)          |

### 👥 Client Management
| Method  | Endpoint                                 | Description                                 |
|---------|------------------------------------------|---------------------------------------------|
| GET     | `/api/owner/clients`                     | Get client list                             |
| GET     | `/api/owner/clients/{id}/history`        | Get client booking history                  |

---

## ⚙️ Technical Details

| Component   | Description                                                        |
|-------------|--------------------------------------------------------------------|
| JWT         | Authorization for both roles via `Authorization: Bearer <token>`   |
| DTO         | DTOs in `shared/dto/`, specs via Swagger                           |
| Migrations  | Single migration file (e.g., Liquibase), consistent FKs            |
| Database    | Shared DB, unified table and FK naming                             |
| Errors      | Unified error format: `400`, `401`, `403`, `409`, `422` (JSON)     |

---

## 🛠 Technologies
- Java 17
- Spring Boot
- Spring Security + JWT
- PostgreSQL
- Maven
- Liquibase
- Swagger/OpenAPI
- Email services (e.g., SMTP)

---

## 🧪 Local Launch Example

```bash
# Clone the repository
git clone https://github.com/your-username/your-repo.git

# Go to project folder
cd your-repo

# Configure application.yml (DB, email)

# Run the project
./mvnw spring-boot:run

# Referral Management System

A full-stack application for managing referrals, featuring a Spring Boot backend and a modern React frontend.

## 🏗 Project Structure

- **referral-system**: Java Spring Boot backend application.
- **client**: React frontend application built with Vite and Tailwind CSS.

---

## 🚀 Getting Started

### 1. Prerequisites (Database Setup)

The backend requires a PostgreSQL database. You can spin up a containerized instance using Docker:

```bash
docker run -d \
  --name referral-postgres \
  -p 5433:5432 \
  -e POSTGRES_USER=user \
  -e POSTGRES_PASSWORD=password \
  -e POSTGRES_DB=referral_db \
  postgres:16
```

### 2. Backend Setup (referral-system)

1.  **Verify Configuration**:
    Check `referral-system/src/main/resources/application.properties` to ensure the database connection details match your Docker setup:

    ```properties
    spring.datasource.url=jdbc:postgresql://localhost:5433/referral_db
    spring.datasource.username=user
    spring.datasource.password=password
    ```

2.  **Run the Backend**:
    Navigate to the `referral-system` directory and run:
    ```bash
    mvn spring-boot:run
    ```
    The server will start on [http://localhost:8080](http://localhost:8080).

### 3. Frontend Setup (client)

1.  **Install Dependencies**:
    Navigate to the `client` directory and run:

    ```bash
    npm install
    ```

2.  **Run Development Server**:

    ```bash
    npm run dev
    ```

    The client will be available at [http://localhost:3000](http://localhost:3000).

3.  **Build for Production** (Optional):
    ```bash
    npm run build
    ```

---

## 🛠 Tech Stack

- **Backend**: Java 21, Spring Boot, Spring Data JPA, PostgreSQL, Liquibase.
- **Frontend**: React, Vite, Tailwind CSS, TanStack Query, TanStack Router.

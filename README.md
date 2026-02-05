# 🏥 Referral Management System

A robust full-stack application designed to streamline the referral process. Built with a powerful **Spring Boot** backend and a high-performance **React** frontend.

---

## 📁 Project Structure

```text
.
├── referral-system/    # Spring Boot Backend (Java 21, Maven)
└── client/             # React Frontend (Vite, Tailwind, TanStack)
```

---

## 🚀 Getting Started

Follow these steps to get the environment up and running.

### 1. Database Setup (Docker) 🐳

The backend requires a PostgreSQL database. Start a container using the following command:

```bash
docker run -d \
  --name referral-postgres \
  -p 5433:5432 \
  -e POSTGRES_USER=user \
  -e POSTGRES_PASSWORD=password \
  -e POSTGRES_DB=referral_db \
  postgres:16
```

### 2. Backend Setup (`referral-system`) ☕

1.  **Configuration Check**:
    Verify the database credentials in `referral-system/src/main/resources/application.properties`:

    ```properties
    spring.datasource.url=jdbc:postgresql://localhost:5433/referral_db
    spring.datasource.username=user
    spring.datasource.password=password
    ```

2.  **Run the Server**:
    Navigate to the `referral-system` directory and execute:
    ```bash
    mvn spring-boot:run
    ```
    _The API will be available at:_ `http://localhost:8080`

### 3. Frontend Setup (`client`) ⚛️

1.  **Installation**:
    Navigate to the `client` directory and install dependencies:

    ```bash
    npm install
    ```

2.  **Development Mode**:
    Launch the Vite development server:

    ```bash
    npm run dev
    ```

    _The app will be available at:_ `http://localhost:3000`

3.  **Production Build**:
    To create a production-ready bundle:
    ```bash
    npm run build
    ```

---

## 🛠 Tech Stack

### Backend

- **Java 21** & **Spring Boot 3**
- **Spring Data JPA** (Hibernate)
- **PostgreSQL** (Port 5433)
- **Liquibase** (Database Migrations)

### Frontend

- **React 19** & **Vite**
- **Tailwind CSS** (Styling)
- **TanStack Query** (Data Fetching)
- **TanStack Router** (Type-safe Routing)
- **Lucide React** (Icons)

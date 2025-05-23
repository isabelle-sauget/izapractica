# Full Stack Dockerized Application

This project contains:
- **Frontend**: Vue.js SPA (Vite)
- **Backend**: Spring Boot REST API
- **Database**: PostgreSQL
- **Orchestration**: Docker Compose

## Getting Started

### Prerequisites
- Docker & Docker Compose installed

### Running the Application

1. Build and start all services:
   ```sh
   docker compose up --build
   ```
2. Frontend: http://localhost:5173
3. Backend API: http://localhost:8080
4. PostgreSQL: localhost:5432 (user: postgres, password: postgres)

### Development
- Frontend code: root directory (`src/`)
- Backend code: `backend/`

---

For customizations, edit the respective Dockerfiles, `docker-compose.yml`, or service source code.

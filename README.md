# Proiect Clinica

This is a full-stack application with a Vue.js SPA frontend, a Spring Boot REST API backend, and a PostgreSQL database. All services are containerized and orchestrated with Docker Compose.

## Prerequisites
- [Docker](https://www.docker.com/get-started) and [Docker Compose](https://docs.docker.com/compose/install/) installed
- [Git](https://git-scm.com/)

### How to Install Docker and Docker Compose (Windows)

1. **Install Docker Desktop:**
   - Download Docker Desktop for Windows from [here](https://www.docker.com/products/docker-desktop/).
   - Run the installer and follow the setup instructions.
   - Docker Compose is included with Docker Desktop.

2. **Verify installation:**
   ```sh
   docker --version
   docker compose version
   ```

## Installation & Running

1. **Clone the repository:**
   ```sh
   git clone https://github.com/jeancipa/izapractica.git
   cd izapractica
   ```

2. **Build and start all services:**
   ```sh
   docker compose up --build
   ```
   This will start the frontend (Vue.js), backend (Spring Boot), PostgreSQL database, and pgAdmin.

3. **Access the application:**
   - Frontend: [http://localhost:5173](http://localhost:5173)
   - Backend API: [http://localhost:8080](http://localhost:8080)
   - pgAdmin: [http://localhost:5050](http://localhost:5050) (login: admin@admin.com / admin)

4. **Stopping the services:**
   Press `Ctrl+C` in the terminal, then run:
   ```sh
   docker compose down
   ```

## Notes
- All data in the database and pgAdmin settings are persisted using Docker volumes.
- You can manage the database visually using pgAdmin.
- The frontend supports adding, listing, deleting, and searching records by ID.

---

For any issues, please open an issue on the [GitHub repository](https://github.com/jeancipa/izapractica).

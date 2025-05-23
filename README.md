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

## Frontend–Backend Interaction

The Vue.js frontend communicates with the Spring Boot backend via REST API endpoints. All API requests are sent to the backend service, which manages records in the PostgreSQL database.

### API Endpoints

All endpoints are prefixed with `/api/records`:

- **GET `/api/records`**
  - List all records.
  - **Response:** Array of record objects.

- **GET `/api/records/{id}`**
  - Get a single record by its ID.
  - **Response:** Record object if found, 404 if not found.

- **POST `/api/records`**
  - Create a new record.
  - **Request body:**
    ```json
    {
      "index1": "string",
      "field1": "string",
      "field2": "string",
      "field3": "string"
    }
    ```
  - **Response:** The created record object.

- **PUT `/api/records/{id}`**
  - Update an existing record by ID.
  - **Request body:** Same as POST.
  - **Response:** The updated record object.

- **DELETE `/api/records/{id}`**
  - Delete a record by ID.
  - **Response:** 204 No Content if successful.

The frontend uses these endpoints to:
- List all records in a table
- Add new records
- Edit records inline
- Delete records (with confirmation)
- Search for a record by ID

## Viewing the Database Table in pgAdmin

1. **Open pgAdmin:**
   - Go to [http://localhost:5050](http://localhost:5050) in your browser.
   - Login with:
     - **Email:** `admin@admin.com`
     - **Password:** `admin`

2. **Register the PostgreSQL server:**
   - In the pgAdmin sidebar, right-click on **Servers** and choose **Register > Server...**
   - In the **General** tab, enter a name (e.g., `Local Postgres`).
   - In the **Connection** tab, use:
     - **Host name/address:** `db`
     - **Port:** `5432`
     - **Username:** `postgres`
     - **Password:** `postgres`
   - Click **Save**.

3. **Browse the table:**
   - Expand the server, then **Databases > postgres > Schemas > public > Tables**.
   - You will see the application's table (e.g., `record`).
   - Right-click the table and select **View/Edit Data > All Rows** to see and manage the data.

## Notes
- All data in the database and pgAdmin settings are persisted using Docker volumes.
- You can manage the database visually using pgAdmin.
- The frontend supports adding, listing, editing, deleting, and searching records by ID.

---

For any issues, please open an issue on the [GitHub repository](https://github.com/jeancipa/izapractica).

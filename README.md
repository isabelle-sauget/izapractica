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

All endpoints are prefixed with `/api/pacienti`:

- **GET `/api/pacienti`**
  - List all pacienti.
  - **Response:** Array of pacient objects.

- **GET `/api/pacienti/{id}`**
  - Get a single pacient by its ID.
  - **Response:** Pacient object if found, 404 if not found.

- **POST `/api/pacienti`**
  - Create a new pacient.
  - **Request body:**
    ```json
    {
      "nume": "string",
      "prenume": "string",
      "cnp": "string",
      "telefon": "string",
      "adresa": "string",
      "dataNasterii": "YYYY-MM-DD",
      "email": "string",
      "dateMedicale": "string"
    }
    ```
  - **Response:** The created pacient object.

- **PUT `/api/pacienti/{id}`**
  - Update an existing pacient by ID.
  - **Request body:** Same as POST.
  - **Response:** The updated pacient object.

- **DELETE `/api/pacienti/{id}`**
  - Delete a pacient by ID.
  - **Response:** 204 No Content if successful.

The frontend uses these endpoints to:
- List all pacienti in a table
- Add new pacienti
- Edit pacienti inline
- Delete pacienti (with confirmation)
- Search for a pacient by ID

### API Endpoints (Doctori)

All endpoints are prefixed with `/api/doctori`:

- **GET `/api/doctori`**
  - List all doctori.
  - **Response:** Array of doctor objects.

- **GET `/api/doctori/{id}`**
  - Get a single doctor by its ID.
  - **Response:** Doctor object if found, 404 if not found.

- **POST `/api/doctori`**
  - Create a new doctor.
  - **Request body:**
    ```json
    {
      "nume": "string",
      "prenume": "string",
      "cnp": "string",
      "adresa": "string",
      "telefon": "string",
      "email": "string",
      "specializare": "string",
      "clinica": "string",
      "alteInformatii": "string"
    }
    ```
  - **Response:** The created doctor object.

- **PUT `/api/doctori/{id}`**
  - Update an existing doctor by ID.
  - **Request body:** Same as POST.
  - **Response:** The updated doctor object.

- **DELETE `/api/doctori/{id}`**
  - Delete a doctor by ID.
  - **Response:** 204 No Content if successful.

The frontend uses these endpoints to:
- List all doctori in a table
- Add new doctori
- Edit doctori inline
- Delete doctori (with confirmation)
- Search for a doctor by ID

### API Endpoints (Retete)

All endpoints are prefixed with `/api/retete`:

- **GET `/api/retete`**
  - List all retete (prescriptions).
  - **Response:** Array of reteta objects.

- **GET `/api/retete/{id}`**
  - Get a single reteta by its ID.
  - **Response:** Reteta object if found, 404 if not found.

- **POST `/api/retete`**
  - Create a new reteta (prescription).
  - **Request body:**
    ```json
    {
      "doctor": { "id": 1 },
      "pacient": { "id": 2 },
      "data": "YYYY-MM-DD",
      "reteta": "string",
      "compensata": "DA" // or "NU"
    }
    ```
  - **Response:** The created reteta object.

- **PUT `/api/retete/{id}`**
  - Update an existing reteta by ID.
  - **Request body:** Same as POST.
  - **Response:** The updated reteta object.

- **DELETE `/api/retete/{id}`**
  - Delete a reteta by ID.
  - **Response:** 204 No Content if successful.

The frontend uses these endpoints to:
- List all retete in a table
- Add new retete (with dropdowns for doctor and pacient)
- Edit retete inline (including changing doctor/pacient)
- Delete retete (with confirmation)
- Search for a reteta by ID

## Frontend Pages

- **Pacienti:** Manage patients (CRUD, search, inline edit)
- **Doctori:** Manage doctors (CRUD, search, inline edit)
- **Retete:** Manage prescriptions (CRUD, search, inline edit). When adding or editing a reteta, you can select a doctor and pacient from dropdowns populated with their names from the respective tables. Fields: Doctor, Pacient, Data, Reteta, Compensata (DA/NU).

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
   - You will see the application's tables: `pacient`, `doctor`, and `reteta`.
   - Right-click any table and select **View/Edit Data > All Rows** to see and manage the data.

## Notes
- All data in the database and pgAdmin settings are persisted using Docker volumes.
- You can manage the database visually using pgAdmin.
- The frontend supports adding, listing, editing, deleting, and searching records by ID.

---

For any issues, please open an issue on the [GitHub repository](https://github.com/jeancipa/izapractica).

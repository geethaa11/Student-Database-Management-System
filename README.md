# Student Database Management System

A complete application built to manage student records. It now includes **both** a JavaFX desktop application and a modern Web Application.

## Project Structure
- `src/` & `pom.xml` - The original JavaFX Desktop Application.
- `web-backend/` - The Spring Boot REST API for the web version.
- `web-frontend/` - The HTML/CSS/JS frontend for the web version.
- `Dockerfile` - Used for deploying the web version to production.

---

## 1. Running the JavaFX Desktop App (Local)
Ensure you have JDK 17+ and Maven installed.
```bash
mvn clean javafx:run
```
Data is stored locally in `student_manager.db` in the root folder.

---

## 2. Running the Web App (Local Development)

### Start the Backend API
Navigate to the `web-backend` directory and run the Spring Boot app:
```bash
cd web-backend
mvn spring-boot:run
```
This starts the backend on `http://localhost:8080`. It connects to the exact same `student_manager.db` file used by the JavaFX app.

### Start the Frontend
The frontend files are static. You can simply open `web-frontend/index.html` in your browser, or serve it via a simple HTTP server (e.g., `python -m http.server 8000`). Since the backend serves it automatically in production, CORS is enabled for local dev.

---

## 3. Production Deployment (Docker)

The web application is designed to be deployed as a single Docker container. The Spring Boot backend automatically serves the frontend static files.

### Security Note
**Never commit secrets!** Environment variables control configuration, and no credentials are hardcoded.

### Building the Image
```bash
docker build -t student-db-web .
```

### Running in Production & Migrating Data
Since SQLite stores data in a file, you **must use a Docker Volume** to prevent data loss when the container restarts. 

To migrate your existing local database into production, simply mount the local file or directory into the container's `/data` directory:

```bash
# Run the container, exposing port 8080, and mapping your local folder containing student_manager.db to /data
docker run -d -p 8080:8080 -v $(pwd):/data student-db-web
```
*If deploying to a cloud provider like Render or Railway, configure a **Persistent Disk** mounted at `/data`.*

Access the live web app at: `http://localhost:8080/`

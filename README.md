# Student Database Management System

A complete desktop application built with JavaFX, SQLite, and JDBC to manage student records.

## Features
- **Dashboard:** View statistics on total students and departments.
- **Student Management (CRUD):** Add, Edit, Delete, and Clear student forms.
- **Search:** Quickly search for students by name, roll number, or department.
- **Validation:** Ensures valid email, phone, CGPA, and prevents duplicate roll numbers.
- **Persistence:** Uses a local SQLite database (`student_manager.db`) stored in the project root.

## Architecture
- **Language:** Java 17
- **GUI:** JavaFX with FXML and CSS
- **Database:** SQLite & JDBC
- **Build Tool:** Maven

## Prerequisites
- JDK 17 or higher
- Maven (installed and added to PATH)

## Setup and Execution

1. **Clone the repository (if not already done):**
   ```bash
   git clone <repository_url>
   cd Student-Database-Management-System
   ```

2. **Run the application using Maven:**
   ```bash
   mvn clean javafx:run
   ```
   *Note: If you are using an IDE like IntelliJ or Eclipse, you can run the `com.studentdb.Main` class directly (ensure JavaFX is configured in your IDE settings).*

## Project Structure
- `src/main/java/com/studentdb/` - Contains the Java source code (MVC structure).
- `src/main/resources/` - Contains the FXML layouts and CSS styles.
- `student_manager.db` - Automatically created on the first run.

# Sports Equipment Management Application

This Java-based CRUD (Create, Read, Update, Delete) application provides efficient management of sports equipment records using an embedded H2 database. It employs a clean, layered architecture to ensure maintainability and separation of concerns. Each equipment item is automatically timestamped when added, and the application displays how long each item has been in inventory.

## Features

- Add new sports equipment entries with automatic timestamp
- Retrieve and display all equipment with time in inventory
- Search for equipment by ID
- Modify existing equipment details
- Remove equipment records
- Track inventory duration (days, hours, minutes)

## Prerequisites

- Java 17 or higher
- H2 Database (included in the lib directory)

## How to Compile

```bash
javac -cp "src/main/java;lib/h2-2.3.232.jar" src/main/java/com/crud/crud/entities/*.java src/main/java/com/crud/crud/repositories/*.java src/main/java/com/crud/crud/services/*.java src/main/java/com/crud/crud/views/*.java src/main/java/com/crud/crud/*.java
```

## How to Run

### Using Maven:
```bash
mvn exec:java
```

### Using Java directly:
```bash
java -cp "src/main/java;lib/h2-2.3.232.jar" com.crud.crud.Application
```

## Database

The application uses an H2 embedded database which is stored in a local file named `sports_equipment_db.mv.db` in the project directory. The database is automatically created on first run. Each equipment record includes a timestamp of when it was added to inventory.

## Application Structure

The application follows a layered architecture:

- `com.crud.crud.Application` - Main entry point
- `com.crud.crud.entities` - Data models (SportsEquipment)
- `com.crud.crrud.repositories` - Data access layer (SportsEquipmentRepository)
- `com.crud.crud.services` - Business logic layer (SportsEquipmentService)
- `com.crud.crud.views` - User interface layer (SportsEquipmentView)

## Usage

The application provides a user-friendly GUI interface with:
- Input fields for equipment details
- A table to display all equipment
- Buttons for Create, Update, Delete, Clear, and Refresh operations
- Automatic selection handling when clicking on table rows

## Sports Equipment Model

Each sports equipment record contains:
- ID (auto-generated)
- Name (text)
- Category (text)
- Price (decimal)
- Quantity (integer)
- Date Added (timestamp)
- Time in Inventory (years, months, days, hours, minutes) - updates in real-time every 30 seconds
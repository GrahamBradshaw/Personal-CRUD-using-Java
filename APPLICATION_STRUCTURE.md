# Application Structure

```
com.crud.crud
├── Application.java (main entry point)
├── entities
│   └── SportsEquipment.java (data model)
├── repositories
│   └── SportsEquipmentRepository.java (data access layer)
├── services
│   └── SportsEquipmentService.java (business logic layer)
└── views
    └── SportsEquipmentView.java (GUI layer)
```

## Layer Responsibilities

### Entities Layer
- Contains data models that represent the structure of the data
- [SportsEquipment.java](file:///c%3A/Users/Admin/Documents/NetBeansProjects/CRUD/src/main/java/com/crud/crud/entities/SportsEquipment.java#L1-L80) represents a sports equipment item with its properties

### Repositories Layer
- Handles direct database operations
- [SportsEquipmentRepository.java](file:///c%3A/Users/Admin/Documents/NetBeansProjects/CRUD/src/main/java/com/crud/crud/repositories/SportsEquipmentRepository.java#L1-L147) contains all CRUD operations for sports equipment

### Services Layer
- Contains business logic and validation
- [SportsEquipmentService.java](file:///c%3A/Users/Admin/Documents/NetBeansProjects/CRUD/src/main/java/com/crud/crud/services/SportsEquipmentService.java#L1-L69) acts as an intermediary between the view and repository layers

### Views Layer
- Handles the user interface
- [SportsEquipmentView.java](file:///c%3A/Users/Admin/Documents/NetBeansProjects/CRUD/src/main/java/com/crud/crud/views/SportsEquipmentView.java#L1-L341) provides the GUI for interacting with the application

### Main Application
- [Application.java](file:///c%3A/Users/Admin/Documents/NetBeansProjects/CRUD/src/main/java/com/crud/crud/Application.java#L1-L21) serves as the single entry point for the application
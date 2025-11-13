package com.crud.crud.repositories;

import com.crud.crud.entities.SportsEquipment;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class SportsEquipmentRepository {
    private static final String DB_URL = "jdbc:h2:./sports_equipment_db";
    private static final String DB_USER = "sa";
    private static final String DB_PASSWORD = "";

    // Initialize the database and create the table if it doesn't exist
    public static void initializeDatabase() throws SQLException {
        // Load the H2 driver
        try {
            Class.forName("org.h2.Driver");
        } catch (ClassNotFoundException e) {
            throw new SQLException("H2 Driver not found", e);
        }
        
        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            // Create table if it doesn't exist
            String createTableSQL = """
                CREATE TABLE IF NOT EXISTS sports_equipment (
                    id INT AUTO_INCREMENT PRIMARY KEY,
                    name VARCHAR(255) NOT NULL,
                    category VARCHAR(255) NOT NULL,
                    price DOUBLE NOT NULL,
                    quantity INT NOT NULL
                )
                """;
            
            try (Statement statement = connection.createStatement()) {
                statement.execute(createTableSQL);
                
                // Try to add the date_added column if it doesn't exist
                try {
                    String addColumnSQL = "ALTER TABLE sports_equipment ADD COLUMN date_added TIMESTAMP DEFAULT CURRENT_TIMESTAMP";
                    statement.execute(addColumnSQL);
                } catch (SQLException e) {
                    // Column may already exist, ignore this error
                    // System.out.println("Column date_added might already exist: " + e.getMessage());
                }
                
                System.out.println("Database initialized successfully.");
            }
        }
    }

    // Create a new sports equipment record
    public static int createEquipment(SportsEquipment equipment) throws SQLException {
        String insertSQL = "INSERT INTO sports_equipment (name, category, price, quantity, date_added) VALUES (?, ?, ?, ?, ?)";
        
        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement(insertSQL, Statement.RETURN_GENERATED_KEYS)) {
            
            preparedStatement.setString(1, equipment.getName());
            preparedStatement.setString(2, equipment.getCategory());
            preparedStatement.setDouble(3, equipment.getPrice());
            preparedStatement.setInt(4, equipment.getQuantity());
            preparedStatement.setTimestamp(5, Timestamp.valueOf(equipment.getDateAdded()));
            
            int affectedRows = preparedStatement.executeUpdate();
            
            if (affectedRows == 0) {
                throw new SQLException("Creating equipment failed, no rows affected.");
            }
            
            try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    return generatedKeys.getInt(1);
                } else {
                    throw new SQLException("Creating equipment failed, no ID obtained.");
                }
            }
        }
    }

    // Read all sports equipment records
    public static List<SportsEquipment> getAllEquipment() throws SQLException {
        List<SportsEquipment> equipmentList = new ArrayList<>();
        String selectSQL = "SELECT id, name, category, price, quantity, date_added FROM sports_equipment";
        
        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(selectSQL)) {
            
            while (resultSet.next()) {
                SportsEquipment equipment = new SportsEquipment();
                equipment.setId(resultSet.getInt("id"));
                equipment.setName(resultSet.getString("name"));
                equipment.setCategory(resultSet.getString("category"));
                equipment.setPrice(resultSet.getDouble("price"));
                equipment.setQuantity(resultSet.getInt("quantity"));
                
                // Handle potential null values for date_added
                try {
                    Timestamp timestamp = resultSet.getTimestamp("date_added");
                    if (timestamp != null) {
                        equipment.setDateAdded(timestamp.toLocalDateTime());
                    } else {
                        // Set to current time if null (for existing records without date)
                        equipment.setDateAdded(LocalDateTime.now());
                    }
                } catch (Exception e) {
                    // Set to current time if there's any issue retrieving the date
                    equipment.setDateAdded(LocalDateTime.now());
                }
                
                equipmentList.add(equipment);
            }
        }
        
        return equipmentList;
    }

    // Read a specific sports equipment by ID
    public static SportsEquipment getEquipmentById(int id) throws SQLException {
        String selectSQL = "SELECT id, name, category, price, quantity, date_added FROM sports_equipment WHERE id = ?";
        
        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement(selectSQL)) {
            
            preparedStatement.setInt(1, id);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    SportsEquipment equipment = new SportsEquipment();
                    equipment.setId(resultSet.getInt("id"));
                    equipment.setName(resultSet.getString("name"));
                    equipment.setCategory(resultSet.getString("category"));
                    equipment.setPrice(resultSet.getDouble("price"));
                    equipment.setQuantity(resultSet.getInt("quantity"));
                    
                    // Handle potential null values for date_added
                    try {
                        Timestamp timestamp = resultSet.getTimestamp("date_added");
                        if (timestamp != null) {
                            equipment.setDateAdded(timestamp.toLocalDateTime());
                        } else {
                            // Set to current time if null (for existing records without date)
                            equipment.setDateAdded(LocalDateTime.now());
                        }
                    } catch (Exception e) {
                        // Set to current time if there's any issue retrieving the date
                        equipment.setDateAdded(LocalDateTime.now());
                    }
                    
                    return equipment;
                }
            }
        }
        
        return null;
    }

    // Update a sports equipment record
    public static boolean updateEquipment(SportsEquipment equipment) throws SQLException {
        String updateSQL = "UPDATE sports_equipment SET name = ?, category = ?, price = ?, quantity = ? WHERE id = ?";
        
        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement(updateSQL)) {
            
            preparedStatement.setString(1, equipment.getName());
            preparedStatement.setString(2, equipment.getCategory());
            preparedStatement.setDouble(3, equipment.getPrice());
            preparedStatement.setInt(4, equipment.getQuantity());
            preparedStatement.setInt(5, equipment.getId());
            
            int affectedRows = preparedStatement.executeUpdate();
            return affectedRows > 0;
        }
    }

    // Delete a sports equipment record
    public static boolean deleteEquipment(int id) throws SQLException {
        String deleteSQL = "DELETE FROM sports_equipment WHERE id = ?";
        
        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement(deleteSQL)) {
            
            preparedStatement.setInt(1, id);
            
            int affectedRows = preparedStatement.executeUpdate();
            return affectedRows > 0;
        }
    }
}
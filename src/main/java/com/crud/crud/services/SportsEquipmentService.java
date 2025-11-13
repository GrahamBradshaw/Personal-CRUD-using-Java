package com.crud.crud.services;

import com.crud.crud.entities.SportsEquipment;
import com.crud.crud.repositories.SportsEquipmentRepository;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

public class SportsEquipmentService {
    
    public static void initializeDatabase() throws SQLException {
        SportsEquipmentRepository.initializeDatabase();
    }
    
    public static int createEquipment(SportsEquipment equipment) throws SQLException {
        // Set the date added if not already set
        if (equipment.getDateAdded() == null) {
            equipment.setDateAdded(LocalDateTime.now());
        }
        
        // Validate equipment before creating
        if (equipment.getName() == null || equipment.getName().trim().isEmpty()) {
            throw new IllegalArgumentException("Equipment name cannot be empty");
        }
        
        if (equipment.getCategory() == null || equipment.getCategory().trim().isEmpty()) {
            throw new IllegalArgumentException("Equipment category cannot be empty");
        }
        
        if (equipment.getPrice() < 0) {
            throw new IllegalArgumentException("Equipment price cannot be negative");
        }
        
        if (equipment.getQuantity() < 0) {
            throw new IllegalArgumentException("Equipment quantity cannot be negative");
        }
        
        return SportsEquipmentRepository.createEquipment(equipment);
    }
    
    public static List<SportsEquipment> getAllEquipment() throws SQLException {
        return SportsEquipmentRepository.getAllEquipment();
    }
    
    public static SportsEquipment getEquipmentById(int id) throws SQLException {
        return SportsEquipmentRepository.getEquipmentById(id);
    }
    
    public static boolean updateEquipment(SportsEquipment equipment) throws SQLException {
        // Validate equipment before updating
        if (equipment.getName() == null || equipment.getName().trim().isEmpty()) {
            throw new IllegalArgumentException("Equipment name cannot be empty");
        }
        
        if (equipment.getCategory() == null || equipment.getCategory().trim().isEmpty()) {
            throw new IllegalArgumentException("Equipment category cannot be empty");
        }
        
        if (equipment.getPrice() < 0) {
            throw new IllegalArgumentException("Equipment price cannot be negative");
        }
        
        if (equipment.getQuantity() < 0) {
            throw new IllegalArgumentException("Equipment quantity cannot be negative");
        }
        
        return SportsEquipmentRepository.updateEquipment(equipment);
    }
    
    public static boolean deleteEquipment(int id) throws SQLException {
        return SportsEquipmentRepository.deleteEquipment(id);
    }
}
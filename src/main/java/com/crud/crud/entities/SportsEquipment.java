package com.crud.crud.entities;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

public class SportsEquipment {
    private int id;
    private String name;
    private String category;
    private double price;
    private int quantity;
    private LocalDateTime dateAdded;

    // Constructors
    public SportsEquipment() {}

    public SportsEquipment(String name, String category, double price, int quantity) {
        this.name = name;
        this.category = category;
        this.price = price;
        this.quantity = quantity;
        this.dateAdded = LocalDateTime.now();
    }

    public SportsEquipment(int id, String name, String category, double price, int quantity, LocalDateTime dateAdded) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.price = price;
        this.quantity = quantity;
        this.dateAdded = dateAdded;
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public LocalDateTime getDateAdded() {
        return dateAdded;
    }

    public void setDateAdded(LocalDateTime dateAdded) {
        this.dateAdded = dateAdded;
    }

    // Calculate time in inventory with years, months, days, hours, minutes
    public String getTimeInInventory() {
        if (dateAdded == null) return "Unknown";
        
        LocalDateTime now = LocalDateTime.now();
        
        // Calculate differences
        long years = ChronoUnit.YEARS.between(dateAdded, now);
        LocalDateTime temp = dateAdded.plusYears(years);
        
        long months = ChronoUnit.MONTHS.between(temp, now);
        temp = temp.plusMonths(months);
        
        long days = ChronoUnit.DAYS.between(temp, now);
        temp = temp.plusDays(days);
        
        long hours = ChronoUnit.HOURS.between(temp, now);
        temp = temp.plusHours(hours);
        
        long minutes = ChronoUnit.MINUTES.between(temp, now);
        
        // Build the formatted string
        StringBuilder result = new StringBuilder();
        if (years > 0) result.append(years).append(" year").append(years > 1 ? "s" : "").append(", ");
        if (months > 0) result.append(months).append(" month").append(months > 1 ? "s" : "").append(", ");
        if (days > 0) result.append(days).append(" day").append(days > 1 ? "s" : "").append(", ");
        if (hours > 0) result.append(hours).append(" hour").append(hours > 1 ? "s" : "").append(", ");
        result.append(minutes).append(" minute").append(minutes > 1 ? "s" : "");
        
        // Handle special case where all values are zero
        if (result.length() == 0) {
            return "0 minutes";
        }
        
        // Remove trailing comma and space if present
        String resultStr = result.toString();
        if (resultStr.endsWith(", ")) {
            resultStr = resultStr.substring(0, resultStr.length() - 2);
        }
        
        return resultStr;
    }

    // Format date for display
    public String getFormattedDateAdded() {
        if (dateAdded == null) return "Unknown";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        return dateAdded.format(formatter);
    }

    @Override
    public String toString() {
        return "SportsEquipment{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", category='" + category + '\'' +
                ", price=" + price +
                ", quantity=" + quantity +
                ", dateAdded=" + (dateAdded != null ? dateAdded.toString() : "null") +
                '}';
    }
}
package com.crud.crud.views;

import com.crud.crud.entities.SportsEquipment;
import com.crud.crud.services.SportsEquipmentService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class SportsEquipmentView extends JFrame {
    private JTable equipmentTable;
    private DefaultTableModel tableModel;
    private JTextField nameField, categoryField, priceField, quantityField, idField;
    private JButton createButton, updateButton, deleteButton, clearButton, refreshButton;
    private Timer refreshTimer;
    
    public SportsEquipmentView() {
        initializeDatabase();
        initializeComponents();
        setupLayout();
        setupEventHandlers();
        refreshTable();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("Sports Equipment CRUD Application");
        setSize(1000, 600);
        setLocationRelativeTo(null); // Center the window
        
        // Start real-time refresh timer (every 30 seconds)
        startRealTimeRefresh();
    }
    
    private void initializeDatabase() {
        try {
            SportsEquipmentService.initializeDatabase();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Database initialization error: " + e.getMessage(), 
                                          "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void initializeComponents() {
        // Table setup
        tableModel = new DefaultTableModel(new Object[]{"ID", "Name", "Category", "Price", "Quantity", "Date Added", "Time in Inventory"}, 0);
        equipmentTable = new JTable(tableModel);
        equipmentTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        
        // Input fields
        nameField = new JTextField(20);
        categoryField = new JTextField(20);
        priceField = new JTextField(20);
        quantityField = new JTextField(20);
        idField = new JTextField(20);
        idField.setEditable(false); // ID is auto-generated
        
        // Buttons
        createButton = new JButton("Create");
        updateButton = new JButton("Update");
        deleteButton = new JButton("Delete");
        clearButton = new JButton("Clear");
        refreshButton = new JButton("Refresh");
        
        // Disable update/delete buttons initially
        updateButton.setEnabled(false);
        deleteButton.setEnabled(false);
    }
    
    private void setupLayout() {
        setLayout(new BorderLayout());
        
        // Top panel for input fields
        JPanel inputPanel = new JPanel(new GridBagLayout());
        inputPanel.setBorder(BorderFactory.createTitledBorder("Equipment Details"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        
        // ID field
        gbc.gridx = 0; gbc.gridy = 0; gbc.anchor = GridBagConstraints.WEST;
        inputPanel.add(new JLabel("ID:"), gbc);
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL;
        inputPanel.add(idField, gbc);
        
        // Name field
        gbc.gridx = 0; gbc.gridy = 1; gbc.fill = GridBagConstraints.NONE;
        inputPanel.add(new JLabel("Name:"), gbc);
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL;
        inputPanel.add(nameField, gbc);
        
        // Category field
        gbc.gridx = 0; gbc.gridy = 2; gbc.fill = GridBagConstraints.NONE;
        inputPanel.add(new JLabel("Category:"), gbc);
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL;
        inputPanel.add(categoryField, gbc);
        
        // Price field
        gbc.gridx = 0; gbc.gridy = 3; gbc.fill = GridBagConstraints.NONE;
        inputPanel.add(new JLabel("Price:"), gbc);
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL;
        inputPanel.add(priceField, gbc);
        
        // Quantity field
        gbc.gridx = 0; gbc.gridy = 4; gbc.fill = GridBagConstraints.NONE;
        inputPanel.add(new JLabel("Quantity:"), gbc);
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL;
        inputPanel.add(quantityField, gbc);
        
        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.add(createButton);
        buttonPanel.add(updateButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(clearButton);
        buttonPanel.add(refreshButton);
        
        gbc.gridx = 0; gbc.gridy = 5; gbc.gridwidth = 2; gbc.fill = GridBagConstraints.NONE;
        inputPanel.add(buttonPanel, gbc);
        
        // Table panel with scroll pane
        JScrollPane scrollPane = new JScrollPane(equipmentTable);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Equipment List"));
        
        // Add components to frame
        add(inputPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
    }
    
    private void setupEventHandlers() {
        // Table selection listener
        equipmentTable.getSelectionModel().addListSelectionListener(e -> {
            int selectedRow = equipmentTable.getSelectedRow();
            if (selectedRow >= 0) {
                idField.setText(tableModel.getValueAt(selectedRow, 0).toString());
                nameField.setText(tableModel.getValueAt(selectedRow, 1).toString());
                categoryField.setText(tableModel.getValueAt(selectedRow, 2).toString());
                priceField.setText(tableModel.getValueAt(selectedRow, 3).toString());
                quantityField.setText(tableModel.getValueAt(selectedRow, 4).toString());
                
                // Enable update/delete buttons
                updateButton.setEnabled(true);
                deleteButton.setEnabled(true);
            }
        });
        
        // Create button
        createButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                createEquipment();
            }
        });
        
        // Update button
        updateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateEquipment();
            }
        });
        
        // Delete button
        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deleteEquipment();
            }
        });
        
        // Clear button
        clearButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                clearFields();
            }
        });
        
        // Refresh button
        refreshButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                refreshTable();
            }
        });
    }
    
    private void startRealTimeRefresh() {
        // Create a timer that refreshes the table every 30 seconds
        refreshTimer = new Timer();
        refreshTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                // Update the time in inventory display on the EDT
                SwingUtilities.invokeLater(() -> refreshTable());
            }
        }, 30000, 30000); // 30 seconds delay, 30 seconds period
    }
    
    private void createEquipment() {
        try {
            String name = nameField.getText().trim();
            String category = categoryField.getText().trim();
            String priceText = priceField.getText().trim();
            String quantityText = quantityField.getText().trim();
            
            // Validate inputs
            if (name.isEmpty() || category.isEmpty() || priceText.isEmpty() || quantityText.isEmpty()) {
                JOptionPane.showMessageDialog(this, "All fields are required!", "Input Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            double price = Double.parseDouble(priceText);
            int quantity = Integer.parseInt(quantityText);
            
            if (price < 0 || quantity < 0) {
                JOptionPane.showMessageDialog(this, "Price and quantity must be non-negative!", "Input Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            SportsEquipment equipment = new SportsEquipment(name, category, price, quantity);
            int id = SportsEquipmentService.createEquipment(equipment);
            
            JOptionPane.showMessageDialog(this, "Equipment created successfully with ID: " + id, "Success", JOptionPane.INFORMATION_MESSAGE);
            clearFields();
            refreshTable();
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Price and quantity must be valid numbers!", "Input Error", JOptionPane.ERROR_MESSAGE);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Database error: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error creating equipment: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void updateEquipment() {
        try {
            int id = Integer.parseInt(idField.getText().trim());
            String name = nameField.getText().trim();
            String category = categoryField.getText().trim();
            String priceText = priceField.getText().trim();
            String quantityText = quantityField.getText().trim();
            
            // Validate inputs
            if (name.isEmpty() || category.isEmpty() || priceText.isEmpty() || quantityText.isEmpty()) {
                JOptionPane.showMessageDialog(this, "All fields are required!", "Input Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            double price = Double.parseDouble(priceText);
            int quantity = Integer.parseInt(quantityText);
            
            if (price < 0 || quantity < 0) {
                JOptionPane.showMessageDialog(this, "Price and quantity must be non-negative!", "Input Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            SportsEquipment existingEquipment = SportsEquipmentService.getEquipmentById(id);
            boolean success = false;
            if (existingEquipment != null) {
                // Preserve the original date added
                SportsEquipment equipment = new SportsEquipment(id, name, category, price, quantity, existingEquipment.getDateAdded());
                success = SportsEquipmentService.updateEquipment(equipment);
            } else {
                // Fallback if equipment not found
                SportsEquipment equipment = new SportsEquipment(name, category, price, quantity);
                equipment.setId(id);
                success = SportsEquipmentService.updateEquipment(equipment);
            }
            
            if (success) {
                JOptionPane.showMessageDialog(this, "Equipment updated successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                clearFields();
                refreshTable();
            } else {
                JOptionPane.showMessageDialog(this, "Failed to update equipment!", "Update Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Price and quantity must be valid numbers!", "Input Error", JOptionPane.ERROR_MESSAGE);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Database error: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error updating equipment: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void deleteEquipment() {
        try {
            int id = Integer.parseInt(idField.getText().trim());
            
            int confirm = JOptionPane.showConfirmDialog(this, 
                "Are you sure you want to delete equipment with ID " + id + "?", 
                "Confirm Delete", JOptionPane.YES_NO_OPTION);
            
            if (confirm == JOptionPane.YES_OPTION) {
                boolean success = SportsEquipmentService.deleteEquipment(id);
                
                if (success) {
                    JOptionPane.showMessageDialog(this, "Equipment deleted successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                    clearFields();
                    refreshTable();
                } else {
                    JOptionPane.showMessageDialog(this, "Failed to delete equipment!", "Delete Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Database error: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error deleting equipment: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void refreshTable() {
        try {
            // Clear existing data
            tableModel.setRowCount(0);
            
            // Load data from database
            List<SportsEquipment> equipmentList = SportsEquipmentService.getAllEquipment();
            
            for (SportsEquipment equipment : equipmentList) {
                Object[] row = {
                    equipment.getId(),
                    equipment.getName(),
                    equipment.getCategory(),
                    equipment.getPrice(),
                    equipment.getQuantity(),
                    equipment.getFormattedDateAdded(),
                    equipment.getTimeInInventory()
                };
                tableModel.addRow(row);
            }
            
            // Reset button states
            updateButton.setEnabled(false);
            deleteButton.setEnabled(false);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Database error: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void clearFields() {
        idField.setText("");
        nameField.setText("");
        categoryField.setText("");
        priceField.setText("");
        quantityField.setText("");
        
        // Clear table selection
        equipmentTable.clearSelection();
        
        // Reset button states
        updateButton.setEnabled(false);
        deleteButton.setEnabled(false);
    }
    
    // Clean up resources when window is closed
    @Override
    public void dispose() {
        if (refreshTimer != null) {
            refreshTimer.cancel();
        }
        super.dispose();
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                e.printStackTrace();
            }
            
            new SportsEquipmentView().setVisible(true);
        });
    }
}
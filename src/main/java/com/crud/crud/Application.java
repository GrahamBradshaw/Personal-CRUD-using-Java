package com.crud.crud;

import com.crud.crud.views.SportsEquipmentView;

import javax.swing.*;

public class Application {
    public static void main(String[] args) {
        // Launch GUI version by default
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
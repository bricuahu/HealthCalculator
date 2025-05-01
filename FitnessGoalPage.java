package loginandsignup;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class FitnessGoalPage extends JFrame {
    private JComboBox<String> goalCombo;
    private int age;
    private double height;
    private double weight;
    private String gender;
    private String email;

    public FitnessGoalPage(int age, double height, double weight, String gender, String email) {
        this.age = age;
        this.height = height;
        this.weight = weight;
        this.gender = gender;
        this.email = email;
        
        setTitle("Fitness Goal");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setPreferredSize(new Dimension(1000, 500));

        JPanel mainPanel = new JPanel();
        mainPanel.setBackground(Color.WHITE);
        mainPanel.setLayout(null);

        // Left Panel (Blue background with title)
        JPanel leftPanel = new JPanel();
        leftPanel.setBackground(new Color(20, 195, 60));
        leftPanel.setBounds(0, 0, 400, 500);
        leftPanel.setLayout(new GridBagLayout());

        JLabel titleLabel = new JLabel("Fitness Goal");
        titleLabel.setFont(new Font("Times New Roman", Font.BOLD, 48));
        titleLabel.setForeground(Color.WHITE);
        leftPanel.add(titleLabel);

        // Right Panel (Form)
        JPanel rightPanel = new JPanel();
        rightPanel.setBackground(Color.WHITE);
        rightPanel.setBounds(400, 0, 400, 500);
        rightPanel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(15, 15, 15, 15);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel formTitle = new JLabel("Select Your Goal");
        formTitle.setFont(new Font("Segoe UI", Font.BOLD, 36));
        formTitle.setForeground(new Color(20, 195, 60));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        rightPanel.add(formTitle, gbc);

        JLabel goalLabel = new JLabel("Fitness Goal:");
        goalCombo = new JComboBox<>(new String[]{"Lose Weight", "Maintain Weight", "Gain Weight"});
        goalCombo.setPreferredSize(new Dimension(200, 30));
        
        JButton calculateButton = new JButton("Calculate Nutrition Plan");
        calculateButton.setBackground(new Color(20, 195, 60));
        calculateButton.setForeground(Color.BLACK);
        calculateButton.setPreferredSize(new Dimension(200, 35));
        calculateButton.addActionListener(this::calculateNutrition);

        // Add components to right panel
        gbc.gridwidth = 1;
        gbc.gridy = 1;
        gbc.gridx = 0;
        rightPanel.add(goalLabel, gbc);
        
        gbc.gridx = 1;
        rightPanel.add(goalCombo, gbc);
        
        gbc.gridy = 2;
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        rightPanel.add(calculateButton, gbc);

        // Add profile summary
        JLabel profileLabel = new JLabel(String.format(
            "<html><div style='text-align:center;'><h3>Your Profile</h3>" +
            "<p>Age: %d</p>" +
            "<p>Height: %.1f cm</p>" +
            "<p>Weight: %.1f kg</p>" +
            "<p>Gender: %s</p></div></html>",
            age, height, weight, gender
        ));
        gbc.gridy = 3;
        rightPanel.add(profileLabel, gbc);

        mainPanel.add(leftPanel);
        mainPanel.add(rightPanel);

        add(mainPanel);
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void calculateNutrition(ActionEvent e) {
        String goal = (String) goalCombo.getSelectedItem();
        double bmr = calculateBMR();
        double dailyCalories = adjustCaloriesForGoal(bmr, goal);
        double[] macros = calculateMacronutrients(dailyCalories);
        
        saveNutritionPlan(goal, dailyCalories, macros);
        showResults(goal, dailyCalories, macros);
        dispose();
    }

    private double calculateBMR() {
        return gender.equalsIgnoreCase("Male") 
            ? 88.362 + (13.397 * weight) + (4.799 * height) - (5.677 * age)
            : 447.593 + (9.247 * weight) + (3.098 * height) - (4.330 * age);
    }

    private double adjustCaloriesForGoal(double bmr, String goal) {
        switch (goal) {
            case "Lose Weight": return bmr * 1.2 - 500;
            case "Gain Weight": return bmr * 1.2 + 500;
            default: return bmr * 1.2;
        }
    }

    private double[] calculateMacronutrients(double calories) {
        double protein = weight * 2.2;
        double fat = (calories * 0.25) / 9;
        double carbs = (calories - (protein * 4) - (fat * 9)) / 4;
        return new double[]{protein, carbs, fat};
    }

    private void saveNutritionPlan(String goal, double calories, double[] macros) {
        String date = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        try (FileWriter writer = new FileWriter("nutrition_plans.txt", true)) {
            writer.write(String.format(
                "Date: %s, Email: %s, Goal: %s, Calories: %.0f, Protein: %.0fg, Carbs: %.0fg, Fats: %.0fg\n",
                date, email, goal, calories, macros[0], macros[1], macros[2]
            ));
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, "Error saving nutrition plan: " + ex.getMessage());
        }
    }

    private void showResults(String goal, double calories, double[] macros) {
        String result = String.format(
            "<html><div style='width:300px;'><h2>Your Nutrition Plan (%s)</h2>" +
            "<p><b>Daily Calories:</b> %.0f kcal</p>" +
            "<p><b>Protein:</b> %.0f g</p>" +
            "<p><b>Carbohydrates:</b> %.0f g</p>" +
            "<p><b>Fats:</b> %.0f g</p><br>" +
            "<p>Based on your profile:</p>" +
            "<p>Age: %d, Height: %.1f cm, Weight: %.1f kg, Gender: %s</p></div></html>",
            goal, calories, macros[0], macros[1], macros[2], age, height, weight, gender
        );
        JOptionPane.showMessageDialog(this, result, "Nutrition Plan", JOptionPane.INFORMATION_MESSAGE);
    }
}
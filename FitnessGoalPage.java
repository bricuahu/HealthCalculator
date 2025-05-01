package loginandsignup;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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
        setSize(400, 250);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel titleLabel = new JLabel("Select Your Fitness Goal");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
        
        JLabel goalLabel = new JLabel("Goal:");
        goalCombo = new JComboBox<>(new String[]{"Lose Weight", "Maintain Weight", "Gain Weight"});
        
        JButton calculateButton = new JButton("Calculate Nutrition Plan");
        calculateButton.addActionListener(this::calculateNutrition);

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        panel.add(titleLabel, gbc);
        
        gbc.gridwidth = 1;
        gbc.gridy = 1;
        gbc.gridx = 0;
        panel.add(goalLabel, gbc);
        
        gbc.gridx = 1;
        panel.add(goalCombo, gbc);
        
        gbc.gridy = 2;
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        panel.add(calculateButton, gbc);

        add(panel);
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
            ? 88.362 + (13.397 * weight) + (4.799 * height) - (5.677 * age) //Male calculations
            : 447.593 + (9.247 * weight) + (3.098 * height) - (4.330 * age); //Female calculations
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
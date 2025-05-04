package loginandsignup;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.*;
import java.util.*;

public class HealthProgressTracker extends JFrame {
    private int age;
    private double height;
    private double initialWeight;
    private String gender;
    private String userEmail;
    private String goal;

    private JTextArea outputArea;
    private JTextField weightField;

    public HealthProgressTracker(int age, double height, double weight, String gender, String userEmail, String goal) {
        this.age = age;
        this.height = height;
        this.initialWeight = weight;
        this.gender = gender;
        this.userEmail = userEmail;
        this.goal = goal;

        setTitle("Health Progress Tracker");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setPreferredSize(new Dimension(1000, 500));
        JPanel mainPanel = new JPanel();
        mainPanel.setBackground(Color.WHITE);
        mainPanel.setLayout(null);

        // Left panel
        JPanel leftPanel = new JPanel();
        leftPanel.setBackground(new Color(20, 195, 60));
        leftPanel.setBounds(0, 0, 400, 500);
        leftPanel.setLayout(new GridBagLayout());
        JLabel titleLabel = new JLabel("Track Progress");
        titleLabel.setFont(new Font("Times New Roman", Font.BOLD, 48));
        titleLabel.setForeground(Color.WHITE);
        leftPanel.add(titleLabel);

        // Right panel
        JPanel rightPanel = new JPanel();
        rightPanel.setBackground(Color.WHITE);
        rightPanel.setBounds(400, 0, 600, 500);
        rightPanel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Title
        JLabel formTitle = new JLabel("Enter Your Current Weight");
        formTitle.setFont(new Font("Segoe UI", Font.BOLD, 28));
        formTitle.setForeground(new Color(20, 195, 60));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        rightPanel.add(formTitle, gbc);

        // Input
        JLabel weightLabel = new JLabel("Current Weight (kg):");
        weightField = new JTextField(10);
        JButton submitBtn = new JButton("Submit Weight");
        submitBtn.setBackground(new Color(20, 195, 60));

        // Navigation buttons
        JButton logoutBtn = new JButton("Log Out");
        JButton backBtn = new JButton("Back to Dashboard");
        logoutBtn.setBackground(Color.LIGHT_GRAY);
        backBtn.setBackground(Color.LIGHT_GRAY);

        gbc.gridy = 1;
        gbc.gridwidth = 1;
        rightPanel.add(weightLabel, gbc);
        gbc.gridx = 1;
        rightPanel.add(weightField, gbc);

        gbc.gridy = 2;
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        rightPanel.add(submitBtn, gbc);

        // Output area
        outputArea = new JTextArea(8, 30);  // Set rows and columns visibly
outputArea.setLineWrap(true);       // Wrap long text instead of scrolling horizontally
outputArea.setWrapStyleWord(true);
outputArea.setEditable(false);
outputArea.setFont(new Font("Monospaced", Font.PLAIN, 13));

// Wrap in scroll pane
JScrollPane scrollPane = new JScrollPane(outputArea);
scrollPane.setPreferredSize(new Dimension(500, 150));  // 💡 Ensure it has a visible size

gbc.gridy = 3;
gbc.gridwidth = 2;  // Take full width
rightPanel.add(scrollPane, gbc);


        // Profile summary
        JLabel profileLabel = new JLabel(String.format(
            "<html><div style='text-align:center;'><h3>Your Profile</h3>" +
            "<p>Age: %d</p>" +
            "<p>Height: %.1f cm</p>" +
            "<p>Weight: %.1f kg</p>" +
            "<p>Gender: %s</p>" +
            "<p>Goal: %s</p></div></html>",
            age, height, initialWeight, gender, goal
        ));
        gbc.gridy = 4;
        rightPanel.add(profileLabel, gbc);

        gbc.gridy = 5;
        rightPanel.add(backBtn, gbc);
        gbc.gridy = 6;
        rightPanel.add(logoutBtn, gbc);

        mainPanel.add(leftPanel);
        mainPanel.add(rightPanel);
        add(mainPanel);
        pack();
        setLocationRelativeTo(null);
        setVisible(true);

        // Button actions
        submitBtn.addActionListener(this::handleSubmit);
        logoutBtn.addActionListener(e -> {
            new Login().setVisible(true);
            dispose();
        });
        backBtn.addActionListener(e -> {
            new FitnessGoalPage(age, height, initialWeight, gender, userEmail);
            dispose();
        });
    }

    private void handleSubmit(ActionEvent e) {
        String input = weightField.getText();
        try {
            double currentWeight = Double.parseDouble(input);

            logWeightEntry(currentWeight);
            List<Double> history = readWeightHistory();
            String graph = generateGraph(history);
            outputArea.setText(graph);
            saveToFile(graph);

            // Goal tracking
            if (goal.equalsIgnoreCase("Lose Weight") && currentWeight <= initialWeight - 5) {
                JOptionPane.showMessageDialog(this, "🎉 Goal reached: You’ve lost enough weight!");
                weightField.setEnabled(false);
            } else if (goal.equalsIgnoreCase("Gain Weight") && currentWeight >= initialWeight + 5) {
                JOptionPane.showMessageDialog(this, "🎉 Goal reached: You’ve gained enough weight!");
                weightField.setEnabled(false);
            } else if (goal.equalsIgnoreCase("Maintain Weight") && Math.abs(currentWeight - initialWeight) > 3) {
                JOptionPane.showMessageDialog(this, "⚠️ Alert: Your weight has changed significantly.");
            }

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Please enter a valid number.");
        }
    }

    // Save weight entry as just the number
    private void logWeightEntry(double weight) {
        String filename = "weight_log_" + userEmail.replaceAll("[^a-zA-Z0-9]", "_") + ".txt";
        try (PrintWriter writer = new PrintWriter(new FileWriter(filename, true))) {
            writer.println(weight);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error saving weight.");
        }
    }

    // Read all weights
    private List<Double> readWeightHistory() {
        List<Double> history = new ArrayList<>();
        String filename = "weight_log_" + userEmail.replaceAll("[^a-zA-Z0-9]", "_") + ".txt";
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = reader.readLine()) != null) {
                history.add(Double.parseDouble(line));
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "No history found.");
        }
        return history;
    }

    // Show  graph: Day X (weight): ===>
    private String generateGraph(List<Double> history) {
        StringBuilder graph = new StringBuilder();
        graph.append("Progress for: ").append(userEmail).append("\n");
        graph.append("Goal: ").append(goal).append("\n\n");

        for (int i = 0; i < history.size(); i++) {
            double weight = history.get(i);
            graph.append(String.format("Day %d (%.1f kg): ", i + 1, weight));
            int bars = (int) Math.abs(weight - initialWeight);
            for (int j = 0; j < bars; j++) graph.append("=");
            graph.append(">\n");
        }

        return graph.toString();
    }

    // Save entire output (optional)
    private void saveToFile(String content) {
        try (PrintWriter writer = new PrintWriter(new FileWriter("progress_graph.txt", true))) {
            writer.println("-----");
            writer.print(content);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error saving graph.");
        }
    }
}


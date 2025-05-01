package loginandsignup;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileWriter;
import java.io.IOException;

public class HealthDataPage extends JFrame {
    private JTextField ageField;
    private JTextField heightField;
    private JTextField weightField;
    private JComboBox<String> genderCombo;
    private String userEmail;

    public HealthDataPage() {
        setTitle("Health Data");
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

        JLabel titleLabel = new JLabel("Health Data");
        titleLabel.setFont(new Font("Times New Roman", Font.BOLD, 48));
        titleLabel.setForeground(Color.WHITE);
        leftPanel.add(titleLabel);

        // Right Panel (Form)
        JPanel rightPanel = new JPanel();
        rightPanel.setBackground(Color.WHITE);
        rightPanel.setBounds(400, 0, 600, 500);
        rightPanel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel formTitle = new JLabel("Enter Your Health Data");
        formTitle.setFont(new Font("Segoe UI", Font.BOLD, 36));
        formTitle.setForeground(new Color(20, 195, 60));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        rightPanel.add(formTitle, gbc);

        JLabel ageLabel = new JLabel("Age:");
        ageField = new JTextField(20);
        ageField.setPreferredSize(new Dimension(100, 30));

        JLabel heightLabel = new JLabel("Height (cm):");
        heightField = new JTextField(20);
        heightField.setPreferredSize(new Dimension(100, 30));

        JLabel weightLabel = new JLabel("Weight (kg):");
        weightField = new JTextField(20);
        weightField.setPreferredSize(new Dimension(100, 30));

        JLabel genderLabel = new JLabel("Gender:");
        genderCombo = new JComboBox<>(new String[]{"Male", "Female", "Other"});
        genderCombo.setPreferredSize(new Dimension(200, 30));

        JButton submitButton = new JButton("Submit");
        submitButton.setBackground(new Color(20, 195, 60));
        submitButton.setForeground(Color.BLACK);
        submitButton.setPreferredSize(new Dimension(100, 35));
        submitButton.addActionListener(new SubmitAction());

        // Add components to right panel
        gbc.gridwidth = 1;
        gbc.gridy = 1;
        rightPanel.add(ageLabel, gbc);
        
        gbc.gridx = 1;
        rightPanel.add(ageField, gbc);
        
        gbc.gridx = 0;
        gbc.gridy = 2;
        rightPanel.add(heightLabel, gbc);
        
        gbc.gridx = 1;
        rightPanel.add(heightField, gbc);
        
        gbc.gridx = 0;
        gbc.gridy = 3;
        rightPanel.add(weightLabel, gbc);
        
        gbc.gridx = 1;
        rightPanel.add(weightField, gbc);
        
        gbc.gridx = 0;
        gbc.gridy = 4;
        rightPanel.add(genderLabel, gbc);
        
        gbc.gridx = 1;
        rightPanel.add(genderCombo, gbc);
        
        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.CENTER;
        rightPanel.add(submitButton, gbc);

        mainPanel.add(leftPanel);
        mainPanel.add(rightPanel);

        add(mainPanel);
        pack();
        setLocationRelativeTo(null);
    }

    public void setUserEmail(String email) {
        this.userEmail = email;
    }

    private class SubmitAction implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            try {
                int age = Integer.parseInt(ageField.getText());
                double height = Double.parseDouble(heightField.getText());
                double weight = Double.parseDouble(weightField.getText());
                String gender = (String) genderCombo.getSelectedItem();

                try (FileWriter writer = new FileWriter("health_data.txt", true)) {
                    writer.write(String.format("Email: %s, Age: %d, Height: %.1f, Weight: %.1f, Gender: %s\n",
                            userEmail, age, height, weight, gender));
                    JOptionPane.showMessageDialog(HealthDataPage.this, 
                            "Health data saved successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                } catch (IOException ioException) {
                    JOptionPane.showMessageDialog(HealthDataPage.this, 
                            "Error saving data: " + ioException.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }

                new FitnessGoalPage(age, height, weight, gender, userEmail);
                dispose();
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(HealthDataPage.this, 
                        "Please enter valid numeric values for age, height, and weight.", 
                        "Input Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            HealthDataPage healthDataPage = new HealthDataPage();
            healthDataPage.setUserEmail("test@example.com"); // Set a test email
            healthDataPage.setVisible(true);
        });
    }
}
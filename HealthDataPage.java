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
        setSize(350, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel ageLabel = new JLabel("Age:");
        ageField = new JTextField();

        JLabel heightLabel = new JLabel("Height (cm):");
        heightField = new JTextField();

        JLabel weightLabel = new JLabel("Weight (kg):");
        weightField = new JTextField();

        JLabel genderLabel = new JLabel("Gender:");
        genderCombo = new JComboBox<>(new String[]{"Male", "Female", "Other"});

        JButton submitButton = new JButton("Submit");
        submitButton.addActionListener(new SubmitAction());

        // Add components to panel
        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(ageLabel, gbc);

        gbc.gridx = 1;
        panel.add(ageField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        panel.add(heightLabel, gbc);

        gbc.gridx = 1;
        panel.add(heightField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        panel.add(weightLabel, gbc);

        gbc.gridx = 1;
        panel.add(weightField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        panel.add(genderLabel, gbc);

        gbc.gridx = 1;
        panel.add(genderCombo, gbc);

        gbc.gridx = 1;
        gbc.gridy = 4;
        panel.add(submitButton, gbc);

        add(panel);
        setVisible(true);
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
                } catch (IOException ioException) {
                    JOptionPane.showMessageDialog(HealthDataPage.this, 
                            "Error saving data: " + ioException.getMessage());
                }

                new FitnessGoalPage(age, height, weight, gender, userEmail);
                dispose();
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(HealthDataPage.this, 
                        "Please enter valid numeric values.");
            }
        }
    }
}
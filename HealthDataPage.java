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

    public HealthDataPage() {
        setTitle("Health Data");
        setSize(350, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // center the window

        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Labels and fields
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

        // Adding components to the panel
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

    private class SubmitAction implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            try {
                int age = Integer.parseInt(ageField.getText());
                double height = Double.parseDouble(heightField.getText());
                double weight = Double.parseDouble(weightField.getText());
                String gender = (String) genderCombo.getSelectedItem();

                // Save to a file
                try (FileWriter writer = new FileWriter("health_data.txt", true)) {
                    writer.write("Age: " + age + ", Height: " + height + " cm, Weight: " + weight + " kg, Gender: " + gender + "\n");
                } catch (IOException ioException) {
                    JOptionPane.showMessageDialog(HealthDataPage.this, "Error saving data: " + ioException.getMessage());
                }

                JOptionPane.showMessageDialog(HealthDataPage.this,
                        "Data saved:\nAge: " + age + "\nHeight: " + height + " cm\nWeight: " + weight + " kg\nGender: " + gender);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(HealthDataPage.this, "Please enter valid numeric values.");
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(HealthDataPage::new);
    }
}

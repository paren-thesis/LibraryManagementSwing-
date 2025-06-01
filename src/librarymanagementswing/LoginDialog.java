package librarymanagementswing;

import javax.swing.*;
import java.awt.*;

public class LoginDialog extends JDialog {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private boolean loginSuccessful = false;
    private UserAuth userAuth;

    public LoginDialog(Frame parent, UserAuth userAuth) {
        super(parent, "Login", true);
        this.userAuth = userAuth;
        initComponents();
    }

    private void initComponents() {
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);

        // Username
        gbc.gridx = 0;
        gbc.gridy = 0;
        add(new JLabel("Username:"), gbc);

        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        usernameField = new JTextField(20);
        add(usernameField, gbc);

        // Password
        gbc.gridx = 0;
        gbc.gridy = 1;
        add(new JLabel("Password:"), gbc);

        gbc.gridx = 1;
        passwordField = new JPasswordField(20);
        add(passwordField, gbc);

        // Buttons
        JPanel buttonPanel = new JPanel();
        JButton loginButton = new JButton("Login");
        JButton registerButton = new JButton("Register");

        loginButton.addActionListener(e -> {
            String username = usernameField.getText();
            String password = new String(passwordField.getPassword());
            
            if (userAuth.login(username, password)) {
                loginSuccessful = true;
                dispose();
            } else {
                JOptionPane.showMessageDialog(this,
                    "Invalid username or password",
                    "Login Error",
                    JOptionPane.ERROR_MESSAGE);
            }
        });

        registerButton.addActionListener(e -> showRegistrationDialog());

        buttonPanel.add(loginButton);
        buttonPanel.add(registerButton);

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        add(buttonPanel, gbc);

        pack();
        setLocationRelativeTo(getParent());
    }

    private void showRegistrationDialog() {
        JDialog registerDialog = new JDialog(this, "Register", true);
        registerDialog.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);

        // Username
        gbc.gridx = 0;
        gbc.gridy = 0;
        registerDialog.add(new JLabel("Username:"), gbc);

        gbc.gridx = 1;
        JTextField regUsernameField = new JTextField(20);
        registerDialog.add(regUsernameField, gbc);

        // Password
        gbc.gridx = 0;
        gbc.gridy = 1;
        registerDialog.add(new JLabel("Password:"), gbc);

        gbc.gridx = 1;
        JPasswordField regPasswordField = new JPasswordField(20);
        registerDialog.add(regPasswordField, gbc);

        // Role
        gbc.gridx = 0;
        gbc.gridy = 2;
        registerDialog.add(new JLabel("Role:"), gbc);

        gbc.gridx = 1;
        String[] roles = {"student", "librarian"};
        JComboBox<String> roleCombo = new JComboBox<>(roles);
        registerDialog.add(roleCombo, gbc);

        // Register Button
        JButton registerButton = new JButton("Register");
        registerButton.addActionListener(e -> {
            String username = regUsernameField.getText();
            String password = new String(regPasswordField.getPassword());
            String role = (String) roleCombo.getSelectedItem();

            if (userAuth.register(username, password, role)) {
                JOptionPane.showMessageDialog(registerDialog,
                    "Registration successful! Please login.",
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE);
                registerDialog.dispose();
            } else {
                JOptionPane.showMessageDialog(registerDialog,
                    "Registration failed. Username might already exist.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            }
        });

        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        registerDialog.add(registerButton, gbc);

        registerDialog.pack();
        registerDialog.setLocationRelativeTo(this);
        registerDialog.setVisible(true);
    }

    public boolean isLoginSuccessful() {
        return loginSuccessful;
    }
} 
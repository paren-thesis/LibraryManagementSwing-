package librarymanagementswing;

import javax.swing.*;
import java.awt.*;

public class LoginDialog extends JDialog {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private boolean loginSuccessful = false;
    private UserAuth userAuth;
    
    // Use the same color scheme as the main application
    private static final Color PRIMARY_COLOR = new Color(41, 128, 185);    // Blue
    private static final Color SECONDARY_COLOR = new Color(52, 152, 219);  // Light Blue
    private static final Color BACKGROUND_COLOR = new Color(236, 240, 241); // Light Gray
    private static final Color TEXT_COLOR = new Color(44, 62, 80);         // Dark Blue
    private static final Color BUTTON_COLOR = new Color(52, 152, 219);     // Light Blue
    private static final Color BUTTON_TEXT_COLOR = Color.WHITE;

    public LoginDialog(Frame parent, UserAuth userAuth) {
        super(parent, "Login", true);
        this.userAuth = userAuth;
        initComponents();
    }

    private void initComponents() {
        setLayout(new BorderLayout());
        getContentPane().setBackground(BACKGROUND_COLOR);
        
        // Create main panel with padding
        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBackground(BACKGROUND_COLOR);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Add title
        JLabel titleLabel = new JLabel("Library Management System", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(PRIMARY_COLOR);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(0, 0, 20, 0);
        mainPanel.add(titleLabel, gbc);

        // Username
        gbc.gridwidth = 1;
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.gridy = 1;
        JLabel usernameLabel = new JLabel("Username:");
        usernameLabel.setFont(new Font("Arial", Font.BOLD, 14));
        usernameLabel.setForeground(TEXT_COLOR);
        mainPanel.add(usernameLabel, gbc);

        gbc.gridx = 1;
        usernameField = new JTextField(20);
        styleTextField(usernameField);
        mainPanel.add(usernameField, gbc);

        // Password
        gbc.gridx = 0;
        gbc.gridy = 2;
        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setFont(new Font("Arial", Font.BOLD, 14));
        passwordLabel.setForeground(TEXT_COLOR);
        mainPanel.add(passwordLabel, gbc);

        gbc.gridx = 1;
        passwordField = new JPasswordField(20);
        styleTextField(passwordField);
        mainPanel.add(passwordField, gbc);

        // Buttons panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        buttonPanel.setBackground(BACKGROUND_COLOR);
        
        JButton loginButton = new JButton("Login");
        JButton registerButton = new JButton("Register");
        
        styleButton(loginButton);
        styleButton(registerButton);

        // Add enter key support for login
        getRootPane().setDefaultButton(loginButton);
        
        // Add tooltips
        usernameField.setToolTipText("Enter your username");
        passwordField.setToolTipText("Enter your password");
        loginButton.setToolTipText("Click to login");
        registerButton.setToolTipText("Click to create a new account");

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
                passwordField.setText("");
                passwordField.requestFocus();
            }
        });

        registerButton.addActionListener(e -> showRegistrationDialog());

        buttonPanel.add(loginButton);
        buttonPanel.add(registerButton);

        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(20, 10, 10, 10);
        mainPanel.add(buttonPanel, gbc);

        add(mainPanel, BorderLayout.CENTER);
        
        // Set dialog properties
        setResizable(false);
        pack();
        setLocationRelativeTo(getParent());
    }

    private void styleTextField(JTextField textField) {
        textField.setBackground(Color.WHITE);
        textField.setForeground(TEXT_COLOR);
        textField.setFont(new Font("Arial", Font.PLAIN, 14));
    }

    private void styleButton(JButton button) {
        button.setBackground(BUTTON_COLOR);
        button.setForeground(BUTTON_TEXT_COLOR);
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }

    private void showRegistrationDialog() {
        JDialog registerDialog = new JDialog(this, "Register", true);
        registerDialog.setLayout(new BorderLayout());
        registerDialog.getContentPane().setBackground(BACKGROUND_COLOR);
        
        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBackground(BACKGROUND_COLOR);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Username
        gbc.gridx = 0;
        gbc.gridy = 0;
        JLabel usernameLabel = new JLabel("Username:");
        usernameLabel.setFont(new Font("Arial", Font.BOLD, 14));
        usernameLabel.setForeground(TEXT_COLOR);
        mainPanel.add(usernameLabel, gbc);

        gbc.gridx = 1;
        JTextField regUsernameField = new JTextField(20);
        styleTextField(regUsernameField);
        mainPanel.add(regUsernameField, gbc);

        // Password
        gbc.gridx = 0;
        gbc.gridy = 1;
        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setFont(new Font("Arial", Font.BOLD, 14));
        passwordLabel.setForeground(TEXT_COLOR);
        mainPanel.add(passwordLabel, gbc);

        gbc.gridx = 1;
        JPasswordField regPasswordField = new JPasswordField(20);
        styleTextField(regPasswordField);
        mainPanel.add(regPasswordField, gbc);

        // Role
        gbc.gridx = 0;
        gbc.gridy = 2;
        JLabel roleLabel = new JLabel("Role:");
        roleLabel.setFont(new Font("Arial", Font.BOLD, 14));
        roleLabel.setForeground(TEXT_COLOR);
        mainPanel.add(roleLabel, gbc);

        gbc.gridx = 1;
        String[] roles = {"student", "librarian"};
        JComboBox<String> roleCombo = new JComboBox<>(roles);
        roleCombo.setBackground(Color.WHITE);
        roleCombo.setForeground(TEXT_COLOR);
        roleCombo.setFont(new Font("Arial", Font.PLAIN, 14));
        mainPanel.add(roleCombo, gbc);

        // Register Button
        JButton registerButton = new JButton("Register");
        styleButton(registerButton);
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
        gbc.insets = new Insets(20, 10, 10, 10);
        mainPanel.add(registerButton, gbc);

        registerDialog.add(mainPanel, BorderLayout.CENTER);
        
        // Set dialog properties
        registerDialog.setResizable(false);
        registerDialog.pack();
        registerDialog.setLocationRelativeTo(this);
        registerDialog.setVisible(true);
    }

    public boolean isLoginSuccessful() {
        return loginSuccessful;
    }
} 
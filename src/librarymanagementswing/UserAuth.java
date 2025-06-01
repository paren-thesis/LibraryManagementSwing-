package librarymanagementswing;

import javax.swing.*;
import java.awt.*;
import java.sql.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class UserAuth {
    private Connection connection;
    private static UserAuth instance;
    private String currentUser;
    private String currentRole;

    private UserAuth(Connection connection) {
        this.connection = connection;
    }

    public static UserAuth getInstance(Connection connection) {
        if (instance == null) {
            instance = new UserAuth(connection);
        }
        return instance;
    }

    public boolean login(String username, String password) {
        try {
            String hashedPassword = hashPassword(password);
            String query = "SELECT * FROM users WHERE username = ? AND password = ?";
            PreparedStatement stmt = connection.prepareStatement(query);
            stmt.setString(1, username);
            stmt.setString(2, hashedPassword);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                currentUser = username;
                currentRole = rs.getString("role");
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean register(String username, String password, String role) {
        try {
            String hashedPassword = hashPassword(password);
            String query = "INSERT INTO users (username, password, role) VALUES (?, ?, ?)";
            PreparedStatement stmt = connection.prepareStatement(query);
            stmt.setString(1, username);
            stmt.setString(2, hashedPassword);
            stmt.setString(3, role);
            stmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    private String hashPassword(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hash = md.digest(password.getBytes());
            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }

    public String getCurrentUser() {
        return currentUser;
    }

    public String getCurrentRole() {
        return currentRole;
    }

    public boolean isAdmin() {
        return "admin".equals(currentRole);
    }

    public boolean isLibrarian() {
        return "librarian".equals(currentRole);
    }

    public void logout() {
        currentUser = null;
        currentRole = null;
    }
} 
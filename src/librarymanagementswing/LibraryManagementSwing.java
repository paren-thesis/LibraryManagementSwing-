/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package librarymanagementswing;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class LibraryManagementSwing {
    private static final String DB_URL = "jdbc:mysql://localhost:3306/LibraryDB";
    private static final String DB_USER = "root"; // Replace with your MySQL username
    private static final String DB_PASSWORD = ""; // Replace with your MySQL password
    private Connection connection;
    private UserAuth userAuth;
    private JFrame mainFrame;
    private JTabbedPane tabbedPane;

    public LibraryManagementSwing() {
        try {
            connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
            userAuth = UserAuth.getInstance(connection);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Database connection failed: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            System.exit(1);
        }
        showLoginDialog();
    }

    private void showLoginDialog() {
        LoginDialog loginDialog = new LoginDialog(null, userAuth);
        loginDialog.setVisible(true);

        if (loginDialog.isLoginSuccessful()) {
            initComponents();
        } else {
            System.exit(0);
        }
    }

    private void initComponents() {
        mainFrame = new JFrame("Library Management System");
        mainFrame.setSize(1000, 700);
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        tabbedPane = new JTabbedPane();

        // Add tabs based on user role
        if (userAuth.isAdmin() || userAuth.isLibrarian()) {
            tabbedPane.add("Add Book", createAddBookPanel());
            tabbedPane.add("Issue Book", createIssueBookPanel());
            tabbedPane.add("Return Book", createReturnBookPanel());
        }
        tabbedPane.add("View Books", createViewBooksPanel());
        tabbedPane.add("Search Books", createSearchPanel());

        // Add logout button
        JButton logoutButton = new JButton("Logout");
        logoutButton.addActionListener(e -> {
            userAuth.logout();
            mainFrame.dispose();
            showLoginDialog();
        });

        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        topPanel.add(new JLabel("Welcome, " + userAuth.getCurrentUser()));
        topPanel.add(logoutButton);

        mainFrame.add(topPanel, BorderLayout.NORTH);
        mainFrame.add(tabbedPane);
        mainFrame.setVisible(true);
    }

    private JPanel createAddBookPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(5, 2, 10, 10));

        JLabel titleLabel = new JLabel("Book Title:");
        JTextField titleField = new JTextField();
        JLabel authorLabel = new JLabel("Author:");
        JTextField authorField = new JTextField();
        JLabel isbnLabel = new JLabel("ISBN:");
        JTextField isbnField = new JTextField();
        JLabel categoryLabel = new JLabel("Category:");
        JComboBox<String> categoryCombo = new JComboBox<>();
        
        try {
            String query = "SELECT category_name FROM book_categories";
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            while (rs.next()) {
                categoryCombo.addItem(rs.getString("category_name"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        JButton addButton = new JButton("Add Book");

        panel.add(titleLabel);
        panel.add(titleField);
        panel.add(authorLabel);
        panel.add(authorField);
        panel.add(isbnLabel);
        panel.add(isbnField);
        panel.add(categoryLabel);
        panel.add(categoryCombo);
        panel.add(new JLabel());
        panel.add(addButton);

        addButton.addActionListener(e -> {
            String title = titleField.getText();
            String author = authorField.getText();
            String isbn = isbnField.getText();
            String category = (String) categoryCombo.getSelectedItem();

            if (!title.isEmpty() && !author.isEmpty()) {
                try {
                    String query = "INSERT INTO books (title, author, isbn, category_id) " +
                                 "SELECT ?, ?, ?, category_id FROM book_categories WHERE category_name = ?";
                    PreparedStatement stmt = connection.prepareStatement(query);
                    stmt.setString(1, title);
                    stmt.setString(2, author);
                    stmt.setString(3, isbn);
                    stmt.setString(4, category);
                    stmt.executeUpdate();
                    JOptionPane.showMessageDialog(null, "Book added successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                    titleField.setText("");
                    authorField.setText("");
                    isbnField.setText("");
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(null, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(null, "Please fill in all required fields!", "Warning", JOptionPane.WARNING_MESSAGE);
            }
        });

        return panel;
    }

    private JPanel createIssueBookPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(5, 2, 10, 10));

        JLabel bookIdLabel = new JLabel("Book ID:");
        JTextField bookIdField = new JTextField();
        JLabel studentLabel = new JLabel("Student Name:");
        JTextField studentField = new JTextField();
        JLabel daysLabel = new JLabel("Days to Return:");
        JSpinner daysSpinner = new JSpinner(new SpinnerNumberModel(14, 1, 30, 1));
        JButton issueButton = new JButton("Issue Book");

        panel.add(bookIdLabel);
        panel.add(bookIdField);
        panel.add(studentLabel);
        panel.add(studentField);
        panel.add(daysLabel);
        panel.add(daysSpinner);
        panel.add(new JLabel());
        panel.add(issueButton);

        issueButton.addActionListener(e -> {
            String bookId = bookIdField.getText();
            String studentName = studentField.getText();
            int days = (Integer) daysSpinner.getValue();
            LocalDate dueDate = LocalDate.now().plusDays(days);

            if (!bookId.isEmpty() && !studentName.isEmpty()) {
                try {
                    String checkQuery = "SELECT * FROM books WHERE book_id = ? AND available = TRUE";
                    PreparedStatement checkStmt = connection.prepareStatement(checkQuery);
                    checkStmt.setInt(1, Integer.parseInt(bookId));
                    ResultSet rs = checkStmt.executeQuery();

                    if (rs.next()) {
                        String issueQuery = "INSERT INTO issued_books (book_id, student_name, issue_date, due_date) " +
                                          "VALUES (?, ?, CURDATE(), ?)";
                        PreparedStatement issueStmt = connection.prepareStatement(issueQuery);
                        issueStmt.setInt(1, Integer.parseInt(bookId));
                        issueStmt.setString(2, studentName);
                        issueStmt.setDate(3, Date.valueOf(dueDate));
                        issueStmt.executeUpdate();

                        String updateQuery = "UPDATE books SET available = FALSE WHERE book_id = ?";
                        PreparedStatement updateStmt = connection.prepareStatement(updateQuery);
                        updateStmt.setInt(1, Integer.parseInt(bookId));
                        updateStmt.executeUpdate();

                        JOptionPane.showMessageDialog(null, 
                            "Book issued successfully!\nDue date: " + dueDate, 
                            "Success", 
                            JOptionPane.INFORMATION_MESSAGE);
                        bookIdField.setText("");
                        studentField.setText("");
                    } else {
                        JOptionPane.showMessageDialog(null, "Book not available or doesn't exist!", "Warning", JOptionPane.WARNING_MESSAGE);
                    }
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(null, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(null, "Please fill in all fields!", "Warning", JOptionPane.WARNING_MESSAGE);
            }
        });

        return panel;
    }

    private JPanel createReturnBookPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(3, 2, 10, 10));

        JLabel bookIdLabel = new JLabel("Book ID:");
        JTextField bookIdField = new JTextField();
        JButton returnButton = new JButton("Return Book");

        panel.add(bookIdLabel);
        panel.add(bookIdField);
        panel.add(new JLabel());
        panel.add(returnButton);

        returnButton.addActionListener(e -> {
            String bookId = bookIdField.getText();
            if (!bookId.isEmpty()) {
                try {
                    String checkQuery = "SELECT * FROM issued_books WHERE book_id = ? AND returned_date IS NULL";
                    PreparedStatement checkStmt = connection.prepareStatement(checkQuery);
                    checkStmt.setInt(1, Integer.parseInt(bookId));
                    ResultSet rs = checkStmt.executeQuery();

                    if (rs.next()) {
                        LocalDate dueDate = rs.getDate("due_date").toLocalDate();
                        LocalDate returnDate = LocalDate.now();
                        long daysLate = ChronoUnit.DAYS.between(dueDate, returnDate);
                        double fine = daysLate > 0 ? daysLate * 1.0 : 0.0; // $1 per day late

                        String updateQuery = "UPDATE issued_books SET returned_date = CURDATE(), fine_amount = ? WHERE book_id = ?";
                        PreparedStatement updateStmt = connection.prepareStatement(updateQuery);
                        updateStmt.setDouble(1, fine);
                        updateStmt.setInt(2, Integer.parseInt(bookId));
                        updateStmt.executeUpdate();

                        String bookQuery = "UPDATE books SET available = TRUE WHERE book_id = ?";
                        PreparedStatement bookStmt = connection.prepareStatement(bookQuery);
                        bookStmt.setInt(1, Integer.parseInt(bookId));
                        bookStmt.executeUpdate();

                        String message = "Book returned successfully!";
                        if (fine > 0) {
                            message += "\nFine amount: $" + String.format("%.2f", fine);
                        }
                        JOptionPane.showMessageDialog(null, message, "Success", JOptionPane.INFORMATION_MESSAGE);
                        bookIdField.setText("");
                    } else {
                        JOptionPane.showMessageDialog(null, "No record found for the issued book ID.", "Warning", JOptionPane.WARNING_MESSAGE);
                    }
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(null, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(null, "Please enter the book ID!", "Warning", JOptionPane.WARNING_MESSAGE);
            }
        });

        return panel;
    }

    private JPanel createViewBooksPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());

        JButton refreshButton = new JButton("Refresh");
        JTable table = new JTable(new DefaultTableModel(
            new Object[]{"Book ID", "Title", "Author", "ISBN", "Category", "Available", "Status"}, 0));
        JScrollPane scrollPane = new JScrollPane(table);

        refreshButton.addActionListener(e -> {
            DefaultTableModel model = (DefaultTableModel) table.getModel();
            model.setRowCount(0);
            try {
                String query = "SELECT b.*, c.category_name, " +
                             "CASE WHEN b.available THEN 'Available' " +
                             "ELSE CONCAT('Issued to ', ib.student_name, ' (Due: ', ib.due_date, ')') END as status " +
                             "FROM books b " +
                             "LEFT JOIN book_categories c ON b.category_id = c.category_id " +
                             "LEFT JOIN issued_books ib ON b.book_id = ib.book_id AND ib.returned_date IS NULL";
                Statement stmt = connection.createStatement();
                ResultSet rs = stmt.executeQuery(query);

                while (rs.next()) {
                    model.addRow(new Object[]{
                        rs.getInt("book_id"),
                        rs.getString("title"),
                        rs.getString("author"),
                        rs.getString("isbn"),
                        rs.getString("category_name"),
                        rs.getBoolean("available") ? "Yes" : "No",
                        rs.getString("status")
                    });
                }
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(null, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        panel.add(refreshButton, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    private JPanel createSearchPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());

        JPanel searchPanel = new JPanel(new FlowLayout());
        JTextField searchField = new JTextField(20);
        JComboBox<String> searchType = new JComboBox<>(new String[]{"Title", "Author", "ISBN", "Category"});
        JButton searchButton = new JButton("Search");
        JTable table = new JTable(new DefaultTableModel(
            new Object[]{"Book ID", "Title", "Author", "ISBN", "Category", "Available", "Status"}, 0));
        JScrollPane scrollPane = new JScrollPane(table);

        searchButton.addActionListener(e -> {
            String searchText = searchField.getText();
            String searchBy = (String) searchType.getSelectedItem();
            DefaultTableModel model = (DefaultTableModel) table.getModel();
            model.setRowCount(0);

            try {
                String query = "SELECT b.*, c.category_name, " +
                             "CASE WHEN b.available THEN 'Available' " +
                             "ELSE CONCAT('Issued to ', ib.student_name, ' (Due: ', ib.due_date, ')') END as status " +
                             "FROM books b " +
                             "LEFT JOIN book_categories c ON b.category_id = c.category_id " +
                             "LEFT JOIN issued_books ib ON b.book_id = ib.book_id AND ib.returned_date IS NULL " +
                             "WHERE ";

                switch (searchBy) {
                    case "Title":
                        query += "b.title LIKE ?";
                        break;
                    case "Author":
                        query += "b.author LIKE ?";
                        break;
                    case "ISBN":
                        query += "b.isbn LIKE ?";
                        break;
                    case "Category":
                        query += "c.category_name LIKE ?";
                        break;
                }

                PreparedStatement stmt = connection.prepareStatement(query);
                stmt.setString(1, "%" + searchText + "%");
                ResultSet rs = stmt.executeQuery();

                while (rs.next()) {
                    model.addRow(new Object[]{
                        rs.getInt("book_id"),
                        rs.getString("title"),
                        rs.getString("author"),
                        rs.getString("isbn"),
                        rs.getString("category_name"),
                        rs.getBoolean("available") ? "Yes" : "No",
                        rs.getString("status")
                    });
                }
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(null, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        searchPanel.add(new JLabel("Search:"));
        searchPanel.add(searchField);
        searchPanel.add(new JLabel("By:"));
        searchPanel.add(searchType);
        searchPanel.add(searchButton);

        panel.add(searchPanel, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(LibraryManagementSwing::new);
    }
}
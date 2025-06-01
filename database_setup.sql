-- Drop existing database and create new one
DROP DATABASE IF EXISTS LibraryDB;
CREATE DATABASE LibraryDB;
USE LibraryDB;

-- Create users table for authentication
CREATE TABLE users (
    user_id INT PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(50) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    role ENUM('admin', 'librarian', 'student') NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Create book_categories table
CREATE TABLE book_categories (
    category_id INT PRIMARY KEY AUTO_INCREMENT,
    category_name VARCHAR(50) NOT NULL,
    description TEXT
);

-- Create books table
CREATE TABLE books (
    book_id INT PRIMARY KEY AUTO_INCREMENT,
    title VARCHAR(255) NOT NULL,
    author VARCHAR(255) NOT NULL,
    isbn VARCHAR(13),
    category_id INT,
    available BOOLEAN DEFAULT TRUE,
    FOREIGN KEY (category_id) REFERENCES book_categories(category_id)
);

-- Create issued_books table
CREATE TABLE issued_books (
    issue_id INT PRIMARY KEY AUTO_INCREMENT,
    book_id INT NOT NULL,
    student_name VARCHAR(255) NOT NULL,
    issue_date DATE NOT NULL,
    due_date DATE NOT NULL,
    returned_date DATE,
    fine_amount DECIMAL(10,2) DEFAULT 0.00,
    FOREIGN KEY (book_id) REFERENCES books(book_id)
);

-- Insert default categories
INSERT INTO book_categories (category_name, description) VALUES
('Fiction', 'Novels and fictional works'),
('Non-Fiction', 'Educational and factual books'),
('Science', 'Scientific and technical books'),
('History', 'Historical books and documents'),
('Literature', 'Classical and contemporary literature');

-- Create indexes for better search performance
CREATE INDEX idx_books_title ON books(title);
CREATE INDEX idx_books_author ON books(author);
CREATE INDEX idx_books_isbn ON books(isbn);
CREATE INDEX idx_books_category ON books(category_id);

-- Insert a default admin user (password: admin123)
INSERT INTO users (username, password, role) VALUES
('admin', '240be518fabd2724ddb6f04eeb1da5967448d7e831c08c8fa822809f74c720a9', 'admin'); 
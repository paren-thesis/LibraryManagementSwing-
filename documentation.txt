Library Management System Documentation
=====================================

Overview
--------
This is a Library Management System developed using Java Swing for the user interface and MySQL (XAMPP) for the database. The application provides a comprehensive solution for managing library operations with user authentication, book categorization, search functionality, and due date tracking.

System Requirements
-----------------
1. Java Development Kit (JDK) 8 or higher
2. MySQL Server (XAMPP)
3. MySQL JDBC Driver

Database Configuration
--------------------
- Database Name: LibraryDB
- Host: localhost
- Port: 3306
- Username: root
- Password: (empty by default)

Database Schema
--------------
1. users table:
   - user_id (INT, Primary Key, Auto Increment)
   - username (VARCHAR(50), Unique)
   - password (VARCHAR(255), Hashed)
   - role (ENUM: 'admin', 'librarian', 'student')
   - created_at (TIMESTAMP)

2. book_categories table:
   - category_id (INT, Primary Key, Auto Increment)
   - category_name (VARCHAR(50))
   - description (TEXT)

3. books table:
   - book_id (INT, Primary Key, Auto Increment)
   - title (VARCHAR(255))
   - author (VARCHAR(255))
   - isbn (VARCHAR(13))
   - category_id (INT, Foreign Key)
   - available (BOOLEAN)

4. issued_books table:
   - issue_id (INT, Primary Key, Auto Increment)
   - book_id (INT, Foreign Key)
   - student_name (VARCHAR(255))
   - issue_date (DATE)
   - due_date (DATE)
   - returned_date (DATE)
   - fine_amount (DECIMAL(10,2))

Features
--------
1. User Authentication
   - Role-based access control (admin, librarian, student)
   - Secure password hashing
   - User registration
   - Login/Logout functionality
   - Default admin account (username: admin, password: admin123)

2. Book Management
   - Add new books with categories
   - ISBN tracking
   - Book availability status
   - Book categorization
   - View all books with detailed information

3. Book Issuance
   - Issue books to students
   - Set custom return periods (1-30 days)
   - Automatic due date calculation
   - Track issued books
   - View current book status

4. Book Returns
   - Process book returns
   - Automatic fine calculation ($1 per day late)
   - Update book availability
   - Track return history

5. Search Functionality
   - Search by title
   - Search by author
   - Search by ISBN
   - Search by category
   - Real-time search results

6. Book Categories
   - Predefined categories
   - Category-based organization
   - Category search
   - Category-based book filtering

User Roles and Permissions
-------------------------
1. Admin
   - Full system access
   - Manage all books
   - Issue and return books
   - View all records
   - Search functionality

2. Librarian
   - Manage books
   - Issue and return books
   - View all records
   - Search functionality

3. Student
   - View available books
   - Search books
   - View their issued books

Database Indexes
---------------
- idx_books_title: Optimizes title-based searches
- idx_books_author: Optimizes author-based searches
- idx_books_isbn: Optimizes ISBN-based searches
- idx_books_category: Optimizes category-based searches

Default Data
-----------
1. Book Categories:
   - Fiction
   - Non-Fiction
   - Science
   - History
   - Literature

2. Default Admin User:
   - Username: admin
   - Password: admin123

Usage Instructions
----------------
1. Database Setup:
   - Start XAMPP and ensure MySQL service is running
   - Run the database_setup.sql script
   - Verify database creation and default data

2. Application Usage:
   - Launch the application
   - Log in with appropriate credentials
   - Use the tabbed interface for different operations:
     * Add Book: Enter book details and category
     * Issue Book: Enter book ID, student name, and return period
     * Return Book: Enter book ID to process return
     * View Books: See all books with their status
     * Search Books: Find books using various criteria

3. Book Issuance Process:
   - Select book by ID
   - Enter student name
   - Set return period (1-30 days)
   - Confirm issuance

4. Book Return Process:
   - Enter book ID
   - System calculates any fines
   - Confirm return

5. Search Operations:
   - Select search criteria
   - Enter search term
   - View results in table format

Error Handling
-------------
The system includes comprehensive error handling for:
- Database connection failures
- Invalid input data
- Book availability checks
- User authentication
- SQL operation failures
- Empty field validation

Security Features
---------------
1. Password Security:
   - SHA-256 password hashing
   - Secure password storage
   - No plain text passwords

2. Data Protection:
   - Prepared statements for SQL operations
   - Input validation
   - Role-based access control
   - Secure session management

3. Error Messages:
   - User-friendly error messages
   - No sensitive information exposure
   - Clear validation feedback

Maintenance
----------
To maintain the system:
1. Regular database backups
2. Monitor database performance
3. Update JDBC driver when needed
4. Keep Java runtime environment updated
5. Regular security audits
6. Monitor user activities
7. Check for overdue books

Troubleshooting
--------------
Common issues and solutions:
1. Database Connection Error:
   - Check if XAMPP is running
   - Verify database credentials
   - Ensure LibraryDB exists

2. Authentication Issues:
   - Verify username and password
   - Check user role permissions
   - Reset admin password if needed

3. Book Not Found:
   - Verify book ID exists
   - Check if book is available
   - Verify search criteria

4. GUI Issues:
   - Ensure Java runtime is properly installed
   - Check screen resolution compatibility
   - Verify system requirements

Support
-------
For technical support or bug reports, please contact the development team. 
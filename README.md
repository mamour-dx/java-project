# Bank Management System

A console-based banking application that allows users to manage their accounts and perform various banking operations.

## Features

### For Customers

- Login (authentication with username and password)
- Deposit money
- Withdraw money
- Check balance
- Transfer between accounts
- View transaction history

### For Administrators

- Login
- User management (add, modify, delete)
- View all transactions

## Prerequisites

- Java JDK 8 or higher
- XAMPP (for MySQL database)
- MySQL Connector/J (JDBC driver)

## Setup Instructions

### 1. Database Setup (Using XAMPP)

1. Start XAMPP and ensure MySQL service is running
2. Open phpMyAdmin (http://localhost/phpmyadmin)
3. Create a new database named `bank`
4. Copy and execute the following SQL in phpMyAdmin's SQL tab:

```sql
-- Create users table
CREATE TABLE IF NOT EXISTS users (
    userId INT PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(50) UNIQUE NOT NULL,
    password VARCHAR(50) NOT NULL,
    role VARCHAR(20) NOT NULL
);

-- Create accounts table
CREATE TABLE IF NOT EXISTS accounts (
    accountId INT PRIMARY KEY AUTO_INCREMENT,
    userId INT NOT NULL,
    balance DECIMAL(10,2) NOT NULL DEFAULT 0.00,
    FOREIGN KEY (userId) REFERENCES users(userId)
);

-- Create transactions table
CREATE TABLE IF NOT EXISTS transactions (
    transactionId INT PRIMARY KEY AUTO_INCREMENT,
    fromAccountId INT NOT NULL,
    toAccountId INT NOT NULL,
    amount DECIMAL(10,2) NOT NULL,
    dateTime DATETIME NOT NULL,
    type VARCHAR(20) NOT NULL,
    FOREIGN KEY (fromAccountId) REFERENCES accounts(accountId),
    FOREIGN KEY (toAccountId) REFERENCES accounts(accountId)
);

-- Insert default admin user
INSERT INTO users (username, password, role) VALUES ('admin', 'admin', 'ADMIN');
INSERT INTO accounts (userId, balance)
SELECT userId, 0.00 FROM users WHERE username = 'admin';
```

### 2. Project Setup

1. Clone the repository:

```bash
git clone <repository-url>
cd bank-management-system
```

2. Download MySQL Connector/J:

- Go to https://dev.mysql.com/downloads/connector/j/
- Download the MySQL Connector/J JAR file (version 8.x)
- Create a `lib` directory in the project root
- Place the downloaded JAR file in the `lib` directory

3. Compile the project:

```bash
javac -cp .:lib/mysql-connector-j-8.x.x.jar src/main/java/main/Main.java src/main/java/model/*.java src/main/java/repository/*.java src/main/java/service/*.java src/main/java/util/*.java
```

4. Run the application:

```bash
java -cp src/main/java:lib/mysql-connector-j-8.x.x.jar main.Main
```

Note: On Windows, use semicolon (;) instead of colon (:) in the classpath.

## Default Admin Account

- Username: admin
- Password: admin

## Project Structure

```
src/main/java/
├── main/
│   └── Main.java
├── model/
│   ├── User.java
│   ├── Account.java
│   └── Transaction.java
├── repository/
│   ├── UserRepository.java
│   ├── AccountRepository.java
│   └── TransactionRepository.java
├── service/
│   ├── AuthService.java
│   ├── UserService.java
│   └── AccountService.java
└── util/
    ├── DBConnection.java
    └── ConsoleInput.java
```

## Database Schema

### Users Table

- userId (INT, PRIMARY KEY)
- username (VARCHAR(50), UNIQUE)
- password (VARCHAR(50))
- role (VARCHAR(20))

### Accounts Table

- accountId (INT, PRIMARY KEY)
- userId (INT, FOREIGN KEY)
- balance (DECIMAL(10,2))

### Transactions Table

- transactionId (INT, PRIMARY KEY)
- fromAccountId (INT, FOREIGN KEY)
- toAccountId (INT, FOREIGN KEY)
- amount (DECIMAL(10,2))
- dateTime (DATETIME)
- type (VARCHAR(20))

## Security Notes

- This is a prototype application. In a production environment:
  - Passwords should be hashed
  - Use prepared statements for all database queries
  - Implement proper session management
  - Add input validation and sanitization
  - Use secure connection (SSL/TLS) for database communication

## Troubleshooting

1. Database Connection Issues:

   - Ensure XAMPP MySQL service is running
   - Verify database name is "bank"
   - Check if tables are created correctly in phpMyAdmin
   - Default connection uses root user with no password

2. Compilation Issues:

   - Ensure Java JDK is installed and JAVA_HOME is set
   - Verify MySQL Connector/J is in the lib directory
   - Check classpath includes both src/main/java and the MySQL connector

3. Runtime Issues:
   - Make sure all required files are compiled
   - Verify classpath includes both src/main/java and the MySQL connector
   - Check for any error messages in the console.

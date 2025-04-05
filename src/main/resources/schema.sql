-- Create the database if it doesn't exist
CREATE DATABASE IF NOT EXISTS bank;
USE bank;

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
INSERT INTO accounts (userId, balance) VALUES (1, 0.00); 
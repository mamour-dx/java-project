---
description: 
globs: 
alwaysApply: true
---
Voici un rapport complet et détaillé pour construire une application de gestion bancaire en Java, accessible via la console, qui utilise une base de données MySQL (en écoute sur le port 3306 avec les identifiants par défaut) pour stocker les données.

---

## 1. Présentation générale

L’application permettra :

- **Pour un Client** :
  - Se connecter (authentification).
  - Déposer de l’argent sur son compte.
  - Retirer de l’argent de son compte.
  - Afficher le solde de son compte.
  - Effectuer un virement entre comptes.
  - Consulter l’historique des transactions.

- **Pour un Administrateur** :
  - Se connecter.
  - Gérer les utilisateurs (ajouter, modifier, supprimer).

Le programme démarre via un fichier `Main.java` compilé et exécuté avec :
```bash
javac Main.java
java Main
```
La base de données MySQL sera utilisée pour stocker les données relatives aux utilisateurs, aux comptes et aux transactions.

---

## 2. Pré-requis et création de la base MySQL

### 2.1. Installation du driver MySQL

- Télécharge le **MySQL Connector/J** (le fichier JAR, par exemple `mysql-connector-java-8.x.x.jar`) et ajoute-le dans le classpath lors de la compilation et de l’exécution.

### 2.2. Création de la base et des tables

Connecte-toi à MySQL (en utilisant l’utilisateur par défaut `root` sans mot de passe ou avec celui par défaut) et exécute le script suivant :

```sql
-- Création de la base de données
CREATE DATABASE IF NOT EXISTS bank;
USE bank;

-- Table des utilisateurs
CREATE TABLE IF NOT EXISTS users (
    userId VARCHAR(255) PRIMARY KEY,
    username VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    role VARCHAR(50) NOT NULL
);

-- Table des comptes
CREATE TABLE IF NOT EXISTS accounts (
    accountId VARCHAR(255) PRIMARY KEY,
    userId VARCHAR(255),
    balance DOUBLE NOT NULL,
    FOREIGN KEY (userId) REFERENCES users(userId)
);

-- Table des transactions
CREATE TABLE IF NOT EXISTS transactions (
    transactionId VARCHAR(255) PRIMARY KEY,
    fromAccountId VARCHAR(255),
    toAccountId VARCHAR(255),
    amount DOUBLE NOT NULL,
    dateTime DATETIME NOT NULL,
    type VARCHAR(50) NOT NULL
);
```

---

## 3. Organisation du projet et des packages

Voici la structure de l’application proposée :

```
.
├── main
│   └── Main.java
├── model
│   ├── User.java
│   ├── Account.java
│   └── Transaction.java
├── repository
│   ├── UserRepository.java
│   ├── AccountRepository.java
│   └── TransactionRepository.java
├── service
│   ├── AuthService.java
│   ├── UserService.java
│   └── AccountService.java
└── util
    ├── ConsoleInput.java
    └── DBConnection.java
```

- **model** : classes représentant les entités.
- **repository** : accès aux données via JDBC.
- **service** : logique métier et règles de gestion.
- **util** : classes utilitaires, notamment pour la connexion MySQL et la gestion des entrées console.
- **main** : point d’entrée de l’application.

---

## 4. Les classes modèles (Model)

### 4.1. `User.java`

```java
package model;

public class User {
    private String userId;
    private String username;
    private String password;
    private String role; // "CLIENT" ou "ADMIN"

    public User(String userId, String username, String password, String role) {
        this.userId = userId;
        this.username = username;
        this.password = password;
        this.role = role;
    }

    // Getters et setters
    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }
}
```

### 4.2. `Account.java`

```java
package model;

public class Account {
    private String accountId;
    private String userId;
    private double balance;

    public Account(String accountId, String userId, double balance) {
        this.accountId = accountId;
        this.userId = userId;
        this.balance = balance;
    }

    // Getters et setters
    public String getAccountId() { return accountId; }
    public void setAccountId(String accountId) { this.accountId = accountId; }

    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }

    public double getBalance() { return balance; }
    public void setBalance(double balance) { this.balance = balance; }
}
```

### 4.3. `Transaction.java`

```java
package model;

import java.time.LocalDateTime;

public class Transaction {
    private String transactionId;
    private String fromAccountId;
    private String toAccountId;
    private double amount;
    private LocalDateTime dateTime;
    private String type; // "DEPOT", "RETRAIT", "VIREMENT"

    public Transaction(String transactionId, String fromAccountId, String toAccountId, 
                       double amount, LocalDateTime dateTime, String type) {
        this.transactionId = transactionId;
        this.fromAccountId = fromAccountId;
        this.toAccountId = toAccountId;
        this.amount = amount;
        this.dateTime = dateTime;
        this.type = type;
    }

    // Getters et setters
    public String getTransactionId() { return transactionId; }
    public void setTransactionId(String transactionId) { this.transactionId = transactionId; }

    public String getFromAccountId() { return fromAccountId; }
    public void setFromAccountId(String fromAccountId) { this.fromAccountId = fromAccountId; }

    public String getToAccountId() { return toAccountId; }
    public void setToAccountId(String toAccountId) { this.toAccountId = toAccountId; }

    public double getAmount() { return amount; }
    public void setAmount(double amount) { this.amount = amount; }

    public LocalDateTime getDateTime() { return dateTime; }
    public void setDateTime(LocalDateTime dateTime) { this.dateTime = dateTime; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
}
```

---

## 5. Connexion à MySQL (Utilitaire)

Crée la classe suivante pour gérer la connexion à la base de données :

### 5.1. `DBConnection.java`

```java
package util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
    // Adresse, nom de la base, utilisateur et mot de passe par défaut
    private static final String URL = "jdbc:mysql://localhost:3306/bank?serverTimezone=UTC";
    private static final String USER = "root";
    private static final String PASSWORD = ""; // Laisser vide si aucun mot de passe

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}
```

*Astuce :* Ajoute le paramètre `?serverTimezone=UTC` si nécessaire pour éviter des avertissements de fuseau horaire.

---

## 6. Les repositories (accès aux données via JDBC)

Chaque repository exécutera des requêtes SQL pour interagir avec la base.

### 6.1. `UserRepository.java`

```java
package repository;

import model.User;
import util.DBConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserRepository {

    public static List<User> getAllUsers() {
        List<User> users = new ArrayList<>();
        String sql = "SELECT * FROM users";
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while(rs.next()){
                User user = new User(
                        rs.getString("userId"),
                        rs.getString("username"),
                        rs.getString("password"),
                        rs.getString("role"));
                users.add(user);
            }
        } catch(SQLException e) {
            e.printStackTrace();
        }
        return users;
    }

    public static void addUser(User user) {
        String sql = "INSERT INTO users (userId, username, password, role) VALUES (?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, user.getUserId());
            pstmt.setString(2, user.getUsername());
            pstmt.setString(3, user.getPassword());
            pstmt.setString(4, user.getRole());
            pstmt.executeUpdate();
        } catch(SQLException e) {
            e.printStackTrace();
        }
    }

    public static User findByUsername(String username) {
        String sql = "SELECT * FROM users WHERE username = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, username);
            ResultSet rs = pstmt.executeQuery();
            if(rs.next()){
                return new User(
                        rs.getString("userId"),
                        rs.getString("username"),
                        rs.getString("password"),
                        rs.getString("role"));
            }
        } catch(SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void updateUser(User user) {
        String sql = "UPDATE users SET username = ?, password = ?, role = ? WHERE userId = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, user.getUsername());
            pstmt.setString(2, user.getPassword());
            pstmt.setString(3, user.getRole());
            pstmt.setString(4, user.getUserId());
            pstmt.executeUpdate();
        } catch(SQLException e) {
            e.printStackTrace();
        }
    }

    public static void deleteUser(String userId) {
        String sql = "DELETE FROM users WHERE userId = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, userId);
            pstmt.executeUpdate();
        } catch(SQLException e) {
            e.printStackTrace();
        }
    }
}
```

### 6.2. `AccountRepository.java`

```java
package repository;

import model.Account;
import util.DBConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AccountRepository {

    public static List<Account> getAllAccounts() {
        List<Account> accounts = new ArrayList<>();
        String sql = "SELECT * FROM accounts";
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Account account = new Account(
                        rs.getString("accountId"),
                        rs.getString("userId"),
                        rs.getDouble("balance"));
                accounts.add(account);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return accounts;
    }

    public static void addAccount(Account account) {
        String sql = "INSERT INTO accounts (accountId, userId, balance) VALUES (?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, account.getAccountId());
            pstmt.setString(2, account.getUserId());
            pstmt.setDouble(3, account.getBalance());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static Account findById(String accountId) {
        String sql = "SELECT * FROM accounts WHERE accountId = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, accountId);
            ResultSet rs = pstmt.executeQuery();
            if(rs.next()){
                return new Account(
                        rs.getString("accountId"),
                        rs.getString("userId"),
                        rs.getDouble("balance"));
            }
        } catch(SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Account findByUserId(String userId) {
        String sql = "SELECT * FROM accounts WHERE userId = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, userId);
            ResultSet rs = pstmt.executeQuery();
            if(rs.next()){
                return new Account(
                        rs.getString("accountId"),
                        rs.getString("userId"),
                        rs.getDouble("balance"));
            }
        } catch(SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void updateAccount(Account account) {
        String sql = "UPDATE accounts SET userId = ?, balance = ? WHERE accountId = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, account.getUserId());
            pstmt.setDouble(2, account.getBalance());
            pstmt.setString(3, account.getAccountId());
            pstmt.executeUpdate();
        } catch(SQLException e) {
            e.printStackTrace();
        }
    }
}
```

### 6.3. `TransactionRepository.java`

```java
package repository;

import model.Transaction;
import util.DBConnection;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class TransactionRepository {

    public static void addTransaction(Transaction transaction) {
        String sql = "INSERT INTO transactions (transactionId, fromAccountId, toAccountId, amount, dateTime, type) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, transaction.getTransactionId());
            pstmt.setString(2, transaction.getFromAccountId());
            pstmt.setString(3, transaction.getToAccountId());
            pstmt.setDouble(4, transaction.getAmount());
            pstmt.setTimestamp(5, Timestamp.valueOf(transaction.getDateTime()));
            pstmt.setString(6, transaction.getType());
            pstmt.executeUpdate();
        } catch(SQLException e) {
            e.printStackTrace();
        }
    }

    public static List<Transaction> getAllTransactions() {
        List<Transaction> transactions = new ArrayList<>();
        String sql = "SELECT * FROM transactions";
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while(rs.next()){
                Transaction t = new Transaction(
                        rs.getString("transactionId"),
                        rs.getString("fromAccountId"),
                        rs.getString("toAccountId"),
                        rs.getDouble("amount"),
                        rs.getTimestamp("dateTime").toLocalDateTime(),
                        rs.getString("type"));
                transactions.add(t);
            }
        } catch(SQLException e) {
            e.printStackTrace();
        }
        return transactions;
    }

    public static List<Transaction> getTransactionsByAccountId(String accountId) {
        List<Transaction> transactions = new ArrayList<>();
        String sql = "SELECT * FROM transactions WHERE fromAccountId = ? OR toAccountId = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, accountId);
            pstmt.setString(2, accountId);
            ResultSet rs = pstmt.executeQuery();
            while(rs.next()){
                Transaction t = new Transaction(
                        rs.getString("transactionId"),
                        rs.getString("fromAccountId"),
                        rs.getString("toAccountId"),
                        rs.getDouble("amount"),
                        rs.getTimestamp("dateTime").toLocalDateTime(),
                        rs.getString("type"));
                transactions.add(t);
            }
        } catch(SQLException e) {
            e.printStackTrace();
        }
        return transactions;
    }
}
```

---

## 7. Les services (logique métier)

### 7.1. `AuthService.java`

```java
package service;

import model.User;
import repository.UserRepository;

public class AuthService {

    public static User login(String username, String password) {
        User user = UserRepository.findByUsername(username);
        if(user != null && user.getPassword().equals(password)) {
            return user;
        }
        return null;
    }
}
```

### 7.2. `UserService.java`

```java
package service;

import model.User;
import repository.UserRepository;
import java.util.UUID;

public class UserService {

    public static void createUser(String username, String password, String role) {
        String userId = UUID.randomUUID().toString();
        User newUser = new User(userId, username, password, role);
        UserRepository.addUser(newUser);
    }

    public static void updateUser(String userId, String newUsername, String newPassword, String newRole) {
        // Récupération puis modification de l'utilisateur
        for(User u : UserRepository.getAllUsers()){
            if(u.getUserId().equals(userId)) {
                u.setUsername(newUsername);
                u.setPassword(newPassword);
                u.setRole(newRole);
                UserRepository.updateUser(u);
                return;
            }
        }
    }

    public static void deleteUser(String userId) {
        UserRepository.deleteUser(userId);
    }
}
```

### 7.3. `AccountService.java`

Cette classe orchestre les opérations sur les comptes (dépôt, retrait, virement, consultation du solde et historique).

```java
package service;

import model.Account;
import model.Transaction;
import repository.AccountRepository;
import repository.TransactionRepository;
import java.time.LocalDateTime;
import java.util.UUID;

public class AccountService {

    public static double getBalance(String accountId) {
        Account account = AccountRepository.findById(accountId);
        return (account != null) ? account.getBalance() : 0.0;
    }

    public static boolean deposit(String accountId, double amount) {
        Account account = AccountRepository.findById(accountId);
        if (account != null) {
            account.setBalance(account.getBalance() + amount);
            AccountRepository.updateAccount(account);

            Transaction transaction = new Transaction(
                UUID.randomUUID().toString(),
                null,  // Pas de compte source pour un dépôt
                accountId,
                amount,
                LocalDateTime.now(),
                "DEPOT"
            );
            TransactionRepository.addTransaction(transaction);
            return true;
        }
        return false;
    }

    public static boolean withdraw(String accountId, double amount) {
        Account account = AccountRepository.findById(accountId);
        if (account != null && account.getBalance() >= amount) {
            account.setBalance(account.getBalance() - amount);
            AccountRepository.updateAccount(account);

            Transaction transaction = new Transaction(
                UUID.randomUUID().toString(),
                accountId,
                null,  // Pas de compte destination pour un retrait
                amount,
                LocalDateTime.now(),
                "RETRAIT"
            );
            TransactionRepository.addTransaction(transaction);
            return true;
        }
        return false;
    }

    public static boolean transfer(String fromAccountId, String toAccountId, double amount) {
        Account fromAcc = AccountRepository.findById(fromAccountId);
        Account toAcc = AccountRepository.findById(toAccountId);
        if (fromAcc != null && toAcc != null && fromAcc.getBalance() >= amount) {
            fromAcc.setBalance(fromAcc.getBalance() - amount);
            AccountRepository.updateAccount(fromAcc);

            toAcc.setBalance(toAcc.getBalance() + amount);
            AccountRepository.updateAccount(toAcc);

            Transaction transaction = new Transaction(
                UUID.randomUUID().toString(),
                fromAccountId,
                toAccountId,
                amount,
                LocalDateTime.now(),
                "VIREMENT"
            );
            TransactionRepository.addTransaction(transaction);
            return true;
        }
        return false;
    }

    public static void showTransactionHistory(String accountId) {
        for (Transaction t : TransactionRepository.getTransactionsByAccountId(accountId)) {
            System.out.println("ID: " + t.getTransactionId() +
                               ", Type: " + t.getType() +
                               ", Montant: " + t.getAmount() +
                               ", Date: " + t.getDateTime() +
                               ", From: " + t.getFromAccountId() +
                               ", To: " + t.getToAccountId());
        }
    }
}
```

---

## 8. Classe utilitaire pour la saisie console

### 8.1. `ConsoleInput.java`

```java
package util;

import java.util.Scanner;

public class ConsoleInput {
    private static Scanner scanner = new Scanner(System.in);

    public static String readString(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine();
    }

    public static int readInt(String prompt) {
        System.out.print(prompt);
        while (!scanner.hasNextInt()) {
            System.out.print("Veuillez saisir un nombre valide : ");
            scanner.next();
        }
        int value = scanner.nextInt();
        scanner.nextLine();
        return value;
    }

    public static double readDouble(String prompt) {
        System.out.print(prompt);
        while (!scanner.hasNextDouble()) {
            System.out.print("Veuillez saisir un nombre décimal valide : ");
            scanner.next();
        }
        double value = scanner.nextDouble();
        scanner.nextLine();
        return value;
    }
}
```

---

## 9. Le point d’entrée de l’application : `Main.java`

Ce fichier orchestre le démarrage, la connexion et la navigation dans les menus (pour CLIENT et ADMIN).

```java
package main;

import model.User;
import service.AuthService;
import service.AccountService;
import service.UserService;
import util.ConsoleInput;
import repository.AccountRepository;
import model.Account;

public class Main {
    public static void main(String[] args) {
        while (true) {
            System.out.println("\n=== APPLICATION BANCAIRE ===");
            System.out.println("1. Se connecter");
            System.out.println("2. Quitter");

            int choice = ConsoleInput.readInt("Votre choix : ");

            switch (choice) {
                case 1:
                    User loggedInUser = handleLogin();
                    if (loggedInUser != null) {
                        if ("ADMIN".equalsIgnoreCase(loggedInUser.getRole())) {
                            adminMenu(loggedInUser);
                        } else {
                            clientMenu(loggedInUser);
                        }
                    }
                    break;
                case 2:
                    System.out.println("Au revoir !");
                    System.exit(0);
                    break;
                default:
                    System.out.println("Choix invalide.");
            }
        }
    }

    private static User handleLogin() {
        String username = ConsoleInput.readString("Nom d'utilisateur : ");
        String password = ConsoleInput.readString("Mot de passe : ");
        User user = AuthService.login(username, password);
        if (user == null) {
            System.out.println("Échec de connexion. Vérifiez vos identifiants.");
        } else {
            System.out.println("Connexion réussie. Bienvenue, " + user.getUsername() + " !");
        }
        return user;
    }

    private static void clientMenu(User user) {
        String userId = user.getUserId();
        // On cherche le compte associé à l'utilisateur
        Account account = AccountRepository.findByUserId(userId);
        String accountId;
        if (account == null) {
            System.out.println("Aucun compte trouvé pour cet utilisateur. Création d'un compte...");
            accountId = java.util.UUID.randomUUID().toString();
            double initialBalance = 0.0;
            account = new Account(accountId, userId, initialBalance);
            AccountRepository.addAccount(account);
            System.out.println("Compte créé avec ID : " + accountId);
        } else {
            accountId = account.getAccountId();
        }

        boolean logout = false;
        while (!logout) {
            System.out.println("\n=== MENU CLIENT ===");
            System.out.println("1. Déposer de l'argent");
            System.out.println("2. Retirer de l'argent");
            System.out.println("3. Afficher le solde");
            System.out.println("4. Effectuer un virement");
            System.out.println("5. Consulter l'historique des transactions");
            System.out.println("6. Se déconnecter");

            int choice = ConsoleInput.readInt("Votre choix : ");

            switch (choice) {
                case 1:
                    double amountDeposit = ConsoleInput.readDouble("Montant à déposer : ");
                    if (AccountService.deposit(accountId, amountDeposit)) {
                        System.out.println("Dépôt effectué avec succès.");
                    } else {
                        System.out.println("Échec du dépôt.");
                    }
                    break;
                case 2:
                    double amountWithdraw = ConsoleInput.readDouble("Montant à retirer : ");
                    if (AccountService.withdraw(accountId, amountWithdraw)) {
                        System.out.println("Retrait effectué avec succès.");
                    } else {
                        System.out.println("Échec du retrait (fonds insuffisants ?).");
                    }
                    break;
                case 3:
                    double balance = AccountService.getBalance(accountId);
                    System.out.println("Solde actuel : " + balance);
                    break;
                case 4:
                    String toAccountId = ConsoleInput.readString("ID du compte destinataire : ");
                    double amountTransfer = ConsoleInput.readDouble("Montant à transférer : ");
                    if (AccountService.transfer(accountId, toAccountId, amountTransfer)) {
                        System.out.println("Virement effectué avec succès.");
                    } else {
                        System.out.println("Échec du virement (fonds insuffisants ou compte invalide ?).");
                    }
                    break;
                case 5:
                    AccountService.showTransactionHistory(accountId);
                    break;
                case 6:
                    logout = true;
                    System.out.println("Déconnexion...");
                    break;
                default:
                    System.out.println("Choix invalide.");
            }
        }
    }

    private static void adminMenu(User user) {
        boolean logout = false;
        while (!logout) {
            System.out.println("\n=== MENU ADMIN ===");
            System.out.println("1. Ajouter un utilisateur");
            System.out.println("2. Modifier un utilisateur");
            System.out.println("3. Supprimer un utilisateur");
            System.out.println("4. Se déconnecter");

            int choice = ConsoleInput.readInt("Votre choix : ");

            switch (choice) {
                case 1:
                    addUserFlow();
                    break;
                case 2:
                    updateUserFlow();
                    break;
                case 3:
                    deleteUserFlow();
                    break;
                case 4:
                    logout = true;
                    System.out.println("Déconnexion...");
                    break;
                default:
                    System.out.println("Choix invalide.");
            }
        }
    }

    private static void addUserFlow() {
        System.out.println("\n=== AJOUTER UN UTILISATEUR ===");
        String username = ConsoleInput.readString("Nom d'utilisateur : ");
        String password = ConsoleInput.readString("Mot de passe : ");
        String role = ConsoleInput.readString("Rôle (CLIENT/ADMIN) : ");
        UserService.createUser(username, password, role);
        System.out.println("Utilisateur créé avec succès.");
    }

    private static void updateUserFlow() {
        System.out.println("\n=== MODIFIER UN UTILISATEUR ===");
        String userId = ConsoleInput.readString("ID de l'utilisateur à modifier : ");
        String newUsername = ConsoleInput.readString("Nouveau nom d'utilisateur : ");
        String newPassword = ConsoleInput.readString("Nouveau mot de passe : ");
        String newRole = ConsoleInput.readString("Nouveau rôle (CLIENT/ADMIN) : ");
        UserService.updateUser(userId, newUsername, newPassword, newRole);
        System.out.println("Utilisateur modifié avec succès.");
    }

    private static void deleteUserFlow() {
        System.out.println("\n=== SUPPRIMER UN UTILISATEUR ===");
        String userId = ConsoleInput.readString("ID de l'utilisateur à supprimer : ");
        UserService.deleteUser(userId);
        System.out.println("Utilisateur supprimé avec succès.");
    }
}
```

---

## 10. Compilation et exécution

1. **Organisation du code**  
   Respecte l’arborescence des dossiers correspondant aux packages.

2. **Compilation**  
   Depuis le dossier parent, compile l’ensemble du projet en incluant le driver MySQL dans le classpath. Par exemple :
   ```bash
   javac -cp .:mysql-connector-java-8.x.x.jar main/Main.java model/*.java repository/*.java service/*.java util/*.java
   ```
   *(Sur Windows, remplace `:` par `;` dans le classpath.)*

3. **Exécution**  
   Lance l’application en incluant également le JAR du connecteur :
   ```bash
   java -cp .:mysql-connector-java-8.x.x.jar main.Main
   ```
---

En suivant ce rapport, tu disposeras d’un prototype fonctionnel utilisant une base de données MySQL pour stocker les données de l’application bancaire.

**Bon développement !**
package main;

import model.Account;
import model.Transaction;
import model.User;
import service.AccountService;
import service.AuthService;
import service.UserService;
import util.ConsoleInput;

import java.sql.SQLException;
import java.util.List;

public class Main {
    private static final AuthService authService = new AuthService();
    private static final AccountService accountService = new AccountService();
    private static final UserService userService = new UserService();

    public static void main(String[] args) {
        try {
            while (true) {
                if (!authService.isLoggedIn()) {
                    showLoginMenu();
                } else {
                    if (authService.isAdmin()) {
                        showAdminMenu();
                    } else {
                        showUserMenu();
                    }
                }
            }
        } catch (SQLException e) {
            System.out.println("Database error: " + e.getMessage());
        } finally {
            ConsoleInput.close();
        }
    }

    private static void showLoginMenu() throws SQLException {
        System.out.println("\n=== Bank Login ===");
        String username = ConsoleInput.readString("Username: ");
        String password = ConsoleInput.readString("Password: ");

        if (authService.login(username, password)) {
            System.out.println("Login successful!");
        } else {
            System.out.println("Invalid credentials!");
        }
    }

    private static void showUserMenu() throws SQLException {
        while (true) {
            System.out.println("\n=== User Menu ===");
            System.out.println("1. Check Balance");
            System.out.println("2. Deposit");
            System.out.println("3. Withdraw");
            System.out.println("4. Transfer");
            System.out.println("5. Transaction History");
            System.out.println("6. Logout");
            System.out.println("0. Exit");

            int choice = ConsoleInput.readInt("Enter your choice: ");

            switch (choice) {
                case 1:
                    checkBalance();
                    break;
                case 2:
                    deposit();
                    break;
                case 3:
                    withdraw();
                    break;
                case 4:
                    transfer();
                    break;
                case 5:
                    showTransactionHistory();
                    break;
                case 6:
                    authService.logout();
                    return;
                case 0:
                    System.exit(0);
                default:
                    System.out.println("Invalid choice!");
            }
        }
    }

    private static void showAdminMenu() throws SQLException {
        while (true) {
            System.out.println("\n=== Admin Menu ===");
            System.out.println("1. List All Users");
            System.out.println("2. Add User");
            System.out.println("3. Update User");
            System.out.println("4. Delete User");
            System.out.println("5. List All Transactions");
            System.out.println("6. Logout");
            System.out.println("0. Exit");

            int choice = ConsoleInput.readInt("Enter your choice: ");

            switch (choice) {
                case 1:
                    listAllUsers();
                    break;
                case 2:
                    addUser();
                    break;
                case 3:
                    updateUser();
                    break;
                case 4:
                    deleteUser();
                    break;
                case 5:
                    listAllTransactions();
                    break;
                case 6:
                    authService.logout();
                    return;
                case 0:
                    System.exit(0);
                default:
                    System.out.println("Invalid choice!");
            }
        }
    }

    private static void checkBalance() throws SQLException {
        int userId = authService.getCurrentUser().getUserId();
        // Create account if it doesn't exist
        accountService.createAccountIfNotExists(userId);

        // Now get the account (should exist now)
        Account account = accountService.getAccountByUserId(userId);
        if (account != null) {
            System.out.println("\n=== Account Balance ===");
            System.out.println("Account ID: " + account.getAccountId());
            System.out.println("Current balance: $" + String.format("%.2f", account.getBalance()));
        } else {
            System.out.println("Account not found!");
        }
    }

    private static void deposit() throws SQLException {
        int userId = authService.getCurrentUser().getUserId();
        // Create account if it doesn't exist
        accountService.createAccountIfNotExists(userId);

        double amount = ConsoleInput.readDouble("Enter amount to deposit: ");
        if (amount > 0) {
            accountService.deposit(userId, amount);
            System.out.println("Deposit successful!");
        } else {
            System.out.println("Invalid amount!");
        }
    }

    private static void withdraw() throws SQLException {
        int userId = authService.getCurrentUser().getUserId();
        // Create account if it doesn't exist
        accountService.createAccountIfNotExists(userId);

        double amount = ConsoleInput.readDouble("Enter amount to withdraw: ");
        if (amount > 0) {
            accountService.withdraw(userId, amount);
            System.out.println("Withdrawal successful!");
        } else {
            System.out.println("Invalid amount!");
        }
    }

    private static void transfer() throws SQLException {
        int fromUserId = authService.getCurrentUser().getUserId();
        // Create account if it doesn't exist for current user
        accountService.createAccountIfNotExists(fromUserId);

        int toUserId = ConsoleInput.readInt("Enter recipient user ID: ");
        // Create account if it doesn't exist for recipient
        accountService.createAccountIfNotExists(toUserId);

        double amount = ConsoleInput.readDouble("Enter amount to transfer: ");
        if (amount > 0) {
            accountService.transfer(fromUserId, toUserId, amount);
            System.out.println("Transfer successful!");
        } else {
            System.out.println("Invalid amount!");
        }
    }

    private static void showTransactionHistory() throws SQLException {
        int userId = authService.getCurrentUser().getUserId();
        // Create account if it doesn't exist
        accountService.createAccountIfNotExists(userId);

        List<Transaction> transactions = accountService.getTransactionHistory(userId);
        System.out.println("\n=== Transaction History ===");
        if (transactions.isEmpty()) {
            System.out.println("No transactions found.");
        } else {
            for (Transaction transaction : transactions) {
                System.out.println(transaction.getDateTime() + " - " +
                        transaction.getType() + " - Amount: $" + String.format("%.2f", transaction.getAmount()));
            }
        }
    }

    private static void listAllUsers() throws SQLException {
        List<User> users = userService.getAllUsers();
        System.out.println("\n=== All Users ===");
        for (User user : users) {
            System.out.println("ID: " + user.getUserId() + ", Username: " + user.getUsername() +
                    ", Role: " + user.getRole());
        }
    }

    private static void addUser() throws SQLException {
        String username = ConsoleInput.readString("Enter username: ");
        String password = ConsoleInput.readString("Enter password: ");
        String role = ConsoleInput.readString("Enter role (USER/ADMIN): ");
        userService.createUser(username, password, role);
        System.out.println("User added successfully!");
    }

    private static void updateUser() throws SQLException {
        int userId = ConsoleInput.readInt("Enter user ID to update: ");
        String username = ConsoleInput.readString("Enter new username: ");
        String password = ConsoleInput.readString("Enter new password: ");
        String role = ConsoleInput.readString("Enter new role (USER/ADMIN): ");

        User user = new User(userId, username, password, role);
        userService.updateUser(user);
        System.out.println("User updated successfully!");
    }

    private static void deleteUser() throws SQLException {
        int userId = ConsoleInput.readInt("Enter user ID to delete: ");
        userService.deleteUser(userId);
        System.out.println("User deleted successfully!");
    }

    private static void listAllTransactions() throws SQLException {
        List<Transaction> transactions = accountService.getAllTransactions();
        System.out.println("\n=== All Transactions ===");
        if (transactions.isEmpty()) {
            System.out.println("No transactions found.");
        } else {
            for (Transaction transaction : transactions) {
                System.out.println(transaction.getDateTime() + " - " +
                        transaction.getType() + " - Amount: $" + String.format("%.2f", transaction.getAmount()) +
                        " - From: " + transaction.getFromAccountId() +
                        " - To: " + transaction.getToAccountId());
            }
        }
    }
}
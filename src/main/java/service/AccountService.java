package service;

import model.Account;
import model.Transaction;
import repository.AccountRepository;
import repository.TransactionRepository;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class AccountService {
    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;

    public AccountService() {
        this.accountRepository = new AccountRepository();
        this.transactionRepository = new TransactionRepository();
    }

    public Account getAccountByUserId(int userId) throws SQLException {
        return accountRepository.findByUserId(userId);
    }

    public void deposit(int userId, double amount) throws SQLException {
        Account account = accountRepository.findByUserId(userId);
        if (account != null) {
            double newBalance = account.getBalance() + amount;
            accountRepository.updateBalance(userId, newBalance);

            Transaction transaction = new Transaction(
                    0, // ID will be set by the database
                    account.getAccountId(),
                    account.getAccountId(),
                    amount,
                    LocalDateTime.now(),
                    "DEPOSIT");
            transactionRepository.create(transaction);
        }
    }

    public void withdraw(int userId, double amount) throws SQLException {
        Account account = accountRepository.findByUserId(userId);
        if (account != null && account.getBalance() >= amount) {
            double newBalance = account.getBalance() - amount;
            accountRepository.updateBalance(userId, newBalance);

            Transaction transaction = new Transaction(
                    0, // ID will be set by the database
                    account.getAccountId(),
                    account.getAccountId(),
                    amount,
                    LocalDateTime.now(),
                    "WITHDRAWAL");
            transactionRepository.create(transaction);
        }
    }

    public void transfer(int fromUserId, int toUserId, double amount) throws SQLException {
        Account fromAccount = accountRepository.findByUserId(fromUserId);
        Account toAccount = accountRepository.findByUserId(toUserId);

        if (fromAccount != null && toAccount != null && fromAccount.getBalance() >= amount) {
            // Update source account
            double newFromBalance = fromAccount.getBalance() - amount;
            accountRepository.updateBalance(fromUserId, newFromBalance);

            // Update destination account
            double newToBalance = toAccount.getBalance() + amount;
            accountRepository.updateBalance(toUserId, newToBalance);

            // Create transaction record
            Transaction transaction = new Transaction(
                    0, // ID will be set by the database
                    fromAccount.getAccountId(),
                    toAccount.getAccountId(),
                    amount,
                    LocalDateTime.now(),
                    "TRANSFER");
            transactionRepository.create(transaction);
        }
    }

    public List<Transaction> getTransactionHistory(int userId) throws SQLException {
        Account account = accountRepository.findByUserId(userId);
        if (account != null) {
            return transactionRepository.findByAccountId(account.getAccountId());
        }
        return new ArrayList<>();
    }

    public List<Transaction> getAllTransactions() throws SQLException {
        return transactionRepository.findAll();
    }

    public void createAccountIfNotExists(int userId) throws SQLException {
        Account account = accountRepository.findByUserId(userId);
        if (account == null) {
            Account newAccount = new Account(0, userId, 0.0);
            accountRepository.create(newAccount);
        }
    }
}
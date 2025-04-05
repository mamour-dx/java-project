package service;

import model.Account;
import model.Transaction;
import repository.AccountRepository;
import repository.TransactionRepository;

import java.sql.SQLException;
import java.time.LocalDateTime;
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

    public void deposit(int accountId, double amount) throws SQLException {
        Account account = accountRepository.findByUserId(accountId);
        if (account != null) {
            double newBalance = account.getBalance() + amount;
            accountRepository.updateBalance(accountId, newBalance);

            Transaction transaction = new Transaction(
                    0, // ID will be set by the database
                    accountId,
                    accountId,
                    amount,
                    LocalDateTime.now(),
                    "DEPOSIT");
            transactionRepository.create(transaction);
        }
    }

    public void withdraw(int accountId, double amount) throws SQLException {
        Account account = accountRepository.findByUserId(accountId);
        if (account != null && account.getBalance() >= amount) {
            double newBalance = account.getBalance() - amount;
            accountRepository.updateBalance(accountId, newBalance);

            Transaction transaction = new Transaction(
                    0, // ID will be set by the database
                    accountId,
                    accountId,
                    amount,
                    LocalDateTime.now(),
                    "WITHDRAWAL");
            transactionRepository.create(transaction);
        }
    }

    public void transfer(int fromAccountId, int toAccountId, double amount) throws SQLException {
        Account fromAccount = accountRepository.findByUserId(fromAccountId);
        Account toAccount = accountRepository.findByUserId(toAccountId);

        if (fromAccount != null && toAccount != null && fromAccount.getBalance() >= amount) {
            // Update source account
            double newFromBalance = fromAccount.getBalance() - amount;
            accountRepository.updateBalance(fromAccountId, newFromBalance);

            // Update destination account
            double newToBalance = toAccount.getBalance() + amount;
            accountRepository.updateBalance(toAccountId, newToBalance);

            // Create transaction record
            Transaction transaction = new Transaction(
                    0, // ID will be set by the database
                    fromAccountId,
                    toAccountId,
                    amount,
                    LocalDateTime.now(),
                    "TRANSFER");
            transactionRepository.create(transaction);
        }
    }

    public List<Transaction> getTransactionHistory(int accountId) throws SQLException {
        return transactionRepository.findByAccountId(accountId);
    }

    public List<Transaction> getAllTransactions() throws SQLException {
        return transactionRepository.findAll();
    }
}
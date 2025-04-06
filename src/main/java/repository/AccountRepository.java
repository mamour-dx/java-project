package repository;

import model.Account;
import util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AccountRepository {

    public Account findByUserId(int userId) throws SQLException {
        String sql = "SELECT * FROM accounts WHERE userId = ?";
        try (Connection conn = DBConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return new Account(
                        rs.getInt("accountId"),
                        rs.getInt("userId"),
                        rs.getDouble("balance"));
            }
        }
        return null;
    }

    public void create(Account account) throws SQLException {
        String sql = "INSERT INTO accounts (userId, balance) VALUES (?, ?)";
        try (Connection conn = DBConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, account.getUserId());
            stmt.setDouble(2, account.getBalance());
            stmt.executeUpdate();
        }
    }

    public void updateBalance(int userId, double newBalance) throws SQLException {
        String sql = "UPDATE accounts SET balance = ? WHERE userId = ?";
        try (Connection conn = DBConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setDouble(1, newBalance);
            stmt.setInt(2, userId);
            stmt.executeUpdate();
        }
    }

    public List<Account> findAll() throws SQLException {
        List<Account> accounts = new ArrayList<>();
        String sql = "SELECT * FROM accounts";

        try (Connection conn = DBConnection.getConnection();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                accounts.add(new Account(
                        rs.getInt("accountId"),
                        rs.getInt("userId"),
                        rs.getDouble("balance")));
            }
        }
        return accounts;
    }
}
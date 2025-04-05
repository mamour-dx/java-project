package repository;

import model.Transaction;
import util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TransactionRepository {

    public void create(Transaction transaction) throws SQLException {
        String sql = "INSERT INTO transactions (fromAccountId, toAccountId, amount, dateTime, type) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, transaction.getFromAccountId());
            stmt.setInt(2, transaction.getToAccountId());
            stmt.setDouble(3, transaction.getAmount());
            stmt.setTimestamp(4, Timestamp.valueOf(transaction.getDateTime()));
            stmt.setString(5, transaction.getType());
            stmt.executeUpdate();
        }
    }

    public List<Transaction> findByAccountId(int accountId) throws SQLException {
        List<Transaction> transactions = new ArrayList<>();
        String sql = "SELECT * FROM transactions WHERE fromAccountId = ? OR toAccountId = ? ORDER BY dateTime DESC";

        try (Connection conn = DBConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, accountId);
            stmt.setInt(2, accountId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                transactions.add(new Transaction(
                        rs.getInt("transactionId"),
                        rs.getInt("fromAccountId"),
                        rs.getInt("toAccountId"),
                        rs.getDouble("amount"),
                        rs.getTimestamp("dateTime").toLocalDateTime(),
                        rs.getString("type")));
            }
        }
        return transactions;
    }

    public List<Transaction> findAll() throws SQLException {
        List<Transaction> transactions = new ArrayList<>();
        String sql = "SELECT * FROM transactions ORDER BY dateTime DESC";

        try (Connection conn = DBConnection.getConnection();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                transactions.add(new Transaction(
                        rs.getInt("transactionId"),
                        rs.getInt("fromAccountId"),
                        rs.getInt("toAccountId"),
                        rs.getDouble("amount"),
                        rs.getTimestamp("dateTime").toLocalDateTime(),
                        rs.getString("type")));
            }
        }
        return transactions;
    }
}
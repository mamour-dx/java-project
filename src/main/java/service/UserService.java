package service;

import model.Account;
import model.User;
import repository.AccountRepository;
import repository.UserRepository;

import java.sql.SQLException;
import java.util.List;

public class UserService {
    private final UserRepository userRepository;
    private final AccountRepository accountRepository;

    public UserService() {
        this.userRepository = new UserRepository();
        this.accountRepository = new AccountRepository();
    }

    public void createUser(String username, String password, String role) throws SQLException {
        // Create user
        User user = new User(0, username, password, role);
        int userId = userRepository.create(user);

        // Create account for the user with initial balance of 0
        Account account = new Account(0, userId, 0.0);
        accountRepository.create(account);
    }

    public void updateUser(User user) throws SQLException {
        userRepository.update(user);
    }

    public void deleteUser(int userId) throws SQLException {
        userRepository.delete(userId);
    }

    public List<User> getAllUsers() throws SQLException {
        return userRepository.findAll();
    }

    public User getUserByUsername(String username) throws SQLException {
        return userRepository.findByUsername(username);
    }
}
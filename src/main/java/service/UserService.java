package service;

import model.User;
import repository.UserRepository;

import java.sql.SQLException;
import java.util.List;

public class UserService {
    private final UserRepository userRepository;

    public UserService() {
        this.userRepository = new UserRepository();
    }

    public void createUser(String username, String password, String role) throws SQLException {
        User user = new User(0, username, password, role);
        userRepository.create(user);
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
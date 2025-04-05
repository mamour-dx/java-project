package service;

import model.User;
import repository.UserRepository;

import java.sql.SQLException;

public class AuthService {
    private final UserRepository userRepository;
    private User currentUser;

    public AuthService() {
        this.userRepository = new UserRepository();
        this.currentUser = null;
    }

    public boolean login(String username, String password) throws SQLException {
        User user = userRepository.findByUsername(username);
        if (user != null && user.getPassword().equals(password)) {
            this.currentUser = user;
            return true;
        }
        return false;
    }

    public void logout() {
        this.currentUser = null;
    }

    public User getCurrentUser() {
        return currentUser;
    }

    public boolean isLoggedIn() {
        return currentUser != null;
    }

    public boolean isAdmin() {
        return currentUser != null && "ADMIN".equals(currentUser.getRole());
    }
}
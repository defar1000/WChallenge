package co.com.wolox.WChallenge.service;

import co.com.wolox.WChallenge.model.User;

import java.util.List;

public interface UserService {
    User getUserById(int id);
    User getUserByEmail(String email);
    List<User> getAllUsers();
}

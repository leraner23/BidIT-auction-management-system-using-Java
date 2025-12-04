package com.project.BidIT.Service.User;

import com.project.BidIT.entity.User;

import java.util.List;

public interface UserService {
    User registerUser(User user);
    User findByEmail(String email);
    List<User> getAllUser();
    void deleteUser(Long userID);
    User loginUser(String email, String rawPassword);
}
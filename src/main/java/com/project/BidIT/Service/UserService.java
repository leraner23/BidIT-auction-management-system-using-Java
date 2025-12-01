package com.project.BidIT.Service;

import com.project.BidIT.entity.Admin;
import com.project.BidIT.entity.User;

import java.util.List;

public interface UserService {
    User registerUser(User user);
    User findByEmail(String email);
    List<User> getAllUser();
    void deleteUser(Long userID);
}
package com.project.BidIT.Service;

import com.project.BidIT.entity.Admin;
import com.project.BidIT.entity.User;

import java.util.List;
import java.util.Optional;

public interface AdminService {

    Admin registerAdmin(Admin admin);

    Admin findAdminByEmail(String email);

    List<Admin> getAllAdmin();

    void deleteAdmin(Long adminId);
}
package com.project.BidIT.Service.Admin;

import com.project.BidIT.entity.Admin;
import com.project.BidIT.entity.User;

import java.util.List;

public interface AdminService {

    Admin registerAdmin(Admin admin);

    Admin findAdminByEmail(String email);

    List<Admin> getAllAdmin();
    Admin loginAdmin(String email, String rawPassword);
    void deleteAdmin(Long adminId);
}
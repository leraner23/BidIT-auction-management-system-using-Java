package com.project.BidIT.Service.Admin;

import com.project.BidIT.Repo.AdminRepo;
import com.project.BidIT.entity.Admin;
import com.project.BidIT.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdminServiceImpl implements AdminService {

    private final AdminRepo adminRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public AdminServiceImpl(AdminRepo adminRepository,
                            PasswordEncoder passwordEncoder) {
        this.adminRepository = adminRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public Admin registerAdmin(Admin admin) {
        if (adminRepository.existsByEmail(admin.getEmail())) {
            throw new RuntimeException("Email already exists!");
        }

        admin.setPassword(passwordEncoder.encode(admin.getPassword()));

        return adminRepository.save(admin);
    }

    @Override
    public Admin findAdminByEmail(String email) {
        return adminRepository.findAdminByEmail(email)
                .orElse(null);
    }

    @Override
    public Admin loginAdmin(String email, String rawPassword) {
        Admin admin = findAdminByEmail(email);
        if (admin == null) return null;

        if (!passwordEncoder.matches(rawPassword, admin.getPassword())) {
            return null;
        }

        return admin;
    }


    @Override
    public List<Admin> getAllAdmin() {
        return adminRepository.findAll();
    }

    @Override
    public void deleteAdmin(Long adminId) {
        adminRepository.deleteById(adminId);
    }
}

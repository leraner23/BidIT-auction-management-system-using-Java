package com.project.BidIT.Service.Admin;
import com.project.BidIT.Repo.AdminRepo;
import com.project.BidIT.entity.Admin;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomAdminService implements UserDetailsService {
    private final AdminRepo adminRepo;

    public CustomAdminService(AdminRepo adminRepo) {
        this.adminRepo = adminRepo;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Admin appAdmin = adminRepo.findAdminByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Admin not found"));

        return org.springframework.security.core.userdetails.User.builder()
                .username(appAdmin.getEmail())
                .password(appAdmin.getPassword())
                .roles("ADMIN")
                .build();
    }
}


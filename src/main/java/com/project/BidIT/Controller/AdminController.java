package com.project.BidIT.Controller;

import com.project.BidIT.DTO.UserDto;
import com.project.BidIT.Service.AdminService;
import com.project.BidIT.Service.UserService;
import com.project.BidIT.entity.Admin;
import com.project.BidIT.entity.User;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Value("${file.upload.admins}")
    private String uploadDir;


    @Autowired
    private AdminService adminService;
    // Show registration form
    @GetMapping("/register")
    public String userRegister(Model model){
        model.addAttribute("admins", new UserDto());
        return "AdminRegisterForm";
    }

    //Handles the registration form
    @PostMapping("/register")
    public String userRegister(@Valid @ModelAttribute("admins") UserDto dto, BindingResult bindingResult,
                               Model model){
        System.out.println("Form submitted: " + dto.getEmail());

        if (bindingResult.hasErrors()) {
            bindingResult.getAllErrors().forEach(err -> System.out.println("Validation error: " + err));
            return "AdminRegisterForm";
        }

        Admin admin = new Admin();
        admin.setFullName(dto.getFullName());
        admin.setUsername(dto.getUsername());
        admin.setEmail(dto.getEmail());
        admin.setPhone(dto.getPhone());
        admin.setGender(dto.getGender());
        admin.setAddress(dto.getPAddress());

        admin.setPassword(dto.getPassword());

        System.out.println("no error ");
        try {
            System.out.println("Processing registration...");
            MultipartFile file = dto.getUserImage();

            if (file != null && !file.isEmpty()) {
                String filename = UUID.randomUUID() + "_" + file.getOriginalFilename();
                Path savePath = Paths.get(uploadDir , filename);

                Files.createDirectories(savePath.getParent());
                file.transferTo(savePath.toFile());

                admin.setUserImage(filename);
            }
            Admin savedAdmin= adminService.registerAdmin(admin);
            System.out.println("Saved user ID: " + savedAdmin.getAdminId());
            return "redirect:/admin/login";
        } catch (Exception ex) {
            ex.printStackTrace();
            model.addAttribute("errorMessage", ex.getMessage());
            return "AdminRegisterForm";
        }


    }

    @GetMapping("/login")
    public String userlogin( @RequestParam(value = "error", required = false) String error,
                             @RequestParam(value = "logout", required = false) String logout,Model model){
        model.addAttribute("admin", new Admin());
        if (error != null) {
            model.addAttribute("errorMessage", "Invalid email or password!");
        }
        if (logout != null) {
            model.addAttribute("successMessage", "You have been logged out successfully.");
        }

        return "AdminLogin";

    }


}

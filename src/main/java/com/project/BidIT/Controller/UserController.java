package com.project.BidIT.Controller;

import com.project.BidIT.Compontents.JwtUtil;
import com.project.BidIT.DTO.UserDto;
import com.project.BidIT.Repo.CategoryRepo;
import com.project.BidIT.Service.User.UserService;
import com.project.BidIT.entity.User;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
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
import java.security.Principal;
import java.util.UUID;

@Controller
@RequestMapping("/user")
public class UserController {

    @Value("${file.upload.users}")
    private String uploadDir;

    @Autowired
    private CategoryRepo categoryRepo;
    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserService userService;
    // Show registration form
    @GetMapping("/register")
    public String userRegister(Model model){
        model.addAttribute("users", new UserDto());
        return "User/UserRegisterForm";
    }

    //Handles the registration form
    @PostMapping("/register")
    public String userRegister(@Valid @ModelAttribute("users") UserDto dto, BindingResult bindingResult,
                               Model model){
        System.out.println("Form submitted: " + dto.getEmail());

        if(!dto.getPassword().equals(dto.getCpassword())){
            bindingResult.rejectValue("Cpassword", "error.user", "Passwords do not match");
        }

        if (bindingResult.hasErrors()) {
            bindingResult.getAllErrors().forEach(err -> System.out.println("Validation error: " + err));
            return "User/UserRegisterForm";
        }

        User user = new User();
        user.setFullName(dto.getFullName());
        user.setUsername(dto.getUsername());
        user.setEmail(dto.getEmail());
        user.setPhone(dto.getPhone());
        user.setGender(dto.getGender());
        user.setPAddress(dto.getPAddress());
        user.setUserAge(dto.getUserAge());
        user.setPassword(dto.getPassword());
        user.setCpassword(dto.getCpassword());
        System.out.println("no error ");
        try {
            System.out.println("Processing registration...");
            MultipartFile file = dto.getUserImage();

            if (file != null && !file.isEmpty()) {
                String filename = UUID.randomUUID() + "_" + file.getOriginalFilename();
                Path savePath = Paths.get(uploadDir , filename);

                Files.createDirectories(savePath.getParent());
                file.transferTo(savePath.toFile());

                user.setUserImage(filename);
            }
           User savedUser= userService.registerUser(user);
            System.out.println("Saved user ID: " + savedUser.getUserId());
            return "redirect:/user/login";
        } catch (Exception ex) {
            ex.printStackTrace();
            model.addAttribute("errorMessage", ex.getMessage());
            return "User/UserRegisterForm";
        }


    }

    @GetMapping("/login")
    public String userlogin( @RequestParam(value = "error", required = false) String error,
                             @RequestParam(value = "logout", required = false) String logout,Model model){
        model.addAttribute("user", new User());
        if (error != null) {
            model.addAttribute("errorMessage", "Invalid email or password!");
        }
        if (logout != null) {
            model.addAttribute("successMessage", "You have been logged out successfully.");
        }

        return "User/UserLogin";

    }

    @PostMapping("/login")
    public String loginUser(@RequestParam("email") String email,
                            @RequestParam("password") String password,
                            HttpServletResponse response,
                            Model model) {
        try {
            User loggedUser = userService.loginUser(email, password);

            if (loggedUser == null) {
                model.addAttribute("errorMessage", "Invalid email or password!");
                return "User/UserLogin";
            }

            String token = jwtUtil.generateToken(loggedUser);

            Cookie cookie = new Cookie("jwt_token", token);
            cookie.setHttpOnly(true);
            cookie.setPath("/");
            cookie.setMaxAge(24 * 60 * 60);
            response.addCookie(cookie);

            System.out.println("JWT cookie set: " + cookie.getValue());

            return "redirect:/user/dashboard";

        } catch (Exception ex) {
            ex.printStackTrace();
            model.addAttribute("errorMessage", ex.getMessage());
            return "User/UserLogin";
        }
    }
    @GetMapping("/dashboard")
    public String UserDashboard(Model model, Principal principal){
        System.out.println("Principal: " + principal);
        if (principal == null) {
            return "redirect:/user/login"; // extra safety
        }

        String email = principal.getName(); // get email from Jwt
        User loggedUser = userService.findByEmail(email);// fetch the logged user email
        model.addAttribute("users",loggedUser);
        model.addAttribute("amount","budget");
        return "User/UserDashboard";
    }



    @PostMapping("/logout")
    public String logout(HttpServletResponse response) {
        Cookie cookie = new Cookie("jwt_token", null);
        cookie.setMaxAge(0);
        cookie.setPath("/");
        response.addCookie(cookie);
        return "redirect:/user/login";
    }

}

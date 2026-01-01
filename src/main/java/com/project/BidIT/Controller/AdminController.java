package com.project.BidIT.Controller;

import com.project.BidIT.Compontents.JwtUtil;
import com.project.BidIT.DTO.AdminDto;
import com.project.BidIT.DTO.AdminUserDto;
import com.project.BidIT.Repo.*;
import com.project.BidIT.Service.Admin.AdminService;
import com.project.BidIT.Service.Item.ItemService;
import com.project.BidIT.entity.Admin;
import com.project.BidIT.entity.Item;
import com.project.BidIT.entity.User;
import com.project.BidIT.enums.Status;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.security.core.Authentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Value("${file.upload.admins}")
    private String uploadDir;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private ItemService itemService;

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private AdminService adminService;

    @Autowired
    private AdminRepo adminRepo;

    @Autowired
    private CategoryRepo categoryRepo;

@Autowired
private UserRepository userRepository;

@Autowired
private BudgetRepo budgetRepo;
    // Show registration form
    @GetMapping("/register")
    public String userRegister(Model model){
        model.addAttribute("admins", new AdminDto());
        return "Admin/AdminRegisterForm";
    }

    //Handles the registration form
    @PostMapping("/register")
    public String userRegister(@Valid @ModelAttribute("admins") AdminDto dto, BindingResult bindingResult,
                               Model model){
        System.out.println("Form submitted: " + dto.getEmail());

        if (bindingResult.hasErrors()) {
            bindingResult.getAllErrors().forEach(err -> System.out.println("Validation error: " + err));
            return "Admin/AdminRegisterForm";
        }

        Admin admin = new Admin();
        admin.setFullName(dto.getFullName());
        admin.setUsername(dto.getUsername());
        admin.setEmail(dto.getEmail());
        admin.setPhone(dto.getPhone());
        admin.setGender(dto.getGender());
        admin.setAddress(dto.getAddress());

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
            return "Admin/AdminRegisterForm";
        }


    }

    @GetMapping("/login")
    public String loginAdmin( @RequestParam(value = "error", required = false) String error,
                             @RequestParam(value = "logout", required = false) String logout,Model model){
        model.addAttribute("admin", new Admin());
        if (error != null) {
            model.addAttribute("errorMessage", "Invalid email or password!");
        }
        if (logout != null) {
            model.addAttribute("successMessage", "You have been logged out successfully.");
        }

        return "Admin/AdminLogin";

    }

    @PostMapping("/login")
    public String loginAdmin(@RequestParam("email") String email,
                            @RequestParam("password") String password,
                            HttpServletResponse response,
                            Model model) {
        try {
            System.out.println("Received Email: " + email);
            System.out.println("Received Password: " + password);
            Admin loggedAdmin = adminService.loginAdmin(email, password);


            System.out.println("Logged Admin: " + loggedAdmin);
            if (loggedAdmin == null) {
                model.addAttribute("errorMessage", "Invalid email or password!");
                return "Admin/AdminLogin";
            }

            String token = jwtUtil.generateTokenAdmin(loggedAdmin);

            Cookie cookie = new Cookie("admin_jwt_token", token);
            cookie.setHttpOnly(true);
            cookie.setPath("/");
            cookie.setMaxAge(24 * 60 * 60);
            response.addCookie(cookie);

            System.out.println("JWT cookie set: " + cookie.getValue());

            return "redirect:/admin/dashboard";

        } catch (Exception ex) {
            ex.printStackTrace();
            model.addAttribute("errorMessage", ex.getMessage());
            return "Admin/Adminlogin";
        }
    }

//    @GetMapping("/dashboard")
//    public String AdminDashboard(Model model, Principal principal){
//        System.out.println("Principal: " + principal);
//        if (principal == null) {
//            return "redirect:/admin/login"; // extra safety
//        }
//
//        String email = principal.getName(); // get email from Jwt
//        Admin loggedAdmin = adminService.findAdminByEmail(email); // fetch the logged user email
//        model.addAttribute("admin",loggedAdmin);
//        return "Admin/AdminDashboard";
//    }


    @GetMapping("/dashboard")
    public String AdminDashboard(Model model, Authentication authentication) {
        System.out.println("Authentication: " + authentication);

        if (authentication == null) {
            return "redirect:/admin/login";
        }

        Admin loggedAdmin = (Admin) authentication.getPrincipal(); // cast to Admin
        model.addAttribute("admin", loggedAdmin);

        long totalUsers = userRepository.count();
        long totalItems = itemRepository.count();
        long totalCategories = categoryRepo.count();
        double totalBudget = budgetRepo.getTotalBudget();

        model.addAttribute("totalUsers", totalUsers);
        model.addAttribute("totalItems", totalItems);
        model.addAttribute("totalCategories", totalCategories);
        model.addAttribute("totalBudget", totalBudget);
        return "Admin/AdminDashboard";
    }



    @PostMapping("/logout")
    public String logout(HttpServletResponse response) {
        Cookie cookie = new Cookie("admin_jwt_token", null);
        cookie.setMaxAge(0);
        cookie.setPath("/");
        response.addCookie(cookie);
        return "redirect:/admin/login";
    }

    @GetMapping("/auctions")
    public String adminAuctions(Model model, Authentication authentication) {

        System.out.println("Authentication: " + authentication);

        if (authentication == null) {
            return "redirect:/admin/login";
        }
        Admin loggedAdmin = (Admin) authentication.getPrincipal(); // cast to Admin
        model.addAttribute("admin", loggedAdmin);


        List<Item> pendingItems =
                itemRepository.findByStatus(Status.PENDING);

        model.addAttribute("items", pendingItems);
        return "Admin/AdminItems";
    }

    @PostMapping("/startAuction/{itemId}")
    @ResponseBody
    public String startAuction(@PathVariable Long itemId, @RequestParam int durationMinutes) {
        Item item = itemService.getItemById(itemId);

        if (item.getAuctionStartTime() != null) {
            return "ALREADY_STARTED";
        }
        System.out.println("🔥 item found = " + item);
        item.setAuctionStartTime(LocalDateTime.now()); // mark start time
        item.setAuctionDurationMinutes(durationMinutes);
        System.out.println("Duration = " + durationMinutes);
        item.setStatus(Status.ACTIVE);
        itemRepository.save(item);
        return "SUCCESS";
    }

    @GetMapping("/profile")
    public String profile(Model model, Authentication authentication){
        System.out.println("Authentication: " + authentication);

        if (authentication == null) {
            return "redirect:/admin/login";
        }

        Admin loggedAdmin = (Admin) authentication.getPrincipal(); // cast to Admin
        model.addAttribute("admin", loggedAdmin);
        return "Admin/AdminProfile";
    }

    @GetMapping("/users")
    public String adminUsers(Model model, Authentication authentication) {

        if (authentication == null) {
            return "redirect:/admin/login";
        }

        Admin admin = (Admin) authentication.getPrincipal();

        model.addAttribute("admin", admin);
        model.addAttribute("usersList", userRepository.findAll());

        return "Admin/UserTable";
    }

    @GetMapping("/users/delete/{id}")
    public String deleteAdminUser(Authentication authentication, Model model, @PathVariable Long id){
        if (authentication == null) {
            return "redirect:/admin/login";
        }

        Admin admin = (Admin) authentication.getPrincipal();
        model.addAttribute("admin", admin);
        userRepository.deleteById(id);
        return "redirect:/admin/users";
    }

    @GetMapping("/users/add")
    public String addUser(Model model, Authentication authentication){
        if (authentication == null) {
            return "redirect:/admin/login";
        }

        Admin admin = (Admin) authentication.getPrincipal();
        model.addAttribute("admin", admin);
        model.addAttribute("userDto", new AdminUserDto());
        return "Admin/UserRegister";
    }

    @PostMapping("/user/register")
    public String registerAdminuser(   @ModelAttribute("userDto") AdminUserDto dto,
                                       Authentication authentication,
                                       RedirectAttributes redirectAttributes) throws IOException {
        if (authentication == null) {
            return "redirect:/admin/login";
        }

        // Validate passwords
        if (!dto.getPassword().equals(dto.getCpassword())) {
            redirectAttributes.addFlashAttribute("errorMessage", "Passwords do not match");
            return "redirect:/admin/users/add";
        }
        String email = dto.getEmail();
        if(userRepository.existsByEmail(email)){
            redirectAttributes.addFlashAttribute("errorMessage", "Email already exists");
            return "redirect:/admin/users/add";
        }
String UserName = dto.getUsername();
        if(userRepository.existsByUsername(UserName)){
            redirectAttributes.addFlashAttribute("errorMessage", "username already taken");
            return "redirect:/admin/users/add";
        }

        String Phone = dto.getPhone();
        if(userRepository.existsByPhone(Phone)){
            redirectAttributes.addFlashAttribute("errorMessage", "phone number  already registered");
            return "redirect:/admin/users/add";
        }
        // Save image
        MultipartFile file = dto.getUserImage();
        String imageName = file.getOriginalFilename();

        Path imagePath = Paths.get(uploadDir, imageName);
        Files.createDirectories(imagePath.getParent());
        Files.write(imagePath, file.getBytes());

        // Convert DTO → Entity
        User user = new User();
        user.setFullName(dto.getFullName());
        user.setUsername(dto.getUsername());
        user.setEmail(dto.getEmail());
        user.setPhone(dto.getPhone());
        user.setUserAge(dto.getUserAge());
        user.setGender(dto.getGender());
        user.setPAddress(dto.getPAddress());
        user.setUserImage(imageName);

        // 🔐 Always encode password
        user.setPassword(dto.getPassword());
        user.setCpassword(dto.getCpassword());

        userRepository.save(user);

        return "redirect:/admin/users";

    }
}

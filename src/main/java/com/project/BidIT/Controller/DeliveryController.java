package com.project.BidIT.Controller;

import com.project.BidIT.Compontents.JwtUtil;
import com.project.BidIT.DTO.DeliveryManDto;
import com.project.BidIT.Service.DeliveryMan.DeliveryManService;
import com.project.BidIT.entity.Admin;
import com.project.BidIT.entity.DeliveryMan;
import com.project.BidIT.enums.Rate;
import com.project.BidIT.enums.Status;
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
@RequestMapping("/delivery")
public class DeliveryController {

    @Value("${file.upload.users}")
    private String uploadDir;

    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    private DeliveryManService deliveryManService;
    @GetMapping("/register")
    public String showForm(Model model) {
        model.addAttribute("deliveryMan", new DeliveryManDto());
        return "Deliveryman/DeliveryManRegisterForm";
    }

    @PostMapping("/register")
    public String registerDeliveryMan(@Valid @ModelAttribute("deliveryMan") DeliveryManDto dto,
                                      BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "Deliveryman/DeliveryManRegisterForm";
        }

        DeliveryMan deliveryMan = new DeliveryMan();
        deliveryMan.setdName(dto.getdName());
        deliveryMan.setdEmail(dto.getdEmail());
        deliveryMan.setdPhone(dto.getdPhone());
        deliveryMan.setPassword(dto.getPassword());
        deliveryMan.setGender(dto.getGender());
        deliveryMan.setPAddress(dto.getPAddress());
        deliveryMan.setdAge(dto.getdAge());
        deliveryMan.setRate(Rate.average);   // default rate
        deliveryMan.setStatus(Status.PENDING); // default status
        deliveryMan.setCategory(null);       // or set default category
        deliveryMan.setUser(null);

        try {
            System.out.println("Processing registration...");
            MultipartFile file = dto.getDeliveryImage();

            if (file != null && !file.isEmpty()) {
                String filename = UUID.randomUUID() + "_" + file.getOriginalFilename();
                Path savePath = Paths.get(uploadDir , filename);

                Files.createDirectories(savePath.getParent());
                file.transferTo(savePath.toFile());

                deliveryMan.setDeliveryImage(filename);
            }
            DeliveryMan savedDelivery= deliveryManService.registerDeliveryMan(deliveryMan);
            System.out.println("Saved user ID: " + savedDelivery.getDeliveryId());
            return "redirect:/delivery/login";
        } catch (Exception ex) {
            ex.printStackTrace();
            model.addAttribute("errorMessage", ex.getMessage());
            return "Deliveryman/DeliveryManRegisterForm";
        }
    }
    @GetMapping("/login")
    public String DeliveryManlogin( @RequestParam(value = "error", required = false) String error,
                             @RequestParam(value = "logout", required = false) String logout,Model model){
        model.addAttribute("deliveryMan", new DeliveryMan());
        if (error != null) {
            model.addAttribute("errorMessage", "Invalid email or password!");
        }
        if (logout != null) {
            model.addAttribute("successMessage", "You have been logged out successfully.");
        }

        return "Deliveryman/DeliveryManLogin";

    }

    @PostMapping("/login")
    public String login(@RequestParam String email,
                        @RequestParam String password,
                        HttpServletResponse response,
                        Model model) {
        DeliveryMan dm = deliveryManService.login(email, password);
        if (dm == null) {
            model.addAttribute("errorMessage", "Invalid credentials");
            return "DeliveryMan/Login";
        }

        String token = jwtUtil.generateTokenDeliveryMan(dm);
        Cookie cookie = new Cookie("jwt_token", token);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        cookie.setMaxAge(24 * 60 * 60);
        response.addCookie(cookie);

        return "redirect:/delivery/dashboard";
    }

    @GetMapping("/dashboard")
    public String dashboard(Model model, Principal principal) {
        if (principal == null) return "redirect:/delivery/login";
        model.addAttribute("deliveryMan", deliveryManService.findByDEmail(principal.getName()));
        return "Deliveryman/DeliveryManDashboard";
    }

    @PostMapping("/logout")
    public String logout(HttpServletResponse response) {
        Cookie cookie = new Cookie("jwt_token", null);
        cookie.setMaxAge(0);
        cookie.setPath("/");
        response.addCookie(cookie);
        return "redirect:/delivery/login";
    }

}

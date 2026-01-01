package com.project.BidIT.Controller;

import com.project.BidIT.DTO.ItemDto;
import com.project.BidIT.Repo.AdminRepo;
import com.project.BidIT.Repo.CategoryRepo;
import com.project.BidIT.Repo.ItemRepository;
import com.project.BidIT.Repo.UserRepository;
import com.project.BidIT.Service.CategoryService;
import com.project.BidIT.Service.Item.ItemService;
import com.project.BidIT.Service.User.UserService;
import com.project.BidIT.entity.Admin;
import com.project.BidIT.entity.Category;
import com.project.BidIT.entity.Item;
import com.project.BidIT.entity.User;
import com.project.BidIT.enums.Rate;
import com.project.BidIT.enums.Status;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
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
import java.util.List;

@Controller
@RequestMapping("/item")
public class ItemController {

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private  CategoryRepo categoryRepo;
    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AdminRepo adminRepo;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private ItemService itemService;



    @Value("${file.upload.item.images}")
    private String uploadDir;

    @GetMapping("/add")
    public String addItem(Model model, Principal principal){
        ItemDto itemDto = new ItemDto();

        model.addAttribute("itemDto", new ItemDto());
        if (principal == null) return "redirect:/login";

        String email = principal.getName();
        User users = userRepository.findByEmail(email).orElse(null);

        if (users == null) return "redirect:/login";
        model.addAttribute("users", users);
        model.addAttribute("categories", categoryService.getAllCategories());
        model.addAttribute("rates", Rate.values());
        model.addAttribute("statuses", Status.values());
return "ItemADDForm";
    }

    @PostMapping("/save")
    public String addItem(ItemDto dto, Principal principal) throws IOException {

        if (principal == null) return "redirect:/login"; // user not logged in

        // Get logged-in user
        String email = principal.getName();
        User user = userRepository.findByEmail(email).orElse(null);
        if (user == null) return "redirect:/login";

        Category category = categoryRepo.findById(dto.getCategoryId()).orElse(null);
        if (category == null) return "redirect:/item/add"; // invalid category
        // Set the user explicitly in the DTO
        dto.setUser(user);

        MultipartFile file = dto.getItemImage();
        String imageName = file.getOriginalFilename();

        // Create directory if not exists
        Path imagePath = Paths.get(uploadDir, imageName);
        if (!Files.exists(imagePath.getParent())) {
            Files.createDirectories(imagePath.getParent());
        }

        // Save file
        Files.write(imagePath, file.getBytes());

        // Convert DTO → Entity
        Item item = new Item();
        item.setUser(dto.getUser());
        item.setCategory(category);

        item.setRate(dto.getRate());
        item.setDescription(dto.getDescription());
        item.setAmount(dto.getAmount());
        item.setStatus(Status.PENDING);
        item.setAuctionStartTime(null);
        item.setAuctionDurationMinutes(0);
        item.setItemImage(imageName);
        item.setItemName(dto.getItemName());
        itemRepository.save(item);

        return "redirect:/item/dashboard";
    }

    @GetMapping("/dashboard")
    public String ItemDashboard(Model model, Principal principal) {

        if (principal == null) return "redirect:/login";

        String email = principal.getName();

        // Check in User table
        User currentUser = userRepository.findByEmail(email).orElse(null);

        // Check in Admin table
        Admin currentAdmin = adminRepo.findAdminByEmail(email).orElse(null);


        if (currentUser == null && currentAdmin == null) return "redirect:/login";

        model.addAttribute("itemDto", new ItemDto());
        model.addAttribute("items", itemRepository.findAll());
        model.addAttribute("categories", categoryService.getAllCategories());
        model.addAttribute("rates", Rate.values());
        model.addAttribute("statuses", Status.values());


        // Pass the current logged-in user/admin to the template
        model.addAttribute("users", currentUser);   // null if admin
        model.addAttribute("currentAdmin", currentAdmin); // null if user

        return "ItemDashBoard";
    }

    @GetMapping("/category")
    public String addCategory(Model model,Principal principal){
        if (principal == null) return "redirect:/login";

        String email = principal.getName();
        User users = userRepository.findByEmail(email).orElse(null);

        if (users == null) return "redirect:/login";
        model.addAttribute("users",users);
        model.addAttribute("category", new Category());
        return "CategoryAddForm";

    }

    @PostMapping("/category/save")
    public String addCategory(Category category){
        category.setName(category.getName());
        categoryRepo.save(category);

        return "redirect:/item/dashboard";

    }


    @GetMapping("/edit/{id}")
    public String editItemForm(@PathVariable Long id, Model model, Principal principal) {
        if (principal == null) return "redirect:/login";

        String email = principal.getName();
        User users = userRepository.findByEmail(email).orElse(null);

        if (users == null) return "redirect:/login";
        Item item = itemService.getItemById(id);
        ItemDto itemDto = new ItemDto();
        itemDto.setItemId(item.getItemId());
        itemDto.setAmount(item.getAmount());
        itemDto.setRate(item.getRate());
        itemDto.setStatus(item.getStatus());
        itemDto.setDescription(item.getDescription());
        itemDto.setCategoryId(item.getCategory().getCategoryId());
        List<Category> categories = categoryService.getAllCategories();
        model.addAttribute("users", users);
        model.addAttribute("categories", categories);
        model.addAttribute("itemDto", itemDto);
        model.addAttribute("item", item);
        return "editItem";
    }

    @PostMapping("/edit/save")
    public String saveEditedItem(@ModelAttribute ItemDto itemDto, Principal principal) throws IOException {
        if (principal == null) return "redirect:/login";

        User users = userRepository.findByEmail(principal.getName()).orElse(null);
        if (users == null) return "redirect:/login";

        if (itemDto.getItemId() == null) {
            throw new IllegalArgumentException("Item ID cannot be null");
        }

        Item item = itemService.getItemById(itemDto.getItemId());

        item.setDescription(itemDto.getDescription());
        item.setAmount(itemDto.getAmount());
        item.setRate(itemDto.getRate());
        item.setStatus(itemDto.getStatus());
        item.setUser(users);
        item.setItemName(itemDto.getItemName());
        Category category = categoryRepo.findById(itemDto.getCategoryId()).orElse(null);
        item.setCategory(category);

        MultipartFile file = itemDto.getItemImage();
        String imageName = file.getOriginalFilename();

        // Create directory if not exists
        Path imagePath = Paths.get(uploadDir, imageName);
        if (!Files.exists(imagePath.getParent())) {
            Files.createDirectories(imagePath.getParent());
        }

        // Save file
        Files.write(imagePath, file.getBytes());

        itemService.saveItem(item);

        return "redirect:/item/dashboard";
    }


    @GetMapping("/delete/{id}")
    public String deleteItem(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            itemService.deleteItem(id);
        } catch (RuntimeException ex) {
            redirectAttributes.addFlashAttribute("errorMessage", ex.getMessage());
        }
        return "redirect:/item/dashboard";
    }


//Admin Side
    @GetMapping("/admin/dashboard")
    public String ItemAdminDashboard(Model model, Authentication authentication) {
        System.out.println("Authentication: " + authentication);

        if (authentication == null) {
            return "redirect:/admin/login";
        }

        Admin loggedAdmin = (Admin) authentication.getPrincipal(); // cast to Admin
        model.addAttribute("admin", loggedAdmin);
        model.addAttribute("itemDto", new ItemDto());
        model.addAttribute("items", itemRepository.findAll());
        model.addAttribute("categories", categoryService.getAllCategories());
        model.addAttribute("rates", Rate.values());
        model.addAttribute("statuses", Status.values());
        model.addAttribute("currentAdmin",loggedAdmin);
        return "ItemDashBoard";
    }

    @GetMapping("/admin/add")
    public String addItemAdmin(Model model, Authentication authentication){
        ItemDto itemDto = new ItemDto();

        model.addAttribute("itemDto", new ItemDto());
        System.out.println("Authentication: " + authentication);

        if (authentication == null) {
            return "redirect:/admin/login";
        }

        Admin loggedAdmin = (Admin) authentication.getPrincipal(); // cast to Admin
        model.addAttribute("admin", loggedAdmin);
        model.addAttribute("categories", categoryService.getAllCategories());
        model.addAttribute("rates", Rate.values());
        model.addAttribute("statuses", Status.values());
        return "Admin/AdminAddItem";
    }


    @GetMapping("/admin/category")
    public String addAdminCategory(Model model,Authentication authentication){
        System.out.println("Authentication: " + authentication);

        if (authentication == null) {
            return "redirect:/admin/login";
        }

        Admin loggedAdmin = (Admin) authentication.getPrincipal(); // cast to Admin
        model.addAttribute("admin", loggedAdmin);
        model.addAttribute("category", new Category());
        return "CategoryAddForm";

    }

    @PostMapping("/category/admin/save")
    public String addAdminCategory(Category category){
        category.setName(category.getName());
        categoryRepo.save(category);
         return "redirect:/item/admin/dashboard";

    }

    @PostMapping("/admin/save")
    public String addAdminItem(ItemDto dto,Authentication authentication) throws IOException {

        System.out.println("Authentication: " + authentication);

        if (authentication == null) {
            return "redirect:/admin/login";
        }

        Admin loggedAdmin = (Admin) authentication.getPrincipal(); // cast to Admin
        Category category = categoryRepo.findById(dto.getCategoryId()).orElse(null);
        if (category == null) return "redirect:/item/admin/add"; // invalid category
        // Set the user explicitly in the DTO


        MultipartFile file = dto.getItemImage();
        String imageName = file.getOriginalFilename();

        // Create directory if not exists
        Path imagePath = Paths.get(uploadDir, imageName);
        if (!Files.exists(imagePath.getParent())) {
            Files.createDirectories(imagePath.getParent());
        }

        // Save file
        Files.write(imagePath, file.getBytes());

        // Convert DTO → Entity
        Item item = new Item();
        item.setAdmin(loggedAdmin);
        item.setCategory(category);

        item.setRate(dto.getRate());
        item.setDescription(dto.getDescription());
        item.setAmount(dto.getAmount());
        item.setStatus(Status.PENDING);
        item.setAuctionStartTime(null);
        item.setAuctionDurationMinutes(0);
        item.setItemImage(imageName);
        item.setItemName(dto.getItemName());
        itemRepository.save(item);

        return "redirect:/item/admin/dashboard";
    }

}

package com.project.BidIT.DTO;

import com.project.BidIT.entity.Category;
import com.project.BidIT.entity.User;
import com.project.BidIT.enums.Rate;
import com.project.BidIT.enums.Status;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import org.springframework.web.multipart.MultipartFile;

public class DeliveryManDto {

    @NotBlank(message = "Name is required")
    private String dName;

    @Email(message = "Enter a valid email")
    @NotBlank(message = "Email is required")
    private String dEmail;

    @Pattern(regexp = "^98\\d{8}$", message = "Phone must start with 98 and be 10 digits")
    private String dPhone;

    @NotBlank(message = "Password is required")
    private String password;

    private String gender;

    @NotBlank(message = "Address is required")
    private String PAddress;

    @NotBlank(message = "Age is required")
    private String dAge;

    private MultipartFile deliveryImage;

    private Rate rate;
    private Status status;
    private User user;
    private Category category;
    // Getters and Setters


    public Rate getRate() {
        return rate;
    }

    public void setRate(Rate rate) {
        this.rate = rate;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public String getdName() { return dName; }
    public void setdName(String dName) { this.dName = dName; }

    public String getdEmail() { return dEmail; }
    public void setdEmail(String dEmail) { this.dEmail = dEmail; }

    public String getdPhone() { return dPhone; }
    public void setdPhone(String dPhone) { this.dPhone = dPhone; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getGender() { return gender; }
    public void setGender(String gender) { this.gender = gender; }

    public String getPAddress() { return PAddress; }
    public void setPAddress(String PAddress) { this.PAddress = PAddress; }

    public String getdAge() { return dAge; }
    public void setdAge(String dAge) { this.dAge = dAge; }

    public MultipartFile getDeliveryImage() { return deliveryImage; }
    public void setDeliveryImage(MultipartFile deliveryImage) { this.deliveryImage = deliveryImage; }
}

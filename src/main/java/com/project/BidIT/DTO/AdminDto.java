package com.project.BidIT.DTO;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import org.springframework.web.multipart.MultipartFile;

public class AdminDto {
    @NotBlank
    private String fullName;

    @NotBlank
    private String username;

    @Email(message = "Please enter a valid email")
    private String email;

    @NotBlank
    private String password;

    private String gender;

    private String address;

    @Pattern(regexp = "^98\\d{8}$", message = "Phone must start with 98 and be 10 digits")
    private String phone;

    private MultipartFile adminImage;

    public @NotBlank String getFullName() {
        return fullName;
    }

    public void setFullName(@NotBlank String fullName) {
        this.fullName = fullName;
    }

    public @NotBlank String getUsername() {
        return username;
    }

    public void setUsername(@NotBlank String username) {
        this.username = username;
    }

    public @Email String getEmail() {
        return email;
    }

    public void setEmail(@Email String email) {
        this.email = email;
    }

    public @NotBlank String getPassword() {
        return password;
    }

    public void setPassword(@NotBlank String password) {
        this.password = password;
    }



    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }



    public MultipartFile getUserImage() {
        return adminImage;
    }

    public void setUserImage(MultipartFile adminImage) {
        this.adminImage = adminImage;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}

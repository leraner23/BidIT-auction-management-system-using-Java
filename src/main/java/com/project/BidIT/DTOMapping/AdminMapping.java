package com.project.BidIT.DTOMapping;

import com.project.BidIT.DTO.AdminDto;
import com.project.BidIT.DTO.UserDto;
import com.project.BidIT.entity.Admin;
import com.project.BidIT.entity.User;

public class AdminMapping {

    // user data to database/backend
    public Admin mapDtoToEntity(AdminDto dto) {
        Admin admin = new Admin();

        admin.setFullName(dto.getFullName());
        admin.setUsername(dto.getUsername());
        admin.setEmail(dto.getEmail());
        admin.setPassword(dto.getPassword());

        admin.setGender(dto.getGender());
        admin.setAddress(dto.getAddress());


        if (dto.getUserImage() != null && !dto.getUserImage().isEmpty()) {
            admin.setUserImage(dto.getUserImage().getOriginalFilename());
        }

        return admin;
    }

    //from backend to frontend
    public AdminDto mapEntityToDto(Admin admin) {
        AdminDto dto = new AdminDto();

        dto.setFullName(admin.getFullName());
        dto.setUsername(admin.getUsername());
        dto.setEmail(admin.getEmail());
        dto.setGender(admin.getGender());
        return dto;
    }

}

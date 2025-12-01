package com.project.BidIT.DTOMapping;

import com.project.BidIT.DTO.UserDto;
import com.project.BidIT.entity.User;

public class UserMapping {

    // user data to database/backend
    public User mapDtoToEntity(UserDto dto) {
        User user = new User();

        user.setFullName(dto.getFullName());
        user.setUsername(dto.getUsername());
        user.setEmail(dto.getEmail());
        user.setPassword(dto.getPassword());
        user.setCpassword(dto.getCpassword());
        user.setGender(dto.getGender());
        user.setPAddress(dto.getPAddress());
        user.setUserAge(dto.getUserAge());

        if (dto.getUserImage() != null && !dto.getUserImage().isEmpty()) {
            user.setUserImage(dto.getUserImage().getOriginalFilename());
        }

        return user;
    }

    //from backend to frontend
    public UserDto mapEntityToDto(User user) {
        UserDto dto = new UserDto();

        dto.setFullName(user.getFullName());
        dto.setUsername(user.getUsername());
        dto.setEmail(user.getEmail());
        dto.setGender(user.getGender());
        dto.setPAddress(user.getPAddress());
        dto.setUserAge(user.getUserAge());

        return dto;
    }

}

package com.sapient.productengineering.mapper;

import com.sapient.productengineering.dto.UserDTO;
import com.sapient.productengineering.entity.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper{
    public UserDTO toDTO(User u){
        if (u==null) return null;
        return UserDTO.builder()
                .id(u.getId())
                .firstName(u.getFirstName())
                .lastName(u.getLastName())
                .email(u.getEmail())
                .ssn(u.getSsn())
                .age(u.getAge())
                .role(u.getRole())
                .build();
    }

    public User toEntity(UserDTO dto){
        if(dto==null) return null;
        return User.builder()
                .id(dto.getId())
                .firstName(dto.getFirstName())
                .lastName(dto.getLastName())
                .email(dto.getEmail())
                .ssn(dto.getSsn())
                .age(dto.getAge())
                .role(dto.getRole())
                .build();
    }
}

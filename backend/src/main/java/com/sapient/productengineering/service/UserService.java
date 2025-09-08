package com.sapient.productengineering.service;

import com.sapient.productengineering.dto.UserDTO;

import java.util.List;

public interface UserService {
    void loadUsersFromExternalAPI();
    List<UserDTO> getAllUsers();
    UserDTO getUserById(Long id);
    UserDTO getUserByEmail(String email);
    List<UserDTO> searchUsers(String text);
}

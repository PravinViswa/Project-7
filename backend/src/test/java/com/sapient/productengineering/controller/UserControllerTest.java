package com.sapient.productengineering.controller;

import com.sapient.productengineering.dto.APIResponse;
import com.sapient.productengineering.dto.UserDTO;
import com.sapient.productengineering.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.http.ResponseEntity;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class UserControllerTest{
    @Mock
    private UserService userService;
    @InjectMocks
    private UserController userController;
    private UserDTO userDTO;
    @BeforeEach
    public void setUp(){
        MockitoAnnotations.openMocks(this);
        userDTO = UserDTO.builder().id(1L).firstName("John").lastName("Doe")
                .email("john@example.com").ssn("123").age(30).role("Admin").build();
    }
    @Test
    public void testGetAllUsers(){
        List<UserDTO> mockUsers=Collections.singletonList(userDTO);
        when(userService.getAllUsers()).thenReturn(mockUsers);
        ResponseEntity<APIResponse<List<UserDTO>>> response=userController.getAllUsers();
        assertEquals(200,response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertEquals(mockUsers,response.getBody().getData());
        verify(userService,times(1)).getAllUsers();
    }

    @Test
    public void testGetUserById(){
        when(userService.getUserById(1L)).thenReturn(userDTO);
        ResponseEntity<APIResponse<UserDTO>> response=userController.getUserById(1L);
        assertEquals(200,response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertEquals(userDTO,response.getBody().getData());
        verify(userService,times(1)).getUserById(1L);
    }
}

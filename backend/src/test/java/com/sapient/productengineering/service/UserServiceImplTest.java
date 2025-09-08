package com.sapient.productengineering.service;

import com.sapient.productengineering.dto.UserDTO;
import com.sapient.productengineering.entity.User;
import com.sapient.productengineering.exception.ResourceNotFoundException;
import com.sapient.productengineering.mapper.UserMapper;
import com.sapient.productengineering.repository.UserRepository;
import com.sapient.productengineering.service.impl.UserServiceImpl;
import org.junit.jupiter.api.*;
import org.mockito.*;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceImplTest{

    @InjectMocks
    private UserServiceImpl userService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserMapper userMapper;

    @BeforeEach
    void setup(){
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetUserById_found(){
        User user=new User(1L,"John","Doe","john@example.com","123456789",30,"Admin");
        UserDTO dto=new UserDTO(1L,"John","Doe","john@example.com","123456789",30,"Admin");
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userMapper.toDTO(user)).thenReturn(dto);

        UserDTO result=userService.getUserById(1L);
        assertEquals("John", result.getFirstName());
    }

    @Test
    void testGetUserById_notFound(){
        when(userRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> userService.getUserById(1L));
    }

    @Test
    void testSearchUsers(){
        User user = new User(1L,"John","Doe","john@example.com","123456789",30,"Admin");
        UserDTO dto = new UserDTO(1L,"John","Doe","john@example.com","123456789",30,"Admin");
        when(userRepository.searchUsers("Joh")).thenReturn(List.of(user));
        when(userMapper.toDTO(user)).thenReturn(dto);

        List<UserDTO> results=userService.searchUsers("Joh");
        assertEquals(1,results.size());
        assertEquals("John",results.get(0).getFirstName());
    }
}

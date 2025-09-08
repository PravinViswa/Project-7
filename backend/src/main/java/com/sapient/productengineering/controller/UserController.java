package com.sapient.productengineering.controller;

import com.sapient.productengineering.dto.APIResponse;
import com.sapient.productengineering.dto.UserDTO;
import com.sapient.productengineering.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.constraints.Size;
import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController{

    private final UserService userService;

    @PostMapping("/load")
    public ResponseEntity<APIResponse<Object>> loadUsers(){
        userService.loadUsersFromExternalAPI();
        return ResponseEntity.ok(APIResponse.success("Loaded users","Users imported",200));
    }

    @GetMapping
    public ResponseEntity<APIResponse<List<UserDTO>>> getAllUsers(){
        List<UserDTO> list=userService.getAllUsers();
        return ResponseEntity.ok(APIResponse.success(list,"Fetched all users",200));
    }

    @GetMapping("/{id}")
    public ResponseEntity<APIResponse<UserDTO>> getUserById(@PathVariable Long id){
        return ResponseEntity.ok(APIResponse.success(userService.getUserById(id),"Fetched user",200));
    }

    @GetMapping("/email/{email}")
    public ResponseEntity<APIResponse<UserDTO>> getUserByEmail(@PathVariable String email){
        return ResponseEntity.ok(APIResponse.success(userService.getUserByEmail(email),"Fetched user",200));
    }

    @GetMapping("/search")
    public ResponseEntity<APIResponse<List<UserDTO>>> searchUsers(
            @RequestParam @Size(min = 3,message ="Search text must be at least 3 characters") String text){
        return ResponseEntity.ok(APIResponse.success(userService.searchUsers(text),"Search results",200));
    }
}

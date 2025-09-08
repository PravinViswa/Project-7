package com.sapient.productengineering.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDTO{
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String ssn;
    private int age;
    private String role;
}

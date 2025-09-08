package com.sapient.productengineering.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User{
    @Id
    private Long id;

    @Column(nullable=false)
    private String firstName;
    @Column(nullable=false)
    private String lastName;

    @Column(nullable=false,unique=true)
    private String email;

    @Column(unique=true)
    private String ssn;

    private int age;
    private String role;
}

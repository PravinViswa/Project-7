package com.sapient.productengineering.repository;

import com.sapient.productengineering.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class UserRepositoryTest{
    @Autowired
    private UserRepository repo;

    @Test
    void saveAndFind(){
        User u=User.builder().id(100L).firstName("A").lastName("B")
                .email("a@b.com").ssn("123456789").age(30).role("User").build();
        repo.save(u);
        assertThat(repo.findById(100L)).isPresent();
    }
}

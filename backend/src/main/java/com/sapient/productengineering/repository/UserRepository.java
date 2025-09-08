package com.sapient.productengineering.repository;

import com.sapient.productengineering.entity.User;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import java.util.*;

@Repository
public interface UserRepository extends JpaRepository<User,Long>{
    Optional<User> findByEmail(String email);

    @Query("SELECT u FROM User u WHERE LOWER(u.firstName) LIKE LOWER(CONCAT('%',:text,'%')) " +
            "OR LOWER(u.lastName) LIKE LOWER(CONCAT('%',:text,'%')) " +
            "OR u.ssn LIKE CONCAT('%',:text,'%')")
    List<User> searchUsers(String text);
}

package com.sapient.productengineering.mapper;

import com.sapient.productengineering.dto.UserDTO;
import com.sapient.productengineering.entity.User;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

class UserMapperTest{
    private final UserMapper mapper=new UserMapper();

    @Test
    void testEntityToDTOAndBack(){
        User user = User.builder()
                .id(1L).firstName("Jane").lastName("Smith")
                .email("jane@example.com").ssn("987654321")
                .age(25).role("user").build();

        UserDTO dto=mapper.toDTO(user);
        User entity=mapper.toEntity(dto);

        assertThat(entity.getFirstName()).isEqualTo(user.getFirstName());
        assertThat(entity.getEmail()).isEqualTo(user.getEmail());
        assertThat(entity.getSsn()).isEqualTo(user.getSsn());
    }
}

package com.sapient.productengineering.integration;

import com.sapient.productengineering.dto.APIResponse;
import com.sapient.productengineering.dto.UserDTO;
import com.sapient.productengineering.entity.User;
import com.sapient.productengineering.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestTemplate;

import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment=SpringBootTest.WebEnvironment.RANDOM_PORT)
class UserIntegrationTest{

    @LocalServerPort
    private int port;
    @Autowired
    private TestRestTemplate restTemplate;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RestTemplate template;
    private String baseUrl;
    @BeforeEach
    void setup(){
        baseUrl="http://localhost:"+port+"/api/users";
        userRepository.deleteAll();
    }

    @Test
    void testLoadUsersFromExternalAPI(){
        MockRestServiceServer server=MockRestServiceServer.createServer(template);
        String jsonResponse="{ \"users\": [ {\"id\":1,\"firstName\":\"John\",\"lastName\":\"Doe\",\"email\":\"john@example.com\",\"age\":30,\"company\":{\"department\":\"Admin\"}} ] }";
        server.expect(requestTo("https://dummyjson.com/users?limit=200"))
                .andRespond(withSuccess(jsonResponse,MediaType.APPLICATION_JSON));

        ResponseEntity<APIResponse<Object>> response=
                restTemplate.exchange(baseUrl+"/load",HttpMethod.POST,null,
                        new ParameterizedTypeReference<APIResponse<Object>>() {});

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getData()).isEqualTo("Loaded users");

        server.verify();
    }

    @Test
    void testGetAllUsers(){
        userRepository.save(new User(100L,"Jane","Doe","jane@example.com","987",25,"User"));

        ResponseEntity<APIResponse<List<UserDTO>>> response =
                restTemplate.exchange(baseUrl,HttpMethod.GET,null,
                        new ParameterizedTypeReference<APIResponse<List<UserDTO>>>() {});

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getData()).isNotEmpty();
        assertThat(response.getBody().getData().get(0).getFirstName()).isEqualTo("Jane");
    }

    @Test
    void testGetUserById(){
        User saved = userRepository.save(new User(101L,"Alice","Smith","alice@example.com","555",28,"Admin"));

        ResponseEntity<APIResponse<UserDTO>> response =
                restTemplate.exchange(baseUrl + "/" + saved.getId(), HttpMethod.GET, null,
                        new ParameterizedTypeReference<APIResponse<UserDTO>>() {});

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getData().getEmail()).isEqualTo("alice@example.com");
    }

    @Test
    void testSearchUsers(){
        userRepository.save(new User(201L,"Bob","Johnson","bob@example.com","111",40,"User"));
        userRepository.save(new User(202L,"Carol","Smith","carol@example.com","222",35,"Admin"));

        ResponseEntity<APIResponse<List<UserDTO>>> response =
                restTemplate.exchange(baseUrl+"/search?text=bob",HttpMethod.GET,null,
                        new ParameterizedTypeReference<APIResponse<List<UserDTO>>>() {});

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();

        List<UserDTO> results = response.getBody().getData();
        assertThat(results).hasSize(1);
        assertThat(results.get(0).getFirstName()).isEqualTo("Bob");
    }
}

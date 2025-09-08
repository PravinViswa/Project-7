package com.sapient.productengineering.service.impl;

import com.sapient.productengineering.dto.UserDTO;
import com.sapient.productengineering.entity.User;
import com.sapient.productengineering.exception.ResourceNotFoundException;
import com.sapient.productengineering.mapper.UserMapper;
import com.sapient.productengineering.repository.UserRepository;
import com.sapient.productengineering.service.UserService;
import lombok.RequiredArgsConstructor;
import org.slf4j.*;
import org.springframework.http.*;
import org.springframework.retry.annotation.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@EnableRetry
public class UserServiceImpl implements UserService {

    private static final Logger log = LoggerFactory.getLogger(UserServiceImpl.class);

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final RestTemplate restTemplate;

    private static final String EXTERNAL_URL = "https://dummyjson.com/users?limit=200";

    @Override
    @Transactional
    @Retryable(value = Exception.class, maxAttempts = 3, backoff = @Backoff(delay = 1500))
    public void loadUsersFromExternalAPI() {
        log.info("Fetching from external API: {}", EXTERNAL_URL);
        ResponseEntity<Map> resp = restTemplate.getForEntity(EXTERNAL_URL, Map.class);
        if (resp.getStatusCode() != HttpStatus.OK || resp.getBody() == null) {
            throw new RuntimeException("External API failed: " + resp.getStatusCode());
        }

        Object usersObj = resp.getBody().get("users");
        if (!(usersObj instanceof List)) {
            throw new RuntimeException("Unexpected external payload");
        }

        List<Map<String, Object>> usersList = (List<Map<String, Object>>) usersObj;

        List<User> toSave = usersList.stream().map(map -> {
                    Long id = map.get("id") != null ? ((Number) map.get("id")).longValue() : null;
                    String firstName = Objects.toString(map.get("firstName"), null);
                    String lastName = Objects.toString(map.get("lastName"), null);
                    String email = Objects.toString(map.get("email"), null);
                    Integer age = map.get("age") != null ? ((Number) map.get("age")).intValue() : 0;
                    String ssn = Objects.toString(map.get("ssn"), null); // may be null
                    String role = null;

                    Object company = map.get("company");
                    if (company instanceof Map) {
                        Map<?, ?> comp = (Map<?, ?>) company;
                        role = Objects.toString(comp.get("department"), null);
                    }

                    return User.builder()
                            .id(id)
                            .firstName(firstName)
                            .lastName(lastName)
                            .email(email)
                            .ssn(ssn)
                            .age(age != null ? age : 0)
                            .role(role)
                            .build();
                })
                .filter(u -> u.getId() != null && u.getEmail() != null)
                .collect(Collectors.toList());

        userRepository.saveAll(toSave);
        log.info("Loaded {} users into H2", toSave.size());
    }

    @Override
    public List<UserDTO> getAllUsers() {
        return userRepository.findAll().stream().map(userMapper::toDTO).collect(Collectors.toList());
    }

    @Override
    public UserDTO getUserById(Long id) {
        User u = userRepository.findById(id).orElseThrow(() ->
                new ResourceNotFoundException("User not found with id: " + id));
        return userMapper.toDTO(u);
    }

    @Override
    public UserDTO getUserByEmail(String email) {
        User u = userRepository.findByEmail(email).orElseThrow(() ->
                new ResourceNotFoundException("User not found with email: " + email));
        return userMapper.toDTO(u);
    }

    @Override
    public List<UserDTO> searchUsers(String text) {
        if (text == null || text.trim().length() < 3) {
            throw new IllegalArgumentException("Search text must be at least 3 characters");
        }
        return userRepository.searchUsers(text).stream().map(userMapper::toDTO).collect(Collectors.toList());
    }
}

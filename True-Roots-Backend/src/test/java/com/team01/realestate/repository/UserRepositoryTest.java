package com.team01.realestate.repository;

import com.team01.realestate.entity.concretes.user.User;
import com.team01.realestate.repository.user.UserRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest //to initialize beans to use app
@ActiveProfiles("test")
@Testcontainers // initialize the containers
@Transactional // added to clean up DB after each test case
class UserRepositoryTest {

    Logger logger = LoggerFactory.getLogger(UserRepositoryTest.class);

    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:15")
            .withDatabaseName("testdb")
            .withUsername("test")
            .withPassword("test");


    @DynamicPropertySource
    static void configureProperties(org.springframework.test.context.DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
    }

    @Autowired
    private UserRepository userRepository;


    @Test
    @DisplayName("Test finding a user by email in repository")
    void findByNameRepositoryTest() {
        User user = new User();
        user.setEmail("email@email.com");
        user.setFirstName("First");
        user.setLastName("Last");
        user.setCreatedAt(LocalDateTime.now());
        user.setBuiltIn(false);
        user.setPhone("123-456-7890");
        user.setPasswordHash("ngfajdkfgnadjkfgnjfkg");

        userRepository.save(user);
        assertThat(user.getId()).isNotNull(); // Ensure the user is saved

        Optional<User> foundUser = userRepository.findByEmailOptional(user.getEmail());
        assertThat(foundUser).isPresent();
        assertThat(foundUser.get().getEmail()).isEqualTo(user.getEmail());

        logger.info("Found user email: {}", foundUser.get().getEmail());
    }


    @Test
    @DisplayName("Test finding an user by id in repository")
    void findByIdRepositoryTest() {
        String userFirstName = "UserFirstName";
        User user = new User();
        user.setEmail("email@email.com");
        user.setFirstName(userFirstName);
        user.setLastName("Last");
        user.setCreatedAt(LocalDateTime.now());
        user.setBuiltIn(false);
        user.setPhone("123-456-7890");
        user.setPasswordHash("ngfajdkfgnadjkfgnjfkg");
        userRepository.save(user);
        Long id = user.getId();
        String firstName = userRepository.findById(id).get().getFirstName();
        assertThat(firstName).isEqualTo(userFirstName);
        logger.info(userRepository.findById(id).get().getFirstName());
    }


}
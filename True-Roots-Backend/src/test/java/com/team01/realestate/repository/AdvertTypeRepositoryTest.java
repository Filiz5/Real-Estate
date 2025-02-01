package com.team01.realestate.repository;

import com.team01.realestate.base.TestDataReaderForAdvertTypes;
import com.team01.realestate.entity.concretes.business.AdvertType;
import com.team01.realestate.payload.request.business.AdvertTypeRequest;
import com.team01.realestate.repository.business.AdvertTypeRepository;
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
import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.Optional;

@SpringBootTest //to initialize beans to use app
@ActiveProfiles("test")
@Testcontainers // initialize the containers
@Transactional // added to clean up DB after each test case
public class AdvertTypeRepositoryTest {
    Logger logger = LoggerFactory.getLogger(AdvertTypeRepositoryTest.class);

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
    private AdvertTypeRepository advertTypeRepository;

    @Test
    @DisplayName("Test find advert type by id in repository")
    void findByIdRepositoryTest() {
        AdvertType advertType = TestDataReaderForAdvertTypes.getAdvertTypePayload();
        advertTypeRepository.save(advertType);

        Optional<AdvertType> advertTypeReturn = advertTypeRepository.findById(advertType.getId());

        assertThat(advertTypeReturn.get().getId()).isEqualTo(advertType.getId());

        logger.info("Retrieved advert types: {}", advertTypeReturn);

    }

    @Test
    @DisplayName("Test find all advert type in repository")
    void findAllRepositoryTest() {
        AdvertType advertType = TestDataReaderForAdvertTypes.getAdvertTypePayload();
        advertTypeRepository.save(advertType);

        List<AdvertType> advertTypeReturn = advertTypeRepository.findAll();

        assertThat(advertTypeReturn.get(0).getId()).isEqualTo(advertType.getId());

        logger.info("Retrieved advert types: {}", advertTypeReturn);

    }
}

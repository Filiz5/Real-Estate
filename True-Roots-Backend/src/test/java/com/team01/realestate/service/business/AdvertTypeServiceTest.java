package com.team01.realestate.service.business;



import com.team01.realestate.base.TestDataReaderForAdvertTypes;
import com.team01.realestate.entity.concretes.business.Advert;
import com.team01.realestate.exception.BadRequestException;
import com.team01.realestate.exception.ResourceNotFoundException;
import com.team01.realestate.payload.messages.ErrorMessages;
import com.team01.realestate.payload.request.business.AdvertTypeRequest;
import com.team01.realestate.payload.response.business.AdvertTypeResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

@SpringBootTest
@ActiveProfiles("test")
@Testcontainers
public class AdvertTypeServiceTest {

    Logger logger = LoggerFactory.getLogger(AdvertTypeServiceIMPL.class);

    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:15")
            .withDatabaseName("testdb")
            .withUsername("test")
            .withPassword("test");


    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
    }

    @Autowired
    private AdvertTypeServiceIMPL advertTypeServiceIMPL;


    @Test
    @DisplayName("Test create advert service")
    void testCreateAdvertTyp() {

        AdvertTypeRequest advertTypeResponse = TestDataReaderForAdvertTypes.getAdvertTypeRequestPayload();
        AdvertTypeResponse advertTypeResponseReturn = advertTypeServiceIMPL.createAdvertType(advertTypeResponse);
        assertThat(advertTypeResponseReturn).isNotNull();
        assertThat(advertTypeResponseReturn.getTitle()).isEqualTo(advertTypeResponse.getTitle());
        assertThat(advertTypeResponseReturn.isBuiltIn()).isEqualTo(advertTypeResponse.isBuiltIn());

        logger.info(advertTypeResponseReturn.toString());

    }

    @Test
    @DisplayName("Test get all advert type")
    void testGetAllAdvertType() {
        AdvertTypeRequest advertTypeResponse = TestDataReaderForAdvertTypes.getAdvertTypeRequestPayload();
        AdvertTypeResponse advertTypeResponseReturn1 = advertTypeServiceIMPL.createAdvertType(advertTypeResponse);
        AdvertTypeResponse advertTypeResponseReturn2 = advertTypeServiceIMPL.createAdvertType(advertTypeResponse);


        List<AdvertTypeResponse> AdvertTypeResponseReturnAll = advertTypeServiceIMPL.getAllAdvertType();

        assertThat(AdvertTypeResponseReturnAll).isNotNull();
        assertThat(AdvertTypeResponseReturnAll).hasSizeGreaterThan(1);
        assertThat(AdvertTypeResponseReturnAll.get(0).getId()).isEqualTo(1L);
        assertThat(AdvertTypeResponseReturnAll.get(1).getId()).isEqualTo(2L);
        assertThat(AdvertTypeResponseReturnAll.get(0).getTitle()).isEqualTo(advertTypeResponseReturn1.getTitle());
        assertThat(AdvertTypeResponseReturnAll.get(1).getTitle()).isEqualTo(advertTypeResponseReturn2.getTitle());
        assertThat(AdvertTypeResponseReturnAll.get(0).isBuiltIn()).isEqualTo(advertTypeResponseReturn1.isBuiltIn());
        assertThat(AdvertTypeResponseReturnAll.get(1).isBuiltIn()).isEqualTo(advertTypeResponseReturn2.isBuiltIn());

        logger.info(AdvertTypeResponseReturnAll.toString());

    }


    @Test
    @DisplayName("Test get advert type by ID service")
    void testGetAdvertTypeById() {
        AdvertTypeRequest advertTypeResponse = TestDataReaderForAdvertTypes.getAdvertTypeRequestPayload();
        AdvertTypeResponse advertTypeResponseReturn = advertTypeServiceIMPL.createAdvertType(advertTypeResponse);
        Long createdId = advertTypeResponseReturn.getId();

        AdvertTypeResponse advertTypeResponseReturn2 = advertTypeServiceIMPL.getAdvertTypeById(createdId);

        assertThat(advertTypeResponseReturn2).isNotNull();
        assertThat(advertTypeResponseReturn2.getId()).isEqualTo(advertTypeResponseReturn.getId());
        assertThat(advertTypeResponseReturn2.getTitle()).isEqualTo(advertTypeResponseReturn.getTitle());
        assertThat(advertTypeResponseReturn2.isBuiltIn()).isEqualTo(advertTypeResponseReturn.isBuiltIn());

        logger.info(advertTypeResponseReturn2.toString());

    }

    @Test
    @DisplayName("Test update advert type service")
    void testUpdateAdvertType() {
        AdvertTypeRequest advertTypeResponse = TestDataReaderForAdvertTypes.getAdvertTypeRequestPayload3();
        AdvertTypeResponse advertTypeResponseReturn = advertTypeServiceIMPL.createAdvertType(advertTypeResponse);
        Long createdId = advertTypeResponseReturn.getId();

        AdvertTypeResponse advertTypeResponseReturn2 = advertTypeServiceIMPL.updateAdvertType(createdId, TestDataReaderForAdvertTypes.getAdvertTypeRequestPayload2());

        assertThat(advertTypeResponseReturn2).isNotNull();
        assertThat(advertTypeResponseReturn2.getId()).isEqualTo(advertTypeResponseReturn.getId());
        assertThat(advertTypeResponseReturn2.getTitle()).isNotEqualTo(advertTypeResponseReturn.getTitle());
        assertThat(advertTypeResponseReturn2.isBuiltIn()).isEqualTo(advertTypeResponseReturn.isBuiltIn());

        logger.info(advertTypeResponseReturn2.toString());

    }

    @Test
    @DisplayName("Test update advert type if is built in service")
    void testUpdateAdvertTypeIfIsBuiltIn() {
        AdvertTypeRequest advertTypeResponse = TestDataReaderForAdvertTypes.getAdvertTypeRequestPayload();
        AdvertTypeResponse advertTypeResponseReturn = advertTypeServiceIMPL.createAdvertType(advertTypeResponse);
        Long createdId = advertTypeResponseReturn.getId();


        AdvertTypeRequest updateRequest = TestDataReaderForAdvertTypes.getAdvertTypeRequestPayload2();

        assertThatThrownBy(() -> advertTypeServiceIMPL.updateAdvertType(createdId, updateRequest))
                .isInstanceOf(BadRequestException.class)
                .hasMessageContaining(ErrorMessages.NOT_PERMITTED_UPDATE);


        logger.info("Verified that built-in advert types cannot be updated.");

    }

    @Test
    @DisplayName("Test delete advert type service")
    void testDeleteAdvertType() {
        AdvertTypeRequest advertTypeResponse = TestDataReaderForAdvertTypes.getAdvertTypeRequestPayload3();
        AdvertTypeResponse advertTypeResponseReturn = advertTypeServiceIMPL.createAdvertType(advertTypeResponse);
        Long createdId = advertTypeResponseReturn.getId();

        advertTypeServiceIMPL.deleteAdvertType(createdId);

        assertThatThrownBy(() -> advertTypeServiceIMPL.getAdvertTypeById(createdId))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Advert type not found");


        logger.info("Successfully deleted advert type with ID: {}", createdId);

    }

    @Test
    @DisplayName("Test delete advert type if is built in service")
    void testDeleteAdvertTypeIfIsBuiltIn() {
        AdvertTypeRequest advertTypeResponse = TestDataReaderForAdvertTypes.getAdvertTypeRequestPayload();
        AdvertTypeResponse advertTypeResponseReturn = advertTypeServiceIMPL.createAdvertType(advertTypeResponse);
        Long createdId = advertTypeResponseReturn.getId();

        assertThatThrownBy(() -> advertTypeServiceIMPL.deleteAdvertType(createdId))
                .isInstanceOf(BadRequestException.class)
                .hasMessageContaining(ErrorMessages.NOT_PERMITTED_DELETE);


        logger.info("Verified that built-in advert types cannot be deleted.");

    }


}

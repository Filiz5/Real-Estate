package com.team01.realestate.controller.business;

import com.team01.realestate.base.TestDataReaderForAdvertTypes;
import com.team01.realestate.payload.messages.SuccessMessages;
import com.team01.realestate.payload.response.business.AdvertTypeResponse;
import com.team01.realestate.payload.response.business.ResponseMessage;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


@SpringBootTest
@ActiveProfiles("test")
@Testcontainers
public class AdvertTypeControllerTest {

    Logger logger = LoggerFactory.getLogger(AdvertTypeControllerTest.class);

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
   private AdvertTypeController advertTypeController;

//    @Autowired
//    private AdvertTypeRepository advertTypeRepository;


    @Test
    @DisplayName("Test create advert type")
    @WithMockUser(username = "testAdmin", authorities = {"ADMIN"})
    void createAdvertTypeTest(){
        ResponseEntity<ResponseMessage<AdvertTypeResponse>> advertTypeResponse = advertTypeController.createAdvertType(TestDataReaderForAdvertTypes.getAdvertTypeRequestPayload());

        //Assert
        assertThat(advertTypeResponse.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(advertTypeResponse).isNotNull();
        assertThat(advertTypeResponse.getBody().getObject().getId()).isEqualTo(1L);
        assertThat(advertTypeResponse.getBody().getObject().getTitle()).isEqualTo("For Sale");
        assertThat(advertTypeResponse.getBody().getObject().isBuiltIn()).isTrue();

        logger.info("Advert type created: {}",advertTypeResponse);

    }

    @Test
    @DisplayName("Test get all advert types")
    @WithMockUser(username = "testAdmin", authorities = {"ADMIN"})
    void getAllAdvertTypesTest(){

        // Step 1: Creating two advertTypes
        ResponseEntity<ResponseMessage<AdvertTypeResponse>> advertTypeResponse1 =
                advertTypeController.createAdvertType(TestDataReaderForAdvertTypes.getAdvertTypeRequestPayload());

        ResponseEntity<ResponseMessage<AdvertTypeResponse>> advertTypeResponse2 =
                advertTypeController.createAdvertType(TestDataReaderForAdvertTypes.getAdvertTypeRequestPayload());
        //Assert creation Success
        assertThat(advertTypeResponse1.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(advertTypeResponse2.getStatusCode()).isEqualTo(HttpStatus.CREATED);

        // Step 2: calling getAllAdvertTypes(); from advertTypeController.
        ResponseEntity<ResponseMessage<List<AdvertTypeResponse>>> getAllResponse = advertTypeController.getAllAdvertTypes();

        // Step 3: assert for getAllAdvertTypes();
        assertThat(getAllResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(getAllResponse.getBody()).isNotNull();
        assertThat(getAllResponse.getBody().getObject()).hasSizeGreaterThan(1);

        List<AdvertTypeResponse> advertTypes = getAllResponse.getBody().getObject();
        assertThat(advertTypes).extracting(AdvertTypeResponse::getTitle).contains("For Sale", "For Sale");
        assertThat(advertTypes).extracting(AdvertTypeResponse::isBuiltIn).contains(true, true);


        logger.info("Retrieved advert types: {}", getAllResponse.getBody().getObject());


    }


    @Test
    @DisplayName("Test get advert type by id")
    @WithMockUser(username = "testAdmin", authorities = {"ADMIN"})
    void getAdvertTypeByIdTest(){
        // Step 1: Creating two advertTypes
        ResponseEntity<ResponseMessage<AdvertTypeResponse>> advertTypeResponse =
                advertTypeController.createAdvertType(TestDataReaderForAdvertTypes.getAdvertTypeRequestPayload());

        //Assert creation Success
        assertThat(advertTypeResponse.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        Long createdId = advertTypeResponse.getBody().getObject().getId();

        //step 2: calling getAdvertTypeById
        ResponseEntity<ResponseMessage<AdvertTypeResponse>> getAdvertTypeById = advertTypeController.getAdvertTypeById(createdId);

        //step 3: assert for getAdvertTypeById()
        assertThat(getAdvertTypeById.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(getAdvertTypeById.getBody().getObject().getId()).isEqualTo(createdId);
        assertThat(getAdvertTypeById.getBody().getObject().getTitle()).isEqualTo(TestDataReaderForAdvertTypes.getAdvertTypeResponsePayload().getTitle());
        assertThat(getAdvertTypeById.getBody().getObject().isBuiltIn()).isEqualTo(TestDataReaderForAdvertTypes.getAdvertTypeResponsePayload().isBuiltIn());


        logger.info("Retrieved advert type by id: {}", getAdvertTypeById.getBody().getObject());

    }

    @Test
    @DisplayName("Test update advert type")
    @WithMockUser(username = "testAdmin", authorities = {"ADMIN"})
    void updateAdvertTypeTest(){
        // Step 1: Creating two advertTypes
        ResponseEntity<ResponseMessage<AdvertTypeResponse>> advertTypeResponse =
                advertTypeController.createAdvertType(TestDataReaderForAdvertTypes.getAdvertTypeRequestPayload3());

        //Assert creation Success
        assertThat(advertTypeResponse.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        Long createdId = advertTypeResponse.getBody().getObject().getId();

        //step 2: calling updateAdvertType
        ResponseEntity<ResponseMessage<AdvertTypeResponse>> updateAdvertType = advertTypeController.updateAdvertType(createdId,TestDataReaderForAdvertTypes.getAdvertTypeRequestPayload2());

        //step 3: assert for updateAdvertType()
        assertThat(updateAdvertType.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(updateAdvertType).isNotNull();
        assertThat(updateAdvertType.getBody().getObject().getId()).isEqualTo(createdId);
        assertThat(updateAdvertType.getBody().getObject().getTitle()).isNotEqualTo(advertTypeResponse.getBody().getObject().getTitle());
        assertThat(updateAdvertType.getBody().getObject().getTitle()).isEqualTo("For Rent");
        assertThat(updateAdvertType.getBody().getObject().isBuiltIn()).isEqualTo(advertTypeResponse.getBody().getObject().isBuiltIn());

        logger.info("Retrieved update advert type: {}", updateAdvertType.getBody().getObject());


    }

    @Test
    @DisplayName("Test delete advert type")
    @WithMockUser(username = "testAdmin", authorities = {"ADMIN"})
    void deleteAdvertTypeTest(){
        // Step 1: Creating two advertTypes
        ResponseEntity<ResponseMessage<AdvertTypeResponse>> advertTypeResponse =
                advertTypeController.createAdvertType(TestDataReaderForAdvertTypes.getAdvertTypeRequestPayload3());

        //Assert creation Success
        assertThat(advertTypeResponse.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        Long createdId = advertTypeResponse.getBody().getObject().getId();

        //step 2: calling updateAdvertType
        ResponseEntity<ResponseMessage<Void>> deleteAdvertType = advertTypeController.deleteAdvertType(createdId);

        //step 3: assert for deleteAdvertType()
        assertThat(deleteAdvertType.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(deleteAdvertType.getBody().getMessage()).isEqualTo(SuccessMessages.ADVERT_TYPE_DELETED);
        assertThat(deleteAdvertType.getBody().getObject()).isNull();


        logger.info("Deleted advert type: {}", deleteAdvertType.getBody().getMessage());


    }



}

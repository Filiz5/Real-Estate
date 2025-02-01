package com.team01.realestate.controller.business;

import com.team01.realestate.contactmessage.controller.ContactMessageController;
import com.team01.realestate.contactmessage.payload.ContactMessageRequest;
import com.team01.realestate.contactmessage.payload.ContactMessageResponse;
import com.team01.realestate.contactmessage.service.ContactMessageService;
import com.team01.realestate.payload.response.business.ResponseMessage;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.HttpStatus;
import org.springframework.security.test.context.support.WithMockUser;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

class ContactMessageControllerTest {

    @InjectMocks
    private ContactMessageController contactMessageController;

    @Mock
    private ContactMessageService contactMessageService;

    @Mock
    private HttpServletRequest httpServletRequest;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void createContactMessage_ShouldReturnSuccessResponse() {
        // Arrange
        ContactMessageRequest contactMessageRequest = new ContactMessageRequest("Subject", "Body");

        ContactMessageResponse contactMessageResponse = new ContactMessageResponse();
        contactMessageResponse.setMessage("Body");
        contactMessageResponse.setRead(false);

        ResponseMessage<ContactMessageResponse> expectedResponse = new ResponseMessage<>();
        expectedResponse.setHttpStatus(HttpStatus.CREATED);
        expectedResponse.setMessage("Contact message created successfully");
        expectedResponse.setObject(contactMessageResponse);

        when(contactMessageService.createContactMessage(any(ContactMessageRequest.class))).thenReturn(expectedResponse);

        // Act
        ResponseMessage<ContactMessageResponse> response = contactMessageController.createContactMessage(contactMessageRequest);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.CREATED, response.getHttpStatus());
        assertEquals("Contact message created successfully", response.getMessage());
        assertNotNull(response.getObject());
        assertEquals("Body", response.getObject().getMessage());
        verify(contactMessageService, times(1)).createContactMessage(any(ContactMessageRequest.class));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void getAllContactMessages_ShouldReturnSuccessResponse() {
        // Arrange
        String query = "testQuery";
        int page = 0;
        int size = 10;
        String sort = "createdAt";
        String type = "asc";

        Page<ContactMessageResponse> contactMessagesPage = new PageImpl<>(List.of(
                new ContactMessageResponse(1L, "John", "Doe", "john.doe@example.com", "Message 1", false, LocalDateTime.now()),
                new ContactMessageResponse(2L, "Jane", "Doe", "jane.doe@example.com", "Message 2", false, LocalDateTime.now())
        ));


        when(contactMessageService.getAllContactMessages(query, page, size, sort, type))
                .thenReturn(contactMessagesPage);

        // Act
        Page<ContactMessageResponse> response = contactMessageController.getAllContactMessages(
                query, page, size, sort, type);

        // Assert
        assertNotNull(response);
        assertEquals(2, response.getContent().size());
        assertEquals("Message 1", response.getContent().get(0).getMessage());
        assertEquals("Message 2", response.getContent().get(1).getMessage());
        verify(contactMessageService, times(1)).getAllContactMessages(query, page, size, sort, type);
    }


    @Test
    @WithMockUser(roles = "ADMIN")
    void getContactMessageById_ShouldReturnSuccessResponse() {
        // Arrange
        Long messageId = 1L;
        ContactMessageResponse contactMessageResponse = new ContactMessageResponse();
        contactMessageResponse.setMessage("Body");
        contactMessageResponse.setRead(false);

        ResponseMessage<ContactMessageResponse> expectedResponse = new ResponseMessage<>();
        expectedResponse.setHttpStatus(HttpStatus.OK);
        expectedResponse.setMessage("Contact message found");
        expectedResponse.setObject(contactMessageResponse);

        when(contactMessageService.getContactMessagesById(messageId)).thenReturn(expectedResponse);

        // Act
        ResponseMessage<ContactMessageResponse> response = contactMessageController.getContactMessageById(messageId);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getHttpStatus());
        assertEquals("Contact message found", response.getMessage());
        assertNotNull(response.getObject());
        assertEquals("Body", response.getObject().getMessage());
        verify(contactMessageService, times(1)).getContactMessagesById(messageId);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void deleteContactMessageById_ShouldReturnSuccessResponse() {
        // Arrange
        Long messageId = 1L;
        ContactMessageResponse contactMessageResponse = new ContactMessageResponse();
        contactMessageResponse.setMessage("Body");
        contactMessageResponse.setRead(false);

        ResponseMessage<ContactMessageResponse> expectedResponse = new ResponseMessage<>();
        expectedResponse.setHttpStatus(HttpStatus.OK);
        expectedResponse.setMessage("Contact message deleted successfully");
        expectedResponse.setObject(contactMessageResponse);

        when(contactMessageService.deleteContactMessagesById(messageId)).thenReturn(expectedResponse);

        // Act
        ResponseMessage<ContactMessageResponse> response = contactMessageController.deleteContactMessageById(messageId);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getHttpStatus());
        assertEquals("Contact message deleted successfully", response.getMessage());
        assertNotNull(response.getObject());
        assertEquals("Body", response.getObject().getMessage());
        verify(contactMessageService, times(1)).deleteContactMessagesById(messageId);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void readContactMessageById_ShouldReturnSuccessResponse() {
        // Arrange
        Long messageId = 1L;
        ContactMessageResponse contactMessageResponse = new ContactMessageResponse();
        contactMessageResponse.setMessage("Body");
        contactMessageResponse.setRead(true);

        ResponseMessage<ContactMessageResponse> expectedResponse = new ResponseMessage<>();
        expectedResponse.setHttpStatus(HttpStatus.OK);
        expectedResponse.setMessage("Contact message marked as read");
        expectedResponse.setObject(contactMessageResponse);

        when(contactMessageService.readContactMessageById(messageId)).thenReturn(expectedResponse);

        // Act
        ResponseMessage<ContactMessageResponse> response = contactMessageController.readMessageById(messageId);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getHttpStatus());
        assertEquals("Contact message marked as read", response.getMessage());
        assertNotNull(response.getObject());
        assertEquals("Body", response.getObject().getMessage());
        verify(contactMessageService, times(1)).readContactMessageById(messageId);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void unreadContactMessageById_ShouldReturnSuccessResponse() {
        // Arrange
        Long messageId = 1L;
        ContactMessageResponse contactMessageResponse = new ContactMessageResponse();
        contactMessageResponse.setMessage("Body");
        contactMessageResponse.setRead(false);

        ResponseMessage<ContactMessageResponse> expectedResponse = new ResponseMessage<>();
        expectedResponse.setHttpStatus(HttpStatus.OK);
        expectedResponse.setMessage("Contact message marked as unread");
        expectedResponse.setObject(contactMessageResponse);

        when(contactMessageService.unreadContactMessageById(messageId)).thenReturn(expectedResponse);

        // Act
        ResponseMessage<ContactMessageResponse> response = contactMessageController.unreadMessageById(messageId);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getHttpStatus());
        assertEquals("Contact message marked as unread", response.getMessage());
        assertNotNull(response.getObject());
        assertEquals("Body", response.getObject().getMessage());
        verify(contactMessageService, times(1)).unreadContactMessageById(messageId);
    }
}

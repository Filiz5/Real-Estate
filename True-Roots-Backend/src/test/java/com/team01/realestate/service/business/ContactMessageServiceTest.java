package com.team01.realestate.service.business;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import com.team01.realestate.contactmessage.entity.ContactMessage;
import com.team01.realestate.contactmessage.mapper.ContactMessageMapper;
import com.team01.realestate.contactmessage.message.MessagesForContactMes;
import com.team01.realestate.contactmessage.payload.ContactMessageRequest;
import com.team01.realestate.contactmessage.payload.ContactMessageResponse;
import com.team01.realestate.contactmessage.repository.ContactMessageRepository;
import com.team01.realestate.contactmessage.service.ContactMessageService;
import com.team01.realestate.exception.ResourceNotFoundException;
import com.team01.realestate.payload.response.business.ResponseMessage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)  // Enable Mockito
class ContactMessageServiceTest {

    @Mock
    private ContactMessageRepository contactMessageRepository;

    @Mock
    private ContactMessageMapper contactMessageMapper;

    @InjectMocks
    private ContactMessageService contactMessageService;

    @Test
    void createContactMessage_shouldReturnSuccessResponse() {
        // Arrange
        ContactMessageRequest contactMessageRequest = new ContactMessageRequest("Subject", "Body");
        ContactMessage contactMessage = new ContactMessage();
        ContactMessageResponse contactMessageResponse = new ContactMessageResponse();
        contactMessageResponse.setRead(false);
        contactMessageResponse.setMessage("Body");

        contactMessage.setRead(false);
        contactMessage.setMessage("Body");

        when(contactMessageMapper.requestToContactMessage(contactMessageRequest)).thenReturn(contactMessage);
        when(contactMessageRepository.save(contactMessage)).thenReturn(contactMessage);
        when(contactMessageMapper.contactMessageToResponse(contactMessage)).thenReturn(contactMessageResponse);

        // Act
        ResponseMessage<ContactMessageResponse> response = contactMessageService.createContactMessage(contactMessageRequest);

        // Assert
        assertEquals(HttpStatus.OK, response.getHttpStatus());
        assertEquals(MessagesForContactMes.CONTACT_MESSAGE_CREATED_SUCCESSFULLY, response.getMessage());
        assertNotNull(response.getObject());
        assertEquals(contactMessageResponse, response.getObject());

        verify(contactMessageRepository, times(1)).save(contactMessage);
        verify(contactMessageMapper, times(1)).requestToContactMessage(contactMessageRequest);
        verify(contactMessageMapper, times(1)).contactMessageToResponse(contactMessage);
    }

    @Test
    void getAllContactMessages_shouldReturnPagedResponse() {
        // Arrange
        String query = "subject";
        int page = 0;
        int size = 10;
        String sort = "date";
        String type = "asc";

        ContactMessage contactMessage = new ContactMessage();
        contactMessage.setMessage("Subject");
        contactMessage.setRead(false);

        ContactMessageResponse contactMessageResponse = new ContactMessageResponse();
        contactMessageResponse.setMessage("Subject");
        contactMessageResponse.setRead(false);

        Pageable pageable = PageRequest.of(page, size, Sort.by(sort).ascending());
        Page<ContactMessage> pageResponse = new PageImpl<>(List.of(contactMessage), pageable, 1);

        when(contactMessageRepository.findByQuery(query, pageable)).thenReturn(pageResponse);
        when(contactMessageMapper.contactMessageToResponse(contactMessage)).thenReturn(contactMessageResponse);

        // Act
        Page<ContactMessageResponse> response = contactMessageService.getAllContactMessages(query, page, size, sort, type);

        // Assert
        assertNotNull(response);
        assertEquals(1, response.getTotalElements());
        assertEquals(contactMessageResponse, response.getContent().get(0));
        verify(contactMessageRepository, times(1)).findByQuery(query, pageable);
        verify(contactMessageMapper, times(1)).contactMessageToResponse(contactMessage);
    }

    @Test
    void getContactMessagesById_shouldReturnSuccessResponse() {
        // Arrange
        Long messageId = 1L;
        ContactMessage contactMessage = new ContactMessage();
        contactMessage.setMessage("Message Body");
        contactMessage.setRead(false);

        ContactMessageResponse contactMessageResponse = new ContactMessageResponse();
        contactMessageResponse.setMessage("Message Body");
        contactMessageResponse.setRead(false);

        when(contactMessageRepository.findById(messageId)).thenReturn(Optional.of(contactMessage));
        when(contactMessageMapper.contactMessageToResponse(contactMessage)).thenReturn(contactMessageResponse);

        // Act
        ResponseMessage<ContactMessageResponse> response = contactMessageService.getContactMessagesById(messageId);

        // Assert
        assertEquals(HttpStatus.OK, response.getHttpStatus());
        assertEquals(String.format(MessagesForContactMes.CONTACT_MESSAGE_FOUND, messageId), response.getMessage());
        assertNotNull(response.getObject());
        assertEquals(contactMessageResponse, response.getObject());
        verify(contactMessageRepository, times(1)).findById(messageId);
        verify(contactMessageMapper, times(1)).contactMessageToResponse(contactMessage);
    }

    @Test
    void getContactMessagesById_shouldThrowNotFoundException() {
        // Arrange
        Long messageId = 1L;

        when(contactMessageRepository.findById(messageId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> {
            contactMessageService.getContactMessagesById(messageId);
        });
    }
    @Test
    void deleteContactMessagesById_shouldReturnSuccessResponse() {
        // Arrange
        Long messageId = 1L;
        ContactMessage contactMessage = new ContactMessage();
        contactMessage.setMessage("Body");
        contactMessage.setRead(false);

        ContactMessageResponse contactMessageResponse = new ContactMessageResponse();
        contactMessageResponse.setMessage("Body");
        contactMessageResponse.setRead(false);

        when(contactMessageRepository.findById(messageId)).thenReturn(Optional.of(contactMessage));
        when(contactMessageMapper.contactMessageToResponse(contactMessage)).thenReturn(contactMessageResponse);

        // Act
        ResponseMessage<ContactMessageResponse> response = contactMessageService.deleteContactMessagesById(messageId);

        // Assert
        assertEquals(HttpStatus.OK, response.getHttpStatus());
        assertEquals(String.format(MessagesForContactMes.DELETED_CONTACT_MESSAGE, messageId), response.getMessage());
        assertNotNull(response.getObject());
        assertEquals(contactMessageResponse, response.getObject());

        verify(contactMessageRepository, times(1)).findById(messageId);
        verify(contactMessageRepository, times(1)).deleteById(messageId);
        verify(contactMessageMapper, times(1)).contactMessageToResponse(contactMessage);
    }

    @Test
    void deleteContactMessagesById_shouldThrowNotFoundException() {
        // Arrange
        Long messageId = 1L;

        when(contactMessageRepository.findById(messageId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> {
            contactMessageService.deleteContactMessagesById(messageId);
        });
    }

    @Test
    void readContactMessageById_shouldReturnSuccessResponse() {
        // Arrange
        Long messageId = 1L;
        ContactMessage contactMessage = new ContactMessage();
        contactMessage.setMessage("Body");
        contactMessage.setRead(false);

        ContactMessageResponse contactMessageResponse = new ContactMessageResponse();
        contactMessageResponse.setMessage("Body");
        contactMessageResponse.setRead(true);

        when(contactMessageRepository.findById(messageId)).thenReturn(Optional.of(contactMessage));
        when(contactMessageRepository.save(contactMessage)).thenReturn(contactMessage);
        when(contactMessageMapper.contactMessageToResponse(contactMessage)).thenReturn(contactMessageResponse);

        // Act
        ResponseMessage<ContactMessageResponse> response = contactMessageService.readContactMessageById(messageId);

        // Assert
        assertEquals(HttpStatus.OK, response.getHttpStatus());
        assertEquals(String.format(MessagesForContactMes.CONTACT_MESSAGE_READ, messageId), response.getMessage());
        assertNotNull(response.getObject());
        assertEquals(contactMessageResponse, response.getObject());

        verify(contactMessageRepository, times(1)).findById(messageId);
        verify(contactMessageRepository, times(1)).save(contactMessage);
        verify(contactMessageMapper, times(1)).contactMessageToResponse(contactMessage);
    }

    @Test
    void readContactMessageById_shouldThrowNotFoundException() {
        // Arrange
        Long messageId = 1L;

        when(contactMessageRepository.findById(messageId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> {
            contactMessageService.readContactMessageById(messageId);
        });
    }

    @Test
    void unreadContactMessageById_shouldReturnSuccessResponse() {
        // Arrange
        Long messageId = 1L;
        ContactMessage contactMessage = new ContactMessage();
        contactMessage.setMessage("Body");
        contactMessage.setRead(true);

        ContactMessageResponse contactMessageResponse = new ContactMessageResponse();
        contactMessageResponse.setMessage("Body");
        contactMessageResponse.setRead(false);

        when(contactMessageRepository.findById(messageId)).thenReturn(Optional.of(contactMessage));
        when(contactMessageRepository.save(contactMessage)).thenReturn(contactMessage);
        when(contactMessageMapper.contactMessageToResponse(contactMessage)).thenReturn(contactMessageResponse);

        // Act
        ResponseMessage<ContactMessageResponse> response = contactMessageService.unreadContactMessageById(messageId);

        // Assert
        assertEquals(HttpStatus.OK, response.getHttpStatus());
        assertEquals(String.format(MessagesForContactMes.CONTACT_MESSAGE_UNREAD, messageId), response.getMessage());
        assertNotNull(response.getObject());
        assertEquals(contactMessageResponse, response.getObject());

        verify(contactMessageRepository, times(1)).findById(messageId);
        verify(contactMessageRepository, times(1)).save(contactMessage);
        verify(contactMessageMapper, times(1)).contactMessageToResponse(contactMessage);
    }

    @Test
    void unreadContactMessageById_shouldThrowNotFoundException() {
        // Arrange
        Long messageId = 1L;

        when(contactMessageRepository.findById(messageId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> {
            contactMessageService.unreadContactMessageById(messageId);
        });
    }

    @Test
    void contactMessageToResponse_ShouldReturnNull_WhenSavedMessageIsNull() {
        // Act
        ContactMessageResponse contactMessageResponse = contactMessageMapper.contactMessageToResponse(null);

        // Assert
        assertNull(contactMessageResponse); // Expecting null response when entity is null
    }
}
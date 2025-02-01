package com.team01.realestate.payload;

import static org.junit.jupiter.api.Assertions.*;

import com.team01.realestate.contactmessage.entity.ContactMessage;
import com.team01.realestate.contactmessage.mapper.ContactMessageMapper;
import com.team01.realestate.contactmessage.payload.ContactMessageRequest;
import com.team01.realestate.contactmessage.payload.ContactMessageResponse;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

class ContactMessageMapperTest {

    private final ContactMessageMapper contactMessageMapper = new ContactMessageMapper();

    @Test
    void shouldMapRequestToContactMessageSuccessfully() {
        // Arrange
        ContactMessageRequest request = new ContactMessageRequest();
        request.setFirstName("John");
        request.setLastName("Doe");
        request.setEmail("john.doe@example.com");
        request.setMessage("This is a test message.");

        // Act
        ContactMessage contactMessage = contactMessageMapper.requestToContactMessage(request);

        // Assert
        assertNotNull(contactMessage);
        assertEquals("John", contactMessage.getFirstName());
        assertEquals("Doe", contactMessage.getLastName());
        assertEquals("john.doe@example.com", contactMessage.getEmail());
        assertEquals("This is a test message.", contactMessage.getMessage());
        assertNotNull(contactMessage.getCreatedAt()); // Zaman atamasını kontrol et
    }

    @Test
    void shouldMapContactMessageToResponseSuccessfully() {
        // Arrange
        ContactMessage contactMessage = ContactMessage.builder()
                .id(1L)
                .firstName("John")
                .lastName("Doe")
                .email("john.doe@example.com")
                .message("This is a test message.")
                .isRead(false)
                .createdAt(LocalDateTime.of(2023, 1, 1, 10, 0))
                .build();

        // Act
        ContactMessageResponse response = contactMessageMapper.contactMessageToResponse(contactMessage);

        // Assert
        assertNotNull(response);
        assertEquals(1L, response.getId());
        assertEquals("John", response.getFirstName());
        assertEquals("Doe", response.getLastName());
        assertEquals("john.doe@example.com", response.getEmail());
        assertEquals("This is a test message.", response.getMessage());
        assertFalse(response.isRead());
        assertEquals(LocalDateTime.of(2023, 1, 1, 10, 0), response.getCreatedAt());
    }
}


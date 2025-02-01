package com.team01.realestate.controller.business;

import com.team01.realestate.payload.response.business.ResponseMessage;
import com.team01.realestate.service.business.SettingsService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class SettingsControllerTest {

    @InjectMocks
    private SettingsController settingsController;

    @Mock
    private SettingsService settingsService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void resetDatabase_ShouldReturnSuccessResponse() {
        // Arrange
        ResponseMessage<String> mockResponse = new ResponseMessage<>();
        mockResponse.setHttpStatus(HttpStatus.OK);
        mockResponse.setMessage("Database reset successfully");

        when(settingsService.resetDatabase()).thenReturn(mockResponse);

        // Act
        ResponseMessage<String> response = settingsController.resetDatabase();

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getHttpStatus());
        assertEquals("Database reset successfully", response.getMessage());
        verify(settingsService, times(1)).resetDatabase();
    }

    @Test
    void resetDatabase_WhenServiceThrowsException_ShouldPropagateException() {
        // Arrange
        when(settingsService.resetDatabase()).thenThrow(new RuntimeException("Test exception"));

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> settingsController.resetDatabase());
        assertEquals("Test exception", exception.getMessage());
        verify(settingsService, times(1)).resetDatabase();
    }
}

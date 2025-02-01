package com.team01.realestate.service.business;

import com.team01.realestate.entity.concretes.business.Advert;
import com.team01.realestate.entity.concretes.business.Category;
import com.team01.realestate.entity.concretes.business.Log;
import com.team01.realestate.entity.concretes.user.User;
import com.team01.realestate.entity.enums.AdvertStatus;
import com.team01.realestate.entity.enums.LogType;
import com.team01.realestate.payload.request.business.AdvertRequestForUpdate;
import com.team01.realestate.repository.business.LogRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class LogServiceTest {

    @Mock
    private LogRepository logRepository;

    @InjectMocks
    private LogService logService;

    private User user;
    private Advert advert;
    private AdvertRequestForUpdate advertRequestForUpdate;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Mocking user and advert objects
        user = new User();
        user.setId(1L);
        user.setFirstName("John Doe");

        Category category = new Category();
        category.setId(1L);
        advert = new Advert();
        advert.setId(1L);
        advert.setTitle("Advert Title");
        advert.setDesc("Advert Description");
        advert.setPrice(BigDecimal.valueOf(1000.0));
        advert.setCategory(category);
        advert.setAdvertStatus(AdvertStatus.PENDING);

        advertRequestForUpdate = new AdvertRequestForUpdate();
        advertRequestForUpdate.setTitle("Updated Advert Title");
        advertRequestForUpdate.setDesc("Updated Description");
        advertRequestForUpdate.setPrice(BigDecimal.valueOf(1200.0));
        advertRequestForUpdate.setCategory_id(2L);
    }

    @Test
    void testCreateLog() {
        Log log = new Log();
        log.setLog(LogType.CREATED);
        log.setDescription("Test description");
        log.setUser(user);
        log.setAdvert(advert);

        logService.createLog(LogType.CREATED, "Test description", user, advert);

        verify(logRepository, times(1)).save(any(Log.class));
    }

    @Test
    void testCreateLogForAdminUpdate() {
        LogType logType = LogType.UPDATED;
        String description = "Admin updated advert";

        logService.createLogForAdminUpdate(logType, description, user, advert, AdvertStatus.PENDING, advertRequestForUpdate);

        verify(logRepository, times(1)).save(any(Log.class));
    }

    @Test
    void testCreateLogForTourRequest() {
        User guestUser = new User();
        guestUser.setId(2L);
        guestUser.setFirstName("Guest User");

        User ownerUser = new User();
        ownerUser.setId(3L);
        ownerUser.setLastName("Owner User");

        LogType logType = LogType.CREATED;
        String description = "Tour request created";

        logService.createLogForTourRequest(logType, description, guestUser, ownerUser, advert);

        verify(logRepository, times(1)).save(any(Log.class));
    }

    @Test
    void testGetLogsByUserId() {
        Long userId = 1L;
        List<Log> logs = List.of(new Log(), new Log());

        when(logRepository.findLogsByUserId(userId)).thenReturn(logs);

        List<Log> result = logService.getLogsByUserId(userId);

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(logRepository, times(1)).findLogsByUserId(userId);
    }
}

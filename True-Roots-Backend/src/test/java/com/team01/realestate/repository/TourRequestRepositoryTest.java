package com.team01.realestate.repository;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import com.team01.realestate.entity.concretes.business.Advert;
import com.team01.realestate.entity.concretes.business.TourRequest;
import com.team01.realestate.entity.concretes.user.User;
import com.team01.realestate.entity.enums.TourRequestStatus;
import com.team01.realestate.repository.business.TourRequestRepository;
import com.team01.realestate.service.business.TourRequestService;
import org.junit.jupiter.api.*;
import org.mockito.*;
import org.springframework.data.domain.*;
import org.springframework.util.Assert;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;

class TourRequestRepositoryTest {

    @Mock
    private TourRequestRepository tourRequestRepository;

    @Mock
    private Pageable pageable;

    private TourRequest tourRequest;


    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        tourRequest = new TourRequest();
        tourRequest.setId(1L);
        // Mocking other necessary fields for the test
        User guestUser = new User();
        guestUser.setId(1L);
        tourRequest.setGuestUser(guestUser);

        Advert advert = new Advert();
        advert.setId(1L);
        tourRequest.setAdvert(advert);

        tourRequest.setTourRequestStatus(TourRequestStatus.PENDING);
    }

    @Test
    @DisplayName("Test FIND tourRequest by Auth User in REPOSITORY")
    void testFindTourRequestsByAuthUser() {
        String email = "test@example.com";
        String query = "test query";

        Page<TourRequest> mockPage = new PageImpl<>(Collections.singletonList(tourRequest));

        when(tourRequestRepository.findTourRequestsByAuthUser(email, query, pageable)).thenReturn(mockPage);

        Page<TourRequest> result = tourRequestRepository.findTourRequestsByAuthUser(email, query, pageable);

        Assert.notNull(result, "Result should not be null");
        Assert.isTrue(!result.getContent().isEmpty(), "Result should contain tour requests");
    }

    @Test
    @DisplayName("Test FIND All By Query in REPOSITORY")
    void testFindAllByQuery() {
        String query = "test query";

        Page<TourRequest> mockPage = new PageImpl<>(Collections.singletonList(tourRequest));

        when(tourRequestRepository.findAllByQuery(query, pageable)).thenReturn(mockPage);

        Page<TourRequest> result = tourRequestRepository.findAllByQuery(query, pageable);

        Assert.notNull(result, "Result should not be null");
        Assert.isTrue(!result.getContent().isEmpty(), "Result should contain tour requests");
    }

    @Test
    @DisplayName("Test FIND By Filters in REPOSITORY")
    void testFindByFilters() {
        LocalDate date1 = LocalDate.now().minusDays(1);
        LocalDate date2 = LocalDate.now();
        TourRequestStatus status = TourRequestStatus.PENDING;

        List<TourRequest> mockList = Collections.singletonList(tourRequest);

        when(tourRequestRepository.findByFilters(date1, date2, status)).thenReturn(mockList);

        List<TourRequest> result = tourRequestRepository.findByFilters(date1, date2, status);

        Assert.notNull(result, "Result should not be null");
        Assert.isTrue(!result.isEmpty(), "Result should contain tour requests");
    }

    @Test
    @DisplayName("Test isExists By Guest in REPOSITORY")
    void testExistsByGuestUserAndTourDateWithinHour() {
        Long guestUserId = 1L;
        LocalDate tourDate = LocalDate.now();
        LocalTime tourTime = LocalTime.now();
        LocalTime tourTimePlusOneHour = tourTime.plusHours(1);
        LocalTime tourTimeMinusOneHour = tourTime.minusHours(1);

        when(tourRequestRepository.existsByGuestUserAndTourDateWithinHour(guestUserId, tourDate, tourTime, tourTimePlusOneHour, tourTimeMinusOneHour))
                .thenReturn(true);

        boolean result = tourRequestRepository.existsByGuestUserAndTourDateWithinHour(guestUserId, tourDate, tourTime, tourTimePlusOneHour, tourTimeMinusOneHour);

        Assert.isTrue(result, "Tour request should exist within the given time frame");
    }

    @Test
    @DisplayName("Test FIND tourRequest by UserId in REPOSITORY")
    void testFindTourRequestsByUserId() {
        Long userId = 1L;

        List<TourRequest> mockList = Collections.singletonList(tourRequest);

        when(tourRequestRepository.findTourRequestsByUserId(userId)).thenReturn(mockList);

        List<TourRequest> result = tourRequestRepository.findTourRequestsByUserId(userId);

        Assert.notNull(result, "Result should not be null");
        Assert.isTrue(!result.isEmpty(), "Result should contain tour requests");
    }
}
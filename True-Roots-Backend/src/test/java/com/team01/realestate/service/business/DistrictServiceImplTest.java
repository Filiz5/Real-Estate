package com.team01.realestate.service.business;

import com.team01.realestate.entity.concretes.business.District;
import com.team01.realestate.exception.ResourceNotFoundException;
import com.team01.realestate.payload.mapper.DistrictMapper;
import com.team01.realestate.payload.response.business.DistrictResponse;
import com.team01.realestate.repository.business.DistrictRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class DistrictServiceImplTest {

    @Mock
    private DistrictRepository districtRepository;

    @Mock
    private DistrictMapper districtMapper;

    @InjectMocks
    private DistrictServiceImpl districtService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllDistrict_Success() {
        // Arrange
        District district1 = new District();
        district1.setId(1L);
        district1.setName("District1");

        District district2 = new District();
        district2.setId(2L);
        district2.setName("District2");

        List<District> districtList = Arrays.asList(district1, district2);

        DistrictResponse districtResponse1 = new DistrictResponse();
        districtResponse1.setId(1L);
        districtResponse1.setName("District1");
        DistrictResponse districtResponse2 = new DistrictResponse();
        districtResponse2.setId(2L);
        districtResponse2.setName("District2");

        when(districtRepository.findAll()).thenReturn(districtList);
        when(districtMapper.toResponse(district1)).thenReturn(districtResponse1);
        when(districtMapper.toResponse(district2)).thenReturn(districtResponse2);

        // Act
        List<DistrictResponse> result = districtService.getAllDistrict();

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("District1", result.get(0).getName());
        assertEquals("District2", result.get(1).getName());

        verify(districtRepository, times(1)).findAll();
        verify(districtMapper, times(1)).toResponse(district1);
        verify(districtMapper, times(1)).toResponse(district2);
    }

    @Test
    void testGetAllDistrict_NoDistrictsFound() {
        // Arrange
        when(districtRepository.findAll()).thenReturn(Collections.emptyList());

        // Act & Assert
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, districtService::getAllDistrict);
        assertEquals("No district found", exception.getMessage());

        verify(districtRepository, times(1)).findAll();
        verifyNoInteractions(districtMapper);
    }
}

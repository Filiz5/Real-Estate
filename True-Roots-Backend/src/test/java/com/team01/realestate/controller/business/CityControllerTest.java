package com.team01.realestate.controller.business;


import com.team01.realestate.payload.response.business.CityResponse;
import com.team01.realestate.service.impl.CityService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@WithMockUser(username = "testUser", roles = {"USER"})
class CityControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CityService cityService;

    @Test
    public void testGetAllCities() throws Exception {
        List<CityResponse> mockCities = List.of(
                new CityResponse(1L, "City 1", "Country 1", Arrays.asList("District 1", "District 2")),
                new CityResponse(2L, "City 2", "Country 2", Arrays.asList("District 3", "District 4"))
        );

        when(cityService.getAllCity()).thenReturn(mockCities);

        mockMvc.perform(get("/cities"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.object.size()").value(2))
                .andExpect(jsonPath("$.object[0].id").value(1))
                .andExpect(jsonPath("$.object[0].name").value("City 1"))
                .andExpect(jsonPath("$.object[0].countryName").value("Country 1"))
                .andExpect(jsonPath("$.object[0].districts.size()").value(2))
                .andExpect(jsonPath("$.object[1].id").value(2))
                .andExpect(jsonPath("$.object[1].name").value("City 2"))
                .andExpect(jsonPath("$.object[1].countryName").value("Country 2"))
                .andExpect(jsonPath("$.object[1].districts.size()").value(2));
    }
}

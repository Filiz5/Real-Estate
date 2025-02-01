package com.team01.realestate.controller.business;

import com.team01.realestate.payload.response.business.CountryResponse;

import com.team01.realestate.service.impl.CountryService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@WithMockUser(username = "testUser", roles = {"USER"})
class CountryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CountryService countryService;

    @Test
    void testGetAllCountries() throws Exception {
        // Mock verileri ayarla
        List<CountryResponse> mockCountries = List.of(
                new CountryResponse(1L, "Country 1", Arrays.asList("City 1", "City 2")),
                new CountryResponse(2L, "Country 2", Arrays.asList("City 3", "City 4"))
        );

        // Mock davranışı tanımla
        when(countryService.getAllCountry()).thenReturn(mockCountries);

        // HTTP isteğini yap ve sonuçları kontrol et
        mockMvc.perform(get("/countries"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.object.size()").value(2)) // JSON array boyutunu kontrol et
                .andExpect(jsonPath("$.object[0].id").value(1))
                .andExpect(jsonPath("$.object[0].name").value("Country 1"))
                .andExpect(jsonPath("$.object[0].cities.size()").value(2))
                .andExpect(jsonPath("$.object[1].id").value(2))
                .andExpect(jsonPath("$.object[1].name").value("Country 2"))
                .andExpect(jsonPath("$.object[1].cities.size()").value(2));
    }

    @Test
    void testGetAllCountries_NotFound() throws Exception {
        // Mock verileri boş döndür
        when(countryService.getAllCountry()).thenReturn(Collections.emptyList());

        // HTTP isteğini yap ve boş listeyi kontrol et
        mockMvc.perform(get("/countries"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.object.size()").value(0)); // JSON array boyutunu kontrol et
    }
}

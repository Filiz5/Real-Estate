package com.team01.realestate.controller.business;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.team01.realestate.entity.concretes.business.Advert;
import com.team01.realestate.entity.concretes.business.City;
import com.team01.realestate.entity.concretes.business.Country;
import com.team01.realestate.entity.concretes.business.District;
import com.team01.realestate.payload.response.business.ResponseMessage;
import com.team01.realestate.repository.business.AdvertRepository;
import com.team01.realestate.repository.business.CityRepository;
import com.team01.realestate.repository.business.CountryRepository;
import com.team01.realestate.repository.business.DistrictRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/location")
public class LocationController {

    private final CountryRepository countryRepository;
    private final CityRepository cityRepository;
    private final DistrictRepository districtRepository;
    private final ObjectMapper objectMapper;
    private final AdvertRepository advertRepository;

    @Autowired
    public LocationController(CountryRepository countryRepository, CityRepository cityRepository,
                              DistrictRepository districtRepository, ObjectMapper objectMapper,
                              AdvertRepository advertRepository) {
        this.countryRepository = countryRepository;
        this.cityRepository = cityRepository;
        this.districtRepository = districtRepository;
        this.objectMapper = objectMapper;
        this.advertRepository = advertRepository;
    }

    @PostMapping("/importData")
    @PreAuthorize("hasAnyAuthority('ADMIN','MANAGER')")
    public ResponseMessage<String> importData(@RequestBody List<Country> countries) {
        try {
            for (Country country : countries) {
                // Save the country first
                Country savedCountry = countryRepository.save(country); // Save the country and get the saved one

                // Save the cities and establish the relationship
                for (City city : savedCountry.getCities()) {
                    city.setCountry(savedCountry); // Link the city to the country
                    cityRepository.save(city); // Save city

                    // Save the districts and link them to the city
                    for (District district : city.getDistricts()) {
                        district.setCity(city); // Link district to the city
                        districtRepository.save(district); // Save district
                    }
                }
            }
            return ResponseMessage.<String>builder()
                    .httpStatus(HttpStatus.OK)
                    .message("Successfully imported")
                    .build();
        } catch (Exception e) {
            String errorMessage = parseDatabaseException(e);
            return ResponseMessage.<String>builder()
                    .httpStatus(HttpStatus.BAD_REQUEST)
                    .message(errorMessage)
                    .build();
        }
    }

    String parseDatabaseException(Exception e) {
        String message = e.getMessage();
        if (message.contains("duplicate key value violates unique constraint")) {
            int startIndex = message.indexOf("Key (name)=") + "Key (name)=".length();
            int endIndex = message.indexOf(")", startIndex);
            String countryName = message.substring(startIndex, endIndex).replace("(", "").trim();
            return "Error: The country '" + countryName + "' already exists in the database.";
        }
        return "An error occurred while importing data.";
    }

    @DeleteMapping("/deleteAllCountries")
    @PreAuthorize("hasAnyAuthority('ADMIN','MANAGER')")
    public ResponseMessage<String> deleteAllCountriesCitiesDistricts() {
        try {
            districtRepository.deleteAll(); // Delete all districts
            cityRepository.deleteAll();     // Delete all cities
            countryRepository.deleteAll();  // Delete all countries
            return ResponseMessage.<String>builder().httpStatus(HttpStatus.OK)
                    .build();

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseMessage.<String>builder().httpStatus(HttpStatus.BAD_REQUEST)
                    .message("An error occurred while deleting data because there are adverts associated with these countries, cities, or regions.").
                    build();
        }
    }

//    @DeleteMapping("/deleteAllCountries")
//    @PreAuthorize("hasAnyAuthority('ADMIN','MANAGER')")
//    public ResponseEntity<String> deleteAllCountriesCitiesDistricts() {
//        try {
//            // Tüm Advert kayıtlarının district, city ve country referanslarını null yap
//            List<Advert> allAdverts = advertRepository.findAll();
//            for (Advert advert : allAdverts) {
//                advert.setDistrict(null);
//                advert.setCity(null);
//                advert.setCountry(null);
//            }
//            advertRepository.saveAll(allAdverts);
//
//            // Tüm District, City ve Country kayıtlarını sil
//            districtRepository.deleteAll(); // Delete all districts
//            cityRepository.deleteAll();     // Delete all cities
//            countryRepository.deleteAll();  // Delete all countries
//
//            return ResponseEntity.ok("All data deleted successfully.");
//        } catch (Exception e) {
//            e.printStackTrace();
//            return ResponseEntity.status(500).body("An error occurred while deleting data.");
//        }
//    }

//    @PutMapping("/updateCountryName/{id}")
//    @PreAuthorize("hasAnyAuthority('ADMIN','MANAGER')")
//    public ResponseEntity<String> updateCountryName(@PathVariable Long id, @RequestParam String newName) {
//        Optional<Country> country = countryRepository.findById(id);
//        if (country.isPresent()) {
//            Country updatedCountry = country.get();
//            updatedCountry.setName(newName);
//            countryRepository.save(updatedCountry);
//            return ResponseEntity.ok("Country name updated successfully.");
//        }
//        return ResponseEntity.status(404).body("Country not found.");
//    }
//
//    @PutMapping("/updateCityName/{id}")
//    @PreAuthorize("hasAnyAuthority('ADMIN','MANAGER')")
//    public ResponseEntity<String> updateCityName(@PathVariable Long id, @RequestParam String newName) {
//        Optional<City> city = cityRepository.findById(id);
//        if (city.isPresent()) {
//            City updatedCity = city.get();
//            updatedCity.setName(newName);
//            cityRepository.save(updatedCity);
//            return ResponseEntity.ok("City name updated successfully.");
//        }
//        return ResponseEntity.status(404).body("City not found.");
//    }
//
//    @PutMapping("/updateDistrictName/{id}")
//    @PreAuthorize("hasAnyAuthority('ADMIN','MANAGER')")
//    public ResponseEntity<String> updateDistrictName(@PathVariable Long id, @RequestParam String newName) {
//        Optional<District> district = districtRepository.findById(id);
//        if (district.isPresent()) {
//            District updatedDistrict = district.get();
//            updatedDistrict.setName(newName);
//            districtRepository.save(updatedDistrict);
//            return ResponseEntity.ok("District name updated successfully.");
//        }
//        return ResponseEntity.status(404).body("District not found.");
//    }
//
//    @DeleteMapping("/deleteCountry/{id}")
//    @PreAuthorize("hasAnyAuthority('ADMIN','MANAGER')")
//    public ResponseEntity<String> deleteCountry(@PathVariable Long id) {
//        if (countryRepository.existsById(id)) {
//            countryRepository.deleteById(id);
//            return ResponseEntity.ok("Country deleted successfully.");
//        }
//        return ResponseEntity.status(404).body("Country not found.");
//    }
//
//    @DeleteMapping("/deleteCity/{id}")
//    @PreAuthorize("hasAnyAuthority('ADMIN','MANAGER')")
//    public ResponseEntity<String> deleteCity(@PathVariable Long id) {
//        if (cityRepository.existsById(id)) {
//            cityRepository.deleteById(id);
//            return ResponseEntity.ok("City deleted successfully.");
//        }
//        return ResponseEntity.status(404).body("City not found.");
//    }
//
//    @DeleteMapping("/deleteDistrict/{id}")
//    @PreAuthorize("hasAnyAuthority('ADMIN','MANAGER')")
//    public ResponseEntity<String> deleteDistrict(@PathVariable Long id) {
//        if (districtRepository.existsById(id)) {
//            districtRepository.deleteById(id);
//            return ResponseEntity.ok("District deleted successfully.");
//        }
//        return ResponseEntity.status(404).body("District not found.");
//    }
//
//    @PostMapping("/addCity")
//    @PreAuthorize("hasAnyAuthority('ADMIN','MANAGER')")
//    public ResponseEntity<String> addCity(@RequestParam String cityName, @RequestParam Long countryId) {
//        Optional<Country> country = countryRepository.findById(countryId);
//        if (country.isPresent()) {
//            City newCity = City.builder()
//                               .name(cityName)
//                               .country(country.get())
//                               .build();
//            cityRepository.save(newCity);
//            return ResponseEntity.ok("City added successfully.");
//        }
//        return ResponseEntity.status(404).body("Country not found.");
//    }
//
//    @PostMapping("/addDistrict")
//    @PreAuthorize("hasAnyAuthority('ADMIN','MANAGER')")
//    public ResponseEntity<String> addDistrict(@RequestParam String districtName, @RequestParam Long cityId) {
//        Optional<City> city = cityRepository.findById(cityId);
//        if (city.isPresent()) {
//            District newDistrict = District.builder()
//                                           .name(districtName)
//                                           .city(city.get())
//                                           .build();
//            districtRepository.save(newDistrict);
//            return ResponseEntity.ok("District added successfully.");
//        }
//        return ResponseEntity.status(404).body("City not found.");
//    }
} 
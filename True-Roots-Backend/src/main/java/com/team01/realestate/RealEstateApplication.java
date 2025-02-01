package com.team01.realestate;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.team01.realestate.entity.concretes.business.City;
import com.team01.realestate.entity.concretes.business.Country;
import com.team01.realestate.entity.concretes.business.District;
import com.team01.realestate.entity.enums.RoleType;
import com.team01.realestate.entity.concretes.user.Role;
import com.team01.realestate.payload.request.user.UserRequest;
import com.team01.realestate.repository.business.CityRepository;
import com.team01.realestate.repository.business.CountryRepository;
import com.team01.realestate.repository.business.DistrictRepository;
import com.team01.realestate.repository.user.RoleRepository;
import com.team01.realestate.service.user.AuthenticationService;
import com.team01.realestate.service.user.RoleService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@SpringBootApplication
public class RealEstateApplication implements CommandLineRunner {

	private final RoleService userRoleService;
	private final RoleRepository userRoleRepository;
	private final AuthenticationService authenticationService;

	public RealEstateApplication(RoleService userRoleService, RoleRepository userRoleRepository, AuthenticationService authenticationService,
								 CountryRepository countryRepository, CityRepository cityRepository, DistrictRepository districtRepository) {
		this.userRoleService = userRoleService;
		this.userRoleRepository = userRoleRepository;
		this.authenticationService = authenticationService;
	}

	public static void main(String[] args) {
		SpringApplication.run(RealEstateApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {

		// Role tablomu dolduracağım ama önce içi boş mu diye kontrol edeceğim
		if (userRoleService.getAllUserRole().isEmpty()) {
			Role admin = new Role();

			admin.setRoleType(RoleType.ADMIN);
			admin.setRoleName("Admin");
			userRoleRepository.save(admin);

			Role manager = new Role();
			manager.setRoleType(RoleType.MANAGER);
			manager.setRoleName("Manager");
			userRoleRepository.save(manager);

			Role customer = new Role();
			customer.setRoleType(RoleType.CUSTOMER);
			customer.setRoleName("Customer");
			userRoleRepository.save(customer);
		}

		// Built_in Admin oluşturuluyor eğer sistemde Admin yoksa
		if (authenticationService.countAllAdmins() == 0) {
			UserRequest adminRequest = new UserRequest();
			adminRequest.setEmail("admin@admin.com");
			adminRequest.setPassword("123456");
			adminRequest.setFirstName("Built in Admin");
			adminRequest.setLastName("Built in Admin");
			adminRequest.setPhone("111-111-1111");
			authenticationService.saveBuiltInAdmin(adminRequest, "Admin");
		}

		// Removed data loading code
	}

}


package com.team01.realestate.service.business;

import com.team01.realestate.entity.concretes.user.User;
import com.team01.realestate.entity.enums.RoleType;
import com.team01.realestate.payload.mapper.UserMapper;
import com.team01.realestate.payload.messages.SuccessMessages;
import com.team01.realestate.payload.response.business.ReportResponse;
import com.team01.realestate.payload.response.business.ResponseMessage;
import com.team01.realestate.payload.response.user.UserResponse;
import com.team01.realestate.repository.business.AdvertRepository;
import com.team01.realestate.repository.business.AdvertTypeRepository;
import com.team01.realestate.repository.business.CategoryRepository;
import com.team01.realestate.repository.business.TourRequestRepository;
import com.team01.realestate.repository.user.UserRepository;
import com.team01.realestate.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReportService {

    private final CategoryRepository categoryRepository;
    private final AdvertRepository advertRepository;
    private final AdvertTypeRepository advertTypeRepository;
    private final TourRequestRepository tourRequestRepository;
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    public ResponseMessage<ReportResponse> getStatistic() {
        int numOfCategory = categoryRepository.findAll().size();
        int numOfAdvert = advertRepository.findAll().size();
        int numOfAdvertType = advertTypeRepository.findAll().size();
        int numOfTourRequest = tourRequestRepository.findAll().size();
        List<User> allUser = userRepository.findAll();

        int numOfCustomer = allUser.stream().
                filter(user -> user.getRoles().stream()
                        .anyMatch(role -> "Customer".equalsIgnoreCase(role.getRoleName())))
                                .toList().size();

        // ReportResponse nesnesini oluştur
        ReportResponse reportResponse = ReportResponse.builder()
                .numberOfCategory(numOfCategory)
                .numberOfAdvert(numOfAdvert)
                .numberOfAdvertType(numOfAdvertType)
                .numberOfTourRequest(numOfTourRequest)
                .numberOfCustomer(numOfCustomer)
                .build();

        return ResponseMessage.<ReportResponse>builder()
                .object(reportResponse)
                .message(SuccessMessages.REPORT_CREATED_SUCCESSFULLY)
                .httpStatus(HttpStatus.OK)
                .build();
    }



































    public Set<UserResponse> getUsersWithRole(String roleName) {
        RoleType roleType = findRoleTypeByName(roleName);

        if (roleType == null) {
            throw new IllegalArgumentException("Invalid role name: " + roleName);
        }

        return userRepository.findUsersQueryForReports(roleType)
                .stream()
                .map(userMapper::mapUserToUserResponse)
                .collect(Collectors.toSet());
    }

    private RoleType findRoleTypeByName(String roleName) {
        for (RoleType roleType : RoleType.values()) {
            if (roleType.getName().equalsIgnoreCase(roleName)) {
                return roleType;
            }
        }
        return null; // Return null if no match is found
    }

}

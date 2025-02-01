package com.team01.realestate.payload.mapper;

import com.team01.realestate.entity.concretes.business.Log;
import com.team01.realestate.entity.concretes.user.Role;
import com.team01.realestate.entity.concretes.user.User;
import com.team01.realestate.payload.request.user.UserRequest;
import com.team01.realestate.payload.request.user.UserRequestWithoutPassword;
import com.team01.realestate.payload.response.business.AdvertResponse;
import com.team01.realestate.payload.response.business.FavoriteResponse;
import com.team01.realestate.payload.response.business.TourRequestResponse;
import com.team01.realestate.payload.response.user.UserResponse;
import com.team01.realestate.payload.response.user.UserResponseWithOtherEntities;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class UserMapper {

    private final AdvertMapper advertMapper;
    private final FavoriteMapper favoriteMapper;
    private final TourRequestMapper tourRequestMapper;

    public UserResponse mapUserToUserResponse(User user){

        return UserResponse.builder()
                .email(user.getEmail())
                .userId(user.getId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .phone(user.getPhone())
                .builtIn(user.getBuiltIn())
                .userRoles(
                        user.getRoles().stream()
                                .map(Role::getRoleName) // Sadece roleName'i al
                                .collect(Collectors.toList())
                )
                .build();
    }

    public UserResponseWithOtherEntities mapUserToUserResponseWithOtherEntities(User user, List<FavoriteResponse> favorites, List<TourRequestResponse> tourRequests, List<Log> logs){

       Set<AdvertResponse> advertResponses = advertMapper.advertToAdvertResponse(user.getAdverts());
        //System.out.println(favorites);

        return UserResponseWithOtherEntities.builder()
                .email(user.getEmail())
                .userId(user.getId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .phone(user.getPhone())
                .userRoles(
                        user.getRoles().stream()
                                .map(Role::getRoleName) // Sadece roleName'i al
                                .collect(Collectors.toList())
                )
                .favorites(favorites)
                .logs(logs)
                .adverts(advertResponses)
                .tourRequests(tourRequests)
                .build();
    }

    public User mapUserRequestToUser(UserRequest userRequestForRegister) {
        return User.builder().email(userRequestForRegister.getEmail())
                .firstName(userRequestForRegister.getFirstName())
                .lastName(userRequestForRegister.getLastName())
                .phone(userRequestForRegister.getPhone())
                .build();
    }

    public User mapUserRequestForAdminToUser(UserRequest userRequest) {
        return User.builder().email(userRequest.getEmail())
                .passwordHash(userRequest.getPassword())
                .firstName(userRequest.getFirstName())
                .lastName(userRequest.getLastName())
                .phone(userRequest.getPhone())
                .build();
    }
}

package com.team01.realestate.service.user;

import com.team01.realestate.entity.concretes.business.Advert;
import com.team01.realestate.entity.concretes.business.Log;
import com.team01.realestate.entity.concretes.business.TourRequest;
import com.team01.realestate.entity.concretes.user.Role;
import com.team01.realestate.entity.concretes.user.User;
import com.team01.realestate.entity.enums.RoleType;
import com.team01.realestate.exception.BadRequestException;
import com.team01.realestate.exception.ConflictException;
import com.team01.realestate.payload.mapper.UserMapper;
import com.team01.realestate.payload.messages.ErrorMessages;
import com.team01.realestate.payload.messages.SuccessMessages;
import com.team01.realestate.payload.request.user.UserRequest;
import com.team01.realestate.payload.request.user.UserRequestWithoutPassword;
import com.team01.realestate.payload.response.business.AdvertResponse;
import com.team01.realestate.payload.response.business.FavoriteResponse;
import com.team01.realestate.payload.response.business.ResponseMessage;
import com.team01.realestate.payload.response.business.TourRequestResponse;
import com.team01.realestate.payload.response.user.UserResponse;
import com.team01.realestate.payload.response.user.UserResponseWithOtherEntities;
import com.team01.realestate.repository.user.UserRepository;
import com.team01.realestate.service.business.FavoriteServiceImpl;
import com.team01.realestate.service.business.LogService;
import com.team01.realestate.service.business.TourRequestService;
import com.team01.realestate.service.helper.MethodHelper;
import com.team01.realestate.service.helper.PageableHelper;
import com.team01.realestate.service.validator.UniquePropertyValidator;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final MethodHelper methodHelper;
    private final UniquePropertyValidator propertyValidator;
    private final PageableHelper pageableHelper;
    private final RoleService roleService;
    private final PasswordEncoder passwordEncoder;
    private final FavoriteServiceImpl favoriteService;
    private final TourRequestService tourRequestService;
    private final LogService logService;

    public ResponseMessage<UserResponse> getAuthenticatedUser(HttpServletRequest httpServletRequest) {
        User foundUser = methodHelper.findAuthenticatedUser(httpServletRequest);

        UserResponse userResponse = userMapper.mapUserToUserResponse(foundUser);

        return ResponseMessage.<UserResponse>builder()
                .message(SuccessMessages.USER_FOUND)
                .httpStatus(HttpStatus.OK)
                .object(userResponse)
                .build();

    }

    @Transactional
    public ResponseMessage<UserResponse> updateAuthenticatedUser(UserRequestWithoutPassword userRequestWithoutPassword, HttpServletRequest httpServletRequest) {
        User foundUser = methodHelper.findAuthenticatedUser(httpServletRequest);

        if (Boolean.TRUE.equals(foundUser.getBuiltIn())) { // null değerleriyle çalışırken güvenli bir yöntemdir. Boolean.TRUE.equals
            throw new BadRequestException(ErrorMessages.NOT_PERMITTED_METHOD_MESSAGE);
        }

        propertyValidator.checkUniqueProperties(foundUser, userRequestWithoutPassword);

        foundUser.setEmail(userRequestWithoutPassword.getEmail());
        foundUser.setFirstName(userRequestWithoutPassword.getFirstName());
        foundUser.setLastName(userRequestWithoutPassword.getLastName());
        foundUser.setPhone(userRequestWithoutPassword.getPhone());
        foundUser.setUpdatedAt(LocalDateTime.now());
        userRepository.save(foundUser);

        UserResponse userResponse = userMapper.mapUserToUserResponse(foundUser);

        return ResponseMessage.<UserResponse>builder()
                .message(SuccessMessages.USER_UPDATE)
                .httpStatus(HttpStatus.OK)
                .object(userResponse)
                .build();
    }

    public ResponseMessage<UserResponse> deleteAuthenticatedUser(HttpServletRequest httpServletRequest) {
        User foundUser = methodHelper.findAuthenticatedUser(httpServletRequest);

        User checkedUser = methodHelper.findUserById(foundUser.getId()); // "is exist" kontrolu.

        userRepository.delete(checkedUser);

        return ResponseMessage.<UserResponse>builder()
                .message(SuccessMessages.USER_DELETE)
                .httpStatus(HttpStatus.OK)
                .build();

    }

    public Page<UserResponse> getUsersByPage(String query, int page, int size, String sort, String type) {
        // Create pageable object using the provided pagination and sorting parameters
        Pageable pageable = pageableHelper.getPageableWithProperties(page, size, sort, type);

        // Fetch paginated list of users matching the query
        Page<User> usersPage = userRepository.findUsersByQuery(query, pageable);

        // Convert User entities to UserResponse DTOs and return the result
        return usersPage.map(userMapper::mapUserToUserResponse);
    }

    @Transactional
    public ResponseMessage<UserResponseWithOtherEntities> getUserById(Long userId) {

        User user = methodHelper.findUserById(userId);
        List<FavoriteResponse> favorites = favoriteService.getFavoritesForUser(userId);
        List<TourRequestResponse> tourRequests = tourRequestService.getTourRequestsByUserId(userId);
        List<Log> logs = logService.getLogsByUserId(userId);

        UserResponseWithOtherEntities userResponse = userMapper.mapUserToUserResponseWithOtherEntities(user, favorites, tourRequests, logs);

        return ResponseMessage.<UserResponseWithOtherEntities>builder().message(SuccessMessages.USER_FOUND).
                httpStatus(HttpStatus.OK).
                object(userResponse).
                build();
    }

    public ResponseMessage<UserResponse> updateUserById(UserRequestWithoutPassword userRequest, Long userId, HttpServletRequest httpServletRequest) {

        User user = methodHelper.findUserById(userId);

        // Metodu tetikleyen kullanıcının rol bilgisi alınıyor
        String email = (String) httpServletRequest.getAttribute("username");
        User requestingUser = userRepository.findByEmail(email); // Güncelleme işlemini talep eden kullanıcı

        if (Boolean.TRUE.equals(user.getBuiltIn())) { // Yerleşik kullanıcılar güncellenemez
            throw new BadRequestException(ErrorMessages.NOT_PERMITTED_METHOD_MESSAGE);
        }

        // MANAGER sadece Customer güncelleyebilir
        if (requestingUser.getRoles().stream().anyMatch(role -> role.getRoleType() == RoleType.MANAGER)) {
            if (user.getRoles().stream().noneMatch(role -> role.getRoleType() == RoleType.CUSTOMER)) {
                throw new BadRequestException(ErrorMessages.NOT_PERMITTED_METHOD_MESSAGE);
            }
        }

        // Email, phone gibi özelliklerin unique olup olmadığını kontrol et
        propertyValidator.checkUniqueProperties(user, userRequest);

        // Kullanıcı temel bilgileri güncelleniyor
        user.setEmail(userRequest.getEmail());
        user.setFirstName(userRequest.getFirstName());
        user.setLastName(userRequest.getLastName());
        user.setPhone(userRequest.getPhone());
        user.setUpdatedAt(LocalDateTime.now());

        // Rol güncelleme işlemi
        if (userRequest.getUserRoles() != null && !userRequest.getUserRoles().isEmpty()) {
            List<Role> rolesToUpdate = userRequest.getUserRoles().stream()
                    .map(roleName -> {
                        RoleType roleType = RoleType.valueOf(roleName.trim().toUpperCase(Locale.ENGLISH)); // Dil ayarı İngilizce
                        return roleService.getRole(roleType);
                    })
                    .collect(Collectors.toList());
            user.setRoles(rolesToUpdate);
        }


        // Kullanıcı kaydediliyor
        userRepository.save(user);

        return ResponseMessage.<UserResponse>builder()
                .message(SuccessMessages.USER_UPDATE_FOR_SUPERUSER)
                .httpStatus(HttpStatus.OK)
                .object(userMapper.mapUserToUserResponse(user))
                .build();
    }


    public ResponseMessage deleteUserById(Long userId, HttpServletRequest httpServletRequest) {

        User user = methodHelper.findUserById(userId);

        // metodu tetikleyen user role bilgisi aliniyor
        User user2 = methodHelper.findAuthenticatedUser(httpServletRequest);

        if (Boolean.TRUE.equals(user.getBuiltIn())) { // null değerleriyle çalışırken güvenli bir yöntemdir. Boolean.TRUE.equals
            throw new BadRequestException(ErrorMessages.NOT_PERMITTED_METHOD_MESSAGE);

        } // MANAGER sadece Customer silebilir
        else if (user2.getRoles().stream().anyMatch(role -> role.getRoleType() == RoleType.MANAGER)) {
            if (user.getRoles().stream().noneMatch(role -> role.getRoleType() == RoleType.CUSTOMER)) {
                throw new BadRequestException(ErrorMessages.NOT_PERMITTED_METHOD_MESSAGE);
            }
        }// ADMIN  Manager ve Customer silebilir

        favoriteService.removeAllFavoritesForAdmin(userId);
        tourRequestService.deleteTourRequestsByUserId(userId);

        userRepository.deleteById(userId);

        return ResponseMessage.<UserResponse>builder()
                .message(SuccessMessages.USER_DELETE)
                .httpStatus(HttpStatus.OK)
                .build();
    }

    public ResponseMessage<UserResponse> createUser(UserRequest userRequest, HttpServletRequest httpServletRequest, String roleName) {
        // Giriş yapan kullanıcının email adresini al
        User adminUser = methodHelper.findAuthenticatedUser(httpServletRequest);

        // Unique property kontrolü
        propertyValidator.checkDuplicate(userRequest.getPhone(), userRequest.getEmail());

        // UserRequest'i User nesnesine dönüştür
        User userToCreate = userMapper.mapUserRequestToUser(userRequest);

        // Varsayılan roller için liste oluştur
        if (userToCreate.getRoles() == null) {
            userToCreate.setRoles(new ArrayList<>());
        }

        // Rol çözümleme
        RoleType roleType;
        try {
            roleType = RoleType.valueOf(roleName.toUpperCase(Locale.ROOT)); // Rolü çözümle
        } catch (IllegalArgumentException e) {
            throw new BadRequestException("Invalid role: " + roleName); // Geçersiz rol hatası
        }

        // Giriş yapan kullanıcının yetkisini kontrol et
        boolean isAdmin = adminUser.getRoles()
                .stream()
                .anyMatch(role -> role.getRoleName().equalsIgnoreCase(RoleType.ADMIN.name()));
        boolean isManager = adminUser.getRoles()
                .stream()
                .anyMatch(role -> role.getRoleName().equalsIgnoreCase(RoleType.MANAGER.name()));

        if (isManager && !isAdmin) {
            // Eğer kullanıcı sadece manager ise
            if (roleType != RoleType.MANAGER && roleType != RoleType.CUSTOMER) {
                throw new BadRequestException("Managers can only assign MANAGER or CUSTOMER roles.");
            }
        } else if (isAdmin) {
            // Eğer kullanıcı admin ise
            if (roleType != RoleType.ADMIN && roleType != RoleType.MANAGER && roleType != RoleType.CUSTOMER) {
                throw new BadRequestException("Admins can only assign ADMIN, MANAGER, or CUSTOMER roles.");
            }
        } else {
            throw new BadRequestException("You do not have the necessary permissions to assign roles.");
        }

        // Kullanıcının mevcut rollerinde bu rolün olup olmadığını kontrol et
        if (userToCreate.getRoles().stream().anyMatch(role -> role.getRoleName().equals(roleType.name()))) {
            throw new ConflictException("This user already has the role: " + roleName);
        }

        // Role ekle
        Role role = roleService.getRole(roleType);
        userToCreate.getRoles().add(role);

        // Kullanıcı özel mi (built-in kontrolü)
        userToCreate.setBuiltIn(false);

        // Şifreyi encode et
        userToCreate.setPasswordHash(passwordEncoder.encode(userRequest.getPassword()));

        userToCreate.setCreatedAt(LocalDateTime.now());

        // Kullanıcıyı kaydet
        User savedUser = userRepository.save(userToCreate);

        // Cevap oluştur ve dön
        return ResponseMessage.<UserResponse>builder()
                .httpStatus(HttpStatus.CREATED)
                .message(SuccessMessages.USER_CREATE)
                .object(userMapper.mapUserToUserResponse(savedUser))
                .build();
    }
    public ResponseMessage<UserResponse> getAdvertsUsersById(Long advertId) {
        Advert foundAdvert = methodHelper.findAdvertById(advertId);
        User foundUsers=foundAdvert.getUser();


        return ResponseMessage.<UserResponse>builder()
                .httpStatus(HttpStatus.OK)
                .message(SuccessMessages.ADVERTS_USER_FOUND)
                .object((userMapper.mapUserToUserResponse(foundUsers)))
                .build();

    }
}

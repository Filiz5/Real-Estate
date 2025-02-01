package com.team01.realestate.service.user;

import com.team01.realestate.entity.concretes.user.Role;
import com.team01.realestate.entity.concretes.user.User;
import com.team01.realestate.entity.enums.RoleType;
import com.team01.realestate.exception.BadRequestException;
import com.team01.realestate.payload.mapper.UserMapper;
import com.team01.realestate.payload.messages.ErrorMessages;
import com.team01.realestate.payload.messages.SuccessMessages;
import com.team01.realestate.payload.request.business.UpdatePasswordRequest;
import com.team01.realestate.payload.request.user.GoogleLoginRequest;
import com.team01.realestate.payload.request.user.LoginRequest;
import com.team01.realestate.payload.request.user.ResetPasswordRequest;
import com.team01.realestate.payload.request.user.UserRequest;
import com.team01.realestate.payload.response.business.ResponseMessage;
import com.team01.realestate.payload.response.user.LoginResponse;
import com.team01.realestate.payload.response.user.UserResponse;
import com.team01.realestate.repository.user.UserRepository;
import com.team01.realestate.security.jwt.JwtUtils;
import com.team01.realestate.security.service.UserDetailsImpl;
import com.team01.realestate.service.helper.MethodHelper;
import com.team01.realestate.service.validator.GoogleTokenValidator;
import com.team01.realestate.service.validator.UniquePropertyValidator;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class AuthenticationService {

    private final JwtUtils jwtUtils;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final UniquePropertyValidator uniquePropertyValidator;
    private final UserMapper userMapper;
    private final RoleService roleService;
    private final EmailService emailService;
    private final MethodHelper methodHelper;

    public ResponseMessage<LoginResponse> authenticateUser(LoginRequest loginRequest) {
        String email = loginRequest.getEmail();
        String password = loginRequest.getPassword();

        Authentication authentication = authenticationManager.
                authenticate(new UsernamePasswordAuthenticationToken(email, password));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        String token = "Bearer " + jwtUtils.generateJwtToken(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        // GrantedAuthority turundeki role yapisini String turune ceviriliyor
        Set<String> roles = userDetails.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toSet());


        // AuthResponse nesnesi olusturuluyor ve gerekli alanlar setleniyor
        LoginResponse loginResponse = LoginResponse.builder()
                .email(userDetails.getEmail())
                .token(token.substring(7))
                .name(userDetails.getName())
                .lastName(userDetails.getLastName())
                .roles(roles)
                .userId(userDetails.getId())
                .build();

        return ResponseMessage.<LoginResponse>builder()
                .message(SuccessMessages.USER_LOGIN)
                .object(loginResponse)
                .httpStatus(HttpStatus.OK).build();

    }

    public ResponseMessage<LoginResponse> googleAuthenticateUser(@Valid GoogleLoginRequest loginRequest) {
        boolean res = GoogleTokenValidator.validateIdToken(loginRequest.getIdToken());
        String email = loginRequest.getEmail();

        System.out.println(email);

        User user = userRepository.findUserByEmail(email);

        System.out.println("User: " + user);

        if (user != null) {

            String token = "Bearer " + jwtUtils.generateTokenFromUsername(user.getEmail());
            // Create LoginResponse
            LoginResponse loginResponse = LoginResponse.builder()
                    .email(user.getEmail())
                    .token(token.substring(7))
                    .name(user.getFirstName())
                    .lastName(user.getLastName())
                    .roles(user.getRoles().stream().map(role ->
                        role.getRoleType().toString()
                    ).collect(Collectors.toSet()))
                    .userId(user.getId())
                    .build();

            System.out.println("LoginResponse: " + loginResponse);

            return ResponseMessage.<LoginResponse>builder()
                    .message(SuccessMessages.USER_LOGIN)
                    .object(loginResponse)
                    .httpStatus(HttpStatus.OK)
                    .build();


        }

        String password = generateRandomPassword();

        Role role = roleService.getRole(RoleType.CUSTOMER);
        Random random = new Random();
        int num = random.nextInt(9000) + 1000;
        String phoneNumber = "000 000 " + num;

        User newUser = User.builder()
                .email(email)
                .firstName(loginRequest.getName())
                .lastName(loginRequest.getLastName())
                .phone(phoneNumber)
                .passwordHash(passwordEncoder.encode(password))
                .builtIn(false)
                .createdAt(LocalDateTime.now())
                .build();

        if (newUser.getRoles() == null) {
            newUser.setRoles(new ArrayList<>());
        }

        newUser.getRoles().add(role);

        User savedUser = userRepository.save(newUser);

        LoginRequest request = LoginRequest.builder().password(password).email(email).build();

        emailService.sendEmailForPasswordChange(email, password);

        return authenticateUser(request);

    }


    public ResponseMessage<UserResponse> register(UserRequest userRequestForRegister) {
        // username - ssn- phoneNumber unique mi kontrolu
        uniquePropertyValidator.checkDuplicate(userRequestForRegister.getEmail(),
                userRequestForRegister.getPhone());
        // DTO --> POJO
        User user = userMapper.mapUserRequestToUser(userRequestForRegister);
        if (user.getRoles() == null) {
            user.setRoles(new ArrayList<>());
        }

        user.setBuiltIn(false);
        // Rol bilgisi setleniyor

        Role role = roleService.getRole(RoleType.CUSTOMER);
        user.getRoles().add(role);

        // password encode ediliyor
        user.setPasswordHash(passwordEncoder.encode(userRequestForRegister.getPassword()));

        user.setCreatedAt(LocalDateTime.now());

        User savedUser = userRepository.save(user);

        return ResponseMessage.<UserResponse>builder()
                .httpStatus(HttpStatus.CREATED)
                .message(SuccessMessages.USER_REGISTER)
                .object(userMapper.mapUserToUserResponse(savedUser))
                .build();

    }

    public ResponseMessage<UserResponse> saveBuiltInAdmin(UserRequest adminRequest, String userRole) {

        uniquePropertyValidator.checkDuplicate(adminRequest.getPhone(), adminRequest.getEmail());

        User user = userMapper.mapUserRequestForAdminToUser(adminRequest);
        // Initialize roles list if null
        if (user.getRoles() == null) {
            user.setRoles(new ArrayList<>());
        }
        // !!! Rol bilgisi setleniyor
        Role role = roleService.getRole(RoleType.ADMIN);
        user.setBuiltIn(true);
        user.getRoles().add(role);
        // !!! password encode ediliyor
        user.setPasswordHash(passwordEncoder.encode(user.getPasswordHash()));

        user.setCreatedAt(LocalDateTime.now()); // Automatically set to create


        User savedUser = userRepository.save(user);

        return ResponseMessage.<UserResponse>builder()
                .message(SuccessMessages.ADMIN_CREATED)
                .object(userMapper.mapUserToUserResponse(savedUser))
                .build();
    }

    public long countAllAdmins() {
        return userRepository.countByRoleType(RoleType.ADMIN);
    }

    public void updateAuthenticatedUsersPassword(UpdatePasswordRequest updatePasswordRequest,
                                                                          HttpServletRequest request) {
        String email = (String) request.getAttribute("username");
        User foundUser = userRepository.findByEmail(email);

        // !!! Builtin attribute: Datalarının Değişmesi istenmeyen bir objenin builtIn değeri true olur.
        if (Boolean.TRUE.equals(foundUser.getBuiltIn())) {
            throw new BadRequestException(ErrorMessages.NOT_PERMITTED_METHOD_MESSAGE);
        }

        // !!! Eski sifre bilgisi dogrumu kontrolu
        if(!passwordEncoder.matches(updatePasswordRequest.getOldPassword(),foundUser.getPasswordHash())) {
            throw new BadRequestException(ErrorMessages.PASSWORD_NOT_MATCHED);
        }

        if (passwordEncoder.matches(updatePasswordRequest.getNewPassword(), foundUser.getPasswordHash())){
            throw new BadRequestException(ErrorMessages.PASSWORD_IS_SAME);
        }
        // !!! yeni sifre hashlenerek Kaydediliyor
        String hashedPassword = passwordEncoder.encode(updatePasswordRequest.getNewPassword());

        foundUser.setPasswordHash(hashedPassword);
        userRepository.save(foundUser);
    }

    public void generateResetCodeAndSendEmail(String email) {
        // Kullanıcıyı e-posta adresine göre bul
        User user = userRepository.findByEmail(email);

        // Kullanıcı varsa bir reset code oluştur ve kaydet
        if (user != null) {
            String resetCode = methodHelper.generateRandomCode();
            //UUID.randomUUID().toString(); // Rastgele UUID oluştur
            user.setResetPasswordCode(resetCode);
            userRepository.save(user); // Kullanıcı kaydını güncelle

            // Kullanıcıya e-posta gönder
            emailService.sendPasswordResetEmail(user.getEmail(), resetCode);
        }else{
            throw new BadRequestException(String.format(ErrorMessages.NOT_FOUND_USER_WITH_MESSAGE, email));
        }

        // Kullanıcı bulunamazsa herhangi bir işlem yapmadan işlem sonlanır
    }

    public void resetPassword(ResetPasswordRequest resetPasswordRequest){
        // Sıfırlama kodu ile kullanıcıyı bul
        User user = userRepository.findByResetPasswordCode(resetPasswordRequest.getCode());

        if (user != null) {
            // Eğer kullanıcı "BuiltIn" ise işlem yapılmasın
            if (Boolean.TRUE.equals(user.getBuiltIn())) {
                throw new BadRequestException(ErrorMessages.NOT_PERMITTED_METHOD_MESSAGE);
            }

            // Yeni şifreyi hash'le
            String hashedPassword = passwordEncoder.encode(resetPasswordRequest.getPassword());

            // Şifreyi güncelle
            user.setPasswordHash(hashedPassword);
            user.setResetPasswordCode(null);  // Sıfırlama kodunu null yap
            userRepository.save(user);  // Güncellenmiş kullanıcıyı kaydet
        } else {
            throw new BadRequestException(ErrorMessages.INVALID_RESET_CODE);  // Kod geçerli değilse hata fırlat
        }
    }


    private String generateRandomPassword() {
        // Characters to include in the password
        String upperCaseLetters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        String lowerCaseLetters = "abcdefghijklmnopqrstuvwxyz";
        String digits = "0123456789";
        String specialCharacters = "!@#$%^&*()-_+=<>?/";

        // Combine all character sets
        String allCharacters = upperCaseLetters + lowerCaseLetters + digits + specialCharacters;

        // Use SecureRandom for cryptographically strong randomness
        SecureRandom random = new SecureRandom();

        // StringBuilder to build the password
        StringBuilder password = new StringBuilder();

        // Ensure the password includes at least one character from each set
        password.append(upperCaseLetters.charAt(random.nextInt(upperCaseLetters.length())));
        password.append(lowerCaseLetters.charAt(random.nextInt(lowerCaseLetters.length())));
        password.append(digits.charAt(random.nextInt(digits.length())));
        password.append(specialCharacters.charAt(random.nextInt(specialCharacters.length())));

        // Fill the rest of the password with random characters from allCharacters
        for (int i = 4; i < 12; i++) {
            password.append(allCharacters.charAt(random.nextInt(allCharacters.length())));
        }

        // Shuffle the password to make it more random
        return password.toString();
    }



}



package com.team01.realestate.controller.user;

import com.team01.realestate.payload.request.user.UserRequest;
import com.team01.realestate.payload.request.user.UserRequestWithoutPassword;
import com.team01.realestate.payload.response.business.ResponseMessage;
import com.team01.realestate.payload.response.user.UserResponse;
import com.team01.realestate.payload.response.user.UserResponseWithOtherEntities;
import com.team01.realestate.service.user.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    // http://localhost:8080/users/auth + GET
    @GetMapping("/auth") // F05
    @PreAuthorize("hasAnyAuthority('ADMIN', 'MANAGER','CUSTOMER')")
    public ResponseMessage<UserResponse> getAuthenticatedUser(HttpServletRequest httpServletRequest){
        return userService.getAuthenticatedUser(httpServletRequest);
    }

    // http://localhost:8080/users/auth + PUT
    @PutMapping("/auth") // F06
    @PreAuthorize("hasAnyAuthority('ADMIN', 'MANAGER','CUSTOMER')")
    public ResponseMessage<UserResponse> updateAuthenticatedUser(@RequestBody @Valid UserRequestWithoutPassword userRequestWithoutPassword,
                                                                 HttpServletRequest httpServletRequest){
        return userService.updateAuthenticatedUser(userRequestWithoutPassword, httpServletRequest);
    }

    // http://localhost:8080/users/auth + DELETE
    @DeleteMapping("/auth") // F08
    @PreAuthorize("hasAnyAuthority('CUSTOMER')")
    public ResponseMessage<UserResponse> deleteAuthenticatedUser(HttpServletRequest httpServletRequest){
        return userService.deleteAuthenticatedUser(httpServletRequest);
    }

    //   Retrieves users based on pagination parameters.
    //   http://localhost:8080/users/admin?q=john&page=0&size=10&sort=createdAt&type=desc + GET
    @GetMapping("/admin") // F09
    @PreAuthorize("hasAnyAuthority('ADMIN', 'MANAGER')")
    public ResponseEntity<Page<UserResponse>> getUsersByPage(
            @RequestParam(value = "q", defaultValue = "") String query,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size,
            @RequestParam(value = "sort", defaultValue = "createdAt") String sort,
            @RequestParam(value = "type", defaultValue = "desc") String type) {

        Page<UserResponse> usersPage = userService.getUsersByPage(query, page, size, sort, type);
        return ResponseEntity.ok(usersPage);
    }

   // http://localhost:8080/users/{userId}/admin + GET
    @GetMapping("/{userId}/admin") //F10
    @PreAuthorize("hasAnyAuthority('ADMIN','MANAGER')")
    public ResponseMessage<UserResponseWithOtherEntities> getUserById(@PathVariable Long userId) {
        return userService.getUserById(userId);
    }

    // http://localhost:8080/users/{userId}/admin + PUT
    @PutMapping("/{userId}/admin") //F11
    @PreAuthorize("hasAnyAuthority('ADMIN','MANAGER')")
    public ResponseMessage<UserResponse> updateUser(@RequestBody @Valid UserRequestWithoutPassword userRequest,
                                                    @PathVariable Long userId,
                                                    HttpServletRequest httpServletRequest){
        return userService.updateUserById(userRequest, userId, httpServletRequest);
    }

    // http://localhost:8080/users/{userId}/admin + DELETE
    @DeleteMapping("/{userId}/admin") //F12
    @PreAuthorize("hasAnyAuthority('ADMIN','MANAGER')")
    public ResponseMessage deleteUser(@PathVariable Long userId,
                                                    HttpServletRequest httpServletRequest){
        return userService.deleteUserById(userId, httpServletRequest);
    }

    @PostMapping("/createUser/{userRole}")
    @PreAuthorize("hasAnyAuthority('ADMIN','MANAGER')")
    public ResponseMessage<UserResponse> createUser(@RequestBody @Valid UserRequest userRequest,
                                                    HttpServletRequest httpServletRequest, @PathVariable String userRole) {
        return userService.createUser(userRequest, httpServletRequest, userRole);
    }

    // http://localhost:8080/users/getUserByAdvertIdAdmin/{advertId} + GET
    @GetMapping("/getUserByAdvertIdAdmin/{advertId}") //isteküzerine
    @PreAuthorize("hasAnyAuthority('ADMIN','MANAGER')")
    public ResponseMessage<UserResponse> getUserByAdvertId(@PathVariable Long advertId) {
        return userService.getAdvertsUsersById(advertId);
    }
    //link düzeltildi

}

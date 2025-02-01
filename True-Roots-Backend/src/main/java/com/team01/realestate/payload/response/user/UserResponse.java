package com.team01.realestate.payload.response.user;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.team01.realestate.entity.concretes.user.Role;
import com.team01.realestate.payload.response.abstracts.BaseUserResponse;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@SuperBuilder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserResponse extends BaseUserResponse {
    private List<String> userRoles; // Sadece rol adlarını tutar

//    public UserResponse(long l, String builtInAdmin, String builtInAdmin1, String mail) {
//    }
}

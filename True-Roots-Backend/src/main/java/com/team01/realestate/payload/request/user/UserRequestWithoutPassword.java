package com.team01.realestate.payload.request.user;

import com.team01.realestate.payload.request.abstracts.AbstractUserRequest;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@SuperBuilder
public class UserRequestWithoutPassword extends AbstractUserRequest {
    private List<String> userRoles;
}

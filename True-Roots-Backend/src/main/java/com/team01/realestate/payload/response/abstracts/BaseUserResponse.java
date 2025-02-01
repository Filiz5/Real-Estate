package com.team01.realestate.payload.response.abstracts;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public abstract class BaseUserResponse {

    private Long userId;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private List<String> roles;
    private boolean builtIn;
}

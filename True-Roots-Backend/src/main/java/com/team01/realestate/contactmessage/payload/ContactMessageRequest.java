package com.team01.realestate.contactmessage.payload;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class ContactMessageRequest {

    @NotNull(message = "Please enter name")
    @Size(min = 2, max = 30, message = "Your name should be at least 3 (max 30) chars")
    @Pattern(regexp = "\\A(?!\\s*\\Z).+", message = "Your firstname must consist of the character .")
    private String firstName;

    @NotNull(message = "Please enter lastname")
    @Size(min = 2, max = 30, message = "Your lastname should be at least 3 (max 30) chars")
    @Pattern(regexp = "\\A(?!\\s*\\Z).+", message = "Your lastname must consist of the character .")
    private String lastName;

    @NotNull(message = "Please enter email")
    @Size(min = 5, max = 60, message = "Your email should be at least 5 (max 60) chars")
    @Email(message = "Please enter valid email")
    private String email;


    @NotNull(message = "Please enter message")
    @Size(min = 4, max = 500, message = "Your message should be at least 4 (max 500) chars")
    @Pattern(regexp = "\\A(?!\\s*\\Z).+", message = "Your message must consist of the character .")
    private String message;

    public ContactMessageRequest(String subject, String body) {
    }
}

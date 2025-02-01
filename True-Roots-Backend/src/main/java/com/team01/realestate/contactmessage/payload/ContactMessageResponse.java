package com.team01.realestate.contactmessage.payload;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.team01.realestate.contactmessage.entity.ContactMessage;
import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class ContactMessageResponse {

    private Long id;
    private String firstName;

    private String lastName;

    private String email;

    private String message;

    private boolean isRead;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime createdAt;
}

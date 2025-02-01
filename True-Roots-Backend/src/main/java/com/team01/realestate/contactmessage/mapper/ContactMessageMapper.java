package com.team01.realestate.contactmessage.mapper;

import com.team01.realestate.contactmessage.entity.ContactMessage;
import com.team01.realestate.contactmessage.payload.ContactMessageRequest;
import com.team01.realestate.contactmessage.payload.ContactMessageResponse;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class ContactMessageMapper {


    public ContactMessage requestToContactMessage(ContactMessageRequest contactMessageRequest) {

        return ContactMessage.builder()
                .firstName(contactMessageRequest.getFirstName())
                .lastName(contactMessageRequest.getLastName())
                .email(contactMessageRequest.getEmail())
                .message(contactMessageRequest.getMessage())
                .createdAt(LocalDateTime.now())
                .build();
    }

    public ContactMessageResponse contactMessageToResponse(ContactMessage savedMessage) {
        return ContactMessageResponse.builder()
                .id(savedMessage.getId())
                .firstName(savedMessage.getFirstName())
                .lastName(savedMessage.getLastName())
                .email(savedMessage.getEmail())
                .message(savedMessage.getMessage())
                .isRead(savedMessage.isRead())
                .createdAt(savedMessage.getCreatedAt())
                .build();

    }
}

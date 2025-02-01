package com.team01.realestate.contactmessage.service;

import com.team01.realestate.contactmessage.entity.ContactMessage;
import com.team01.realestate.contactmessage.mapper.ContactMessageMapper;
import com.team01.realestate.contactmessage.message.MessagesForContactMes;
import com.team01.realestate.contactmessage.payload.ContactMessageRequest;
import com.team01.realestate.contactmessage.payload.ContactMessageResponse;
import com.team01.realestate.contactmessage.repository.ContactMessageRepository;
import com.team01.realestate.exception.ResourceNotFoundException;
import com.team01.realestate.payload.response.business.ResponseMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class ContactMessageService {
    private final ContactMessageRepository contactMessageRepository;
    private final ContactMessageMapper contactMessageMapper;

    public ResponseMessage<ContactMessageResponse> createContactMessage(ContactMessageRequest contactMessageRequest) {
        ContactMessage contactMessage = contactMessageMapper.requestToContactMessage(contactMessageRequest);
        contactMessage.setRead(false);
        ContactMessage savedMessage = contactMessageRepository.save(contactMessage);

        return ResponseMessage.<ContactMessageResponse>builder()
                .message(MessagesForContactMes.CONTACT_MESSAGE_CREATED_SUCCESSFULLY)
                .object(contactMessageMapper.contactMessageToResponse(savedMessage))
                .httpStatus(HttpStatus.OK)
                .build();

    }

    public Page<ContactMessageResponse> getAllContactMessages(String query, int page, int size, String sort, String type) {
        // Sayfalama ve sıralama yapılandırması
        Pageable pageable = PageRequest.of(page, size, Sort.by(sort).ascending());
        if (Objects.equals(type, "desc")) {
            pageable = PageRequest.of(page, size, Sort.by(sort).descending());
        }

        // Eğer bir query varsa, mesaj ismine göre arama yap
        if (query != null && !query.isEmpty()) {
            return contactMessageRepository.findByQuery(query, pageable)
                    .map(contactMessageMapper::contactMessageToResponse);
        }

        // Eğer query yoksa, tüm mesajları getir
        return contactMessageRepository.findAll(pageable)
                .map(contactMessageMapper::contactMessageToResponse);
    }

    public ResponseMessage<ContactMessageResponse> getContactMessagesById(Long messageId) {
        ContactMessage foundContactMessage = contactMessageRepository.findById(messageId)
                .orElseThrow(() -> new ResourceNotFoundException(String.format(MessagesForContactMes.NOT_FOUND_CONTACT_MESSAGE, messageId)));

        return ResponseMessage.<ContactMessageResponse>builder()
                .object(contactMessageMapper.contactMessageToResponse(foundContactMessage))
                .httpStatus(HttpStatus.OK)
                .message(String.format(MessagesForContactMes.CONTACT_MESSAGE_FOUND, messageId))
                .build();
    }

    public ResponseMessage<ContactMessageResponse> deleteContactMessagesById(Long messageId) {
        ContactMessage contactMessage = contactMessageRepository.findById(messageId)
                        .orElseThrow(()->new ResourceNotFoundException(String.format(MessagesForContactMes.NOT_FOUND_CONTACT_MESSAGE, messageId)));
        contactMessageRepository.deleteById(messageId);

        return ResponseMessage.<ContactMessageResponse>builder()
                .message(String.format(MessagesForContactMes.DELETED_CONTACT_MESSAGE,messageId))
                .httpStatus(HttpStatus.OK) // 201
                .object(contactMessageMapper.contactMessageToResponse(contactMessage))//donusumu yapacak kisim. pojo to dto
                .build();

    }

    public ResponseMessage<ContactMessageResponse> readContactMessageById(Long messageId) {
        ContactMessage foundContactMessage = contactMessageRepository.findById(messageId)
                .orElseThrow(()->new ResourceNotFoundException(String.format(MessagesForContactMes.NOT_FOUND_CONTACT_MESSAGE, messageId)));

        foundContactMessage.setRead(true);
        contactMessageRepository.save(foundContactMessage);

        return ResponseMessage.<ContactMessageResponse>builder()
                .object(contactMessageMapper.contactMessageToResponse(foundContactMessage))
                .httpStatus(HttpStatus.OK)
                .message(String.format(MessagesForContactMes.CONTACT_MESSAGE_READ, messageId))
                .build();


    }

    public ResponseMessage<ContactMessageResponse> unreadContactMessageById(Long messageId) {
        ContactMessage foundContactMessage = contactMessageRepository.findById(messageId)
                .orElseThrow(()->new ResourceNotFoundException(String.format(MessagesForContactMes.NOT_FOUND_CONTACT_MESSAGE, messageId)));

        foundContactMessage.setRead(false);
        contactMessageRepository.save(foundContactMessage);

        return ResponseMessage.<ContactMessageResponse>builder()
                .object(contactMessageMapper.contactMessageToResponse(foundContactMessage))
                .httpStatus(HttpStatus.OK)
                .message(String.format(MessagesForContactMes.CONTACT_MESSAGE_UNREAD, messageId))
                .build();
    }
}

package com.team01.realestate.contactmessage.controller;

import com.team01.realestate.contactmessage.payload.ContactMessageRequest;
import com.team01.realestate.contactmessage.payload.ContactMessageResponse;
import com.team01.realestate.contactmessage.service.ContactMessageService;
import com.team01.realestate.payload.response.business.ResponseMessage;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/contact-messages")
@RequiredArgsConstructor
public class ContactMessageController {

    private final ContactMessageService contactMessageService;

    //create a contact message
    //http://localhost:8080/contact-messages/create + POST + JSON
    @PostMapping("/create") //J01
    //postmapping içerisine ne yzaılacak detaylı kontrol sağlanacak
    public ResponseMessage<ContactMessageResponse> createContactMessage(@RequestBody @Valid ContactMessageRequest contactMessageRequest){
        return contactMessageService.createContactMessage(contactMessageRequest);
    }

    @GetMapping("/get") //J02
    // http://localhost:8080/contact-messages/get?q=&page=0&size=10&sort=createdAt&type=asc
    //getmapping içerisine ne yazılacak detaylı kontrol sağlanacak
    @PreAuthorize("hasAnyAuthority('ADMIN','MANAGER')")
    public Page<ContactMessageResponse> getAllContactMessages(
            @RequestParam(value = "q", defaultValue = "") String query,
            @RequestParam(value = "page",defaultValue = "0") int page,
            @RequestParam(value = "size",defaultValue = "20") int size,
            @RequestParam(value = "sort", defaultValue = "createdAt") String sort,
            @RequestParam(value = "type", defaultValue = "asc") String type){
        return contactMessageService.getAllContactMessages(query,page,size,sort,type);
    }

    //http://localhost:8080/contact-messages/getContactMessageById/{messageId}
    @GetMapping("/getContactMessageById/{messageId}")
    @PreAuthorize("hasAnyAuthority('ADMIN','MANAGER')")
    public ResponseMessage<ContactMessageResponse> getContactMessageById(@PathVariable Long messageId){
        return contactMessageService.getContactMessagesById(messageId);
    }

    //http://localhost:8080/contact-messages/deleteContactMessageById/{messageId}
    @DeleteMapping("/deleteContactMessageById/{messageId}")
    @PreAuthorize("hasAnyAuthority('ADMIN','MANAGER')")
    public ResponseMessage<ContactMessageResponse> deleteContactMessageById(@PathVariable Long messageId){
        return contactMessageService.deleteContactMessagesById(messageId);
    }

    //http://localhost:8080/contact-messages/readMessageById/{messageId}
    @PatchMapping("/readMessageById/{messageId}")
    @PreAuthorize("hasAnyAuthority('ADMIN','MANAGER')")
    public ResponseMessage<ContactMessageResponse> readMessageById(@PathVariable Long messageId){
        return contactMessageService.readContactMessageById(messageId);
    }

    //http://localhost:8080/contact-messages/unreadMessageById/{messageId}
    @PatchMapping("/unreadMessageById/{messageId}")
    @PreAuthorize("hasAnyAuthority('ADMIN','MANAGER')")
    public ResponseMessage<ContactMessageResponse> unreadMessageById(@PathVariable Long messageId){
        return contactMessageService.unreadContactMessageById(messageId);
    }



}

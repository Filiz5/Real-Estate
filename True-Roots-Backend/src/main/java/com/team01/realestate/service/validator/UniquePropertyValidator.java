package com.team01.realestate.service.validator;

import com.team01.realestate.entity.concretes.user.User;
import com.team01.realestate.exception.ConflictException;
import com.team01.realestate.payload.messages.ErrorMessages;
import com.team01.realestate.payload.request.abstracts.AbstractUserRequest;
import com.team01.realestate.payload.request.user.UserRequest;
import com.team01.realestate.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UniquePropertyValidator {

    private final UserRepository userRepository;

    public void checkDuplicate(String email, String phone) {

            // Telefon ve e-posta var mı kontrol et
            if (userRepository.existsByPhone(phone)) {
                throw new ConflictException(String.format(ErrorMessages.ALREADY_REGISTER_MESSAGE_PHONE, phone));
            }

            if (userRepository.existsByEmail(email)) {
                throw new ConflictException(String.format(ErrorMessages.ALREADY_REGISTER_MESSAGE_EMAIL, email));
            }
    }




    public void checkUniqueProperties(User user, AbstractUserRequest userRequest){
        String updatedPhone = "";
        String updatedEmail = "";
        boolean isChanged = false;
        if(!user.getPhone().equalsIgnoreCase(userRequest.getPhone())){
            updatedPhone = userRequest.getPhone();
            isChanged = true;
        }
        if(!user.getEmail().equalsIgnoreCase(userRequest.getEmail())){
            updatedEmail = userRequest.getEmail();
            isChanged = true;
        }

        if(isChanged) {
            checkDuplicate(updatedEmail, updatedPhone);
        }
    }
}

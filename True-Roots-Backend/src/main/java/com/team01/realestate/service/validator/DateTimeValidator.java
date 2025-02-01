package com.team01.realestate.service.validator;

import com.team01.realestate.exception.BadRequestException;
import com.team01.realestate.payload.messages.ErrorMessages;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class DateTimeValidator {
    private boolean checkTime(LocalDateTime start, LocalDateTime stop){

        return start.isAfter(stop) || start.equals(stop);

    }
    public void checkTimeWithException(LocalDateTime start, LocalDateTime stop){

        if (checkTime(start,stop)){
            throw new BadRequestException(ErrorMessages.TIME_NOT_VALID_MESSAGE);
        }

    }
}

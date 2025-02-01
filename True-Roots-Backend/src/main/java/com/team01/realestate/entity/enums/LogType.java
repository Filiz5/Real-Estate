package com.team01.realestate.entity.enums;

public enum LogType {
    CREATED,  // Advert is created and waits for approval
    UPDATED,  // Advert is updated
    DELETED,  // Advert is deleted
    DECLINED, // Advert is declined by manager
    TOUR_REQUEST_CREATED,  // Tour request is created
    TOUR_REQUEST_ACCEPTED,  // Tour request is accepted
    TOUR_REQUEST_DECLINED,  // Tour request is declined
    TOUR_REQUEST_CANCELED,  // Tour request is canceled
    TOUR_REQUEST_DELETED,  // Tour request is canceled

    ERROR
}

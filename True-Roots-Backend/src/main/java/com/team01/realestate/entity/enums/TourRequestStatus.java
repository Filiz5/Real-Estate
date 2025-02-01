package com.team01.realestate.entity.enums;
import lombok.Getter;
@Getter
public enum TourRequestStatus {

        PENDING(0, "Pending"),
        APPROVED(1, "Approved"),
        DECLINED(2, "Declined"),
        CANCELED(3, "Canceled");

    private final int value;
    private final String description;

    TourRequestStatus(int value, String description) {
        this.value = value;
        this.description = description;
    }

    public static TourRequestStatus fromValue(int value) {
        for (TourRequestStatus tourRequestStatus : TourRequestStatus.values()) {
            if (tourRequestStatus.value == value) {
                return tourRequestStatus;
            }
        }
        throw new IllegalArgumentException("Invalid value for Status: " + value);
    }
}

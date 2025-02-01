package com.team01.realestate.entity.enums;

import lombok.Getter;

@Getter
public enum AdvertStatus {
    PENDING(0, "Pending"),
    ACTIVATED(1, "Activated"),
    REJECTED(2, "Rejected");

    private final int value;
    private final String description;

    AdvertStatus(int value, String description) {
        this.value = value;
        this.description = description;
    }

    public static AdvertStatus fromValue(int value) {
        for (AdvertStatus advertStatus : AdvertStatus.values()) {
            if (advertStatus.value == value) {
                return advertStatus;
            }
        }
        throw new IllegalArgumentException("Invalid value for Status: " + value);
    }
}

package com.team01.realestate.entity.enums;

import lombok.Getter;

@Getter
public enum     RoleType {

    ADMIN("Admin"),
    MANAGER("Manager"),
    CUSTOMER("Customer");

    public final String name;

    RoleType(String roleName) {
        this.name = roleName;
    }

}

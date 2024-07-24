package com.malltail.erp.member;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum Role{
    ADMIN("ADMIN"),
    USER("USER");

    private String value;
}

package com.hallo.contact.bean;

import com.fasterxml.jackson.annotation.JsonValue;

/**
 * 
 * @author
 * @date
 */
public enum ContactGroupType {
    FRIEND("friend"),
    GROUP("group");

    private String type;

    ContactGroupType(String type) {
        this.type = type;
    }

    @JsonValue
    public String getType() {
        return type;
    }
}

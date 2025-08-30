package com.hallo.fw.util.constant;

/**
 * 
 * @author
 * @date
 */
public enum Message {
    No_Such_User("No_Such_User");

    private String id;

    private Message(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }
}

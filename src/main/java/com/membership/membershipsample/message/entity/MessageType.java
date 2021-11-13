package com.membership.membershipsample.message.entity;

public enum MessageType {
    JOIN_VALIDATION(0),
    PASSWORD_MODIFICATION(1);

    private int messageType;

    MessageType(int messageType) {
        this.messageType = messageType;
    }
}

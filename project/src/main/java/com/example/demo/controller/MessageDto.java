package com.example.demo.controller;

public class MessageDto {
    private final String message;

    public MessageDto(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}

package com.casestudy.service;

public class FirstAndLastNameNotEmptyException extends Throwable {
    public FirstAndLastNameNotEmptyException(String message) {
        super(message);
    }
}

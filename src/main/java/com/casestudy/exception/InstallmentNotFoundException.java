package com.casestudy.exception;

public class InstallmentNotFoundException extends RuntimeException{
    public InstallmentNotFoundException(String message) {
        super(message);
    }
}

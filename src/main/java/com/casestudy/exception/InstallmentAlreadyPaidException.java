package com.casestudy.exception;

public class InstallmentAlreadyPaidException extends RuntimeException {
    public InstallmentAlreadyPaidException(String message) {
        super(message);
    }
}

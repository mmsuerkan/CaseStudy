package com.casestudy.exception;

public class AmountLessThanInstallmentException extends Exception {
    public AmountLessThanInstallmentException(String message) {
        super(message);
    }
}

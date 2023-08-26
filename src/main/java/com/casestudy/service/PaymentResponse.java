package com.casestudy.service;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@AllArgsConstructor
@RequiredArgsConstructor
@Getter
@Setter
public class PaymentResponse {
    private Long creditId;
    private Long userId;
    private Long installmentId;
    private String InstallmentStatus;
}

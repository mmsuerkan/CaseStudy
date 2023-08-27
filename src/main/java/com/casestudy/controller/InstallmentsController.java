package com.casestudy.controller;

import com.casestudy.dto.installment.PayInstallmentRequest;
import com.casestudy.exception.AmountLessThanInstallmentException;
import com.casestudy.service.InstallmentService;
import com.casestudy.dto.PaymentResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/installments")
@RequiredArgsConstructor
public class InstallmentsController {

    private final InstallmentService installmentService;

    @PostMapping("/pay")
    public ResponseEntity<PaymentResponse> payInstallments(@RequestBody PayInstallmentRequest request) throws AmountLessThanInstallmentException {
        PaymentResponse paymentResponse = installmentService.payInstallment(request);
        return ResponseEntity.ok().body(paymentResponse);
    }
}

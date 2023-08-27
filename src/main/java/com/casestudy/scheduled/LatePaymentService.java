package com.casestudy.scheduled;

import com.casestudy.service.CreditService;
import com.casestudy.service.InstallmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LatePaymentService {

    private final CreditService creditService;
    private final InstallmentService installmentService;


    final void calculateLatePaymentInterest(Double interestRate) {

        creditService.findAllCredits().forEach(credit -> {
            installmentService.findAllByCreditId(Long.valueOf(credit.getId())).forEach(installment -> {
                installmentService.calculateLatePaymentInterest(installment,interestRate);
            });
        });

    }
}

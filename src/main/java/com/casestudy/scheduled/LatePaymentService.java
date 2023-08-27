package com.casestudy.scheduled;

import com.casestudy.service.CreditService;
import com.casestudy.service.InstallmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LatePaymentService {
    //kredilerin taksitleri gecikme faizi eklenerek güncellenir

    private final CreditService creditService;
    private final InstallmentService installmentService;

    //her gün kontrol edilerek, son ödeme tarihi geçmiş taksitlerin faizleri hesaplanır ve güncellenir
    final void calculateLatePaymentInterest(Double interestRate) {

        //kredilerin taksitleri gecikme faizi eklenerek güncellenir
        creditService.findAllCredits().forEach(credit -> {
            installmentService.findAllByCreditId(Long.valueOf(credit.getId())).forEach(installment -> {
                installmentService.calculateLatePaymentInterest(installment,interestRate);
            });
        });

    }
}

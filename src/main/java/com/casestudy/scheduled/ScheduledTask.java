package com.casestudy.scheduled;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ScheduledTask {

    private final LatePaymentService latePaymentService;
    private double interestRate = 0.02;

    @Scheduled(fixedRate = 1000 * 60 * 60 * 24 ) // Her gün
    public void runLatePaymentCalculation() {

        latePaymentService.calculateLatePaymentInterest(interestRate);
    }
}

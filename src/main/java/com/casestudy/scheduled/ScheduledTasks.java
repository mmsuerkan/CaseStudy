package com.casestudy.scheduled;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ScheduledTasks {


   // private final LatePaymentService latePaymentService;

    @Scheduled(fixedRate = 1000 * 60 * 60) // Her saat başında çalışır
    public void runLatePaymentCalculation() {
        System.out.println("Late payment calculation is running");
        //latePaymentService.calculateLatePaymentInterest();
    }
}

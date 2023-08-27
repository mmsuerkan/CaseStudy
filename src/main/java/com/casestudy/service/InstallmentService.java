package com.casestudy.service;

import com.casestudy.dto.PaymentResponse;
import com.casestudy.dto.installment.PayInstallmentRequest;
import com.casestudy.enums.InstallmentStatus;
import com.casestudy.exception.*;
import com.casestudy.model.Credit;
import com.casestudy.model.Installment;
import com.casestudy.model.User;
import com.casestudy.repository.CreditRepository;
import com.casestudy.repository.InstallmentRepository;
import com.casestudy.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;


@Service
@RequiredArgsConstructor
public class InstallmentService {

    private final UserRepository userRepository;
    private final CreditRepository creditRepository;
    private final InstallmentRepository installmentRepository;

    private static final Logger logger = LoggerFactory.getLogger(InstallmentService.class);
    public PaymentResponse payInstallment(PayInstallmentRequest request) throws AmountLessThanInstallmentException {
        User user = userRepository.findById(request.getUserId().longValue())
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        Credit credit = creditRepository.findByIdAndUser(request.getCreditId(), user)
                .orElseThrow(() -> new CreditNotFoundException("Credit not found"));

        Installment installment = installmentRepository.findByIdAndCredit(request.getInstallmentId(), credit)
                .orElseThrow(() -> new InstallmentNotFoundException("Installment not found"));

        if (installment.getStatus() != InstallmentStatus.ACTIVE.ordinal()) {
            logger.error("Installment has already been paid");
            throw new InstallmentAlreadyPaidException("Installment has already been paid");
        }
        if(request.getAmount().compareTo(installment.getAmount().longValue()) < 0){
            logger.error("Amount is less than installment amount");
            throw new AmountLessThanInstallmentException("Amount is less than installment amount");
        }

        installment.setStatus(InstallmentStatus.CLOSED.ordinal());
        installmentRepository.save(installment);

        credit.setAmount(credit.getAmount().subtract(BigDecimal.valueOf(installment.getAmount().longValue())));
        creditRepository.save(credit);


        return new PaymentResponse(
                credit.getId().longValue(),
                user.getId().longValue(),
                installment.getId().longValue(),
                installment.getStatus() == 0 ? "PAID" : "NOT PAID"
        );
    }
}

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
import org.springframework.cache.annotation.CachePut;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;


@Service
@RequiredArgsConstructor
public class InstallmentService {

    private final UserRepository userRepository;
    private final CreditRepository creditRepository;
    private final InstallmentRepository installmentRepository;

    private static final Logger logger = LoggerFactory.getLogger(InstallmentService.class);
    private User user;
    private Credit credit;
    private Installment installment;
    private Integer creditId;

    @CachePut(value = "myCache", key = "#creditId")
    public PaymentResponse payInstallment(PayInstallmentRequest request) throws AmountLessThanInstallmentException {
        creditId = request.getCreditId();

        verifyUserCreditAndInstallment(request);

        updateInstallmentAndSave();
        updateCreditAmountAndSave();

        return new PaymentResponse(
                credit.getId().longValue(),
                user.getId().longValue(),
                installment.getId().longValue(),
                installment.getStatus() == 0 ? "PAID" : "NOT PAID"
        );
    }

    private void verifyUserCreditAndInstallment(PayInstallmentRequest request) throws AmountLessThanInstallmentException {
        if(userRepository.findById(request.getUserId().longValue()).isPresent()){
            user = userRepository.findById(request.getUserId().longValue()).get();
        }else{
            logger.error("User not found");
            throw new UserNotFoundException("User not found");
        }

        if(creditRepository.findByIdAndUser(request.getCreditId(), user).isPresent()){
            credit = creditRepository.findByIdAndUser(request.getCreditId(), user).get();
        }else{
            logger.error("Credit not found");
            throw new CreditNotFoundException("Credit not found");
        }

        if(installmentRepository.findByIdAndCredit(request.getInstallmentId(), credit).isPresent()){
            installment = installmentRepository.findByIdAndCredit(request.getInstallmentId(), credit).get();
        }else{
            logger.error("Installment not found");
            throw new InstallmentNotFoundException("Installment not found");
        }

        if (installment.getStatus() != InstallmentStatus.ACTIVE.ordinal()) {
            logger.error("Installment has already been paid");
            throw new InstallmentAlreadyPaidException("Installment has already been paid");
        }
        if(request.getAmount().compareTo(installment.getAmount().longValue()) < 0){
            logger.error("Amount is less than installment amount");
            throw new AmountLessThanInstallmentException("Amount is less than installment amount");
        }
    }

    private void updateCreditAmountAndSave() {
        credit.setAmount(credit.getAmount().subtract(BigDecimal.valueOf(installment.getAmount().longValue())));
        creditRepository.save(credit);
    }

    private void updateInstallmentAndSave() {
        installment.setStatus(InstallmentStatus.CLOSED.ordinal());
        installmentRepository.save(installment);
    }
}

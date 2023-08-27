package com.casestudy.service;

import com.casestudy.dto.PaymentResponse;
import com.casestudy.dto.installment.PayInstallmentRequest;
import com.casestudy.enums.CreditStatus;
import com.casestudy.enums.InstallmentStatus;
import com.casestudy.exception.AmountLessThanInstallmentException;
import com.casestudy.model.Credit;
import com.casestudy.model.Installment;
import com.casestudy.model.User;
import com.casestudy.repository.CreditRepository;
import com.casestudy.repository.InstallmentRepository;
import com.casestudy.repository.UserRepository;
import com.casestudy.service.InstallmentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
@ExtendWith(MockitoExtension.class)
public class InstallmentServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private CreditRepository creditRepository;

    @Mock
    private InstallmentRepository installmentRepository;

    @InjectMocks
    private InstallmentService installmentService;

/*
    @Test
    public void testPayInstallment_Success() throws AmountLessThanInstallmentException {
        User user = new User();
        user.setId(1);

        Credit credit = new Credit();
        credit.setId(1);
        credit.setStatus(CreditStatus.ACTIVE.ordinal());
        credit.setAmount(BigDecimal.valueOf(1000));
        credit.setUser(user);

        Installment installment = new Installment();
        installment.setId(1);
        installment.setStatus(InstallmentStatus.ACTIVE.ordinal());
        installment.setAmount(BigDecimal.valueOf(200));
        installment.setCredit(credit);

        given(userRepository.findById(anyLong())).willReturn(Optional.of(user));
        given(creditRepository.findByIdAndUser(anyInt(), any(User.class))).willReturn(Optional.of(credit));
        given(installmentRepository.findByIdAndCredit(anyInt(), any(Credit.class))).willReturn(Optional.of(installment));

        PayInstallmentRequest request = new PayInstallmentRequest();
        request.setUserId(1);
        request.setCreditId(1);
        request.setInstallmentId(1);
        request.setAmount(BigDecimal.valueOf(200).longValue());

        PaymentResponse response = installmentService.payInstallment(request);

        assertEquals(1L, response.getCreditId().longValue());
        assertEquals(1L, response.getUserId().longValue());
        assertEquals(1L, response.getInstallmentId().longValue());

        verify(installmentRepository).save(installment);
        verify(creditRepository).save(credit);
    }
    */
}

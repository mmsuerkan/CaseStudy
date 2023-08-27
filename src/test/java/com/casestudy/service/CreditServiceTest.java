package com.casestudy.service;

import com.casestudy.dto.credit.CreditRequestDto;
import com.casestudy.dto.credit.CreditResponseDto;
import com.casestudy.enums.CreditStatus;
import com.casestudy.model.Credit;
import com.casestudy.model.User;
import com.casestudy.repository.CreditRepository;
import com.casestudy.repository.InstallmentRepository;
import com.casestudy.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CreditServiceTest {

    @InjectMocks
    private CreditService creditService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private CreditRepository creditRepository;

    @Mock
    private InstallmentRepository installmentRepository;


    @Test
    public void testCreateCredit() {
        User user = new User();
        user.setId(1);

        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));

        CreditRequestDto requestDto = new CreditRequestDto();
        requestDto.setUserId(1);
        requestDto.setAmount(BigDecimal.valueOf(1000));
        requestDto.setInstallmentCount(3);

        Credit savedCredit = new Credit();
        savedCredit.setId(1);
        savedCredit.setAmount(requestDto.getAmount());
        savedCredit.setInstallments(new ArrayList<>());

        when(creditRepository.save(any(Credit.class))).thenReturn(savedCredit);

        CreditResponseDto responseDto = creditService.createCredit(requestDto);

        assertEquals(1L, responseDto.getCreditId().longValue());
        assertEquals("ACTIVE", responseDto.getStatus());
        assertEquals(requestDto.getAmount(), responseDto.getAmount());
        assertEquals(3, responseDto.getInstallments().size());
    }

    @Test
    public void testListCredits() {
        User user = new User();
        user.setId(1);

        Credit credit = new Credit();
        credit.setId(1);
        credit.setStatus(CreditStatus.ACTIVE.ordinal());
        credit.setAmount(BigDecimal.valueOf(1000));
        credit.setUser(user);
        credit.setInstallments(new ArrayList<>());

        List<Credit> credits = new ArrayList<>();
        credits.add(credit);

        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
        when(creditRepository.findByUserId(anyInt())).thenReturn(Optional.of(credits));

        List<CreditResponseDto> responseDtos = creditService.listCredits(1);

        assertEquals(1, responseDtos.size());
        assertEquals(1L, responseDtos.get(0).getCreditId().longValue());
        assertEquals("ACTIVE", responseDtos.get(0).getStatus());
        assertEquals(BigDecimal.valueOf(1000), responseDtos.get(0).getAmount());
        assertEquals(0, responseDtos.get(0).getInstallments().size());
    }

    @Test
    public void testListFilteredAndPaginatedCredits() {
        LocalDate date = LocalDate.of(2023, 8, 27);

        Credit credit = new Credit();
        credit.setId(1);
        credit.setStatus(CreditStatus.ACTIVE.ordinal());
        credit.setAmount(BigDecimal.valueOf(1000));
        credit.setInstallments(new ArrayList<>());

        List<Credit> credits = new ArrayList<>();
        credits.add(credit);

        when(creditRepository.findByStatusAndCreatedAtGreaterThanEqual(anyInt(), any(LocalDate.class)))
                .thenReturn(credits);

        List<CreditResponseDto> responseDtos = creditService.listFilteredAndPaginatedCredits("ACTIVE", date);

        assertEquals(1, responseDtos.size());
        assertEquals(1L, responseDtos.get(0).getCreditId().longValue());
        assertEquals("ACTIVE", responseDtos.get(0).getStatus());
        assertEquals(BigDecimal.valueOf(1000), responseDtos.get(0).getAmount());
        assertEquals(0, responseDtos.get(0).getInstallments().size());
    }
}

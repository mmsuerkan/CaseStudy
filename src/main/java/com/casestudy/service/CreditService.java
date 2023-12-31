package com.casestudy.service;

import com.casestudy.dto.credit.CreditRequestDto;
import com.casestudy.dto.credit.CreditResponseDto;
import com.casestudy.dto.installment.InstallmentResponse;
import com.casestudy.enums.CreditStatus;
import com.casestudy.enums.InstallmentStatus;
import com.casestudy.repository.CreditRepository;
import com.casestudy.repository.InstallmentRepository;
import com.casestudy.exception.UserNotFoundException;
import com.casestudy.model.Credit;
import com.casestudy.model.Installment;
import com.casestudy.model.User;
import com.casestudy.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CreditService {

    private final UserRepository userRepository;
    private final CreditRepository creditRepository;
    private final InstallmentRepository installmentRepository;

    private static final Logger logger = LoggerFactory.getLogger(CreditService.class);

    public CreditResponseDto createCredit(CreditRequestDto creditRequest) {

        Optional<User> user = userRepository.findById(Long.valueOf(creditRequest.getUserId()));

        if(user.isEmpty()){
            logger.atError().log("User not found");
            throw new UserNotFoundException("User not found");
        }

        Credit credit = new Credit();
        credit.setStatus(CreditStatus.ACTIVE.ordinal());
        credit.setAmount(creditRequest.getAmount());
        credit.setCreatedAt(LocalDate.now());
        credit.setUser(user.get());

        Credit savedCredit = creditRepository.save(credit);

        List<Installment> installments = generateInstallments(savedCredit, creditRequest.getInstallmentCount());

        return buildCreditResponse(savedCredit, installments);
    }

    private List<Installment> generateInstallments(Credit credit, int installmentCount) {
        BigDecimal installmentAmount = credit.getAmount().divide(BigDecimal.valueOf(installmentCount), 2, RoundingMode.HALF_UP);
        LocalDate dueDate = LocalDate.now().plusDays(30);

        List<Installment> installments = new ArrayList<>();

        for (int i = 0; i < installmentCount; i++) {

            dueDate = checkWeekendDay(dueDate);

            Installment installment = createActiveInstallment(credit, installmentAmount, dueDate);

            installmentRepository.save(installment);

            dueDate = dueDate.plusDays(30);
            installments.add(installment);
        }
        return installments;
    }

    private static LocalDate checkWeekendDay(LocalDate dueDate) {
        if (dueDate.getDayOfWeek() == DayOfWeek.SATURDAY) {
            dueDate = dueDate.plusDays(2);
        }

        if (dueDate.getDayOfWeek() == DayOfWeek.SUNDAY) {
            dueDate = dueDate.plusDays(1);
        }
        return dueDate;
    }

    private static Installment createActiveInstallment(Credit credit, BigDecimal installmentAmount, LocalDate dueDate) {
        Installment installment = new Installment();
        installment.setAmount(installmentAmount);
        installment.setStatus(InstallmentStatus.ACTIVE.ordinal());
        installment.setDueDate(dueDate);
        installment.setCredit(credit);
        return installment;
    }

    private CreditResponseDto buildCreditResponse(Credit credit, List<Installment> installments) {
        CreditResponseDto response = new CreditResponseDto();
        response.setCreditId(credit.getId());
        response.setStatus(CreditStatus.ACTIVE.name());
        response.setAmount(credit.getAmount());

        mapInstallments(response, installments);

        return response;
    }

    private void mapInstallments(CreditResponseDto responseDto, List<Installment> installments) {
        List<InstallmentResponse> installmentResponses = new ArrayList<>();

        for (Installment installment : installments) {
            InstallmentResponse installmentResponse = new InstallmentResponse();

            installmentResponse.setId(installment.getId());
            installmentResponse.setDueDate(installment.getDueDate());
            installmentResponse.setAmount(BigDecimal.valueOf(installment.getAmount().longValue()));
            installmentResponse.setStatus(installment.getStatus() == 1 ? "ACTIVE" : "CLOSED");

            installmentResponses.add(installmentResponse);
        }
        responseDto.setInstallments(installmentResponses);
    }
    @Cacheable("myCache")
    public List<CreditResponseDto> listCredits(Integer userId) {
        User user = userRepository.findById(Long.valueOf(userId)).orElseThrow(() -> new UserNotFoundException("User not found"));
        Optional<List<Credit>> credits = creditRepository.findByUserId(user.getId());

        checkAllCreditsIfClosed(credits);

        return mapCreditsToCreditResponseDtos(credits.get());
    }

    private void checkAllCreditsIfClosed(Optional<List<Credit>> credits) {
        for (int i = 0; i < credits.get().size(); i++) {
            if (checkIsCreditClosed(credits.get().get(i))) {
                credits.get().get(i).setStatus(CreditStatus.CLOSED.ordinal());
                creditRepository.save(credits.get().get(i));
            }
        }
    }

    private List<CreditResponseDto> mapCreditsToCreditResponseDtos(List<Credit> credits) {
        List<CreditResponseDto> responseDtos = new ArrayList<>();
        for (Credit credit : credits) {
            CreditResponseDto responseDto = new CreditResponseDto();
            responseDto.setCreditId(credit.getId());
            responseDto.setAmount(credit.getAmount());
            responseDto.setInstallments(mapInstallmentsToInstallmentResponses(credit.getInstallments()));
            responseDto.setStatus(checkIsCreditClosed(credit) ? "ACTIVE" : "CLOSED");
            responseDtos.add(responseDto);
        }
        return responseDtos;
    }

    private boolean checkIsCreditClosed(Credit credit) {

        //set all credit to 0
        return credit.getInstallments().stream().allMatch(installment -> installment.getStatus() == 1);
    }

    private List<InstallmentResponse> mapInstallmentsToInstallmentResponses(List<Installment> installments) {
        List<InstallmentResponse> installmentResponses = new ArrayList<>();
        for (Installment installment : installments) {
            InstallmentResponse installmentResponse = new InstallmentResponse();
            installmentResponse.setId(installment.getId());
            installmentResponse.setAmount(BigDecimal.valueOf(installment.getAmount().longValue()));
            installmentResponse.setDueDate(installment.getDueDate());
            installmentResponse.setStatus(installment.getStatus() == 1 ? "ACTIVE" : "CLOSED");
            installmentResponses.add(installmentResponse);
        }
        return installmentResponses;
    }

    public List<Credit> findAllCredits() {
        return creditRepository.findAll();
    }

    public List<CreditResponseDto> listFilteredAndPaginatedCredits(String status, LocalDate date) {

        if(status.equals("ACTIVE"))
            status = "0";
        else if(status.equals("CLOSED"))
            status = "1";
        else
            status = null;

        Integer statusInt = Integer.valueOf(status == null ? "1" : status);
        return mapCreditsToCreditResponseDtos(creditRepository.findByStatusAndCreatedAtGreaterThanEqual(statusInt, date));
    }
}

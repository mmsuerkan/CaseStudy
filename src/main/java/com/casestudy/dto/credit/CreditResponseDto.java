package com.casestudy.dto.credit;

import com.casestudy.dto.installment.InstallmentResponse;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;
@Getter
@Setter
public class CreditResponseDto {
    private Integer creditId;
    private List<InstallmentResponse> installments;
    private String status;
    private BigDecimal amount;
}

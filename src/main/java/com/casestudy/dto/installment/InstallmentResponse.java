package com.casestudy.dto.installment;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
public class InstallmentResponse {
    private Integer id;
    private LocalDate dueDate;
    private BigDecimal amount;
    private String status;
}

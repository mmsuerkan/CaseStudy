package com.casestudy.dto.credit;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
@Getter
@Setter
public class CreditRequestDto {
    private Integer userId;
    private BigDecimal amount;
    private Integer installmentCount;

}

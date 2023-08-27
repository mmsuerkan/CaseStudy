package com.casestudy.dto.installment;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PayInstallmentRequest {

    private Integer userId;
    private Integer creditId;
    private Integer installmentId;
    private Long amount;
}

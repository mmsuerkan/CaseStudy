package com.casestudy.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;


@Entity
@Table(name = "installment")
@Getter
@Setter
public class Installment extends BaseEntity{

    @Column(name = "amount")
    private BigDecimal amount;

    @Column(name = "status")
    private Integer status;

    @Column(name = "dueDate")
    private LocalDate dueDate;

    @ManyToOne
    @JoinColumn(name = "credit_id")
    private Credit credit;
}
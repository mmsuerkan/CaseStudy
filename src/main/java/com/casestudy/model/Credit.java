package com.casestudy.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;


@Entity
@Table(name = "credit")
@Getter
@Setter
public class Credit extends BaseEntity{
    @Column(name = "status")
    private Integer status;

    @Column(name = "amount")
    private BigDecimal amount;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "credit", cascade = CascadeType.ALL)
    private List<Installment> installments;

}
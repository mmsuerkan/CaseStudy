package com.casestudy.repository;

import com.casestudy.model.Credit;
import com.casestudy.model.Installment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface InstallmentRepository extends JpaRepository<Installment, Long>{

    Optional<Installment> findByIdAndCredit(Integer id, Credit credit);

    List<Installment> findByCreditAndStatus(Credit credit, Integer status);


}

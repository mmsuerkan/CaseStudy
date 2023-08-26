package com.casestudy.repository;

import com.casestudy.model.Installment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InstallmentRepository extends JpaRepository<Installment, Long>{
    List<Installment> findByCreditId(Integer id);
}

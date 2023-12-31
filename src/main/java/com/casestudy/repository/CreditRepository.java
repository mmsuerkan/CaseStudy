package com.casestudy.repository;

import com.casestudy.model.Credit;
import com.casestudy.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface CreditRepository extends JpaRepository<Credit, Long> {
    Optional<List<Credit>> findByUserId(Integer id);

    Optional<Credit> findByIdAndUser(Integer id, User user);

    List<Credit> findByStatusAndCreatedAtGreaterThanEqual(Integer status, LocalDate createdAt);







}

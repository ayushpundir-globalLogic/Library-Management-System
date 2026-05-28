package com.project.librarymanagementsystem.repository;

import com.project.librarymanagementsystem.model.Loan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface LoanRepository extends JpaRepository<Loan,Long> {
    Optional<Loan> findByBookIdAndReturnDateIsNull(Long bookId);
    List<Loan> findByReturnDateIsNullAndDueDateBefore(LocalDate date);
}

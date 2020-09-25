package com.chaves.libraryapi.repository;

import com.chaves.libraryapi.model.entity.Book;
import com.chaves.libraryapi.model.entity.Loan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface LoanRepository extends JpaRepository<Loan, Long> {

    @Query("SELECT CASE WHEN (COUNT(l.id) > 0) THEN true ELSE false END FROM Loan l " +
            "WHERE l.book = :book AND (l.returned IS NULL OR l.returned IS false)")
    boolean existsByBookAndNotReturned(@Param("book") Book book);
}

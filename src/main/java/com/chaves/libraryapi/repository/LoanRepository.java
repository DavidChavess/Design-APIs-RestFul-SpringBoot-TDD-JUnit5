package com.chaves.libraryapi.repository;

import com.chaves.libraryapi.model.entity.Book;
import com.chaves.libraryapi.model.entity.Loan;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface LoanRepository extends JpaRepository<Loan, Long> {

    @Query("SELECT CASE WHEN (COUNT(l.id) > 0) THEN true ELSE false END FROM Loan l " +
            "WHERE l.book = :book AND (l.returned IS NULL OR l.returned IS false)")
    boolean existsByBookAndNotReturned(@Param("book") Book book);

    @Query("SELECT l FROM Loan AS l JOIN l.book AS b WHERE l.customer = :customer OR b.isbn = :isbn")
    Page<Loan> findByBookIsbnOrCustomer(
            @Param("customer") String customer,
            @Param("isbn") String isbn,
            Pageable pageRequest
    );

    Page<Loan> findByBook(Book book, Pageable pageable);

    @Query("SELECT l FROM Loan l " +
            "WHERE l.loanDate <= :threeDaysAgo " +
            "AND (l.returned IS NULL OR l.returned IS false)")
    List<Loan> findByLoanDateLessThanAndNotReturned( @Param("threeDaysAgo") LocalDate threeDaysAgo );
}

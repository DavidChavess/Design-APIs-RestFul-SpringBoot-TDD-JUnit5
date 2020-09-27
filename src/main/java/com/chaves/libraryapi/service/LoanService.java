package com.chaves.libraryapi.service;

import com.chaves.libraryapi.dto.LoanFilterOrCreateDTO;
import com.chaves.libraryapi.model.entity.Loan;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface LoanService {
    Loan save(Loan loan);
    Optional<Loan> getById(Long id);
    Loan update(Loan loan);

    Page<Loan> find(LoanFilterOrCreateDTO dto, Pageable pageRequest);
}

package com.chaves.libraryapi.service;

import com.chaves.libraryapi.model.entity.Loan;
import com.chaves.libraryapi.repository.LoanRepository;

public class LoanServiceImpl implements LoanService {

    private LoanRepository repository;

    public LoanServiceImpl(LoanRepository repository) {
        this.repository = repository;
    }

    @Override
    public Loan save(Loan loan) {
        return repository.save(loan);
    }
}

package com.chaves.libraryapi.service.impl;

import com.chaves.libraryapi.exception.BusinessException;
import com.chaves.libraryapi.model.entity.Loan;
import com.chaves.libraryapi.repository.LoanRepository;
import com.chaves.libraryapi.service.LoanService;

public class LoanServiceImpl implements LoanService {

    private LoanRepository repository;

    public LoanServiceImpl(LoanRepository repository) {
        this.repository = repository;
    }

    @Override
    public Loan save(Loan loan) {
        if( repository.existsByBookAndNotReturned(loan.getBook()) ){
            throw new BusinessException("Livro já emprestado");
        }
        return repository.save(loan);
    }
}

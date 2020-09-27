package com.chaves.libraryapi.service.impl;

import com.chaves.libraryapi.dto.LoanFilterOrCreateDTO;
import com.chaves.libraryapi.exception.BusinessException;
import com.chaves.libraryapi.model.entity.Loan;
import com.chaves.libraryapi.repository.LoanRepository;
import com.chaves.libraryapi.service.LoanService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

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

    @Override
    public Optional<Loan> getById(Long id) {
        return repository.findById(id);
    }

    @Override
    public Loan update(Loan loan) {
        return repository.save(loan);
    }

    @Override
    public Page<Loan> find(LoanFilterOrCreateDTO dto, Pageable pageRequest) {
        return repository.findByBookIsbnOrCustomer(dto.getCustomer(), dto.getIsbn(), pageRequest);
    }
}

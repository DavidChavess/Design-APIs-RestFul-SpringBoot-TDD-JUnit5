package com.chaves.libraryapi.controller;

import com.chaves.libraryapi.dto.LoanDTO;
import com.chaves.libraryapi.exception.BusinessException;
import com.chaves.libraryapi.model.entity.Book;
import com.chaves.libraryapi.model.entity.Loan;
import com.chaves.libraryapi.service.BookService;
import com.chaves.libraryapi.service.LoanService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/loans")
@AllArgsConstructor
public class LoanController {
    private final LoanService loanService;
    private final BookService bookService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Long created(@RequestBody LoanDTO loanDTO){
        Book book = bookService.getByIsbn(loanDTO.getIsbn())
                    .orElseThrow(()->
                    new ResponseStatusException(HttpStatus.BAD_REQUEST, "Livro não encontrado para o isbn informado"));

        Loan loanEntity = Loan.builder()
                    .id(null)
                    .customer(loanDTO.getCustomer())
                    .loanDate(LocalDate.now())
                    .book(book)
                    .build();

        loanEntity = loanService.save(loanEntity);
        return loanEntity.getId();
    }
}

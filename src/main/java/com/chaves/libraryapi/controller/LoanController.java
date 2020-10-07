package com.chaves.libraryapi.controller;

import com.chaves.libraryapi.dto.BookDTO;
import com.chaves.libraryapi.dto.LoanDTO;
import com.chaves.libraryapi.dto.LoanFilterOrCreateDTO;
import com.chaves.libraryapi.dto.ReturnedLoanDTO;
import com.chaves.libraryapi.model.entity.Book;
import com.chaves.libraryapi.model.entity.Loan;
import com.chaves.libraryapi.service.BookService;
import com.chaves.libraryapi.service.LoanService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/loans")
@RequiredArgsConstructor
public class LoanController {

    private final LoanService loanService;
    private final BookService bookService;
    private final ModelMapper modelMapper;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Long created(@RequestBody LoanFilterOrCreateDTO loanDTO){
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

    @ResponseStatus(HttpStatus.OK)
    @PatchMapping("/{id}")
    public void returnedBook(@PathVariable Long id, @RequestBody ReturnedLoanDTO dto){
        Loan loan = loanService.getById(id)
                .orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND));

        loan.setReturned(dto.getReturned());
        loanService.update(loan);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping
    public Page<LoanDTO> find(LoanFilterOrCreateDTO dto, Pageable pageRequest){
        Page<Loan> loanPage = loanService.find(dto, pageRequest);
        List<LoanDTO> listLoanDTO = loanPage.getContent()
                .stream()
                .map(loan1 -> {
                        BookDTO bookDto = modelMapper.map(loan1.getBook(), BookDTO.class);
                        LoanDTO loanDto = modelMapper.map(loan1, LoanDTO.class);
                        loanDto.setBook(bookDto);
                        return loanDto;
                }).collect(Collectors.toList());
        return new PageImpl<LoanDTO>(listLoanDTO, pageRequest, loanPage.getTotalElements());
    }
}

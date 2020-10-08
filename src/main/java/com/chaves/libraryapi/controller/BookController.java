package com.chaves.libraryapi.controller;

import com.chaves.libraryapi.dto.BookDTO;
import com.chaves.libraryapi.dto.LoanDTO;
import com.chaves.libraryapi.exception.ApiErrors;
import com.chaves.libraryapi.model.entity.Book;
import com.chaves.libraryapi.model.entity.Loan;
import com.chaves.libraryapi.service.BookService;
import com.chaves.libraryapi.service.LoanService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("api/books")
@RequiredArgsConstructor
@Api("Book API")
@Slf4j
public class BookController {

    private final BookService service;
    private final LoanService loanService;
    private final ModelMapper modelMapper;

    @ApiOperation("Creates a book")
    @ApiResponses({
        @ApiResponse(code = 400, message = "Validation errors", response = ApiErrors.class),
        @ApiResponse(code = 201, message = "book successfully created")
    })
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public BookDTO create(@RequestBody @Valid BookDTO bookDTO){
        log.info("Creating a book for isbn {} ", bookDTO.getIsbn());
        Book entity = modelMapper.map(bookDTO, Book.class);
        entity = service.save(entity);
        return modelMapper.map(entity, BookDTO.class);
    }

    @ApiOperation("Obtains a book details by id")
    @ApiResponse(code = 404, message = "Book not found")
    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public BookDTO getById(@PathVariable Long id){
        log.info("Obtaining details for book id {} ", id);
        return service
                .getById(id)
                .map(book -> modelMapper.map(book, BookDTO.class))
                .orElseThrow( () -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @ApiOperation("Deletes a book by id")
    @ApiResponses({
            @ApiResponse(code = 204, message = "Book succesfully deleted"),
            @ApiResponse(code = 404, message = "Book not found")
    })
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id){
        log.info("Deleting book of id {} ", id);
        service.delete(service.getById(id)
                .orElseThrow( () -> new ResponseStatusException(HttpStatus.NOT_FOUND)));
    }

    @ApiOperation("updates a book")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Book succesfully updated"),
            @ApiResponse(code = 404, message = "Book not found")
    })
    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public BookDTO update(@PathVariable Long id, @Valid @RequestBody BookDTO bookDto) {
        log.info("Updating a book of id {} ", id);
        return service.getById(id)
                .map(book -> {
                    book.setAuthor(bookDto.getAuthor());
                    book.setTitle(bookDto.getTitle());
                    book = service.update(book);
                    return modelMapper.map(book, BookDTO.class);})
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @ApiOperation("Find book by params")
    @GetMapping
    public Page<BookDTO> find(BookDTO bookDTO, Pageable pageRequest){
        log.info("Finding the books");
        Book filter = modelMapper.map(bookDTO, Book.class);
        Page<Book> result = service.find(filter, pageRequest);
        List<BookDTO> list = result.getContent()
                .stream()
                .map(book -> modelMapper.map(book, BookDTO.class))
                .collect(Collectors.toList());
        return new PageImpl<BookDTO>(list, pageRequest, result.getTotalElements() );
    }

    @ApiOperation("Find loans a book by id")
    @ApiResponse(code = 404, message = "Book not found")
    @GetMapping("/{id}/loans")
    public Page<LoanDTO> loansByBook(@PathVariable Long id, Pageable pageable){
        log.info("Finding loans of book ");
        Book book = service.getById(id).orElseThrow( () -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        Page<Loan> loanPage = loanService.getLoansByBook(book, pageable);

        List<LoanDTO> list = loanPage.getContent().stream()
                .map(loan -> {
                    BookDTO bookDTO = modelMapper.map(loan.getBook(), BookDTO.class);
                    LoanDTO loanDTO = modelMapper.map(loan, LoanDTO.class);
                    loanDTO.setBook(bookDTO);
                    return loanDTO;
                }).collect(Collectors.toList());

       return new PageImpl<>(list, pageable, loanPage.getTotalElements());
    }
}

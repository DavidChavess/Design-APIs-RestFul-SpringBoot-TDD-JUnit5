package com.chaves.libraryapi.controller;

import com.chaves.libraryapi.dto.BookDTO;
import com.chaves.libraryapi.model.entity.Book;
import com.chaves.libraryapi.service.BookService;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("api/books")
public class BookController {

    private BookService service;
    private ModelMapper modelMapper;

    public BookController(BookService service, ModelMapper modelMapper) {
        this.service = service;
        this.modelMapper = modelMapper;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public BookDTO create(@RequestBody @Valid BookDTO bookDTO){
        Book entity = modelMapper.map(bookDTO, Book.class);
        entity = service.save(entity);
        return modelMapper.map(entity, BookDTO.class);
    }
}

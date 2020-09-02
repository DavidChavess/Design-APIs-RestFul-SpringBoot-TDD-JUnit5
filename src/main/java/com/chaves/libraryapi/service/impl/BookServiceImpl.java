package com.chaves.libraryapi.service.impl;

import com.chaves.libraryapi.exception.BusinessException;
import com.chaves.libraryapi.model.entity.Book;
import com.chaves.libraryapi.repository.BookRepository;
import com.chaves.libraryapi.service.BookService;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class BookServiceImpl implements BookService {

    private BookRepository repository;

    public BookServiceImpl(BookRepository repository){
        this.repository = repository;
    }

    @Override
    public Book save(Book book) {
        if( repository.existsByIsbn(book.getIsbn()) ){
            throw new BusinessException("Isbn já cadastrado");
        }
        return repository.save(book);
    }

    @Override
    public Optional<Book> getById(Long id) {
        return null;
    }

    @Override
    public void delete(Book book) {

    }
}

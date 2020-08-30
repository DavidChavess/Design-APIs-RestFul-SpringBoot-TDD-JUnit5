package com.chaves.libraryapi.service;

import com.chaves.libraryapi.model.entity.Book;

import java.util.Optional;

public interface BookService {

    Book save(Book any);

    Optional<Book> getById(Long id);
}

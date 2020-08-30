package com.chaves.libraryapi.repository;

import com.chaves.libraryapi.model.entity.Book;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@DataJpaTest
public class BookRepositoryTest {

    @Autowired
    TestEntityManager entityManager;

    @Autowired
    BookRepository bookRepository;

    @Test
    @DisplayName("Deve retornar verdadeiro quando existir um livro na base com o isbn já cadastrado")
    public void returnTrueWhenIsbnExists(){
        //cenario
        String isbn = "123";
        Book book = Book.builder().title("As aventuras").author("Fulano").isbn(isbn).build();
        entityManager.persist(book);

        //execucao
        boolean isbnExists =  bookRepository.existsByIsbn(isbn);

        //verificacao
        assertThat(isbnExists).isTrue();
    }

    @Test
    @DisplayName("Deve retornar fslso quando não existir um livro na base com o isbn já cadastrado")
    public void returnFalseWhenIsbnDoesntExist(){
        //cenario
        String isbn = "123";

        //execucao
        boolean isbnExists =  bookRepository.existsByIsbn(isbn);

        //verificacao
        assertThat(isbnExists).isFalse();
    }

}

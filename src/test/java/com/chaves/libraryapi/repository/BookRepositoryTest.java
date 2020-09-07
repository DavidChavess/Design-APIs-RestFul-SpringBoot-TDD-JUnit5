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

import java.util.Optional;

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
        Book book = createNewBookWithIsbn(isbn);
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

    @Test
    @DisplayName("Deve obter um livro por id")
    public void findByIdTest(){
        Book book = createNewBookWithIsbn("123");
        entityManager.persist(book);

        Optional<Book> bookFound = bookRepository.findById(book.getId());

        assertThat(bookFound.isPresent()).isTrue();
    }

    @Test
    @DisplayName("Deve salvar um livro na base")
    public void saveTest(){
        Book book = createNewBookWithIsbn("123");

        Book savedBook = bookRepository.save(book);

        assertThat(savedBook.getId()).isNotNull();
    }

    @Test
    @DisplayName("Deve deletar um livro na base")
    public void deleteTest(){
        Book book = createNewBookWithIsbn("123");
        entityManager.persist(book);
        Book foundBook = entityManager.find(Book.class, book.getId());

        bookRepository.delete(foundBook);

        Book deletedBook = entityManager.find(Book.class, foundBook.getId());
        assertThat(deletedBook).isNull();
    }

    private Book createNewBookWithIsbn(String isbn) {
        return Book.builder().title("As aventuras").author("Fulano").isbn(isbn).build();
    }
}

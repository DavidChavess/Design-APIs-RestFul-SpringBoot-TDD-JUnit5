package com.chaves.libraryapi.service;

import com.chaves.libraryapi.model.entity.Book;
import com.chaves.libraryapi.repository.BookRepository;
import com.chaves.libraryapi.service.impl.BookServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
public class BookServiceTest {

    BookService service;

    @MockBean
    BookRepository repository;

    @BeforeEach
    public void setup(){
        this.service = new BookServiceImpl(repository);
    }

    @Test
    @DisplayName("Deve salvar um livro")
    public void saveBookTest(){
        Book book = Book.builder().isbn("123").author("Fulano").title("As aventuras").build();

        Mockito.when(service.save(book)).thenReturn(
                Book.builder().id(1l).isbn("123").author("Fulano").title("As aventuras").build());

        Book saveBook = service.save(book);

        assertThat(saveBook.getId()).isNotNull();
        assertThat(saveBook.getIsbn()).isEqualTo("123");
        assertThat(saveBook.getAuthor()).isEqualTo("Fulano");
        assertThat(saveBook.getTitle()).isEqualTo("As aventuras");
    }
}

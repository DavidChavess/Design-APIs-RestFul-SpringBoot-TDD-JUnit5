package com.chaves.libraryapi.service;

import com.chaves.libraryapi.exception.BusinessException;
import com.chaves.libraryapi.model.entity.Book;
import com.chaves.libraryapi.repository.BookRepository;
import com.chaves.libraryapi.service.impl.BookServiceImpl;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.when;

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
        Book book = createValidBook();
        when(repository.existsByIsbn(anyString())).thenReturn(false);
        when(service.save(book))
                .thenReturn(Book.builder()
                        .id(1l)
                        .isbn("123")
                        .author("Fulano")
                        .title("As aventuras")
                        .build());

        Book saveBook = service.save(book);

        assertThat(saveBook.getId()).isNotNull();
        assertThat(saveBook.getIsbn()).isEqualTo("123");
        assertThat(saveBook.getAuthor()).isEqualTo("Fulano");
        assertThat(saveBook.getTitle()).isEqualTo("As aventuras");
    }

    @Test
    @DisplayName("Deve lançar erro de negocio ao tentar salvar um livro com isbn já cadastrado")
    public void shouldNotSaveABookWithDuplicatedISBN(){
        Book book = createValidBook();
        when(repository.existsByIsbn(anyString())).thenReturn(true);

        Throwable exception = Assertions.catchThrowable(() -> service.save(book));

        assertThat(exception)
                .isInstanceOf(BusinessException.class)
                .hasMessage("Isbn já cadastrado");

        Mockito.verify(repository, never()).save(book);
    }

    @Test
    @DisplayName("Deve obter um livro por id")
    public void getByIdTest(){
        Long id = 1l;
        Book book = createValidBook();
        book.setId(id);
        when( service.getById(id) ).thenReturn(Optional.of(book));

        Optional<Book> bookFound = service.getById(id);

        assertThat( bookFound.isPresent() ).isTrue();
        assertThat( bookFound.get().getId() ).isEqualTo(id);
        assertThat( bookFound.get().getAuthor() ).isEqualTo( book.getAuthor() );
        assertThat( bookFound.get().getTitle() ).isEqualTo( book.getTitle() );
        assertThat( bookFound.get().getIsbn() ).isEqualTo( book.getIsbn() );
    }

    @Test
    @DisplayName("Deve retornar vazio quando não encontrar um livro com o id na base")
    public void bookNotFoundByIdTest(){
        Long id = 1l;
        when( service.getById(id) ).thenReturn(Optional.empty());

        Optional<Book> bookFound = service.getById(id);

        assertThat( bookFound.isEmpty() ).isTrue();
    }

    private Book createValidBook() {
        return Book.builder().isbn("123").author("Fulano").title("As aventuras").build();
    }
}

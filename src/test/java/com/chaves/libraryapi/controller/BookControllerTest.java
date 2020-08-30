package com.chaves.libraryapi.controller;

import com.chaves.libraryapi.dto.BookDTO;
import com.chaves.libraryapi.exception.BusinessException;
import com.chaves.libraryapi.model.entity.Book;
import com.chaves.libraryapi.service.BookService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Optional;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@WebMvcTest
@AutoConfigureMockMvc
public class BookControllerTest {

    static String BOOK_API = "/api/books";

    @Autowired
    MockMvc mvc;

    @MockBean
    BookService service;

    @Test
    @DisplayName("Deve criar um livro com sucesso")
    public void createBookTest() throws Exception {
        BookDTO dto = createNewBookDTO();
        String json = new ObjectMapper().writeValueAsString(dto);
        Book savedBook = Book.builder().id(01l).author("Artur").title("As aventuras").isbn("001").build();

        given(service.save(any(Book.class))).willReturn(savedBook);

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .post(BOOK_API)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(json);

        mvc.perform(request)
                .andExpect(status().isCreated())
                .andExpect(jsonPath("id").value(01l))
                .andExpect(jsonPath("title").value(dto.getTitle()))
                .andExpect(jsonPath("author").value(dto.getAuthor()))
                .andExpect(jsonPath("isbn").value(dto.getIsbn()));
    }

    @Test
    @DisplayName("Deve lançar erro de validação quando não houver dados suficientes")
    public void createInvalidBookTest() throws Exception {
        String json = new ObjectMapper().writeValueAsString(new BookDTO());

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .post(BOOK_API)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(json);

        mvc.perform(request)
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("errors", hasSize(3)));
    }

    @Test
    @DisplayName("Deve lançar erro ao tentar cadastrar um livro com isbn já utilizado por outro")
    public void createBookWithDuplicatedIsbn() throws Exception {
        BookDTO dto = createNewBookDTO();
        String json = new ObjectMapper().writeValueAsString(dto);
        String messageError = "Isbn já cadastrado";

        given(service.save(any(Book.class)))
                .willThrow(new BusinessException(messageError));

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .post(BOOK_API)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(json);

        mvc.perform(request)
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("errors", hasSize(1)))
                .andExpect(jsonPath("errors[0]").value(messageError));
    }

    @Test
    @DisplayName("Deve obter informações de um livro pelo id")
    public void getBookByIdTest() throws Exception {
        Long id = 1l;
        BookDTO dtoDefault = createNewBookDTO();
        Book book = Book.builder()
                .id(id)
                .author(dtoDefault.getAuthor())
                .title(dtoDefault.getTitle())
                .isbn(dtoDefault.getIsbn())
                .build();

        given(service.getById(id)).willReturn(Optional.of(book));

        MockHttpServletRequestBuilder request =  MockMvcRequestBuilders
                .get(BOOK_API.concat("/"+id))
                .accept(MediaType.APPLICATION_JSON);

        mvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(jsonPath("id").value(id))
                .andExpect(jsonPath("author").value(dtoDefault.getAuthor()))
                .andExpect(jsonPath("title").value(dtoDefault.getTitle()))
                .andExpect(jsonPath("isbn").value(dtoDefault.getIsbn()));
    }

    private BookDTO createNewBookDTO(){
        return BookDTO.builder().author("Artur").title("As aventuras").isbn("001").build();
    }
}

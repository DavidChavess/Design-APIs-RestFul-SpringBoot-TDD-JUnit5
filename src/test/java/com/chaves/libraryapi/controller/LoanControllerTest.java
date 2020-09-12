package com.chaves.libraryapi.controller;

import com.chaves.libraryapi.dto.LoanDTO;
import com.chaves.libraryapi.exception.BusinessException;
import com.chaves.libraryapi.model.entity.Book;
import com.chaves.libraryapi.model.entity.Loan;
import com.chaves.libraryapi.service.BookService;
import com.chaves.libraryapi.service.LoanService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import javax.swing.plaf.nimbus.NimbusStyle;
import java.time.LocalDate;
import java.util.Optional;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ActiveProfiles("test")
@ExtendWith(SpringExtension.class)
@WebMvcTest
@AutoConfigureMockMvc
public class LoanControllerTest {

    @Autowired
    MockMvc mvc;

    @MockBean
    private BookService bookService;

    @MockBean
    private LoanService loanService;

    private static final String LOAN_API = "/api/loans";

    @Test
    @DisplayName("Deve criar um emprestimo de livro")
    public void createLoanTest() throws Exception{
        LoanDTO dto = LoanDTO.builder().isbn("123").customer("Fulano").build();
        String json = new ObjectMapper().writeValueAsString(dto);

        Book book = Book.builder().id(1l).isbn("123").build();
        given(bookService.getByIsbn("123")).willReturn(Optional.of(book));

        Loan loan = Loan.builder().id(1l).customer("Fulano").loanDate(LocalDate.now()).book(book).build();
        given(loanService.save(any(Loan.class))).willReturn(loan);

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .post(LOAN_API)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json);

        mvc.perform(request)
                .andExpect(status().isCreated())
                .andExpect(content().string(String.valueOf(loan.getId())));
    }

    @Test
    @DisplayName("Não deve criar um emprestimo se o livro com o isbn informado não existir")
    public void invalidIsbnCreateLoanTest() throws Exception{
        LoanDTO dto = LoanDTO.builder().isbn("123").customer("Fulano").build();
        String json = new ObjectMapper().writeValueAsString(dto);

        given(bookService.getByIsbn("123")).willReturn(Optional.empty());

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .post(LOAN_API)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json);

        mvc.perform(request)
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("errors", hasSize(1)))
                .andExpect(jsonPath("errors[0]").value("Livro não encontrado para o isbn informado"));
    }

    @Test
    @DisplayName("Deve retornar erro ao fazer emprestimo de um livro já emprestado")
    public void loanedBookErrorOnCreateLoanTest() throws Exception {
        LoanDTO dto = LoanDTO.builder().isbn("123").customer("Fulano").build();
        String json = new ObjectMapper().writeValueAsString(dto);

        Book book = Book.builder().id(1l).isbn("123").build();
        given(bookService.getByIsbn("123")).willReturn(Optional.of(book));

        given(loanService.save(any(Loan.class))).willThrow(new BusinessException("Livro já emprestado"));

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .post(LOAN_API)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json);

        mvc.perform(request)
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("errors", hasSize(1)))
                .andExpect(jsonPath("errors[0]").value("Livro já emprestado"));
    }
}

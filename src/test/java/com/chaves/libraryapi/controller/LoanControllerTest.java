package com.chaves.libraryapi.controller;

import com.chaves.libraryapi.dto.LoanDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

@ActiveProfiles("test")
@ExtendWith(SpringExtension.class)
@WebMvcTest
@AutoConfigureMockMvc
public class LoanController {

    @Autowired
    MockMvc mvc;

    @Test
    @DisplayName("Deve criar um emprestimo de livro")
    public void createLoanTest(){
        LoanDTO dto = LoanDTO.builder().build();
        String json = new ObjectMapper().writeValueAsString(dto);
    }
}

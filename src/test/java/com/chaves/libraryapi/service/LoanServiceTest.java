package com.chaves.libraryapi.service;

import com.chaves.libraryapi.model.entity.Book;
import com.chaves.libraryapi.model.entity.Loan;
import com.chaves.libraryapi.repository.LoanRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
public class LoanServiceTest {

    @MockBean
    LoanRepository repository;

    LoanService service;

    @BeforeEach
    public void setUp(){
        this.service = new LoanServiceImpl(repository);
    }

    @Test
    @DisplayName("Deve salvar um empréstimo")
    public void saveLoanTest(){
        Loan loan = createValidLoan();

        when(repository.save(loan)).thenReturn(Loan.builder()
                .id(1l)
                .loanDate(loan.getLoanDate())
                .customer(loan.getCustomer())
                .book(loan.getBook())
                .build());

        Loan loanSaved = service.save(loan);

        assertThat(loanSaved.getId()).isNotNull();
        assertThat(loanSaved.getCustomer()).isEqualTo(loan.getCustomer());
        assertThat(loanSaved.getBook()).isEqualTo(loan.getBook());
        assertThat(loanSaved.getLoanDate()).isEqualTo(loan.getLoanDate());
    }

    private Loan createValidLoan(){
        Book book = Book.builder().id(1l).build();
        return Loan.builder().loanDate(LocalDate.now()).customer("Fulano").book(book).build();
    }
}

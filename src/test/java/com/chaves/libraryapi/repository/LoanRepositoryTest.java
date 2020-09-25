package com.chaves.libraryapi.repository;

import com.chaves.libraryapi.model.entity.Book;
import com.chaves.libraryapi.model.entity.Loan;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.persistence.EntityManager;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static com.chaves.libraryapi.repository.BookRepositoryTest.createNewBookWithIsbn;
import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@DataJpaTest
public class LoanRepositoryTest {

    @Autowired
    private EntityManager entityMananger;

    @Autowired
    private LoanRepository repository;

    @Test
    @DisplayName("Deve verificar se existe empréstimo não devolvido para o livro")
    public void existsByBookAndNotReturnedTest(){
        //cenario
        Book book = createNewBookWithIsbn("123");
        entityMananger.persist(book);

        Loan loan = Loan.builder().book(book).customer("Fulano").loanDate(LocalDate.now()).build();
        entityMananger.persist(loan);

        boolean exists = repository.existsByBookAndNotReturned(book);

        assertThat(exists).isTrue();
    }
}

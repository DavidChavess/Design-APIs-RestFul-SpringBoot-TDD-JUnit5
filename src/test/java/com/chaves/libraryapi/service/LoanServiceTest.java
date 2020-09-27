package com.chaves.libraryapi.service;

import com.chaves.libraryapi.dto.LoanFilterOrCreateDTO;
import com.chaves.libraryapi.exception.BusinessException;
import com.chaves.libraryapi.model.entity.Book;
import com.chaves.libraryapi.model.entity.Loan;
import com.chaves.libraryapi.repository.LoanRepository;
import com.chaves.libraryapi.repository.LoanRepositoryTest;
import com.chaves.libraryapi.service.impl.LoanServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.Mockito.*;

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

        when(repository.existsByBookAndNotReturned(loan.getBook())).thenReturn(false);

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

    @Test
    @DisplayName("Deve lançar erro de negócio ao salvar um empréstimo com livro já emprestado")
    public void loanedBookSaveTest(){
        Loan loan = createValidLoan();
        when(repository.existsByBookAndNotReturned(loan.getBook())).thenReturn(true);

        Throwable error = catchThrowable(() -> service.save(loan));

        assertThat(error).isInstanceOf(BusinessException.class)
                .hasMessage("Livro já emprestado");

        verify(repository, never()).save(loan);
    }

    @Test
    @DisplayName("Deve retornar as  informações de um empréstimo pelo id")
    public void getLoanByIdTest(){
        // cenario
        Long id = 1L;
        Loan loan = createValidLoan();
        loan.setId(id);

        when(repository.findById(id)).thenReturn(Optional.of(loan));

        //execução
        Optional<Loan> returnedLoan = service.getById(id);

        //verificação
        assertThat(returnedLoan.isPresent()).isTrue();
        assertThat(returnedLoan.get().getId()).isEqualTo(loan.getId());
        assertThat(returnedLoan.get().getLoanDate()).isEqualTo(loan.getLoanDate());
        assertThat(returnedLoan.get().getBook()).isEqualTo(loan.getBook());
        assertThat(returnedLoan.get().getCustomer()).isEqualTo(loan.getCustomer());

        verify(repository, times(1)).findById(id);
    }

    @Test
    @DisplayName("Deve atualizar um espréstimo, devolvendo um livro emprestado")
    public void updateLoanTest(){
        Loan loan = createValidLoan();
        loan.setId(1L);
        loan.setReturned(true);
        when(repository.save(loan)).thenReturn(loan);

        Loan updatedLoan = service.update(loan);

        assertThat(updatedLoan.getReturned()).isTrue();

        verify(repository, times(1)).save(loan);
    }

    @Test
    @DisplayName("Deve filtar empréstimos pelas propriedades")
    public void findLoanTest(){
        //cenario
        LoanFilterOrCreateDTO loanFilter = LoanFilterOrCreateDTO.builder().isbn("123").customer("Fulano").build();

        Loan loan = createValidLoan();
        Pageable pageRequest = PageRequest.of(0,10);
        List<Loan> list = Arrays.asList(loan);
        Page<Loan> page = new PageImpl<Loan>(list, pageRequest, list.size());
        when(repository.findByBookIsbnOrCustomer(anyString(), anyString(), any(Pageable.class)))
                .thenReturn(page);

        //execução
        Page<Loan> loanPage = service.find(loanFilter, pageRequest);

        //verificações
        assertThat(loanPage.getPageable().getPageNumber()).isEqualTo(0);
        assertThat(loanPage.getPageable().getPageSize()).isEqualTo(10);
        assertThat(loanPage.getTotalElements()).isEqualTo(1);
        assertThat(loanPage.getContent()).isEqualTo(list);
    }

    private Loan createValidLoan(){
        Book book = Book.builder().id(1l).build();
        return Loan.builder().loanDate(LocalDate.now()).customer("Fulano").book(book).build();
    }
}

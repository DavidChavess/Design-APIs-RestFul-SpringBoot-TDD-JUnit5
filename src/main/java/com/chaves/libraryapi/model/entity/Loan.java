package com.chaves.libraryapi.model.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

import javax.persistence.*;
import java.time.LocalDate;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Loan {

    @Id
    @Column
    @GeneratedValue( strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String customer;

    @Column
    private String customerEmail;

    @Column
    private LocalDate loanDate;

    @JoinColumn(name = "book_id")
    @ManyToOne
    private Book book;

    @Column
    private Boolean returned;
}

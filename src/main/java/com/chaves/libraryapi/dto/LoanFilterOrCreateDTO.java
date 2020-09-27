package com.chaves.libraryapi.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoanFilterOrCreateDTO {

    private String isbn;
    private String customer;
}

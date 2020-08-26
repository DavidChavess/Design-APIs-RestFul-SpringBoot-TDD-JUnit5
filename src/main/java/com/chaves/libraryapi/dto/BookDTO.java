package com.chaves.libraryapi.dto;

import lombok.*;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BookDTO {
    private Long id;

    @NotNull
    private String title;

    @NotNull
    private String author;

    @NotNull
    private String isbn;
}
